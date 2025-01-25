package org.hometree.workspacefx.plugin.notification;

import atlantafx.base.controls.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.hometree.workspacefx.plugin.fxproperty.WSPFXProperty;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;
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

    }
}
