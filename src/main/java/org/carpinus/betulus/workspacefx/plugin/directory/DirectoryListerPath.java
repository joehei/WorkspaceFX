package org.carpinus.betulus.workspacefx.plugin.directory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class DirectoryListerPath extends DirectoryLister<Path> {

    public DirectoryListerPath() {
        super();
        selectedDirectoryProperty().addListener((obs, ov, nv) -> {
            getVeil().setVisible(true);
            Task<List<Path>> worker = new Task<>() {
                @Override
                protected List<Path> call() throws Exception {
                    ObservableList<Path> list;
                    try (Stream<Path> s = Files.list(nv)) {
                        list = FXCollections.observableArrayList(s.filter(Files::isRegularFile).toList());
                        list.sort(Comparator.comparing(o -> o.getFileName().toString()));
                    }
                    return list;
                }
            };
            worker.setOnSucceeded(event -> {
                getVeil().setVisible(false);
                getFileList().setItems((ObservableList<Path>) worker.getValue());
            });
            worker.setOnFailed(even -> {
                getVeil().setVisible(false);
                logger.error(worker.getException().getMessage(), worker.getException());
            });
            Thread t = new Thread(worker);
            t.setDaemon(true);
            t.start();
        });

        getFileList().getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> logger.info("File selected : {}", nv));
        getFileList().setCellFactory(new Callback<>() {
            @Override
            public ListCell<Path> call(ListView<Path> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Path item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(null);
                            setText(item.getFileName().toString());
                        }
                    }
                };
            }
        });
    }

}
