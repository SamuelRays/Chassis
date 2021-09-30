package sample.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Rectangle;
import org.xml.sax.SAXException;
import sample.Main;
import sample.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class FUNTestWindowController {
    private Main main;
    private ObservableMap<Fan, ProgressBar> progressBars = FXCollections.observableHashMap();
    private ObservableList<Fan> errorFans = FXCollections.observableArrayList();
    private ObservableMap<FUNTestResult, Boolean> results = FXCollections.observableHashMap();
    @FXML
    private Button start;
    @FXML
    private ProgressBar progressBar1;
    @FXML
    private ProgressBar progressBar2;
    @FXML
    private ProgressBar progressBar3;
    @FXML
    private ProgressBar progressBar4;
    @FXML
    private ProgressBar progressBar5;
    @FXML
    private ProgressBar progressBar6;
    @FXML
    private Label testResult;
    @FXML
    private Rectangle testResultArea;
    @FXML
    private Button testDetails;
    @FXML
    private Button end;

    @FXML
    private void initialize() {
        progressBars.put(Fan._1, progressBar1);
        progressBars.put(Fan._2, progressBar2);
        progressBars.put(Fan._3, progressBar3);
        progressBars.put(Fan._4, progressBar4);
        progressBars.put(Fan._5, progressBar5);
        progressBars.put(Fan._6, progressBar6);
        progressBars.values().forEach(p -> p.setVisible(false));
        testResult.setVisible(false);
        testResultArea.setVisible(false);
        testDetails.setVisible(false);
        end.setVisible(false);
    }

    @FXML
    private void start() {
        testDetails.setVisible(false);
        testResult.setVisible(false);
        testResultArea.setVisible(false);
        results.clear();
        start.setDisable(true);
        for (Fan i : Fan.FAN_LISTS.get(main.getFun())) {
            progressBars.get(i).setVisible(true);
        }
        Thread progressThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    for (Fan i : Fan.FAN_LISTS.get(main.getFun())) {
                        double p = Double.parseDouble(ConnectHandler.CUget("FUFan" + i.toString().substring(1) + "Speed"));
                        progressBars.get(i).setProgress(p / 100.0);
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                main.error();
                e.printStackTrace();
            }
        });
        Task<Void> testTask = new Task<>() {
            private void terminate() {
                progressThread.interrupt();
                updateMessage(ResultMessage.FAILED.getMessage());
                testFail();
            }

            @Override
            protected Void call() throws Exception {
                try {
                    writeFUNPower();
                    runFans();
                    TimeUnit.SECONDS.sleep(3);
                    if (!checkFansOn()) {
                        terminate();
                        return null;
                    }
                } catch (IOException e) {
                    main.error();
                    e.printStackTrace();
                    return null;
                }
                try {
                    writeFUNInfo();
                    TimeUnit.SECONDS.sleep(3);
                    if (!checkFUNInfo()) {
                        terminate();
                        return null;
                    }
                    if (!testFUN10()) {
                        terminate();
                        return null;
                    }
                    if (!testFUN100()) {
                        terminate();
                        return null;
                    }
                    setFUNAuto();
                } catch (IOException e) {
                    main.error();
                    e.printStackTrace();
                    return null;
                }
                progressThread.interrupt();
                updateMessage(ResultMessage.PASSED.getMessage());
                testPass();
                return null;
            }
        };
        Thread testThread = new Thread(testTask);
        testResult.textProperty().bind(testTask.messageProperty());
        progressThread.start();
        testThread.start();
    }

    private void writeFUNPower() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        ConnectHandler.CUset("SlotPowerSet", String.valueOf(main.getFun().getPower()));
        ConnectHandler.CUset("SlotNameSet", main.getFun().getName());
        ConnectHandler.CUset("Slot12v0Bypass", String.valueOf(main.getFun().get_12()));
        ConnectHandler.CUset("Slot3v3Bypass", String.valueOf(main.getFun().get_3v3()));
        ConnectHandler.CUset("SlotWrEEPROM", "14");
    }

    private void runFans() throws InterruptedException, ParserConfigurationException, IOException, SAXException, TransformerException {
        for (int i = 0; i < 13; i++) {
            if (!isFanOn()) {
                ConnectHandler.CUset("FUFanMinRPM", "1000");
                ConnectHandler.CUset("FUFanMaxRPM", "4000");
                ConnectHandler.CUset("FUFanDefaulMinRPM", "1000");
                ConnectHandler.CUset("FUFanDefaulMaxRPM", "4000");
                TimeUnit.SECONDS.sleep(3);
            } else {
                return;
            }
        }
    }

    private boolean checkFansOn() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        boolean result = true;
        Map<Fan, Double> fanSpeeds = fanSpeeds();
        for (Map.Entry<Fan, Double> i : fanSpeeds.entrySet()) {
            if (i.getValue() >= 100) {
                errorFans.add(i.getKey());
                result = false;
            }
        }
        return result;
    }

    private boolean isFanOn() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        return fanSpeeds().values().stream().anyMatch(p -> p < 100);
    }

    private void writeFUNInfo() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        ConnectHandler.CUset("FUFanModels", main.getFanModel());
        ConnectHandler.CUset("FUHwNumber", main.getFun().getHW());
    }

    private boolean checkFUNInfo() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        results.put(FUNTestResult.FUN_PS, ConnectHandler.CUget("FUPowerStatus").equals("2"));
        results.put(FUNTestResult.FUN_NAME, ConnectHandler.CUget("FUName").equals(main.getFun().getName()));
        results.put(FUNTestResult.FUN_POWER, ConnectHandler.CUget("FUPower").equals(String.valueOf(main.getFun().getPower())));
        results.put(FUNTestResult.FUN_MODEL, ConnectHandler.CUget("FUFanModels").equals(main.getFanModel()));
        results.put(FUNTestResult.REV, ConnectHandler.CUget("FUHwNumber").equals(main.getFun().getHW()));
        results.put(FUNTestResult.EEPROM, ConnectHandler.CUget("FUEEPROMState").equals("0"));
        return results.values().stream().allMatch(p -> p);
    }

    private boolean testFUN10() throws InterruptedException, ParserConfigurationException, TransformerException, SAXException, IOException {
        if (results.containsKey(FUNTestResult._10) && results.get(FUNTestResult._10)) {
            return true;
        }
        boolean testResult = false;
        for (int i = 0; i < 23; i++) {
            if (testFUNSpeed(10)) {
                TimeUnit.SECONDS.sleep(2);
                testResult = testFUNSpeed(10);
                results.put(FUNTestResult._10, testResult);
                return testResult;
            }
        }
        results.put(FUNTestResult._10, testResult);
        return testResult;
    }

    private boolean testFUN100() throws InterruptedException, ParserConfigurationException, TransformerException, SAXException, IOException {
        if (results.containsKey(FUNTestResult._100) && results.get(FUNTestResult._100)) {
            return true;
        }
        boolean testResult = false;
        for (int i = 0; i < 23; i++) {
            if (testFUNSpeed(100)) {
                TimeUnit.SECONDS.sleep(2);
                testResult = testFUNSpeed(100);
                results.put(FUNTestResult._100, testResult);
                return testResult;
            }
        }
        results.put(FUNTestResult._100, testResult);
        return testResult;
    }

    private boolean testFUNSpeed(int speed) throws ParserConfigurationException, IOException, SAXException, TransformerException, InterruptedException {
        ConnectHandler.CUset("FUFanMode", "1");
        ConnectHandler.CUset("FUFanSpeedSet", String.valueOf(speed));
        TimeUnit.SECONDS.sleep(3);
        int low = 0;
        int high = 0;
        if (speed == 10) {
            low = 7;
            high = 13;
        } else if (speed == 100) {
            low = 93;
            high = 100;
        }
        int finalHigh = high;
        int finalLow = low;
        return fanSpeeds().values().stream().noneMatch(p -> (p < finalLow || p > finalHigh));
    }

    private void setFUNAuto() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        ConnectHandler.CUset("FUFanMode", "0");
        ConnectHandler.CUset("FUFanSpeedSet", "50");
    }

    private Map<Fan, Double> fanSpeeds() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Map<Fan, Double> result = new TreeMap<>();
        for (Fan i : Fan.FAN_LISTS.get(main.getFun())) {
            result.put(i, Double.parseDouble(ConnectHandler.CUget("FUFan" + i.toString().substring(1) + "Speed")));
        }
        return result;
    }

    private void testFail() {
        start.setDisable(false);
        testDetails.setVisible(true);
        testResult.setVisible(true);
        testResultArea.setVisible(true);
        testResultArea.setFill(Color.ERROR.getPaint());
    }

    private void testPass() {
        testDetails.setVisible(true);
        testResult.setVisible(true);
        testResultArea.setVisible(true);
        testResultArea.setFill(Color.OK.getPaint());
        end.setVisible(true);
    }

    @FXML
    private void end() {
        end.setDisable(true);
        main.oneTestFinished();
    }

    @FXML
    private void showTestResults() {
        main.showFUNTestResultsWindow(errorFans, results);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}