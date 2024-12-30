package org.hometree.workspacefx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.hometree.workspacefx.plugin.LabelPlugin;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;
import org.kordamp.ikonli.materialdesign2.MaterialDesignW;

public class WorkspaceFXDemo extends Application {

    @Override
    public void start(Stage stage) {
        WorkspaceFX ws = new WorkspaceFX();

        ws.addPlugin(new LabelPlugin(MaterialDesignF.FILE_TREE, "left top 1"), WorkspaceFX.WORKSPACE_POSITION.LEFT_TOP);
        ws.addPlugin(new LabelPlugin(MaterialDesignF.FILE_TREE, "left top 2"), WorkspaceFX.WORKSPACE_POSITION.LEFT_TOP);

        ws.addPlugin(new LabelPlugin(MaterialDesignF.FILE_TREE, "left bottom 1"), WorkspaceFX.WORKSPACE_POSITION.LEFT_BOTTOM);
        ws.addPlugin(new LabelPlugin(MaterialDesignF.FILE_TREE, "left bottom 2"), WorkspaceFX.WORKSPACE_POSITION.LEFT_BOTTOM);

        ws.addPlugin(new LabelPlugin(MaterialDesignF.FILE_TREE, "bottom left 1"), WorkspaceFX.WORKSPACE_POSITION.BOTTOM_LEFT);
        ws.addPlugin(new LabelPlugin(MaterialDesignF.FILE_TREE, "bottom left 2"), WorkspaceFX.WORKSPACE_POSITION.BOTTOM_LEFT);


        ws.addPlugin(new LabelPlugin(MaterialDesignF.FORMAT_LIST_BULLETED, "right top 1"), WorkspaceFX.WORKSPACE_POSITION.RIGHT_TOP);
        ws.addPlugin(new LabelPlugin(MaterialDesignF.FORMAT_LIST_BULLETED, "right top 2"), WorkspaceFX.WORKSPACE_POSITION.RIGHT_TOP);

        ws.addPlugin(new LabelPlugin(MaterialDesignB.BELL, "right bottom 1"), WorkspaceFX.WORKSPACE_POSITION.RIGHT_BOTTOM);
        ws.addPlugin(new LabelPlugin(MaterialDesignB.BELL, "right bottom 2"), WorkspaceFX.WORKSPACE_POSITION.RIGHT_BOTTOM);

        ws.addPlugin(new LabelPlugin(MaterialDesignW.WEATHER_SUNNY, "bottom right 1"), WorkspaceFX.WORKSPACE_POSITION.BOTTOM_RIGHT);
        ws.addPlugin(new LabelPlugin(MaterialDesignW.WEATHER_SUNNY, "bottom right 2"), WorkspaceFX.WORKSPACE_POSITION.BOTTOM_RIGHT);

        Label mainContent = new Label("Double click me for hide and restore");
        mainContent.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (ws.isHiding()) {
                    ws.restore();
                } else {
                    ws.hide();
                }
            }
        });
        ws.setContent(mainContent);

        Scene scene = new Scene(ws, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}