module org.hometree.workspacefx {
    exports org.carpinus.betulus.workspacefx.plugin.clock;
    exports org.carpinus.betulus.workspacefx.plugin.fxproperty;
    exports org.carpinus.betulus.workspacefx.plugin.general;
    exports org.carpinus.betulus.workspacefx.plugin.logging;
    exports org.carpinus.betulus.workspacefx.plugin;
    exports org.carpinus.betulus.workspacefx.sidebar;
    exports org.carpinus.betulus.workspacefx;
    exports org.carpinus.betulus.workspacefx.utils.veil;

    opens org.carpinus.betulus.workspacefx to javafx.fxml;
    opens org.carpinus.betulus.workspacefx.plugin to javafx.fxml;
    opens org.carpinus.betulus.workspacefx.plugin.clock to javafx.fxml;
    opens org.carpinus.betulus.workspacefx.plugin.directory to javafx.fxml;
    opens org.carpinus.betulus.workspacefx.plugin.fxproperty to javafx.fxml;
    opens org.carpinus.betulus.workspacefx.plugin.general to javafx.fxml;
    opens org.carpinus.betulus.workspacefx.plugin.logging to javafx.fxml, javafx.base;
    opens org.carpinus.betulus.workspacefx.plugin.notification to javafx.fxml;
    opens org.carpinus.betulus.workspacefx.utils.veil to javafx.fxml;

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