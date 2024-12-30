package org.hometree.workspacefx.plugin;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class LabelPlugin extends AWorkspaceFXPlugin {
    public LabelPlugin(Ikon ikon, String text) {
        setContent(new StackPane(new Label(text)));
        FontIcon icon = new FontIcon(ikon);
        icon.setIconSize(16);
        setToggleButton(icon);
    }
}
