package sample.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import org.xml.sax.SAXException;
import sample.Main;
import sample.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CrateTestWindowController {
    private Main main;
    private ObservableMap<Slot, Label> slotLabels = FXCollections.observableHashMap();
    private ObservableMap<Slot, Rectangle> slots = FXCollections.observableHashMap();
    private ObservableMap<CrateTestResult, Boolean> results = FXCollections.observableHashMap();
    private ObservableMap<Slot, SlotTestResult> slotTestResults = FXCollections.observableHashMap();
    @FXML
    private Button start;
    @FXML
    private Label slot1Label;
    @FXML
    private Label slot2Label;
    @FXML
    private Label slot3Label;
    @FXML
    private Label slot4Label;
    @FXML
    private Label slot5Label;
    @FXML
    private Label slot6Label;
    @FXML
    private Label slot7Label;
    @FXML
    private Label slot8Label;
    @FXML
    private Label slot9Label;
    @FXML
    private Label slot10Label;
    @FXML
    private Label slot11Label;
    @FXML
    private Label slot12Label;
    @FXML
    private Label slot13Label;
    @FXML
    private Rectangle slot1;
    @FXML
    private Rectangle slot2;
    @FXML
    private Rectangle slot3;
    @FXML
    private Rectangle slot4;
    @FXML
    private Rectangle slot5;
    @FXML
    private Rectangle slot6;
    @FXML
    private Rectangle slot7;
    @FXML
    private Rectangle slot8;
    @FXML
    private Rectangle slot9;
    @FXML
    private Rectangle slot10;
    @FXML
    private Rectangle slot11;
    @FXML
    private Rectangle slot12;
    @FXML
    private Rectangle slot13;
    @FXML
    private volatile Label testResult;
    @FXML
    private Rectangle testResultArea;
    @FXML
    private Button testDetails;
    @FXML
    private Button end;

    @FXML
    private void initialize() {
        slotLabels.put(Slot._1, slot1Label);
        slotLabels.put(Slot._2, slot2Label);
        slotLabels.put(Slot._3, slot3Label);
        slotLabels.put(Slot._4, slot4Label);
        slotLabels.put(Slot._5, slot5Label);
        slotLabels.put(Slot._6, slot6Label);
        slotLabels.put(Slot._7, slot7Label);
        slotLabels.put(Slot._8, slot8Label);
        slotLabels.put(Slot._9, slot9Label);
        slotLabels.put(Slot._10, slot10Label);
        slotLabels.put(Slot._11, slot11Label);
        slotLabels.put(Slot._12, slot12Label);
        slotLabels.put(Slot._13, slot13Label);
        slots.put(Slot._1, slot1);
        slots.put(Slot._2, slot2);
        slots.put(Slot._3, slot3);
        slots.put(Slot._4, slot4);
        slots.put(Slot._5, slot5);
        slots.put(Slot._6, slot6);
        slots.put(Slot._7, slot7);
        slots.put(Slot._8, slot8);
        slots.put(Slot._9, slot9);
        slots.put(Slot._10, slot10);
        slots.put(Slot._11, slot11);
        slots.put(Slot._12, slot12);
        slots.put(Slot._13, slot13);
        testResult.setVisible(false);
        testResultArea.setVisible(false);
        testDetails.setVisible(false);
        end.setVisible(false);
        slotLabels.values().forEach(p -> p.setVisible(false));
        slots.values().forEach(p -> p.setVisible(false));
    }

    @FXML
    private void start() {
        testDetails.setVisible(false);
        testResult.setVisible(false);
        testResultArea.setVisible(false);
        results.clear();
        start.setDisable(true);
        Slot.SLOT_LISTS.get(main.getCrate()).stream().filter(p -> slotTestResults.containsKey(p) && slotTestResults.get(p).equals(SlotTestResult.FAILED)).forEach(p -> {
            slots.get(p).setFill(Color.SLOT_WAITING.getPaint());
            slotTestResults.remove(p);
        });
        try {
            writeInfo();
            TimeUnit.SECONDS.sleep(3);
            results.put(CrateTestResult.SNMP, ConnectHandler.CUget("sysObjectID").equals(main.getCrate().getSNMP()));
            results.put(CrateTestResult.CONDITION, ConnectHandler.CUget("VState").equals("0"));
            results.put(CrateTestResult.EEPROM, ConnectHandler.CUget("VEEPROMState").equals("0"));
            results.put(CrateTestResult.MODEL, ConnectHandler.CUget("VPtNumber").equals(main.getCrate().getName()));
        } catch (Exception e) {
            main.error();
            e.printStackTrace();
            return;
        }
        for (Slot i : Slot.SLOT_LISTS.get(main.getCrate())) {
            slotLabels.get(i).setVisible(true);
            slots.get(i).setVisible(true);
        }
        if (!checkResults()) {
            testResult.setText(ResultMessage.FAILED.getMessage());
            testFail();
        } else {
            testSlots();
        }
    }

    private void writeInfo() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        ConnectHandler.CUset("sysObjectID", main.getCrate().getSNMP());
        ConnectHandler.CUset("VPtNumber", main.getCrate().getName());
    }

    private void testSlots() {
        Task<Void> slotTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int countdown = 30;
                while (countdown-- != 0) {
                    Set<Integer> devices = ConnectHandler.getDeviceList().keySet();
                    for (Integer i : devices) {
                        if (i <= main.getCrate().getSlotAmount()) {
                            Slot slot = Slot.valueOf("_" + i);
                            if (ConnectHandler.CUget("Slot" + i + "PowerStatus").equals("2") &&
                                    !ConnectHandler.CUget("Slot" + i + "Name").equals("")) {
                                if (!slotTestResults.containsKey(slot) ||
                                        !slotTestResults.get(slot).equals(SlotTestResult.PASSED)) {
                                    slotTestResults.put(slot, SlotTestResult.PASSED);
                                    slots.get(slot).setFill(Color.SLOT_OK.getPaint());
                                    if (slotTestResults.size() == main.getCrate().getSlotAmount() &&
                                            checkSlotResults()) {
                                        updateMessage(ResultMessage.PASSED.getMessage());
                                        testPass();
                                        return null;
                                    }
                                    countdown += 10;
                                }
                            } else if (!slotTestResults.containsKey(slot)) {
                                slotTestResults.put(slot, SlotTestResult.PS_FAILED);
                                slots.get(slot).setFill(Color.SLOT_PS_ERROR.getPaint());
                            }
                        }
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
                if (slotTestResults.size() == main.getCrate().getSlotAmount() &&
                        checkSlotResults()) {
                    updateMessage(ResultMessage.PASSED.getMessage());
                    testPass();
                } else {
                    if (slotTestResults.size() != main.getCrate().getSlotAmount()) {
                        for (Slot i : Slot.SLOT_LISTS.get(main.getCrate())) {
                            if (!slotTestResults.containsKey(i)) {
                                slotTestResults.put(i, SlotTestResult.FAILED);
                                slots.get(i).setFill(Color.SLOT_ERROR.getPaint());
                            }
                        }
                    }
                    updateMessage(ResultMessage.FAILED.getMessage());
                    testFail();
                }
                return null;
            }
        };
        testResult.textProperty().bind(slotTask.messageProperty());
        new Thread(slotTask).start();
    }

    private boolean checkResults() {
        return results.values().stream().allMatch(p -> p);
    }

    private boolean checkSlotResults() {
        return slotTestResults.values().stream().allMatch(p -> p.equals(SlotTestResult.PASSED));
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
        main.showCrateTestResultsWindow(slotTestResults, results);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}