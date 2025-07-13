package org.carpinus.betulus.workspacefx.plugin;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationGraphic extends StackPane {

    private static final Logger log = LoggerFactory.getLogger(NotificationGraphic.class);
    private final BooleanProperty showNotification = new SimpleBooleanProperty(false);
    @FXML
    private Circle notificationCircle;

    public NotificationGraphic(Node node) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getClass().getSimpleName() + ".fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

            notificationCircle.setVisible(false);
            notificationCircle.visibleProperty().bind(showNotification);

            getChildren().addFirst(node);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Boolean getShowNotification() {
        return showNotification.getValue();
    }

    public void setShowNotification(Boolean showNotification) {
        this.showNotification.setValue(showNotification);
    }

    public BooleanProperty showNotificationProperty() {
        return this.showNotification;
    }

}
