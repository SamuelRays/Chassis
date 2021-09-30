package sample.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sample.Main;
import sample.util.*;

public class MainWindowController {
    private Main main;
    private ObservableList<String> testers = DataSource.getTesters();
    @FXML
    private ChoiceBox<String> tester;
    @FXML
    private TextField ip;
    @FXML
    private Button startButton;
    @FXML
    private Button settings;

    @FXML
    private void initialize() {
        tester.setItems(testers);
        if (!testers.isEmpty()) {
            tester.setValue(testers.get(0));
        }
        String URL = ConnectHandler.getURL();
        ip.setText(URL.substring(URL.lastIndexOf("/") + 1));
    }

    @FXML
    private void showSetting() {
        main.showSettings();
    }

    @FXML
    private void start() {
        if (!checkIP()) {
            Alerter.alert(main.getPrimaryStage(), "Некорректный ip", "Некорректно введён ip-адрес CU",
                    "Пожалуйста, проверьте правильность написания ip-адреса.\nПример: 192.168.1.1");
        } else if (!main.isAreSettingsSet()) {
            Alerter.alert(main.getPrimaryStage(), "Нет настроек", "Не выбраны настройки теста",
                    "Пожалуйста, выберите настройки теста.");
        } else if (tester.getValue() == null) {
            Alerter.alert(main.getPrimaryStage(), "Нет настроек", "Не выбраны настройки теста",
                    "Пожалуйста, выберите тестировщика.");
        } else {
            try {
                ConnectHandler.setURL("http://" + ip.getText());
                ConnectHandler.connect(main.getCrate());
            } catch (Exception e) {
                e.printStackTrace();
                Alerter.alert(main.getPrimaryStage(), "Нет соединения", "Невозможно подключиться к CU",
                        "Пожалуйста, проверте подключен ли кабель,\nи правильно ли записан IP-адрес CU.");
                return;
            }
            main.setTester(tester.getValue());
            startButton.setDisable(true);
            settings.setDisable(true);
            tester.setDisable(true);
            ip.setDisable(true);
            if (isTestNeeded()) {
                if (main.getDeviceType().startsWith("FUN")) {
                    main.oneTestFinished();
                    main.showFUNTestWindow();
                } else if (main.getCurrentSettings().get(TestSetting.NO_FUN)) {
                    main.oneTestFinished();
                    main.showCrateTestWindow(true);
                } else {
                    main.showFUNTestWindow();
                    main.showCrateTestWindow(false);
                }
            } else {
                if (!main.getCurrentSettings().get(TestSetting.SN_ONLY)) {
                    try {
                        String crateSN = ConnectHandler.CUget("VSrNumber");
                        String FUNSN = ConnectHandler.CUget("FUSrNumber");
                        String fanModel = ConnectHandler.CUget("FUFanModels");
                        if (!main.getCurrentSettings().get(TestSetting.NO_FUN) &&
                                !main.getFanModel().equals(fanModel)) {
                            Alerter.alert(main.getPrimaryStage(), "Несовпадение модели",
                                    "Не совпадает модель вентиляторов: выбранная в настройках теста " +
                                            "и прописанная в самом FUN",
                                    "Пожалуйста, выберите правильную модель вентиляторов" +
                                            "\nили проверьте, правильно ли записана модель в самом FUN.");
                            reset();
                            return;
                        } else {
                            main.getCurrentSNs().put(Part.CRATE, crateSN);
                            main.getCurrentSNs().put(Part.FUN, FUNSN);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Alerter.alert(main.getPrimaryStage(), "Ошибка чтения данных",
                                "Проблема с чтением данных с CU",
                                "Пожалуйста, проверьте соединение с CU.");
                        reset();
                        return;
                    }
                }
                main.oneTestFinished();
                main.oneTestFinished();
            }
        }
    }

    private boolean checkIP() {
        String[] strings;
        try {
            strings = ip.getText().split("\\.");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        if (strings.length != 4) {
            return false;
        }
        for (String i : strings) {
            int j;
            try {
                j = Integer.parseInt(i);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
            if (j < 0 || j > 255) {
                return false;
            }
        }
        return true;
    }

    private boolean isTestNeeded() {
        return !(main.getCurrentSettings().get(TestSetting.PROTOCOL_ONLY) ||
                main.getCurrentSettings().get(TestSetting.SN_ONLY));
    }

    public void reset() {
        startButton.setDisable(false);
        settings.setDisable(false);
        tester.setDisable(false);
        ip.setDisable(false);
        main.setTester(null);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}