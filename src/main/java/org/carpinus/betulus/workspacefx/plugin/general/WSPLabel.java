package org.carpinus.betulus.workspacefx.plugin.general;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.carpinus.betulus.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.Ikon;

public class WSPLabel extends AWorkspaceFXPlugin {
    public WSPLabel(Ikon ikon, String text) {
        setContent(new StackPane(new Label(text)));
        setToggleButtonContent(ikon);
        setShortDescription("My short description of the plugin");
    }
}
