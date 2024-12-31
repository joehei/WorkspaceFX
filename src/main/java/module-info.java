module org.hometree.workspacefx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.controlsfx.controls;

    opens org.hometree.workspacefx to javafx.fxml;
    opens org.hometree.workspacefx.plugin to javafx.fxml;

    exports org.hometree.workspacefx;
    exports org.hometree.workspacefx.sidebar;
    exports org.hometree.workspacefx.plugin;
}