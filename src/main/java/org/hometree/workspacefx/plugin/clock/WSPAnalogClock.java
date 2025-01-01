package org.hometree.workspacefx.plugin.clock;

import javafx.fxml.Initializable;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class WSPAnalogClock extends AWorkspaceFXPlugin implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(WSPAnalogClock.class);

    public WSPAnalogClock() {
        setContent(new AnalogClockWithClockwork());
        setShortDescription("Analog clock");
        FontIcon icon = new FontIcon(MaterialDesignC.CLOCK_OUTLINE);
        icon.setIconSize(16);
        setToggleButtonContent(icon);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
