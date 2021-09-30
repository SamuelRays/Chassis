package sample.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.Main;
import sample.util.Color;
import sample.util.FUNTestResult;
import sample.util.Fan;
import sample.util.ResultMessage;

import java.util.List;
import java.util.Map;

public class FUNTestResultsWindowController {
    private Main main;
    private Stage dialogStage;
    private ObservableMap<Fan, Rectangle> fanAreas = FXCollections.observableHashMap();
    private ObservableMap<Fan, Label> fanResults = FXCollections.observableHashMap();
    private ObservableMap<Fan, Label> fanNames = FXCollections.observableHashMap();
    private ObservableMap<FUNTestResult, Rectangle> testAreas = FXCollections.observableHashMap();
    private ObservableMap<FUNTestResult, Label> testResults = FXCollections.observableHashMap();
    @FXML
    private AnchorPane pane;
    @FXML
    private Label fan1;
    @FXML
    private Label fan2;
    @FXML
    private Label fan3;
    @FXML
    private Label fan4;
    @FXML
    private Label fan5;
    @FXML
    private Label fan6;
    @FXML
    private Rectangle fan1Area;
    @FXML
    private Rectangle fan2Area;
    @FXML
    private Rectangle fan3Area;
    @FXML
    private Rectangle fan4Area;
    @FXML
    private Rectangle fan5Area;
    @FXML
    private Rectangle fan6Area;
    @FXML
    private Label fan1Result;
    @FXML
    private Label fan2Result;
    @FXML
    private Label fan3Result;
    @FXML
    private Label fan4Result;
    @FXML
    private Label fan5Result;
    @FXML
    private Label fan6Result;
    @FXML
    private Rectangle test10Area;
    @FXML
    private Rectangle test100Area;
    @FXML
    private Rectangle PSArea;
    @FXML
    private Rectangle nameArea;
    @FXML
    private Rectangle powerArea;
    @FXML
    private Rectangle modelArea;
    @FXML
    private Rectangle revArea;
    @FXML
    private Rectangle EEPROMArea;
    @FXML
    private Label test10Result;
    @FXML
    private Label test100Result;
    @FXML
    private Label PSResult;
    @FXML
    private Label nameResult;
    @FXML
    private Label powerResult;
    @FXML
    private Label modelResult;
    @FXML
    private Label revResult;
    @FXML
    private Label EEPROMResult;

    @FXML
    private void initialize() {
        fanAreas.put(Fan._1, fan1Area);
        fanAreas.put(Fan._2, fan2Area);
        fanAreas.put(Fan._3, fan3Area);
        fanAreas.put(Fan._4, fan4Area);
        fanAreas.put(Fan._5, fan5Area);
        fanAreas.put(Fan._6, fan6Area);
        fanResults.put(Fan._1, fan1Result);
        fanResults.put(Fan._2, fan2Result);
        fanResults.put(Fan._3, fan3Result);
        fanResults.put(Fan._4, fan4Result);
        fanResults.put(Fan._5, fan5Result);
        fanResults.put(Fan._6, fan6Result);
        fanNames.put(Fan._1, fan1);
        fanNames.put(Fan._2, fan2);
        fanNames.put(Fan._3, fan3);
        fanNames.put(Fan._4, fan4);
        fanNames.put(Fan._5, fan5);
        fanNames.put(Fan._6, fan6);
        fanAreas.values().forEach(p -> p.setVisible(false));
        fanResults.values().forEach(p -> p.setVisible(false));
        fanNames.values().forEach(p -> p.setVisible(false));
        testAreas.put(FUNTestResult.EEPROM, EEPROMArea);
        testAreas.put(FUNTestResult.REV, revArea);
        testAreas.put(FUNTestResult.FUN_MODEL, modelArea);
        testAreas.put(FUNTestResult._10, test10Area);
        testAreas.put(FUNTestResult._100, test100Area);
        testAreas.put(FUNTestResult.FUN_NAME, nameArea);
        testAreas.put(FUNTestResult.FUN_POWER, powerArea);
        testAreas.put(FUNTestResult.FUN_PS, PSArea);
        testResults.put(FUNTestResult.EEPROM, EEPROMResult);
        testResults.put(FUNTestResult.REV, revResult);
        testResults.put(FUNTestResult.FUN_MODEL, modelResult);
        testResults.put(FUNTestResult._10, test10Result);
        testResults.put(FUNTestResult._100, test100Result);
        testResults.put(FUNTestResult.FUN_NAME, nameResult);
        testResults.put(FUNTestResult.FUN_POWER, powerResult);
        testResults.put(FUNTestResult.FUN_PS, PSResult);
    }

    @FXML
    private void exit() {
        dialogStage.close();
    }

    public void setResults(List<Fan> errorFans, Map<FUNTestResult, Boolean> results) {
        for (Fan i : Fan.FAN_LISTS.get(main.getFun())) {
            fanAreas.get(i).setVisible(true);
            fanResults.get(i).setVisible(true);
            fanNames.get(i).setVisible(true);
        }
        if (!errorFans.isEmpty()) {
            for (Fan i : errorFans) {
                fanResults.get(i).setText(ResultMessage.ERROR.getMessage());
                fanAreas.get(i).setFill(Color.ERROR.getPaint());
            }
            pane.setVisible(false);
        } else {
            for (FUNTestResult i : FUNTestResult.values()) {
                if (results.containsKey(i)) {
                    if (results.get(i)) {
                        testResults.get(i).setText(ResultMessage.OK.getMessage());
                        testAreas.get(i).setFill(Color.OK.getPaint());
                    } else {
                        testResults.get(i).setText(ResultMessage.ERROR.getMessage());
                        testAreas.get(i).setFill(Color.ERROR.getPaint());
                    }
                } else {
                    testResults.get(i).setText(ResultMessage.UNREACHED.getMessage());
                    testAreas.get(i).setFill(Color.UNREACHED.getPaint());
                }
            }
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}