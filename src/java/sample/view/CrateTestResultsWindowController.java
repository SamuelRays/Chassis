package sample.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.Main;
import sample.util.*;

import java.util.Map;

public class CrateTestResultsWindowController {
    private Main main;
    private Stage dialogStage;
    private ObservableMap<Slot, Rectangle> slotAreas = FXCollections.observableHashMap();
    private ObservableMap<Slot, Label> slotResults = FXCollections.observableHashMap();
    private ObservableMap<Slot, Label> slotNames = FXCollections.observableHashMap();
    private ObservableMap<CrateTestResult, Rectangle> testAreas = FXCollections.observableHashMap();
    private ObservableMap<CrateTestResult, Label> testResults = FXCollections.observableHashMap();
    @FXML
    private AnchorPane pane;
    @FXML
    private Label slot1;
    @FXML
    private Label slot2;
    @FXML
    private Label slot3;
    @FXML
    private Label slot4;
    @FXML
    private Label slot5;
    @FXML
    private Label slot6;
    @FXML
    private Label slot7;
    @FXML
    private Label slot8;
    @FXML
    private Label slot9;
    @FXML
    private Label slot10;
    @FXML
    private Label slot11;
    @FXML
    private Label slot12;
    @FXML
    private Label slot13;
    @FXML
    private Rectangle slot1Area;
    @FXML
    private Rectangle slot2Area;
    @FXML
    private Rectangle slot3Area;
    @FXML
    private Rectangle slot4Area;
    @FXML
    private Rectangle slot5Area;
    @FXML
    private Rectangle slot6Area;
    @FXML
    private Rectangle slot7Area;
    @FXML
    private Rectangle slot8Area;
    @FXML
    private Rectangle slot9Area;
    @FXML
    private Rectangle slot10Area;
    @FXML
    private Rectangle slot11Area;
    @FXML
    private Rectangle slot12Area;
    @FXML
    private Rectangle slot13Area;
    @FXML
    private Label slot1Result;
    @FXML
    private Label slot2Result;
    @FXML
    private Label slot3Result;
    @FXML
    private Label slot4Result;
    @FXML
    private Label slot5Result;
    @FXML
    private Label slot6Result;
    @FXML
    private Label slot7Result;
    @FXML
    private Label slot8Result;
    @FXML
    private Label slot9Result;
    @FXML
    private Label slot10Result;
    @FXML
    private Label slot11Result;
    @FXML
    private Label slot12Result;
    @FXML
    private Label slot13Result;
    @FXML
    private Rectangle SNMPArea;
    @FXML
    private Rectangle conditionArea;
    @FXML
    private Rectangle EEPROMArea;
    @FXML
    private Rectangle modelArea;
    @FXML
    private Label SNMPResult;
    @FXML
    private Label conditionResult;
    @FXML
    private Label EEPROMResult;
    @FXML
    private Label modelResult;

    @FXML
    private void initialize() {
        slotAreas.put(Slot._1, slot1Area);
        slotAreas.put(Slot._2, slot2Area);
        slotAreas.put(Slot._3, slot3Area);
        slotAreas.put(Slot._4, slot4Area);
        slotAreas.put(Slot._5, slot5Area);
        slotAreas.put(Slot._6, slot6Area);
        slotAreas.put(Slot._7, slot7Area);
        slotAreas.put(Slot._8, slot8Area);
        slotAreas.put(Slot._9, slot9Area);
        slotAreas.put(Slot._10, slot10Area);
        slotAreas.put(Slot._11, slot11Area);
        slotAreas.put(Slot._12, slot12Area);
        slotAreas.put(Slot._13, slot13Area);
        slotResults.put(Slot._1, slot1Result);
        slotResults.put(Slot._2, slot2Result);
        slotResults.put(Slot._3, slot3Result);
        slotResults.put(Slot._4, slot4Result);
        slotResults.put(Slot._5, slot5Result);
        slotResults.put(Slot._6, slot6Result);
        slotResults.put(Slot._7, slot7Result);
        slotResults.put(Slot._8, slot8Result);
        slotResults.put(Slot._9, slot9Result);
        slotResults.put(Slot._10, slot10Result);
        slotResults.put(Slot._11, slot11Result);
        slotResults.put(Slot._12, slot12Result);
        slotResults.put(Slot._13, slot13Result);
        slotNames.put(Slot._1, slot1);
        slotNames.put(Slot._2, slot2);
        slotNames.put(Slot._3, slot3);
        slotNames.put(Slot._4, slot4);
        slotNames.put(Slot._5, slot5);
        slotNames.put(Slot._6, slot6);
        slotNames.put(Slot._7, slot7);
        slotNames.put(Slot._8, slot8);
        slotNames.put(Slot._9, slot9);
        slotNames.put(Slot._10, slot10);
        slotNames.put(Slot._11, slot11);
        slotNames.put(Slot._12, slot12);
        slotNames.put(Slot._13, slot13);
        slotAreas.values().forEach(p -> p.setVisible(false));
        slotResults.values().forEach(p -> p.setVisible(false));
        slotNames.values().forEach(p -> p.setVisible(false));
        testAreas.put(CrateTestResult.SNMP, SNMPArea);
        testAreas.put(CrateTestResult.CONDITION, conditionArea);
        testAreas.put(CrateTestResult.EEPROM, EEPROMArea);
        testAreas.put(CrateTestResult.MODEL, modelArea);
        testResults.put(CrateTestResult.SNMP, SNMPResult);
        testResults.put(CrateTestResult.CONDITION, conditionResult);
        testResults.put(CrateTestResult.EEPROM, EEPROMResult);
        testResults.put(CrateTestResult.MODEL, modelResult);
    }

    @FXML
    private void exit() {
        dialogStage.close();
    }

    public void setResults(Map<Slot, SlotTestResult> slotTestResults, Map<CrateTestResult, Boolean> results) {
        boolean isSlotTestResultShowed = true;
        for (CrateTestResult i : CrateTestResult.values()) {
            if (results.get(i)) {
                testResults.get(i).setText(ResultMessage.OK.getMessage());
                testAreas.get(i).setFill(Color.OK.getPaint());
            } else {
                testResults.get(i).setText(ResultMessage.ERROR.getMessage());
                testAreas.get(i).setFill(Color.ERROR.getPaint());
                isSlotTestResultShowed = false;
            }
        }
        if (!isSlotTestResultShowed) {
            pane.setVisible(false);
        } else {
            for (Slot i : Slot.SLOT_LISTS.get(main.getCrate())) {
                slotNames.get(i).setVisible(true);
                slotAreas.get(i).setVisible(true);
                slotResults.get(i).setVisible(true);
                if (slotTestResults.get(i).equals(SlotTestResult.PASSED)) {
                    slotResults.get(i).setText(ResultMessage.OK.getMessage());
                    slotAreas.get(i).setFill(Color.OK.getPaint());
                } else if (slotTestResults.get(i).equals(SlotTestResult.PS_FAILED)) {
                    slotResults.get(i).setText(ResultMessage.PS_ERROR.getMessage());
                    slotAreas.get(i).setFill(Color.PS_ERROR.getPaint());
                } else {
                    slotResults.get(i).setText(ResultMessage.ERROR.getMessage());
                    slotAreas.get(i).setFill(Color.ERROR.getPaint());
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