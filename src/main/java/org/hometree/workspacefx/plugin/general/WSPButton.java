package org.hometree.workspacefx.plugin.general;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.hometree.workspacefx.utils.WorkspaceFXUtils;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

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
