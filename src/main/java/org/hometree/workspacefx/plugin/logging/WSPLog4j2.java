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
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.spi.StandardLevel;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Styles.toggleStyleClass(table, Styles.BORDERED);
        Styles.toggleStyleClass(table, Styles.DENSE);

        Styles.toggleStyleClass(totalInfo, Styles.BG_SUCCESS_SUBTLE);


        initSearchField();

        table.setItems(filteredList);
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        colLevel.setStyle(colLevel.getStyle()+";-fx-alignment: CENTER;");
        colLevel.setCellFactory(new Callback<TableColumn<WSPLog4j2LogMessage, Level>, TableCell<WSPLog4j2LogMessage, Level>>() {
            @Override
            public TableCell<WSPLog4j2LogMessage, Level> call(TableColumn<WSPLog4j2LogMessage, Level> wspLog4j2LogMessageLevelTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Level level, boolean b) {
                        super.updateItem(level, b);
                        if (level==null){
                            setGraphic(null);
                            setText(null);
                            return;
                        }
                        FontIcon icon = new FontIcon(MaterialDesignP.PROGRESS_QUESTION);
                        if (level.intLevel() == StandardLevel.INFO.intLevel()) {
                            icon.setIconCode(MaterialDesignI.INFORMATION);
                            icon.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);
                        }
                        if (level.intLevel() == StandardLevel.WARN.intLevel()) {
                            icon.setIconCode(MaterialDesignA.ALERT_CIRCLE);
                            icon.pseudoClassStateChanged(Styles.STATE_WARNING, true);
                        }
                        if (level.intLevel() == StandardLevel.ERROR.intLevel()) {
                            icon.setIconCode(MaterialDesignA.ALERT_CIRCLE);
                            icon.pseudoClassStateChanged(Styles.STATE_DANGER, true);
                        }
                        if (level.intLevel() == StandardLevel.DEBUG.intLevel()) {
                            icon.setIconCode(MaterialDesignC.CARD_SEARCH_OUTLINE);
                            Styles.appendStyle(icon,"-fx-text-fill", "yellow");
                        }
                        setText(null);
                        setGraphic(icon);
                    }
                };
            }
        });
        colTS.setCellValueFactory(new PropertyValueFactory<>("ts"));
        colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        colException.setCellValueFactory(new PropertyValueFactory<>("throwable"));

    }

    private void initSearchField() {
        var searchTF = new CustomTextField();
        searchTF.setMinWidth(500);
        searchTF.setPromptText("Search ...");
        FontIcon icon = new FontIcon(MaterialDesignC.CLOSE_THICK);
        icon.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                searchTF.setText("");
            }
        });
        searchTF.setRight(icon);
        searchTF.setPrefWidth(150);
        hBox.getChildren().add(searchTF);
    }
}
