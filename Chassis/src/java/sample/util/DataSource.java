package sample.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Main;
import sample.view.SettingsController;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class DataSource {
    private static ObservableList<String> devices = FXCollections.observableArrayList();
    private static ObservableList<String> testers = FXCollections.observableArrayList();
    private static ObservableList<String> fans = FXCollections.observableArrayList();
    private static String settings = "resources/settings.properties";
    private static String config = "resources/config.properties";
    private static final String SPLITTER = ",";
    private static final String ENCODING = "ISO8859-1";

    static {
        readConfig();
    }

    private static void readConfig() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(config));
            testers.addAll(new String(properties.getProperty("testers").getBytes(ENCODING)).split(SPLITTER));
            devices.addAll(new String(properties.getProperty("devices").getBytes(ENCODING)).split(SPLITTER));
            fans.addAll(new String(properties.getProperty("fanModels").getBytes(ENCODING)).split(SPLITTER));
            ConnectHandler.URL = properties.getProperty("URL");
            ConnectHandler.contentType = properties.getProperty("contentType");
            ConnectHandler.method = properties.getProperty("method");
            ExcelHandler.protocolDir = new String(properties.getProperty("protocolDir").getBytes(ENCODING));
        } catch (IOException e) {
            throw new RuntimeException("Can not read file " + config);
        }
    }

    public static void readSettings(SettingsController controller) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(settings));
            controller.getDeviceType().setValue(properties.getProperty("device"));
            for (TestSetting i : TestSetting.values()) {
                controller.getSettings().get(i).setSelected(Boolean.parseBoolean(properties.getProperty(i.toString())));
            }
        } catch (IOException e) {
            throw new RuntimeException("Can not read file " + settings);
        }
    }

    public static void writeSettings(Main main) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(settings));
            properties.setProperty("device", main.getDeviceType());
            for (Map.Entry<TestSetting, Boolean> i : main.getCurrentSettings().entrySet()) {
                properties.setProperty(i.getKey().toString(), i.getValue().toString());
            }
            properties.store(new FileOutputStream(settings), "");
        } catch (IOException e) {
            throw new RuntimeException("Can not read file " + settings);
        }
    }

    public static ObservableList<String> getDevices() {
        return devices;
    }

    public static ObservableList<String> getTesters() {
        return testers;
    }

    public static ObservableList<String> getFans() {
        return fans;
    }
}