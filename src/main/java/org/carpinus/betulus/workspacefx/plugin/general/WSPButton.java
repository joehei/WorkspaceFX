package org.carpinus.betulus.workspacefx.plugin.general;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.carpinus.betulus.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.Ikon;

public class WSPButton extends AWorkspaceFXPlugin {

    private final Button button;

    /**
     * @param text Button text
     * @param ikon Plugin icon literal for the toggle button
     * @param shortDesc Short description for the plugin
     */
    public WSPButton(String text, Ikon ikon, String shortDesc) {
        button = new Button(text);
        setContent(new StackPane(button));
        setToggleButtonContent(ikon);
        setShortDescription(shortDesc);
    }

    public Button getButton() {
        return button;
    }

}
