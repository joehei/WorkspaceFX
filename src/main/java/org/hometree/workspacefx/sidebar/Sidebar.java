package org.hometree.workspacefx.sidebar;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;

import java.util.HashMap;
import java.util.Objects;

public class Sidebar extends VBox {

    private final ToggleGroup toggleGroup;
    private final HashMap<ToggleButton, AWorkspaceFXPlugin> buttonContentMap = new HashMap<>();
    private final SplitPane contentSplitPane;
    private final SPLITPANE_POSITION contentSplitPanePosition;
    private StackPane spacer;
    private int index;
    private double dividerPos = .5d;

    public Sidebar(Pos alignment, Priority priority, SplitPane contentSplitPane, SPLITPANE_POSITION contentSplitPanePosition) {
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

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    private EventHandler<MouseEvent> getEventHandlerMouseMoved(ToggleButton button) {
        return event -> {
            if (spacer == null) {
                spacer = new StackPane();
                spacer.setStyle("-fx-background-color:black;-fx-opacity:.1;-fx-background-radius:5");
                spacer.setMinHeight(3);
                index = getChildren().indexOf(button);
            }
            int indexToInsert;
            if (event.getY() <= button.getHeight() / 2) {
                indexToInsert = index;
            } else {
                indexToInsert = index + 1;
            }
            int currentIndex = getChildren().indexOf(spacer);
            if (currentIndex != indexToInsert || currentIndex == -1) {
                getChildren().remove(spacer);
                if (indexToInsert == -1) {
                    getChildren().addFirst(spacer);
                } else {
                    getChildren().add(indexToInsert, spacer);
                }
            }
        };
    }

    private EventHandler<MouseEvent> getEventHandlerMouseExited() {
        return event -> {
            getChildren().remove(spacer);
            spacer = null;
        };
    }

    public void addButton(AWorkspaceFXPlugin plugin) {
        ToggleButton toggleButton = getButton(plugin.getToggleButtonContent());
        buttonContentMap.put(toggleButton, plugin);
        getChildren().add(toggleButton);
    }

    private ToggleButton getButton(Node node) {
        ToggleButton button = new ToggleButton();
        // TODO : Implement drag and drop
//        button.addEventFilter(MouseEvent.MOUSE_MOVED, getEventHandlerMouseMoved(button));
//        button.addEventFilter(MouseEvent.MOUSE_EXITED, getEventHandlerMouseExited());
        button.setMinWidth(28);
        button.setMinHeight(28);
        String css = Objects.requireNonNull(this.getClass().getResource("Sidebar.css")).toExternalForm();
        button.getStylesheets().add(css);
        button.setGraphic(node);
        toggleGroup.getToggles().add(button);
        return button;
    }

    public enum SPLITPANE_POSITION {FIRST, LAST}

}
