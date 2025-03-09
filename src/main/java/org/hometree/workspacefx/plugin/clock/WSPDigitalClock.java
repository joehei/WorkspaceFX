package org.hometree.workspacefx.plugin.clock;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WSPDigitalClock extends AWorkspaceFXPlugin implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(WSPDigitalClock.class);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("eeee dd.MM.yyyy");
    @FXML
    private Label date;
    @FXML
    private Label time;

    public WSPDigitalClock() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getClass().getSimpleName() + ".fxml"));
            loader.setController(this);
            setContent(loader.load());
            setShortDescription("Digital clock");
            setToggleButtonContent(MaterialDesignC.CLOCK_DIGITAL);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var t = new Timeline(new KeyFrame(
                Duration.seconds(1),
                e -> {
                    time.setText(timeFormatter.format(ZonedDateTime.now()));
                    date.setText(dateFormatter.format(ZonedDateTime.now()));
                }
        ));
        t.setCycleCount(Animation.INDEFINITE);
        t.playFromStart();
    }

}
