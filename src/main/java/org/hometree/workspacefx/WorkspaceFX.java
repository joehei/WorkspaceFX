package org.hometree.workspacefx;

import javafx.beans.InvalidationListener;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.hometree.workspacefx.sidebar.Sidebar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hometree.workspacefx.WorkspaceFX.WORKSPACE_POSITION.*;

public class WorkspaceFX extends HBox {

    private final List<AWorkspaceFXPlugin> plugins = new ArrayList<>();
    private final SplitPane mainTopLeft = new SplitPane();
    private final SplitPane mainTopRight = new SplitPane();
    private final SplitPane mainTop = new SplitPane();
    private final SplitPane mainBottom = new SplitPane();
    private final SplitPane main = new SplitPane();
    private final StackPane mainContentPane;
    private final Map<WORKSPACE_POSITION, Sidebar> sidebarMap = new HashMap<>();
    private double mainDividerBackup = .7d;
    private double mainTopDividerBackupLeft = .3d;
    private double mainTopDividerBackupRight = .7d;
    private ToggleButton dragAndDropButton;
    private Sidebar dragAndDropSidebar;
    private AWorkspaceFXPlugin dragAndDropPlugin;
    private Node content;
    private boolean isHiding = false;

    public WorkspaceFX() {
        sidebarMap.put(LEFT_TOP, new Sidebar(this, Pos.TOP_CENTER, Priority.NEVER, mainTopLeft, Sidebar.SPLITPANE_POSITION.FIRST, Sidebar.SIDEBAR_POSITION.LEFT));
        sidebarMap.put(LEFT_BOTTOM, new Sidebar(this, Pos.TOP_CENTER, Priority.ALWAYS, mainTopLeft, Sidebar.SPLITPANE_POSITION.LAST, Sidebar.SIDEBAR_POSITION.LEFT));
        // TODO : Add special button to show all active plugins

        sidebarMap.put(BOTTOM_LEFT, new Sidebar(this, Pos.BOTTOM_CENTER, Priority.ALWAYS, mainBottom, Sidebar.SPLITPANE_POSITION.FIRST, Sidebar.SIDEBAR_POSITION.LEFT));

        sidebarMap.put(RIGHT_TOP, new Sidebar(this, Pos.TOP_CENTER, Priority.NEVER, mainTopRight, Sidebar.SPLITPANE_POSITION.FIRST, Sidebar.SIDEBAR_POSITION.RIGHT));
        sidebarMap.put(RIGHT_BOTTOM, new Sidebar(this, Pos.TOP_CENTER, Priority.ALWAYS, mainTopRight, Sidebar.SPLITPANE_POSITION.LAST, Sidebar.SIDEBAR_POSITION.RIGHT));
        sidebarMap.put(BOTTOM_RIGHT, new Sidebar(this, Pos.BOTTOM_CENTER, Priority.ALWAYS, mainBottom, Sidebar.SPLITPANE_POSITION.LAST, Sidebar.SIDEBAR_POSITION.RIGHT));

        VBox sidebarLeft = new VBox();

        sidebarLeft.getChildren().addAll(sidebarMap.get(LEFT_TOP), new Separator(), sidebarMap.get(LEFT_BOTTOM), sidebarMap.get(BOTTOM_LEFT));

        mainContentPane = new StackPane();

        mainTopLeft.setOrientation(Orientation.VERTICAL);
        mainTopLeft.getItems().addListener((InvalidationListener) observable -> {
            backupMainTopDivider();
            if (mainTopLeft.getItems().isEmpty()) {
                mainTop.getItems().remove(mainTopLeft);
            } else {
                if (!mainTop.getItems().contains(mainTopLeft)) {
                    mainTop.getItems().addFirst(mainTopLeft);
                }
            }
            restoreMainTopDivider();
        });
        SplitPane.setResizableWithParent(mainTopLeft, false);

        mainTopRight.setOrientation(Orientation.VERTICAL);
        mainTopRight.getItems().addListener((InvalidationListener) observable -> {
            backupMainTopDivider();
            if (mainTopRight.getItems().isEmpty()) {
                mainTop.getItems().remove(mainTopRight);
            } else {
                if (!mainTop.getItems().contains(mainTopRight)) {
                    mainTop.getItems().addLast(mainTopRight);
                }
            }
            restoreMainTopDivider();
        });
        SplitPane.setResizableWithParent(mainTopRight, false);

        mainTop.setOrientation(Orientation.HORIZONTAL);
        mainTop.getItems().addAll(mainContentPane);
        mainBottom.setOrientation(Orientation.HORIZONTAL);
        mainBottom.getItems().addListener((InvalidationListener) observable -> {
            if (mainBottom.getItems().isEmpty()) {
                backupMainDivider();
                main.getItems().remove(mainBottom);
            } else {
                if (!main.getItems().contains(mainBottom)) {
                    main.getItems().add(mainBottom);
                }
                restoreMainDivider();
            }
        });
        SplitPane.setResizableWithParent(mainBottom, false);

        main.setOrientation(Orientation.VERTICAL);
        main.getItems().addAll(mainTop);
        setHgrow(main, Priority.ALWAYS);

        VBox sidebarRight = new VBox();
        sidebarRight.getChildren().addAll(sidebarMap.get(RIGHT_TOP), new Separator(), sidebarMap.get(RIGHT_BOTTOM), sidebarMap.get(BOTTOM_RIGHT));
        getChildren().addAll(sidebarLeft, main, sidebarRight);
    }

