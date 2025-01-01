package org.hometree.workspacefx.plugin.general;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class WSPLabel extends AWorkspaceFXPlugin {
    public WSPLabel(Ikon ikon, String text) {
        setContent(new StackPane(new Label(text)));
        FontIcon icon = new FontIcon(ikon);
        icon.setIconSize(16);
        setToggleButtonContent(icon);
        setShortDescription("My short description of the plugin");
    }
}
