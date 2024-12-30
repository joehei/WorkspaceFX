package org.hometree.workspacefx.plugin;

import javafx.scene.Node;

public abstract class AWorkspaceFXPlugin {

    private Node toggleButtonContent;
    private Node content;

    public AWorkspaceFXPlugin() {
    }

    public AWorkspaceFXPlugin(Node toggleButtonContent, Node content) {
        this.toggleButtonContent = toggleButtonContent;
        this.content = content;
    }

    public Node getToggleButtonContent() {
        return toggleButtonContent;
    }

    public void setToggleButton(Node toggleButtonContent) {
        this.toggleButtonContent = toggleButtonContent;
    }

    public Node getContent() {
        return content;
    }

    public void setContent(Node content) {
        this.content = content;
    }

}
