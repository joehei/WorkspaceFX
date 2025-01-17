package org.hometree.workspacefx.sidebar;

import atlantafx.base.controls.Popover;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.hometree.workspacefx.WorkspaceFX;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;

import java.util.HashMap;
import java.util.Map;

public class Sidebar extends VBox {

    private final ToggleGroup toggleGroup;
    private final HashMap<ToggleButton, AWorkspaceFXPlugin> buttonContentMap = new HashMap<>();
    private final SplitPane contentSplitPane;
    private final SPLITPANE_POSITION contentSplitPanePosition;
    private final WorkspaceFX workspaceFX;
    private final String spacerStyle = "-fx-background-color:-color-accent-3;";
    private final String spacerNotStyled = ""; //""-fx-background-color:black;";
    private final SIDEBAR_POSITION sidebarPos;
    private double dividerPos = .5d;
    private int indexToInsertDragAndDropButton;
    private ToggleButton hideRestoreBackup;

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
        ToggleButton toggleButton = createButton(plugin);
        buttonContentMap.put(toggleButton, plugin);
        getChildren().add(toggleButton);
        getChildren().add(getSpacer());
    }

    public void addButton(AWorkspaceFXPlugin plugin, int index, boolean buttonSelected) {
        ToggleButton toggleButton = createButton(plugin);
        buttonContentMap.put(toggleButton, plugin);
        if (getChildren().size() - 1 < index) {
            getChildren().add(toggleButton);
            getChildren().add(getSpacer());
        } else {
            getChildren().add(index, getSpacer());
            getChildren().add(index, toggleButton);
        }
        toggleButton.setSelected(buttonSelected);
    }

    public void removeButton(ToggleButton button) {
        button.setSelected(false);
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

    private ToggleButton createButton(AWorkspaceFXPlugin plugin) {
        ToggleButton button = new ToggleButton();

        Popover popOver = new Popover();
        popOver.setArrowSize(10);
        double offset;

        if (sidebarPos.equals(SIDEBAR_POSITION.RIGHT)) {
            popOver.setArrowLocation(Popover.ArrowLocation.RIGHT_CENTER);
            offset = -10;
        } else {
            offset = -5;
        }

        Label label = new Label(plugin.getShortDescription());
        label.setFont(Font.font(12));
        StackPane content = new StackPane(label);
        content.setPadding(new Insets(5));
        popOver.setContentNode(content);
        popOver.setArrowSize(0);
        button.setOnMouseEntered(event -> popOver.show(button, offset));
        button.setOnMouseExited(event -> popOver.hide());
        setupDragAndDropSource(button);
        setupDragAndDropTarget(button);
        button.setMinWidth(28);
        button.setMinHeight(28);
        button.setGraphic(plugin.getButtonGraphic());
        toggleGroup.getToggles().add(button);
        button.selectedProperty().addListener((obs, ov, nv) -> plugin.setPluginSelected(nv));
        return button;
    }

    private StackPane getSpacer() {
        StackPane spacer = new StackPane();
        spacer.setStyle(spacerNotStyled);
        spacer.setMinHeight(3);
        return spacer;
    }

    private void insertDraggedButton(DragEvent event) {
        event.setDropCompleted(workspaceFX.getDragAndDropButton() != null && workspaceFX.getDragAndDropPlugin() != null);
        boolean buttonActive = workspaceFX.getDragAndDropButton().isSelected();
        workspaceFX.getDragAndDropSidebar().removeButton(workspaceFX.getDragAndDropButton());
        addButton(workspaceFX.getDragAndDropPlugin(), indexToInsertDragAndDropButton, buttonActive);
        event.consume();
    }

    private void removeStyleFromAllSpacer() {
        for (Node n : getChildren()) {
            if (n instanceof StackPane sp) {
                sp.setStyle(spacerNotStyled);
            }
        }
    }

    private void setupDragAndDropTarget(ToggleButton button) {
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
                if (getChildren().getLast() instanceof StackPane spacer) {
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

    private void setupDragAndDropSource(ToggleButton button) {
        // Source button
        button.setOnDragDetected(event -> {
            // When the drag is started set the button and sidebar in the workspace
            Dragboard db = button.startDragAndDrop(TransferMode.ANY);
            db.setDragView(button.snapshot(new SnapshotParameters(), null));
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
            // When the drag operation is done remove the button and sidebar from the workspace
            workspaceFX.setDragAndDropButton(null);
            workspaceFX.setDragAndDropPlugin(null);
            workspaceFX.setDragAndDropSidebar(null);
            event.consume();
        });
    }

    public enum SIDEBAR_POSITION {LEFT, RIGHT}

    public enum SPLITPANE_POSITION {FIRST, LAST}

}
