package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.util.*;
import sample.view.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main extends Application {
    private Stage primaryStage;
    private MainWindowController mainWindowController;
    private AnchorPane root;
    private AnchorPane funTestPane;
    private AnchorPane crateTestPane;
    private AnchorPane SNPane;
    private boolean areSettingsSet;
    private volatile int finishedTestsAmount;
    private int testsAmount = 2;
    private String deviceType;
    private Crate crate;
    private FUN fun;
    private String tester;
    private String fanModel;
    private ObservableMap<TestSetting, Boolean> currentSettings = FXCollections.observableHashMap();
    private ObservableMap<Part, String> currentSNs = FXCollections.observableHashMap();

    public static void main(String[] args) {
        launch(args);
        try {
            ConnectHandler.logout();
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initMainWindow();
    }

    private void initMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MainWindow.fxml"));
            root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Tester 3000 Double Kill");
            primaryStage.setResizable(false);
            primaryStage.sizeToScene();
            mainWindowController = loader.getController();
            mainWindowController.setMain(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSettings() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Settings.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.sizeToScene();
            dialogStage.setTitle("Настройки");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);
            SettingsController controller = loader.getController();
            controller.setMain(this);
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFUNTestWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/FUNTestWindow.fxml"));
            funTestPane = (AnchorPane) loader.load();
            funTestPane.setLayoutX(180);
            funTestPane.setLayoutY(0);
            root.getChildren().add(funTestPane);
            FUNTestWindowController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCrateTestWindow(boolean isOnly) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CrateTestWindow.fxml"));
            crateTestPane = (AnchorPane) loader.load();
            crateTestPane.setLayoutY(0);
            if (isOnly) {
                crateTestPane.setLayoutX(180);
            } else {
                crateTestPane.setLayoutX(480);
            }
            root.getChildren().add(crateTestPane);
            CrateTestWindowController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFUNTestResultsWindow(List<Fan> errorFans, Map<FUNTestResult, Boolean> results) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/FUNTestResultsWindow.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.sizeToScene();
            dialogStage.setTitle("Результаты теста FUN");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);
            FUNTestResultsWindowController controller = loader.getController();
            controller.setMain(this);
            controller.setDialogStage(dialogStage);
            controller.setResults(errorFans, results);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCrateTestResultsWindow(Map<Slot, SlotTestResult> slotTestResults, Map<CrateTestResult, Boolean> results) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CrateTestResultsWindow.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.sizeToScene();
            dialogStage.setTitle("Результаты теста крейта");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);
            CrateTestResultsWindowController controller = loader.getController();
            controller.setMain(this);
            controller.setDialogStage(dialogStage);
            controller.setResults(slotTestResults, results);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSettings(SettingsController controller) {
        deviceType = controller.getDeviceType().getValue();
        currentSettings.clear();
        for (Map.Entry<TestSetting, CheckBox> i : controller.getSettings().entrySet()) {
            currentSettings.put(i.getKey(), i.getValue().isSelected());
        }
        currentSNs.clear();
        String BPPN = controller.getBackPlanePN().getText();
        String FUNPN = controller.getFUNPN().getText();
        fanModel = controller.getFanModel().getValue();
        currentSNs.put(Part.BACKPLANE, BPPN == null ? " " : BPPN);
        currentSNs.put(Part.FUNPN, FUNPN == null ? " " : FUNPN);
        if (currentSettings.get(TestSetting.ZIP)) {
            if (deviceType.endsWith("3")) {
                fun = FUN._3ZIP;
            } else if (deviceType.endsWith("6")) {
                fun = FUN._6ZIP;
            } else if (deviceType.endsWith("10")) {
                fun = FUN._10ZIP;
            }
        } else if (currentSettings.get(TestSetting.NO_FUN)) {
            fun = FUN.NONE;
        } else {
            if (deviceType.endsWith("3")) {
                fun = FUN._3;
            } else if (deviceType.endsWith("6")) {
                fun = FUN._6;
            } else if (deviceType.endsWith("10")) {
                fun = FUN._10;
            }
        }
        if (deviceType.endsWith("3")) {
            crate = Crate.V3;
        } else if (deviceType.endsWith("6")) {
            crate = Crate.V6;
        } else if (deviceType.endsWith("10")) {
            crate = Crate.V10;
        }
        DataSource.writeSettings(this);
    }

    private void showSNWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/SNWindow.fxml"));
            SNPane = (AnchorPane) loader.load();
            SNPane.setLayoutX(180);
            SNPane.setLayoutY(350);
            root.getChildren().add(SNPane);
            SNWindowController controller = loader.getController();
            controller.setMain(this);
            if (currentSettings.get(TestSetting.PROTOCOL_ONLY)) {
                if (currentSettings.get(TestSetting.SN_ONLY)) {
                    controller.setSNProtocolOnly();
                } else {
                    controller.noSNs();
                    createProtocol();
                }
            } else if (currentSettings.get(TestSetting.SN_ONLY)) {
                controller.setSNOnly();
            } else if (currentSettings.get(TestSetting.RECHECK)) {
                controller.noSNs();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean setSNs() {
        try {
            if (!deviceType.startsWith("FUN")) {
                readCrateSN();
            }
            if (!currentSettings.get(TestSetting.NO_FUN)) {
                readFUNSN();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alerter.alert(primaryStage, "Ошибка записи серийных номеров",
                    "Проблема с записью серийных номеров в CU",
                    "Возможна запись части данных. Пожалуйста, проверьте файлы с серийными номерами " +
                            "и создайте протоколы вручную.");
            return false;
        }
        return true;
    }

    private void readFUNSN() throws ParserConfigurationException, TransformerException {
        String SN = ExcelHandler.readFUNSN(fun);
        switch (SN) {
            case "SN":
                Alerter.alert(primaryStage, "Нет серийного номера",
                        "Закончились доступные серийные номера\nв файле с серийными номерами блока вентиляторов",
                        "Пожалуйста, допишите серийные номера в файл");
                readFUNSN();
                break;
            case "READ":
                Alerter.alert(primaryStage, "Ошибка чтения файла",
                        "Проблема с чтением из файла с серийными\nномерами блока вентиляторов",
                        "Пожалуйста, проверьте файл с серийными номерами блока вентиляторов");
                readFUNSN();
                break;
            case "WRITE":
                Alerter.alert(primaryStage, "Ошибка чтения сохранения файла",
                        "Проблема с сохранением изменений в файле\nс серийными номерами блока вентиляторов",
                        "Пожалуйста, проверьте, открыт ли файл с серийными номерами блока вентиляторов у вас или " +
                                "другого пользователя");
                readFUNSN();
                break;
            default:
                ConnectHandler.CUset("FUSrNumber", SN);
                currentSNs.put(Part.FUN, SN);
                break;
        }
    }

    private void readCrateSN() throws ParserConfigurationException, TransformerException {
        String SN = ExcelHandler.readCrateSN(crate);
        switch (SN) {
            case "SN":
                Alerter.alert(primaryStage, "Нет серийного номера",
                        "Закончились доступные серийные номера\nв файле с серийными номерами шасси",
                        "Пожалуйста, допишите серийные номера в файл");
                readCrateSN();
                break;
            case "READ":
                Alerter.alert(primaryStage, "Ошибка чтения файла",
                        "Проблема с чтением из файла с серийными\nномерами шасси",
                        "Пожалуйста, проверьте файл с серийными номерами шасси");
                readCrateSN();
                break;
            case "WRITE":
                Alerter.alert(primaryStage, "Ошибка чтения сохранения файла",
                        "Проблема с сохранением изменений в файле\nс серийными номерами шасси",
                        "Пожалуйста, проверьте, открыт ли файл с серийными номерами шасси у вас или " +
                                "другого пользователя");
                readCrateSN();
                break;
            default:
                ConnectHandler.CUset("VSrNumber", SN);
                currentSNs.put(Part.CRATE, SN);
                break;
        }
    }

    public void createProtocol() {
        if (!deviceType.startsWith("FUN")) {
            int result = createCrateProtocol();
            if (result == 1) {
                Alerter.alert(primaryStage, "Ошибка при создании протокола",
                        "Проблема с записью данных в файл протокола шасси",
                        "Пожалуйста, проверьте файл шаблона протокола шасси " +
                                "и создайте протокол вручную");
            } else if (result == 2) {
                Alerter.alert(primaryStage, "Ошибка при сохранении протокола",
                        "Проблема с сохранением данных в файл протокола шасси",
                        "Пожалуйста, проверьте файл протокола шасси " +
                                "и создайте протокол вручную");
            }
        }
        if (!currentSettings.get(TestSetting.NO_FUN)) {
            int result = createFUNProtocol();
            if (result == 1) {
                Alerter.alert(primaryStage, "Ошибка при создании протокола",
                        "Проблема с записью данных в файл протокола блока вентиляторов",
                        "Пожалуйста, проверьте файл шаблона протокола блока вентиляторов " +
                                "и создайте протокол вручную");
            } else if (result == 2) {
                Alerter.alert(primaryStage, "Ошибка при сохранении протокола",
                        "Проблема с сохранением данных в файл протокола блока вентиляторов",
                        "Пожалуйста, проверьте файл протокола блока вентиляторов " +
                                "и создайте протокол вручную");
            }
        }
    }

    private int createFUNProtocol() {
        return ExcelHandler.createFUNProtocol(fun, currentSNs.get(Part.FUN), currentSNs.get(Part.FUNPN), fanModel, tester);
    }

    private int createCrateProtocol() {
        return ExcelHandler.createCrateProtocol(crate, currentSNs.get(Part.CRATE), currentSNs.get(Part.BACKPLANE), tester);
    }

    public void reset() {
        finishedTestsAmount = 0;
        currentSNs.clear();
        root.getChildren().remove(funTestPane);
        root.getChildren().remove(crateTestPane);
        root.getChildren().remove(SNPane);
        currentSettings.clear();
        areSettingsSet = false;
        mainWindowController.reset();
        try {
            ConnectHandler.logout();
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public void error() {
        Alerter.alert(primaryStage, "Ошибка", "Ошибка подключения",
                "Пожалуйста, проверте подключен ли кабель,\nи правильно ли записан IP-адрес CU.");
        reset();
    }

    public synchronized void oneTestFinished() {
        finishedTestsAmount++;
        if (finishedTestsAmount == testsAmount) {
            showSNWindow();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public boolean isAreSettingsSet() {
        return areSettingsSet;
    }

    public void setAreSettingsSet(boolean areSettingsSet) {
        this.areSettingsSet = areSettingsSet;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public Crate getCrate() {
        return crate;
    }

    public FUN getFun() {
        return fun;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

    public String getFanModel() {
        return fanModel;
    }

    public ObservableMap<TestSetting, Boolean> getCurrentSettings() {
        return currentSettings;
    }

    public ObservableMap<Part, String> getCurrentSNs() {
        return currentSNs;
    }
}