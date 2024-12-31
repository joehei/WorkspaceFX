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
import java.util.List;

public class WorkspaceFX extends HBox {

    private final Sidebar leftTop;
    private final Sidebar leftBottom;
    private final Sidebar bottomLeft;
    private final Sidebar rightTop;
    private final Sidebar rightBottom;
    private final Sidebar bottomRight;
    private final List<AWorkspaceFXPlugin> plugins = new ArrayList<>();
    private final SplitPane mainTopLeft = new SplitPane();
    private final SplitPane mainTopRight = new SplitPane();
    private final SplitPane mainTop = new SplitPane();
    private final SplitPane mainBottom = new SplitPane();
    private final SplitPane main = new SplitPane();
    private final StackPane mainContentPane;
    private double mainDividerBackup = .7d;
    private double mainTopDividerBackupLeft = .3d;
    private double mainTopDividerBackupRight = .7d;
    private ToggleButton dragAndDropButton;
    private Sidebar dragAndDropSidebar;
    private AWorkspaceFXPlugin dragAndDropPlugin;
    private Node content;

    public WorkspaceFX() {
        leftTop = new Sidebar(this, Pos.TOP_CENTER, Priority.NEVER, mainTopLeft, Sidebar.SPLITPANE_POSITION.FIRST, Sidebar.SIDEBAR_POSITION.LEFT);
        leftBottom = new Sidebar(this, Pos.TOP_CENTER, Priority.ALWAYS, mainTopLeft, Sidebar.SPLITPANE_POSITION.LAST, Sidebar.SIDEBAR_POSITION.LEFT);

        rightTop = new Sidebar(this, Pos.TOP_CENTER, Priority.NEVER, mainTopRight, Sidebar.SPLITPANE_POSITION.FIRST, Sidebar.SIDEBAR_POSITION.RIGHT);
        rightBottom = new Sidebar(this, Pos.TOP_CENTER, Priority.ALWAYS, mainTopRight, Sidebar.SPLITPANE_POSITION.LAST, Sidebar.SIDEBAR_POSITION.RIGHT);

        bottomLeft = new Sidebar(this, Pos.BOTTOM_CENTER, Priority.ALWAYS, mainBottom, Sidebar.SPLITPANE_POSITION.FIRST, Sidebar.SIDEBAR_POSITION.LEFT);
        bottomRight = new Sidebar(this, Pos.BOTTOM_CENTER, Priority.ALWAYS, mainBottom, Sidebar.SPLITPANE_POSITION.LAST, Sidebar.SIDEBAR_POSITION.RIGHT);

        VBox sidebarLeft = new VBox();

        sidebarLeft.getChildren().addAll(leftTop, new Separator(), leftBottom, bottomLeft);

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
        SplitPane.setResizableWithParent(mainTopLeft,false);

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
        SplitPane.setResizableWithParent(mainTopRight,false);

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
        SplitPane.setResizableWithParent(mainBottom,false);

        main.setOrientation(Orientation.VERTICAL);
        main.getItems().addAll(mainTop);
        setHgrow(main, Priority.ALWAYS);

        VBox sidebarRight = new VBox();
        sidebarRight.getChildren().addAll(rightTop, new Separator(), rightBottom, bottomRight);
        getChildren().addAll(sidebarLeft, main, sidebarRight);
    }

    public Node getContent() {
        return content;
    }

    public boolean isHiding() {
        return isHiding;
    }

    public void setHiding(boolean hiding) {
        isHiding = hiding;
    }

    private boolean isHiding=false;

    public void setContent(Node content) {
        this.content = content;
        mainContentPane.getChildren().setAll(content);
    }

    public void hide(){
        leftTop.hide();
        leftBottom.hide();
        rightTop.hide();
        rightBottom.hide();
        bottomLeft.hide();
        bottomRight.hide();
        isHiding=true;
    }

    public void restore(){
        leftTop.restore();
        leftBottom.restore();
        rightTop.restore();
        rightBottom.restore();
        bottomLeft.restore();
        bottomRight.restore();
        isHiding=false;
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
            case LEFT_TOP -> leftTop.addButton(plugin);
            case LEFT_BOTTOM -> leftBottom.addButton(plugin);
            case BOTTOM_LEFT -> bottomLeft.addButton(plugin);
            case RIGHT_TOP -> rightTop.addButton(plugin);
            case RIGHT_BOTTOM -> rightBottom.addButton(plugin);
            case BOTTOM_RIGHT -> bottomRight.addButton(plugin);
        }
    }

    public enum WORKSPACE_POSITION {LEFT_TOP, LEFT_BOTTOM, BOTTOM_LEFT, RIGHT_TOP, RIGHT_BOTTOM, BOTTOM_RIGHT}

}
