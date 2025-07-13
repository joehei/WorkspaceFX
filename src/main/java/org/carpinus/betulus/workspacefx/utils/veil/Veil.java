package org.carpinus.betulus.workspacefx.utils.veil;

import atlantafx.base.controls.RingProgressIndicator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Veil extends StackPane {

    private static final Logger logger = LoggerFactory.getLogger(Veil.class);
    private final BooleanProperty resetOnVisible = new SimpleBooleanProperty(true);
    @FXML
    private RingProgressIndicator indicator;
    @FXML
    private Label label;

    public Veil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getClass().getSimpleName() + ".fxml"));
            loader.setController(this);
            loader.setRoot(this);
            visibleProperty().addListener((obs, ov, nv) -> {
                if (getResetOnVisible() && nv) {
                    label.setText("");
                    indicator.setProgress(-1);
                }
            });
            loader.load();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public RingProgressIndicator getIndicator() {
        return indicator;
    }

    public Label getLabel() {
        return label;
    }

    public Boolean getResetOnVisible() {
        return resetOnVisible.getValue();
    }

    /**
     * @param resetOnVisible If set to true, the progress indicator will be set to intermediate and the label to "" when the viel is shown. The default value is true.
     */
    public void setResetOnVisible(Boolean resetOnVisible) {
        this.resetOnVisible.setValue(resetOnVisible);
    }

    public BooleanProperty resetOnVisibleProperty() {
        return this.resetOnVisible;
    }

}
