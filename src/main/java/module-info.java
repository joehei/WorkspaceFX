module org.hometree.workspacefx {
    exports org.hometree.workspacefx.plugin.clock;
    exports org.hometree.workspacefx.plugin.fxproperty;
    exports org.hometree.workspacefx.plugin.general;
    exports org.hometree.workspacefx.plugin.logging;
    exports org.hometree.workspacefx.plugin;
    exports org.hometree.workspacefx.sidebar;
    exports org.hometree.workspacefx;
    exports org.hometree.workspacefx.utils.veil;

    opens org.hometree.workspacefx to javafx.fxml;
    opens org.hometree.workspacefx.plugin to javafx.fxml;
    opens org.hometree.workspacefx.plugin.clock to javafx.fxml;
    opens org.hometree.workspacefx.plugin.directory to javafx.fxml;
    opens org.hometree.workspacefx.plugin.fxproperty to javafx.fxml;
    opens org.hometree.workspacefx.plugin.general to javafx.fxml;
    opens org.hometree.workspacefx.plugin.logging to javafx.fxml, javafx.base;
    opens org.hometree.workspacefx.plugin.notification to javafx.fxml;
    opens org.hometree.workspacefx.utils.veil to javafx.fxml;

    requires atlantafx.base;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j.core;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.slf4j;
}