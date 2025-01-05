package org.hometree.workspacefx.plugin.logging;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.spi.StandardLevel;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class WSPLog4j2 extends AWorkspaceFXPlugin implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(WSPLog4j2.class);
    private final ObservableList<WSPLog4j2LogMessage> messages = FXCollections.observableArrayList();
    private final FilteredList<WSPLog4j2LogMessage> filteredList = new FilteredList<>(messages);
    @FXML
    private HBox hBox;
    @FXML
    private CheckBox showDebug;
    @FXML
    private CheckBox showError;
    @FXML
    private CheckBox showInfo;
    @FXML
    private CheckBox showWarn;
    @FXML
    private TableView<WSPLog4j2LogMessage> table;
    @FXML
    private Label totalDebug;
    @FXML
    private Label totalError;
    @FXML
    private Label totalInfo;
    @FXML
    private Label totalWarn;
    @FXML
    private TableColumn<WSPLog4j2LogMessage, Throwable> colException;
    @FXML
    private TableColumn<WSPLog4j2LogMessage, String> colMessage;
    @FXML
    private TableColumn<WSPLog4j2LogMessage, ZonedDateTime> colTS;
    @FXML
    private TableColumn<WSPLog4j2LogMessage, Level> colLevel;
    @FXML
    private CustomTextField searchTF;

    public WSPLog4j2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getClass().getSimpleName() + ".fxml"));
            loader.setController(this);
            setContent(loader.load());
            setShortDescription("Logging");
            FontIcon icon = new FontIcon(MaterialDesignF.FORMAT_LIST_TEXT);
            icon.setIconSize(16);
            setToggleButtonContent(icon);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public ObservableList<WSPLog4j2LogMessage> getMessages() {
        return messages;
    }

    private FontIcon getIcon(Ikon code, String color) {
        FontIcon icon = new FontIcon(code);
        Styles.appendStyle(icon, "-fx-fill", color);
        Styles.appendStyle(icon, "-fx-icon-color", color);
        return icon;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Styles.addStyleClass(table, Styles.DENSE);
        Styles.addStyleClass(table, Styles.BORDERED);
        initSearch();

        table.setItems(filteredList);
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        colLevel.setStyle(colLevel.getStyle() + ";-fx-alignment: CENTER;");
        colLevel.setCellFactory(new Callback<>() {
            @Override
            public TableCell<WSPLog4j2LogMessage, Level> call(TableColumn<WSPLog4j2LogMessage, Level> wspLog4j2LogMessageLevelTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Level level, boolean b) {
                        super.updateItem(level, b);
                        setText(null);
                        if (level == null) {
                            setGraphic(null);
                        } else if (level.intLevel() == StandardLevel.INFO.intLevel()) {
                            setGraphic(getIcon(MaterialDesignI.INFORMATION, "#00e007"));
                        } else if (level.intLevel() == StandardLevel.WARN.intLevel()) {
                            setGraphic(getIcon(MaterialDesignA.ALERT, "#d9ed00"));
                        } else if (level.intLevel() == StandardLevel.ERROR.intLevel()) {
                            setGraphic(getIcon(MaterialDesignA.ALERT_OCTAGON, "#ff7b7b"));
                        } else if (level.intLevel() == StandardLevel.DEBUG.intLevel()) {
                            setGraphic(getIcon(MaterialDesignT.TEXT_BOX_SEARCH_OUTLINE, "lightgray"));
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        colTS.setCellValueFactory(new PropertyValueFactory<>("ts"));
        colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        colException.setCellValueFactory(new PropertyValueFactory<>("throwable"));
    }

    private boolean filterMessages(WSPLog4j2LogMessage message) {
        boolean show = false;
        if (searchTF.getText().isBlank()) {
            show = true;
        } else {
            show = message.getMessage().contains(searchTF.getText());
        }

        int level = message.getLevel().intLevel();
        if (level == StandardLevel.INFO.intLevel()) {
            show = show && showInfo.isSelected();
        }
        if (level == StandardLevel.WARN.intLevel()) {
            show = show && showWarn.isSelected();
        }
        if (level == StandardLevel.ERROR.intLevel()) {
            show = show && showError.isSelected();
        }
        if (level == StandardLevel.DEBUG.intLevel()) {
            show = show && showDebug.isSelected();
        }

        return show;
    }

    private void initSearch() {
        searchTF.textProperty().addListener((obs, ov, nv) -> filteredList.setPredicate(this::filterMessages));
        FontIcon icon = new FontIcon(MaterialDesignC.CLOSE_THICK);
        icon.setOnMouseClicked(event -> searchTF.setText(""));
        searchTF.setRight(icon);
        showInfo.selectedProperty().addListener((obs, ov, nv) -> filteredList.setPredicate(this::filterMessages));
        showWarn.selectedProperty().addListener((obs, ov, nv) -> filteredList.setPredicate(this::filterMessages));
        showError.selectedProperty().addListener((obs, ov, nv) -> filteredList.setPredicate(this::filterMessages));
        showDebug.selectedProperty().addListener((obs, ov, nv) -> filteredList.setPredicate(this::filterMessages));
    }
}
