package org.hometree.workspacefx.sidebar;

import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.hometree.workspacefx.WorkspaceFX;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.hometree.workspacefx.utils.WorkspaceFXUtils;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Sidebar extends VBox {

    private final String spacerStyle = "-fx-background-color:-color-accent-3;";
    private final String spacerNotStyled = ""; //""-fx-background-color:black;";
    private final ToggleGroup toggleGroup;
    private final HashMap<ToggleButton, AWorkspaceFXPlugin> buttonContentMap = new HashMap<>();
    private final SplitPane contentSplitPane;
    private final SPLITPANE_POSITION contentSplitPanePosition;
    private final WorkspaceFX workspaceFX;
    private final SIDEBAR_POSITION sidebarPos;
    private double dividerPos = .5d;
    private int indexToInsertDragAndDropButton;
    private ToggleButton hideRestoreBackup;
    private Button managementButton;

    /**
     * @param workspaceFX              The workspace
     * @param alignment                Position where the toggle button should be placed (TOP_CENTER, BOTTOM_CENTER).
     * @param priority                 Priority if the sidebar should grow (NEVER, ALWAYS)
     * @param contentSplitPane         The split pane where the content should be displayed.
     * @param contentSplitPanePosition Position of the content in the split pane (FIRST, LAST) needed when the content is removed and added.
     * @param sidebarPos               Sidebar position (LEFT, RIGHT)
     */
    public Sidebar(WorkspaceFX workspaceFX, Pos alignment, Priority priority, SplitPane contentSplitPane, SPLITPANE_POSITION contentSplitPanePosition, SIDEBAR_POSITION sidebarPos) {
        this.sidebarPos = sidebarPos;
        this.workspaceFX = workspaceFX;
        this.contentSplitPane = contentSplitPane;
        this.contentSplitPanePosition = contentSplitPanePosition;
        toggleGroup = new ToggleGroup();
        setMinWidth(49);
        setPadding(new Insets(3));
        setVgrow(this, priority);
        setAlignment(alignment);
        getChildren().add(getSpacer());
        moveManagementButton();
        toggleGroup.selectedToggleProperty().addListener((obs, ov, nv) -> {

            // Replace the current content
            if (nv != null && ov != null) {
                backupDivider();
                contentSplitPane.getItems().set(contentSplitPane.getItems().indexOf(buttonContentMap.get((ToggleButton) ov).getContent()), buttonContentMap.get((ToggleButton) nv).getContent());
                restoreDivider();
            }

            // Nothing was selected add the selected content
            if (nv != null && ov == null) {
                if (contentSplitPanePosition.equals(SPLITPANE_POSITION.FIRST)) {
                    contentSplitPane.getItems().addFirst(buttonContentMap.get((ToggleButton) nv).getContent());
                } else {
                    contentSplitPane.getItems().add(buttonContentMap.get((ToggleButton) nv).getContent());
                }
                restoreDivider();
            }

            // Nothing is selected remove all the content
            if (nv == null && ov != null) {
                backupDivider();
                contentSplitPane.getItems().remove(buttonContentMap.get((ToggleButton) ov).getContent());
            }
        });
        setupDragAndDrop();
    }

    private void moveManagementButton() {
        if (managementButton != null) {
            getChildren().remove(managementButton);
            getChildren().add(managementButton);
        }
    }

    public void addManagementButton(Side side, Sidebar otherSidebar) {
        managementButton = new Button();
        managementButton.setGraphic(WorkspaceFXUtils.getIcon(MaterialDesignD.DOTS_HORIZONTAL));
        ContextMenu cm = new ContextMenu();
        managementButton.setOnMouseEntered(event -> {
            cm.getItems().clear();

            MenuItem moveToOther;
            if (side.equals(Side.LEFT)) {
                moveToOther = new MenuItem("Move to right");
            } else {
                moveToOther = new MenuItem("Move to left");
            }
            moveToOther.setOnAction(event1 -> {
                this.getChildren().remove(managementButton);
                managementButton=null;
                otherSidebar.addManagementButton(side.equals(Side.LEFT) ? Side.RIGHT : Side.LEFT, this);
            });
            cm.getItems().add(moveToOther);

            if (!getWorkspaceFX().getHiddenPlugins().isEmpty()){
                cm.getItems().add(new SeparatorMenuItem());
            }
            getWorkspaceFX().getHiddenPlugins().forEach(plugin -> {
                MenuItem e = new MenuItem(plugin.plugin().getShortDescription());
                e.setOnAction(ev -> getWorkspaceFX().showPlugin(plugin));
                cm.getItems().add(e);
            });
        });
        managementButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                cm.show(managementButton, side.equals(Side.LEFT) ? Side.RIGHT : Side.LEFT, 0, 0);
            }
        });
        getChildren().add(managementButton);

    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public SIDEBAR_POSITION getSidebarPos() {
        return sidebarPos;
    }

    public WorkspaceFX getWorkspaceFX() {
        return workspaceFX;
    }

    public HashMap<ToggleButton, AWorkspaceFXPlugin> getButtonContentMap() {
        return buttonContentMap;
    }

    public void hide() {
        hideRestoreBackup = (ToggleButton) toggleGroup.getSelectedToggle();
        if (hideRestoreBackup != null) {
            hideRestoreBackup.setSelected(false);
        }
    }

    public void restore() {
        if (hideRestoreBackup != null) {
            hideRestoreBackup.setSelected(true);
        }
    }

    public SPLITPANE_POSITION getContentSplitPanePosition() {
        return contentSplitPanePosition;
    }

    private void backupDivider() {
        if (!contentSplitPane.getDividers().isEmpty()) {
            dividerPos = contentSplitPane.getDividers().getFirst().getPosition();
        }
    }

    private void restoreDivider() {
        if (!contentSplitPane.getDividers().isEmpty()) {
            contentSplitPane.getDividers().getFirst().setPosition(dividerPos);
            contentSplitPane.getDividers().getFirst().positionProperty().addListener((observableValue, oldValue, newValue) -> dividerPos = newValue.doubleValue());
        }
    }

    public void addButton(AWorkspaceFXPlugin plugin) {
        ToggleButton toggleButton = new SideBarButton(plugin, this);
        buttonContentMap.put(toggleButton, plugin);
        getChildren().add(toggleButton);
        getChildren().add(getSpacer());
        moveManagementButton();
        plugin.setSidebar(this);
    }

    public void addButton(AWorkspaceFXPlugin plugin, int index, boolean buttonSelected) {
        ToggleButton toggleButton = new SideBarButton(plugin, this);
        buttonContentMap.put(toggleButton, plugin);
        if (getChildren().size() - 1 < index) {
            getChildren().add(toggleButton);
            getChildren().add(getSpacer());
            moveManagementButton();
        } else {
            getChildren().add(index, getSpacer());
            getChildren().add(index, toggleButton);
            moveManagementButton();
        }
        toggleButton.setSelected(buttonSelected);
        plugin.setSidebar(this);
    }

    public void removeButton(ToggleButton button) {
        button.setSelected(false);
        buttonContentMap.get(button).setSidebar(null);
        int index = getChildren().indexOf(button);
        if (getChildren().get(index + 1) instanceof StackPane sp) {
            getChildren().remove(sp);
        }
        getChildren().remove(button);
        buttonContentMap.remove(button);
    }

    public ToggleButton getButton(AWorkspaceFXPlugin plugin) {
        for (Map.Entry<ToggleButton, AWorkspaceFXPlugin> es : buttonContentMap.entrySet()) {
            if (es.getValue().equals(plugin)) {
                return es.getKey();
            }
        }
        return null;
    }

    private StackPane getSpacer() {
        StackPane spacer = new StackPane();
        spacer.setStyle(spacerNotStyled);
        spacer.setMinHeight(3);
        return spacer;
    }

    public void insertDraggedButton(DragEvent event) {
        event.setDropCompleted(workspaceFX.getDragAndDropButton() != null && workspaceFX.getDragAndDropPlugin() != null);
        boolean buttonActive = workspaceFX.getDragAndDropButton().isSelected();
        workspaceFX.getDragAndDropSidebar().removeButton(workspaceFX.getDragAndDropButton());
        addButton(workspaceFX.getDragAndDropPlugin(), indexToInsertDragAndDropButton, buttonActive);
        event.consume();
    }

    public void removeStyleFromAllSpacer() {
        for (Node n : getChildren()) {
            if (n instanceof StackPane sp) {
                sp.setStyle(spacerNotStyled);
            }
        }
    }

    void setupDragAndDropTarget(ToggleButton button) {
        // Target button
        button.setOnDragOver(event -> {
            removeStyleFromAllSpacer();
            // Give a visual feedback where to drop the button
            if (event.getGestureSource() != button && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

                int index = getChildren().indexOf(button);

                if (event.getY() <= button.getHeight() / 2) {
                    indexToInsertDragAndDropButton = index;
                    StackPane spacer = (StackPane) getChildren().get(index - 1);
                    spacer.setStyle(spacerStyle);
                    spacer = (StackPane) getChildren().get(index + 1);
                    spacer.setStyle(spacerNotStyled);
                } else {
                    indexToInsertDragAndDropButton = index + 2;
                    StackPane spacer = (StackPane) getChildren().get(index - 1);
                    spacer.setStyle(spacerNotStyled);
                    spacer = (StackPane) getChildren().get(index + 1);
                    spacer.setStyle(spacerStyle);
                }
            }
            event.consume();
        });
        button.setOnDragExited(event -> removeStyleFromAllSpacer());
        button.setOnDragDropped(this::insertDraggedButton);
    }

    private void setupDragAndDrop() {
        // Target sidebar (vbox)
        setOnDragOver(event -> {
            removeStyleFromAllSpacer();
            if (event.getDragboard().hasString() && event.getDragboard().getString().startsWith("WorkspaceFX_")) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                if (managementButton != null) {
                    if (getChildren().get(getChildren().size() - 2) instanceof StackPane spacer) {
                        spacer.setStyle(spacerStyle);
                        indexToInsertDragAndDropButton = getChildren().indexOf(spacer) + 1;
                    } else {
                        System.out.println("Warning last element is not a spacer.");
                    }
                } else if (getChildren().getLast() instanceof StackPane spacer) {
                    spacer.setStyle(spacerStyle);
                    indexToInsertDragAndDropButton = getChildren().indexOf(spacer) + 1;
                } else {
                    System.out.println("Warning last element is not a spacer.");
                }
            }
        });
        setOnDragExited(event -> removeStyleFromAllSpacer());
        setOnDragDropped(this::insertDraggedButton);
    }

    void setupDragAndDropSource(ToggleButton button) {
        // Source button
        button.setOnDragDetected(event -> {
            // When the drag is started set the button and sidebar in the workspace
            Dragboard db = button.startDragAndDrop(TransferMode.ANY);
            getWorkspaceFX().setDragAndDropIcon(button.snapshot(null, null));
            db.setDragView(getWorkspaceFX().getDragAndDropIcon());
            ClipboardContent content = new ClipboardContent();
            content.putString("WorkspaceFX_" + button.getClass().getSimpleName());
            db.setContent(content);
            workspaceFX.setDragAndDropButton(button);
            workspaceFX.setDragAndDropPlugin(buttonContentMap.get(button));
            workspaceFX.setDragAndDropSidebar(this);
            event.consume();
        });
        // Source button
        button.setOnDragDone(event -> {
            // Check if the drag was dropped outside
            Point p = MouseInfo.getPointerInfo().getLocation();
            Point2D mouseLoc = new Point2D(p.getX(), p.getY());
            Window window = this.getScene().getWindow();
            Rectangle2D windowBounds = new Rectangle2D(window.getX(), window.getY(), window.getWidth(), window.getHeight());

            if (!windowBounds.contains(mouseLoc)) {
                removeButton(button);
                Sidebar sb = getWorkspaceFX().getDragAndDropSidebar();
                AWorkspaceFXPlugin plugin = getWorkspaceFX().getDragAndDropPlugin();

                Stage newStage = new Stage();
                Scene scene = new Scene(new StackPane(plugin.getContent()));
                newStage.setScene(scene);
                newStage.getIcons().add(WorkspaceFXUtils.copyImage(workspaceFX.getDragAndDropIcon()));
                newStage.setX(event.getScreenX());
                newStage.setY(event.getScreenY());
                newStage.setWidth(window.getWidth());
                newStage.setHeight(window.getHeight());
                newStage.setTitle(plugin.getShortDescription());
                newStage.setOnCloseRequest(ce -> {
                    sb.addButton(plugin);
                    sb.getButton(plugin).setSelected(true);
                });
                newStage.show();
            }

            // When the drag operation is done remove the button and sidebar from the workspace
            workspaceFX.setDragAndDropButton(null);
            workspaceFX.setDragAndDropPlugin(null);
            workspaceFX.setDragAndDropSidebar(null);
            workspaceFX.setDragAndDropIcon(null);
            event.consume();
        });
    }

    public enum SIDEBAR_POSITION {LEFT, RIGHT}

    public enum SPLITPANE_POSITION {FIRST, LAST}

}
