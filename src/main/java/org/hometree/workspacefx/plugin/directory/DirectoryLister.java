package org.hometree.workspacefx.plugin.directory;

import atlantafx.base.theme.Styles;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignF;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

abstract class DirectoryLister<T> extends AWorkspaceFXPlugin implements Initializable {

    private ObjectProperty<Path> selectedDirectory;
    @FXML
    private TextField directory;
    @FXML
    private Button directorySelector;
    @FXML
    private ListView<T> fileList;
    @FXML
    private VBox controlsVBox;

    public DirectoryLister() {
        super("Directory", MaterialDesignF.FOLDER_OUTLINE,"DirectoryLister.fxml");
        Styles.addStyleClass(getContent(), Styles.DENSE);
        Styles.addStyleClass(getContent(), Styles.BORDERED);
    }

    public DirectoryLister(String shortDescription, Ikon icon) {
        super(shortDescription, icon,"DirectoryLister.fxml");
        Styles.addStyleClass(getContent(), Styles.DENSE);
        Styles.addStyleClass(getContent(), Styles.BORDERED);
    }

    public Path getSelectedDirectory() {
        return selectedDirectory.getValue();
    }

    public void setSelectedDirectory(Path selectedDirectory) {
        this.selectedDirectory.setValue(selectedDirectory);
    }

    public ObjectProperty<Path> selectedDirectoryProperty() {
        return this.selectedDirectory;
    }

    public TextField getDirectory() {
        return directory;
    }

    public Button getDirectorySelector() {
        return directorySelector;
    }

    public ListView<T> getFileList() {
        return fileList;
    }

    public VBox getControlsVBox() {
        return controlsVBox;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        directorySelector.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File dir = directoryChooser.showDialog(null);
            if (dir != null) {
                setSelectedDirectory(dir.toPath());
                directory.setText(getSelectedDirectory().toString());
            }
        });
        selectedDirectory = new SimpleObjectProperty<>();
    }

}
