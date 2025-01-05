module org.hometree.workspacefx {
    requires atlantafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j.core;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.slf4j;

    opens org.hometree.workspacefx to javafx.fxml;
    opens org.hometree.workspacefx.plugin to javafx.fxml;
    opens org.hometree.workspacefx.plugin.clock to javafx.fxml;
    opens org.hometree.workspacefx.plugin.fxproperty to javafx.fxml;
    opens org.hometree.workspacefx.plugin.general to javafx.fxml;
    opens org.hometree.workspacefx.plugin.logging to javafx.fxml, javafx.base;

    exports org.hometree.workspacefx.plugin.clock;
    exports org.hometree.workspacefx.plugin.fxproperty;
    exports org.hometree.workspacefx.plugin.general;
    exports org.hometree.workspacefx.plugin.logging to org.apache.logging.log4j.core;
    exports org.hometree.workspacefx.plugin;
    exports org.hometree.workspacefx.sidebar;
    exports org.hometree.workspacefx;
}