    public Map<WORKSPACE_POSITION, Sidebar> getSidebarMap() {
        return sidebarMap;
    }

    private boolean findPlugin(Sidebar s, AWorkspaceFXPlugin plugin) {
        for (Map.Entry<ToggleButton, AWorkspaceFXPlugin> entry : s.getButtonContentMap().entrySet()) {
            if (entry.getValue().equals(plugin)) {
                return true;
            }
        }
        return false;
    }

    public Sidebar getSidebar(AWorkspaceFXPlugin plugin) {
        if (findPlugin(sidebarMap.get(LEFT_TOP), plugin)) {
            return sidebarMap.get(LEFT_TOP);
        }
        if (findPlugin(sidebarMap.get(LEFT_BOTTOM), plugin)) {
            return sidebarMap.get(LEFT_BOTTOM);
        }
        if (findPlugin(sidebarMap.get(BOTTOM_LEFT), plugin)) {
            return sidebarMap.get(BOTTOM_LEFT);
        }

        if (findPlugin(sidebarMap.get(RIGHT_TOP), plugin)) {
            return sidebarMap.get(RIGHT_TOP);
        }
        if (findPlugin(sidebarMap.get(RIGHT_BOTTOM), plugin)) {
            return sidebarMap.get(RIGHT_BOTTOM);
        }
        if (findPlugin(sidebarMap.get(BOTTOM_RIGHT), plugin)) {
            return sidebarMap.get(BOTTOM_RIGHT);
        }
        return null;
    }

    public Node getContent() {
        return content;
    }

    public void setContent(Node content) {
        this.content = content;
        mainContentPane.getChildren().setAll(content);
    }

    public boolean isHiding() {
        return isHiding;
    }

    public void setHiding(boolean hiding) {
        isHiding = hiding;
    }

    public void hide() {
        sidebarMap.get(LEFT_TOP).hide();
        sidebarMap.get(LEFT_BOTTOM).hide();
        sidebarMap.get(RIGHT_TOP).hide();
        sidebarMap.get(RIGHT_BOTTOM).hide();
        sidebarMap.get(BOTTOM_LEFT).hide();
        sidebarMap.get(BOTTOM_RIGHT).hide();
        isHiding = true;
    }

    public void restore() {
        sidebarMap.get(LEFT_TOP).restore();
        sidebarMap.get(LEFT_BOTTOM).restore();
        sidebarMap.get(RIGHT_TOP).restore();
        sidebarMap.get(RIGHT_BOTTOM).restore();
        sidebarMap.get(BOTTOM_LEFT).restore();
        sidebarMap.get(BOTTOM_RIGHT).restore();
        isHiding = false;
    }

