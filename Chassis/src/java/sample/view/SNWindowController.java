package sample.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import sample.Main;
import sample.util.Part;

import java.util.Map;

public class SNWindowController {
    private Main main;
    private ObservableMap<Part, Label> SNs = FXCollections.observableHashMap();

    @FXML
    private Label crate;
    @FXML
    private Label FUN;
    @FXML
    private Label FUNPN;
    @FXML
    private Label backPlane;
    @FXML
    private GridPane SNPane;
    @FXML
    private Button start;
    @FXML
    private Button finish;

    @FXML
    private void initialize() {
        SNs.put(Part.CRATE, crate);
        SNs.put(Part.FUN, FUN);
        SNs.put(Part.FUNPN, FUNPN);
        SNs.put(Part.BACKPLANE, backPlane);
    }

    @FXML
    private void setSNProtocol() {
        start.setDisable(true);
        if (main.setSNs()) {
            main.createProtocol();
        }
        setSNs();
    }

    @FXML
    private void finish() {
        main.getMainWindowController().reset();
        main.reset();
    }

    public void setSNProtocolOnly() {
        start.setVisible(false);
        setSNProtocol();
    }

    public void setSNOnly() {
        start.setVisible(false);
        main.setSNs();
        setSNs();
    }

    public void noSNs() {
        start.setVisible(false);
        SNPane.setVisible(false);
    }

    private void setSNs() {
        for (Map.Entry<Part, Label> i : SNs.entrySet()) {
            i.getValue().setText(main.getCurrentSNs().get(i.getKey()));
        }
    }

    public void setMain(Main main) {
        this.main = main;
        setSNs();
    }
}