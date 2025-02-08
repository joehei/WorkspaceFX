package org.hometree.workspacefx.plugin;

import javafx.beans.property.*;
import javafx.scene.Node;
import org.hometree.workspacefx.sidebar.Sidebar;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public abstract class AWorkspaceFXPlugin {

    private final BooleanProperty pluginSelected = new SimpleBooleanProperty();
    private final IntegerProperty defaultIconSize = new SimpleIntegerProperty(16);
    private final ObjectProperty<Node> toggleButtonContent = new SimpleObjectProperty<>();
    private final StringProperty shortDescription = new SimpleStringProperty();
    private final ObjectProperty<Sidebar> sidebar = new SimpleObjectProperty<>();
    private final StringProperty status = new SimpleStringProperty();
    private Node content;
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

    public String getStatus() {
        return status.getValue();
    }

    public void setStatus(String status) {
        this.status.setValue(status);
    }

    public StringProperty statusProperty() {
        return this.status;
    }

    public Sidebar getSidebar() {
        return sidebar.getValue();
    }

    public void setSidebar(Sidebar sidebar) {
        this.sidebar.setValue(sidebar);
    }

    public ObjectProperty<Sidebar> sidebarProperty() {
        return this.sidebar;
    }

    public String getShortDescription() {
        return shortDescription.getValue();
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription.setValue(shortDescription);
    }

    public StringProperty shortDescriptionProperty() {
        return this.shortDescription;
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
