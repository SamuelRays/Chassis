package sample.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.util.Alerter;
import sample.util.DataSource;
import sample.util.TestSetting;

import java.util.HashMap;
import java.util.Map;

public class SettingsController {
    private Main main;
    private Stage dialogStage;
    private ObservableList<String> devices = DataSource.getDevices();
    private Map<TestSetting, CheckBox> settings = new HashMap<>();
    private ObservableList<String> fanModels = DataSource.getFans();
    private boolean justOpened = true;
    @FXML
    private ChoiceBox<String> deviceType;
    @FXML
    private CheckBox ZIP;
    @FXML
    private CheckBox recheck;
    @FXML
    private CheckBox noFUN;
    @FXML
    private CheckBox SNOnly;
    @FXML
    private CheckBox protocolOnly;
    @FXML
    private TextField backPlanePN;
    @FXML
    private TextField FUNPN;
    @FXML
    private ChoiceBox<String> fanModel;

    @FXML
    private void initialize() {
        deviceType.setItems(devices);
        deviceType.setOnAction(event -> setCheckBoxes());
        fanModel.setItems(fanModels);
        settings.put(TestSetting.ZIP, ZIP);
        settings.put(TestSetting.RECHECK, recheck);
        settings.put(TestSetting.NO_FUN, noFUN);
        settings.put(TestSetting.SN_ONLY, SNOnly);
        settings.put(TestSetting.PROTOCOL_ONLY, protocolOnly);
        settings.values().forEach(p -> p.setDisable(true));
        backPlanePN.setDisable(true);
        FUNPN.setDisable(true);
        fanModel.setDisable(true);
    }

    @FXML
    private void setCheckBoxes() {
        if (justOpened) {
            justOpened = false;
            recheck.setDisable(false);
            SNOnly.setDisable(false);
            protocolOnly.setDisable(false);
            FUNPN.setDisable(false);
            fanModel.setDisable(false);
            if (deviceType.getValue().startsWith("Шасси")) {
                noFUN.setDisable(false);
                backPlanePN.setDisable(false);
            } else if (deviceType.getValue().startsWith("FUN")) {
                ZIP.setDisable(false);
            }
        } else {
            if (deviceType.getValue().startsWith("Шасси")) {
                ZIP.setDisable(true);
                ZIP.setSelected(false);
                noFUN.setDisable(false);
                if (!(recheck.isSelected() || (SNOnly.isSelected() && !protocolOnly.isSelected()))) {
                    backPlanePN.setDisable(false);
                }
            } else if (deviceType.getValue().startsWith("FUN")) {
                ZIP.setDisable(false);
                noFUN.setDisable(true);
                noFUN.setSelected(false);
                backPlanePN.setDisable(true);
                backPlanePN.setText(null);
                if (!(recheck.isSelected() || (SNOnly.isSelected() && !protocolOnly.isSelected()))) {
                    FUNPN.setDisable(false);
                    fanModel.setDisable(false);
                }
            }
        }
    }

    @FXML
    private void setRecheck() {
        if (!recheck.isSelected()) {
            SNOnly.setDisable(false);
            protocolOnly.setDisable(false);
            if (deviceType.getValue().startsWith("Шасси")) {
                backPlanePN.setDisable(false);
            }
            if (!noFUN.isSelected()) {
                fanModel.setDisable(false);
                FUNPN.setDisable(false);
            }
        } else {
            backPlanePN.setDisable(true);
            backPlanePN.setText(null);
            FUNPN.setDisable(true);
            FUNPN.setText(null);
            SNOnly.setDisable(true);
            protocolOnly.setDisable(true);
        }
    }

    @FXML
    private void setSNOnly() {
        if (SNOnly.isSelected()) {
            recheck.setDisable(true);
            if (!protocolOnly.isSelected()) {
                backPlanePN.setDisable(true);
                backPlanePN.setText(null);
                FUNPN.setDisable(true);
                FUNPN.setText(null);
                fanModel.setDisable(true);
                fanModel.setValue(null);
            }
        } else {
            if (!recheck.isSelected()) {
                if (deviceType.getValue().startsWith("Шасси")) {
                    backPlanePN.setDisable(false);
                }
                if (!noFUN.isSelected()) {
                    fanModel.setDisable(false);
                    FUNPN.setDisable(false);
                }
            }
            if (!protocolOnly.isSelected()) {
                recheck.setDisable(false);
            }
        }
    }

    @FXML
    private void setProtocolOnly() {
        if (protocolOnly.isSelected()) {
            recheck.setDisable(true);
            if (deviceType.getValue().startsWith("Шасси")) {
                backPlanePN.setDisable(false);
            }
            if (!noFUN.isSelected()) {
                fanModel.setDisable(false);
                FUNPN.setDisable(false);
            }
        } else if (!SNOnly.isSelected()) {
            recheck.setDisable(false);
        } else {
            backPlanePN.setDisable(true);
            backPlanePN.setText(null);
            FUNPN.setDisable(true);
            FUNPN.setText(null);
            fanModel.setDisable(true);
            fanModel.setValue(null);
        }
    }

    @FXML
    private void setNoFUN() {
        if (noFUN.isSelected()) {
            FUNPN.setDisable(true);
            FUNPN.setText(null);
            fanModel.setDisable(true);
            fanModel.setValue(null);
        } else if (!(recheck.isSelected() || (SNOnly.isSelected() && !protocolOnly.isSelected()))) {
            FUNPN.setDisable(false);
            fanModel.setDisable(false);
        } else if (recheck.isSelected()) {
            fanModel.setDisable(false);
        }
    }

    @FXML
    private void checkValid() {
        if (deviceType.getValue() == null) {
            Alerter.alert(dialogStage, "Не выбрано", "Не выбран тип устройства",
                    "Пожалуйста, выберите тип устройства.");
        } else if (!isPNWritten()) {
            Alerter.alert(dialogStage, "Нет номеров плат", "Не введен(ы) номер(а) плат(ы)",
                    "Пожалуйста, введите номер(а) плат(ы).");
        } else if (!isFanModelChosen()) {
            Alerter.alert(dialogStage, "Нет выбрано", "Не введена модель вентиляторов",
                    "Пожалуйста, выберите модель вентиляторов.");
        } else {
            main.setAreSettingsSet(true);
            main.setSettings(this);
            dialogStage.close();
        }
    }

    @FXML
    private void setLastSettings() {
        try {
            DataSource.readSettings(this);
            setRecheck();
            setSNOnly();
            setProtocolOnly();
            setNoFUN();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Alerter.alert(dialogStage, "Проблема чтения файла",
                    "Проблема с чтением файла с данными о последних настройках",
                    "Пожалуйста, проверьте, не повреждён ли файл с данными о настройках.");
        }
    }

    private boolean isPNWritten() {
        return (backPlanePN.isDisable() || backPlanePN.getText() != null) &&
                (FUNPN.isDisable() || FUNPN.getText() != null);
    }

    private boolean isFanModelChosen() {
        return fanModel.isDisable() || fanModel.getValue() != null;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public ChoiceBox<String> getDeviceType() {
        return deviceType;
    }

    public Map<TestSetting, CheckBox> getSettings() {
        return settings;
    }

    public TextField getBackPlanePN() {
        return backPlanePN;
    }

    public TextField getFUNPN() {
        return FUNPN;
    }

    public ChoiceBox<String> getFanModel() {
        return fanModel;
    }
}