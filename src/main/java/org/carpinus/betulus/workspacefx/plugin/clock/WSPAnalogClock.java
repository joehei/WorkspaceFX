package org.carpinus.betulus.workspacefx.plugin.clock;

import javafx.fxml.Initializable;
import org.carpinus.betulus.workspacefx.plugin.AWorkspaceFXPlugin;
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
        setToggleButtonContent(MaterialDesignC.CLOCK_OUTLINE);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
