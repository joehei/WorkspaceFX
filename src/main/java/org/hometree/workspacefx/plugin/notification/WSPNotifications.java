package org.hometree.workspacefx.plugin.notification;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.hometree.workspacefx.plugin.logging.WSPLog4j2LogMessage;
import org.hometree.workspacefx.utils.WorkspaceFXUtils;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WSPNotifications extends AWorkspaceFXPlugin implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(WSPNotifications.class);
    @FXML
    private Button clearAll;

    @FXML
    private VBox cardBox;

    public WSPNotifications() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(getClass().getSimpleName() + ".fxml")));
            loader.setController(this);
            setContent(loader.load());
            setToggleButtonContent(MaterialDesignB.BELL);
            setShortDescription("Notifications");

            // Define when a notification should be shown
            cardBox.getChildren().addListener((ListChangeListener<Node>) c -> {
                c.next();
                // When no log entries are in the list, hide the notification circle
                if (cardBox.getChildren().isEmpty()) {
                    getButtonGraphic().setShowNotification(false);
                    return;
                }
                // If something was added and the plugin is not shown, show the circle, otherwise hide
                if (c.wasAdded()) {
                    getButtonGraphic().setShowNotification(!getPluginSelected());
                }
            });

            // When the plugin is shown the notification should be hidden
            pluginSelectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    getButtonGraphic().setShowNotification(false);
                }
            });

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void info(String title, String description) {
        addMessage(title, description, WorkspaceFXUtils.infoIcon(), Styles.ACCENT, null);
    }

    public void info(String title, String description, Runnable actionEvent) {
        addMessage(title, description, WorkspaceFXUtils.infoIcon(), Styles.ACCENT, actionEvent);
    }

    public void success(String title, String description) {
        addMessage(title, description, WorkspaceFXUtils.successIcon(), Styles.SUCCESS, null);
    }

    public void success(String title, String description, Runnable actionEvent) {
        addMessage(title, description, WorkspaceFXUtils.successIcon(), Styles.SUCCESS, actionEvent);
    }

    public void warning(String title, String description) {
        addMessage(title, description, WorkspaceFXUtils.warnIcon(), Styles.WARNING, null);
    }

    public void warning(String title, String description, Runnable actionEvent) {
        addMessage(title, description, WorkspaceFXUtils.warnIcon(), Styles.WARNING, actionEvent);
    }

    public void danger(String title, String description) {
        addMessage(title, description, WorkspaceFXUtils.errorIcon(), Styles.DANGER, null);
    }

    public void danger(String title, String description, Runnable actionEvent) {
        addMessage(title, description, WorkspaceFXUtils.errorIcon(), Styles.DANGER, actionEvent);
    }

    private void addMessage(String title, String description, Node graphic, String style, Runnable actionEvent) {
        var message = new Message(title, description, graphic);
        message.getStyleClass().add(style);
        message.setOnClose(event -> cardBox.getChildren().remove(message));
        if (actionEvent != null) {
            message.setActionHandler(actionEvent);
        }
        cardBox.getChildren().add(message);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearAll.setOnAction(event -> cardBox.getChildren().clear());
    }
}
