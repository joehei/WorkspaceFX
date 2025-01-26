package org.hometree.workspacefx.plugin.notification;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearAll.setOnAction(event -> cardBox.getChildren().clear());
        // TODO : Add notifications
        var info = new Message(
                "Info",
                "FAKER.lorem().sentence(10)",
                WorkspaceFXUtils.infoIcon()
        );
        info.getStyleClass().add(Styles.ACCENT);
        info.setOnClose(event -> cardBox.getChildren().remove(info));

        var success = new Message(
                "Success",
                "FAKER.lorem().sentence(15)",
                WorkspaceFXUtils.successIcon()
        );
        success.getStyleClass().add(Styles.SUCCESS);
        success.setOnClose(event -> cardBox.getChildren().remove(success));

        var warning = new Message(
                "Warning",
                "FAKER.lorem().sentence(20)",
                WorkspaceFXUtils.warnIcon()
        );
        warning.getStyleClass().add(Styles.WARNING);
        warning.setOnClose(event -> cardBox.getChildren().remove(warning));

        var danger = new Message(
                "Danger",
                "FAKER.lorem().sentence(25)",
                WorkspaceFXUtils.errorIcon()
        );
        danger.getStyleClass().add(Styles.DANGER);
        danger.setOnClose(event -> cardBox.getChildren().remove(danger));

        cardBox.getChildren().addAll(info, success, warning, danger);

    }
}
