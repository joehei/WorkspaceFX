package org.carpinus.betulus.workspacefx.plugin.logging;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.spi.StandardLevel;
import org.carpinus.betulus.workspacefx.utils.WorkspaceFXUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WSPLog4j2DetailView extends VBox implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(WSPLog4j2DetailView.class);
    @FXML
    private TextArea exception;
    @FXML
    private TextArea message;
    @FXML
    private Label ts;
    @FXML
    private StackPane iconPane;
    @FXML
    private FontIcon closeIcon;

    public WSPLog4j2DetailView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getClass().getSimpleName() + ".fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public FontIcon getCloseIcon() {
        return closeIcon;
    }

    public void setLogMessage(WSPLog4j2LogMessage message) {
        if (message == null) {
            this.ts.setText("");
            this.message.setText("");
            this.exception.setText("");
            iconPane.getChildren().clear();
        } else {
            iconPane.getChildren().clear();
            if (message.getLevel().intLevel() == StandardLevel.INFO.intLevel()) {
                iconPane.getChildren().add(WorkspaceFXUtils.successIcon());
            } else if (message.getLevel().intLevel() == StandardLevel.WARN.intLevel()) {
                iconPane.getChildren().add(WorkspaceFXUtils.warnIcon());
            } else if (message.getLevel().intLevel() == StandardLevel.ERROR.intLevel()) {
                iconPane.getChildren().add(WorkspaceFXUtils.errorIcon());
            } else if (message.getLevel().intLevel() == StandardLevel.DEBUG.intLevel()) {
                iconPane.getChildren().add(WorkspaceFXUtils.debugIcon());
            }

            this.ts.setText(WorkspaceFXUtils.dateTimeFormatter.format(message.getTs()));
            this.message.setText(message.getMessage());
            this.exception.setText(WorkspaceFXUtils.exceptionToString(message.getThrowable()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
