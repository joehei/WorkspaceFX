package org.carpinus.betulus.workspacefx.plugin.clock;

import atlantafx.base.controls.Calendar;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.carpinus.betulus.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;

import java.time.LocalDate;

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
        setToggleButtonContent(MaterialDesignC.CALENDAR);
    }
}
