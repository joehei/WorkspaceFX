package org.hometree.workspacefx;

import atlantafx.base.theme.NordLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.apache.logging.log4j.core.config.Configurator;
import org.hometree.workspacefx.plugin.clock.WSPAnalogClock;
import org.hometree.workspacefx.plugin.general.WSPButton;
import org.hometree.workspacefx.plugin.clock.WSPDigitalClock;
import org.hometree.workspacefx.plugin.general.WSPLabel;
import org.hometree.workspacefx.plugin.logging.WSPLog4j2;
import org.kordamp.ikonli.materialdesign2.MaterialDesignB;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;
import org.kordamp.ikonli.materialdesign2.MaterialDesignW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkspaceFXDemo extends Application {

    @Override
    public void start(Stage stage) {

        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());

        WorkspaceFX ws = new WorkspaceFX();

        ws.addPlugin(new WSPDigitalClock(), WorkspaceFX.WORKSPACE_POSITION.LEFT_TOP, true);
        ws.addPlugin(new WSPAnalogClock(), WorkspaceFX.WORKSPACE_POSITION.LEFT_TOP,true);
        ws.addPlugin(new WSPLabel(MaterialDesignF.FILE_TREE, "left top 2"), WorkspaceFX.WORKSPACE_POSITION.LEFT_TOP);

        WSPButton logInfo = new WSPButton("Log info", MaterialDesignC.CHAT_OUTLINE, "Log info text button.");
        logInfo.getButton().setOnAction(event -> log.info("Test log message"));
        ws.addPlugin(logInfo, WorkspaceFX.WORKSPACE_POSITION.LEFT_BOTTOM, true);

        ws.addPlugin(new WSPLabel(MaterialDesignF.FILE_TREE, "left bottom 2"), WorkspaceFX.WORKSPACE_POSITION.LEFT_BOTTOM);

        ws.addPlugin(new WSPLabel(MaterialDesignF.FILE_TREE, "bottom left 1"), WorkspaceFX.WORKSPACE_POSITION.BOTTOM_LEFT);
        ws.addPlugin(new WSPLog4j2(), WorkspaceFX.WORKSPACE_POSITION.BOTTOM_LEFT, true);


        ws.addPlugin(new WSPLabel(MaterialDesignF.FORMAT_LIST_BULLETED, "right top 1"), WorkspaceFX.WORKSPACE_POSITION.RIGHT_TOP);
        ws.addPlugin(new WSPLabel(MaterialDesignF.FORMAT_LIST_BULLETED, "right top 2"), WorkspaceFX.WORKSPACE_POSITION.RIGHT_TOP);

        ws.addPlugin(new WSPLabel(MaterialDesignB.BELL, "right bottom 1"), WorkspaceFX.WORKSPACE_POSITION.RIGHT_BOTTOM);
        ws.addPlugin(new WSPLabel(MaterialDesignB.BELL, "right bottom 2"), WorkspaceFX.WORKSPACE_POSITION.RIGHT_BOTTOM);

        ws.addPlugin(new WSPLabel(MaterialDesignW.WEATHER_SUNNY, "bottom right 1"), WorkspaceFX.WORKSPACE_POSITION.BOTTOM_RIGHT);
        ws.addPlugin(new WSPLabel(MaterialDesignW.WEATHER_SUNNY, "bottom right 2"), WorkspaceFX.WORKSPACE_POSITION.BOTTOM_RIGHT);

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

    private static final Logger log = LoggerFactory.getLogger(WorkspaceFXDemo.class);

    public static void main(String[] args) {
        Configurator.initialize(null, "log4j2.xml");
        launch();
    }
}