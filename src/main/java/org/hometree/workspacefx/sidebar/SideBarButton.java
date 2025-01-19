package org.hometree.workspacefx.sidebar;

import atlantafx.base.controls.Popover;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import org.hometree.workspacefx.WorkspaceFX;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;

import static org.hometree.workspacefx.WorkspaceFX.WORKSPACE_POSITION.*;

public class SideBarButton extends ToggleButton {

    private void transferButton(Sidebar sidebar, AWorkspaceFXPlugin plugin, WorkspaceFX.WORKSPACE_POSITION position) {
        sidebar.removeButton(this);
        sidebar.getWorkspaceFX().getSidebarMap().get(position).addButton(plugin);
    }

    public SideBarButton(AWorkspaceFXPlugin plugin, Sidebar sidebar) {
        Popover popOver = new Popover();
        popOver.setArrowSize(10);
        double offset;

        if (sidebar.getSidebarPos().equals(Sidebar.SIDEBAR_POSITION.RIGHT)) {
            popOver.setArrowLocation(Popover.ArrowLocation.RIGHT_CENTER);
            offset = -10;
        } else {
            offset = -5;
        }

        Label label = new Label(plugin.getShortDescription());
        label.setFont(Font.font("Monospaced", 11));

        StackPane content = new StackPane(label);
        plugin.shortDescriptionProperty().addListener((obs, ov, nv) -> label.setText(nv));
        content.setPadding(new Insets(5));
        popOver.setContentNode(content);
        popOver.setArrowSize(0);
        setOnMouseEntered(event -> popOver.show(this, offset));
        setOnMouseExited(event -> popOver.hide());
        sidebar.setupDragAndDropSource(this);
        sidebar.setupDragAndDropTarget(this);
        setMinWidth(28);
        setMinHeight(28);
        setGraphic(plugin.getButtonGraphic());
        sidebar.getToggleGroup().getToggles().add(this);
        selectedProperty().addListener((obs, ov, nv) -> plugin.setPluginSelected(nv));

        Menu moveTo = new Menu("Move to");
        MenuItem cmHide = new MenuItem("Hide");
        cmHide.setOnAction(event -> {
            setSelected(false);
            setVisible(false);
            setManaged(false);
        });

        SeparatorMenuItem sep = new SeparatorMenuItem();
        MenuItem cmLeftTop = new MenuItem("Left Top");
        cmLeftTop.setOnAction(event -> transferButton(sidebar, plugin, LEFT_TOP));

        MenuItem cmLeftBottom = new MenuItem("Left Bottom");
        cmLeftBottom.setOnAction(event -> transferButton(sidebar, plugin, LEFT_BOTTOM));

        MenuItem cmBottomLeft = new MenuItem("Bottom Left");
        cmBottomLeft.setOnAction(event -> transferButton(sidebar, plugin, BOTTOM_LEFT));

        MenuItem cmBottomRight = new MenuItem("Bottom Right");
        cmBottomRight.setOnAction(event -> transferButton(sidebar, plugin, BOTTOM_RIGHT));

        MenuItem cmRightBottom = new MenuItem("Right Bottom");
        cmRightBottom.setOnAction(event -> transferButton(sidebar, plugin, RIGHT_BOTTOM));

        MenuItem cmRightTop = new MenuItem("Right Top");
        cmRightTop.setOnAction(event -> transferButton(sidebar, plugin, RIGHT_TOP));

        switch (sidebar.getWorkspaceFX().getWorkspacePosition(sidebar)) {
            case LEFT_TOP -> cmLeftTop.setDisable(true);
            case LEFT_BOTTOM -> cmLeftBottom.setDisable(true);
            case BOTTOM_LEFT -> cmBottomLeft.setDisable(true);
            case RIGHT_TOP -> cmRightTop.setDisable(true);
            case RIGHT_BOTTOM -> cmRightBottom.setDisable(true);
            case BOTTOM_RIGHT -> cmBottomRight.setDisable(true);
        }

        moveTo.getItems().addAll(cmLeftTop, cmLeftBottom, cmBottomLeft, cmBottomRight, cmRightBottom, cmRightTop);
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(cmHide, sep, moveTo);

        setContextMenu(contextMenu);
    }
}
