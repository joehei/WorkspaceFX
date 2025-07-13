package org.hometree.workspacefx.plugin;

import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.hometree.workspacefx.sidebar.Sidebar;
import org.hometree.workspacefx.utils.veil.Veil;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AWorkspaceFXPlugin {

    protected final Logger logger = LoggerFactory.getLogger(AWorkspaceFXPlugin.class);
    private final BooleanProperty pluginSelected = new SimpleBooleanProperty();
    private final IntegerProperty defaultIconSize = new SimpleIntegerProperty(16);
    private final ObjectProperty<Node> toggleButtonContent = new SimpleObjectProperty<>();
    private final StringProperty shortDescription = new SimpleStringProperty();
    private final ObjectProperty<Sidebar> sidebar = new SimpleObjectProperty<>();
    private final StringProperty status = new SimpleStringProperty();
    private Veil veil;
    private Node content;
    private NotificationGraphic notificationGraphic;
    private StackPane contentPane;

    /**
     * General constructor
     */
    public AWorkspaceFXPlugin() {
        setup();
    }

    /**
     * Constructor which can be used together with a fxml file which describes the content to be shown
     * when the toggle button is selected. The fxml file must have the same name as the extending class.
     *
     * @param shortDescription Plugin short description
     * @param icon             Ikonli icon code
     */
    public AWorkspaceFXPlugin(String shortDescription, Ikon icon, String fxmlFile) {
        try {
            setup();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(this);
            setContent(loader.load());
            setShortDescription(shortDescription);
            setToggleButtonContent(icon);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Constructor which can be used together with a fxml file which describes the content to be shown
     * when the toggle button is selected. The fxml file must have the same name as the extending class.
     *
     * @param shortDescription Plugin short description
     * @param icon             Ikonli icon code
     */
    public AWorkspaceFXPlugin(String shortDescription, Ikon icon) {
        try {
            setup();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getClass().getSimpleName() + ".fxml"));
            loader.setController(this);
            setContent(loader.load());
            setShortDescription(shortDescription);
            setToggleButtonContent(icon);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param toggleButtonContent Toggle button content
     * @param content             The content which should be shown when the button is selected
     */
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
        veil = new Veil();
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
        if (contentPane != null) {
            contentPane.getChildren().clear();

        } else {
            contentPane = new StackPane();
        }
        contentPane.getChildren().addAll(content, veil);
        veil.setVisible(false);
        this.content = content;
    }

    public Veil getVeil() {
        return veil;
    }

    public StackPane getContentPane() {
        return contentPane;
    }

}
