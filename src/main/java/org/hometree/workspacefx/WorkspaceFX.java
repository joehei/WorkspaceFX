package org.hometree.workspacefx;

import javafx.beans.InvalidationListener;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
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

    private double mainDividerBackup = .7d;

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

    public WorkspaceFX() {
        leftTop = new Sidebar(Pos.TOP_CENTER, Priority.NEVER, mainTopLeft, Sidebar.SPLITPANE_POSITION.FIRST);
        leftBottom = new Sidebar(Pos.TOP_CENTER, Priority.ALWAYS, mainTopLeft, Sidebar.SPLITPANE_POSITION.LAST);

        rightTop = new Sidebar(Pos.TOP_CENTER, Priority.NEVER, mainTopRight, Sidebar.SPLITPANE_POSITION.FIRST);
        rightBottom = new Sidebar(Pos.TOP_CENTER, Priority.ALWAYS, mainTopRight, Sidebar.SPLITPANE_POSITION.LAST);

        bottomLeft = new Sidebar(Pos.BOTTOM_CENTER, Priority.ALWAYS, mainBottom, Sidebar.SPLITPANE_POSITION.FIRST);
        bottomRight = new Sidebar(Pos.BOTTOM_CENTER, Priority.ALWAYS, mainBottom, Sidebar.SPLITPANE_POSITION.LAST);

        VBox sidebarLeft = new VBox();

        sidebarLeft.getChildren().addAll(leftTop, new Separator(), leftBottom, bottomLeft);

        StackPane mainContent = new StackPane(new Label("mainContent"));

        mainTopLeft.setOrientation(Orientation.VERTICAL);
        mainTopLeft.getItems().addListener((InvalidationListener) observable -> {
            if (mainTopLeft.getItems().isEmpty()) {
                mainTop.getItems().remove(mainTopLeft);
            } else {
                if (!mainTop.getItems().contains(mainTopLeft)) {
                    mainTop.getItems().addFirst(mainTopLeft);
                }
            }
        });
        mainTopRight.setOrientation(Orientation.VERTICAL);
        mainTopRight.getItems().addListener((InvalidationListener) observable -> {
            if (mainTopRight.getItems().isEmpty()) {
                mainTop.getItems().remove(mainTopRight);
            } else {
                if (!mainTop.getItems().contains(mainTopRight)) {
                    mainTop.getItems().addLast(mainTopRight);
                }
            }
        });

        mainTop.setOrientation(Orientation.HORIZONTAL);
        mainTop.getItems().addAll(mainContent);
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

        main.setOrientation(Orientation.VERTICAL);
        main.getItems().addAll(mainTop);
        setHgrow(main, Priority.ALWAYS);

        VBox sidebarRight = new VBox();
        sidebarRight.getChildren().addAll(rightTop, new Separator(), rightBottom, bottomRight);
        getChildren().addAll(sidebarLeft, main, sidebarRight);
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
