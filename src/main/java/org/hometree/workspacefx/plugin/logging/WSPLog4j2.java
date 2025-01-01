package org.hometree.workspacefx.plugin.logging;

import javafx.fxml.FXMLLoader;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WSPLog4j2 extends AWorkspaceFXPlugin {

    private static final Logger log = LoggerFactory.getLogger(WSPLog4j2.class);

    public WSPLog4j2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getClass().getSimpleName() + ".fxml"));
            loader.setController(this);
            setContent(loader.load());
            setShortDescription("Logging");
            FontIcon icon = new FontIcon(MaterialDesignF.FORMAT_LIST_TEXT);
            icon.setIconSize(16);
            setToggleButtonContent(icon);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
