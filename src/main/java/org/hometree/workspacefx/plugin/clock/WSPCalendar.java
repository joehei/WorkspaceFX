package org.hometree.workspacefx.plugin.clock;

import atlantafx.base.controls.Calendar;
import atlantafx.base.theme.Styles;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WSPCalendar extends AWorkspaceFXPlugin {
    public WSPCalendar() {
        var cal = new Calendar(LocalDate.now());
        cal.setTopNode(new WSPCalendarClock());
        cal.setShowWeekNumbers(true);
        VBox vBox = new VBox(cal);
        vBox.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setContent(scrollPane);
        setShortDescription("Calendar");
        FontIcon icon = new FontIcon(MaterialDesignC.CALENDAR);
        icon.setIconSize(16);
        setToggleButtonContent(icon);
    }
}