    public AWorkspaceFXPlugin getDragAndDropPlugin() {
        return dragAndDropPlugin;
    }

    public void setDragAndDropPlugin(AWorkspaceFXPlugin dragAndDropPlugin) {
        this.dragAndDropPlugin = dragAndDropPlugin;
    }

    public Sidebar getDragAndDropSidebar() {
        return dragAndDropSidebar;
    }

    public void setDragAndDropSidebar(Sidebar dragAndDropSidebar) {
        this.dragAndDropSidebar = dragAndDropSidebar;
    }

    public ToggleButton getDragAndDropButton() {
        return dragAndDropButton;
    }

    public void setDragAndDropButton(ToggleButton dragAndDropButton) {
        this.dragAndDropButton = dragAndDropButton;
    }

    private void backupMainTopDivider() {
        if (mainTop.getDividers().size() == 2) {
            mainTopDividerBackupLeft = mainTop.getDividers().getFirst().getPosition();
            mainTopDividerBackupRight = mainTop.getDividers().getLast().getPosition();
        }
        if (mainTop.getDividers().size() == 1 && mainTop.getItems().contains(mainTopLeft)) {
            mainTopDividerBackupLeft = mainTop.getDividers().getFirst().getPosition();
        }
        if (mainTop.getDividers().size() == 1 && mainTop.getItems().contains(mainTopRight)) {
            mainTopDividerBackupRight = mainTop.getDividers().getFirst().getPosition();
        }
    }

    private void restoreMainTopDivider() {
        if (mainTop.getDividers().size() == 2) {
            mainTop.getDividers().getFirst().setPosition(mainTopDividerBackupLeft);
            mainTop.getDividers().getLast().setPosition(mainTopDividerBackupRight);
        }
        if (mainTop.getDividers().size() == 1 && mainTop.getItems().contains(mainTopLeft)) {
            mainTop.getDividers().getFirst().setPosition(mainTopDividerBackupLeft);
        }
        if (mainTop.getDividers().size() == 1 && mainTop.getItems().contains(mainTopRight)) {
            mainTop.getDividers().getFirst().setPosition(mainTopDividerBackupRight);
        }
    }

    public List<AWorkspaceFXPlugin> getPlugins() {
        return plugins;
    }

    private void backupMainDivider() {
        if (!main.getDividers().isEmpty()) {
            mainDividerBackup = main.getDividers().getFirst().getPosition();
        }
    }

    private void restoreMainDivider() {
        if (!main.getDividers().isEmpty()) {
            main.getDividers().getFirst().setPosition(mainDividerBackup);
        }
    }

    public void addPlugin(AWorkspaceFXPlugin plugin, WORKSPACE_POSITION pos) {
        plugins.add(plugin);
        switch (pos) {
            case LEFT_TOP -> sidebarMap.get(LEFT_TOP).addButton(plugin);
            case LEFT_BOTTOM -> sidebarMap.get(LEFT_BOTTOM).addButton(plugin);
            case BOTTOM_LEFT -> sidebarMap.get(BOTTOM_LEFT).addButton(plugin);
            case RIGHT_TOP -> sidebarMap.get(RIGHT_TOP).addButton(plugin);
            case RIGHT_BOTTOM -> sidebarMap.get(RIGHT_BOTTOM).addButton(plugin);
            case BOTTOM_RIGHT -> sidebarMap.get(BOTTOM_RIGHT).addButton(plugin);
        }
    }

    public void addPlugin(AWorkspaceFXPlugin plugin, WORKSPACE_POSITION pos, boolean selected) {
        addPlugin(plugin, pos);
        getSidebar(plugin).getButton(plugin).setSelected(selected);
    }

    public WORKSPACE_POSITION getWorkspacePosition(Sidebar sidebar) {
        for (Map.Entry<WORKSPACE_POSITION, Sidebar> entry : sidebarMap.entrySet()) {
            if (entry.getValue().equals(sidebar)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public enum WORKSPACE_POSITION {LEFT_TOP, LEFT_BOTTOM, BOTTOM_LEFT, RIGHT_TOP, RIGHT_BOTTOM, BOTTOM_RIGHT}

}
