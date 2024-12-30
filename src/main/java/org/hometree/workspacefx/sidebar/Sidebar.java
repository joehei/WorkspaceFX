package org.hometree.workspacefx.sidebar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
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
import org.hometree.workspacefx.WorkspaceFX;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;

import java.util.HashMap;
import java.util.Objects;

public class Sidebar extends VBox {

    private final ToggleGroup toggleGroup;
    private final HashMap<ToggleButton, AWorkspaceFXPlugin> buttonContentMap = new HashMap<>();
    private final SplitPane contentSplitPane;
    private final SPLITPANE_POSITION contentSplitPanePosition;
    private final WorkspaceFX workspaceFX;
    private StackPane spacer;
    private int index;
    private double dividerPos = .5d;
    private int indexToInsertDragAndDropButton;

    public Sidebar(WorkspaceFX workspaceFX, Pos alignment, Priority priority, SplitPane contentSplitPane, SPLITPANE_POSITION contentSplitPanePosition) {
        this.workspaceFX = workspaceFX;
        this.contentSplitPane = contentSplitPane;
        this.contentSplitPanePosition = contentSplitPanePosition;
        toggleGroup = new ToggleGroup();
        setSpacing(3);
        setPadding(new Insets(3));
        setVgrow(this, priority);
        setAlignment(alignment);
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
        }
    }

    public void addButton(AWorkspaceFXPlugin plugin) {
        ToggleButton toggleButton = getButton(plugin.getToggleButtonContent());
        buttonContentMap.put(toggleButton, plugin);
        getChildren().add(toggleButton);
    }

    public void addButton(AWorkspaceFXPlugin plugin, int index) {
        ToggleButton toggleButton = getButton(plugin.getToggleButtonContent());
        buttonContentMap.put(toggleButton, plugin);
        getChildren().add(index, toggleButton);
    }

    public void removeButton(ToggleButton button) {
        getChildren().remove(button);
        buttonContentMap.remove(button);
    }

    private ToggleButton getButton(Node node) {
        ToggleButton button = new ToggleButton();
        setupDragAndDropSource(button);
        setupDragAndDropTarget(button);
        button.setMinWidth(28);
        button.setMinHeight(28);
        String css = Objects.requireNonNull(this.getClass().getResource("Sidebar.css")).toExternalForm();
        button.getStylesheets().add(css);
        button.setGraphic(node);
        toggleGroup.getToggles().add(button);
        return button;
    }

    private void setSpacer() {
        spacer = new StackPane();
        spacer.setStyle("-fx-background-color:black;-fx-opacity:.1;-fx-background-radius:5");
        spacer.setMinHeight(3);
    }

    private void insertDraggedButton(DragEvent event) {
        event.setDropCompleted(workspaceFX.getDragAndDropButton() != null && workspaceFX.getDragAndDropPlugin() != null);
        workspaceFX.getDragAndDropSidebar().removeButton(workspaceFX.getDragAndDropButton());
        addButton(workspaceFX.getDragAndDropPlugin(), indexToInsertDragAndDropButton);
        event.consume();
    }

    private void setupDragAndDropTarget(ToggleButton button) {
        // Target sidebar (vbox)
        setOnDragOver(event -> {
            if (event.getGestureSource() != button && event.getDragboard().hasString() && event.getDragboard().getString().startsWith("WorkspaceFX_")) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                if (spacer == null) {
                    setSpacer();
                }
                if (!getChildren().contains(spacer)) {
                    getChildren().add(spacer);
                }
                indexToInsertDragAndDropButton = getChildren().indexOf(spacer);
            }
        });
        setOnDragExited(event -> {
            getChildren().remove(spacer);
            spacer = null;
        });
        setOnDragDropped(this::insertDraggedButton);

        // Target button
        button.setOnDragOver(event -> {
            // Give a visual feedback where to drop the button
            if (event.getGestureSource() != button && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

                if (spacer == null) {
                    setSpacer();
                    index = getChildren().indexOf(button);
                }

                if (event.getY() <= button.getHeight() / 2) {
                    indexToInsertDragAndDropButton = index;
                } else {
                    indexToInsertDragAndDropButton = index + 1;
                }
                int currentIndex = getChildren().indexOf(spacer);
                if (currentIndex != indexToInsertDragAndDropButton || currentIndex == -1) {
                    getChildren().remove(spacer);
                    if (indexToInsertDragAndDropButton == -1) {
                        getChildren().addFirst(spacer);
                    } else {
                        getChildren().add(indexToInsertDragAndDropButton, spacer);
                    }
                }
            }
            event.consume();
        });
        button.setOnDragExited(event -> {
            getChildren().remove(spacer);
            spacer = null;
        });
        button.setOnDragDropped(this::insertDraggedButton);
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

    public enum SPLITPANE_POSITION {FIRST, LAST}

}
