package org.hometree.workspacefx.plugin;

import javafx.beans.property.*;
import javafx.scene.Node;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public abstract class AWorkspaceFXPlugin {

    private final BooleanProperty pluginSelected = new SimpleBooleanProperty();
    private final IntegerProperty defaultIconSize = new SimpleIntegerProperty(16);
    private final ObjectProperty<Node> toggleButtonContent = new SimpleObjectProperty<Node>();
    private Node content;
    private String shortDescription = "";
    private NotificationGraphic notificationGraphic;

    public AWorkspaceFXPlugin() {
        setup();
    }

    public AWorkspaceFXPlugin(Node toggleButtonContent, Node content) {
        setup();
        setToggleButtonContent(toggleButtonContent);
        this.content = content;
        this.notificationGraphic = new NotificationGraphic(getToggleButtonContent());
    }

    private void setup() {
        toggleButtonContentProperty().addListener((obs, ov, nv) -> notificationGraphic = new NotificationGraphic(getToggleButtonContent()));
    }

    public Node getToggleButtonContent() {
        return toggleButtonContent.getValue();
    }

    public void setToggleButtonContent(Node toggleButtonContent) {
        this.toggleButtonContent.setValue(toggleButtonContent);
    }

    public void setToggleButtonContent(Ikon ikon) {
        FontIcon fontIcon = new FontIcon(ikon);
        fontIcon.setIconSize(getDefaultIconSize());
        this.toggleButtonContent.setValue(fontIcon);
    }

    public ObjectProperty<Node> toggleButtonContentProperty() {
        return this.toggleButtonContent;
    }

    public Integer getDefaultIconSize() {
        return defaultIconSize.getValue();
    }

    public void setDefaultIconSize(Integer defaultIconSize) {
        this.defaultIconSize.setValue(defaultIconSize);
    }

    public IntegerProperty defaultIconSizeProperty() {
        return this.defaultIconSize;
    }

    public Boolean getPluginSelected() {
        return pluginSelected.getValue();
    }

    public void setPluginSelected(Boolean pluginSelected) {
        this.pluginSelected.setValue(pluginSelected);
    }

    public BooleanProperty pluginSelectedProperty() {
        return this.pluginSelected;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public NotificationGraphic getButtonGraphic() {
        return notificationGraphic;
    }

    public Node getContent() {
        return content;
    }

    public void setContent(Node content) {
        this.content = content;
    }

}
