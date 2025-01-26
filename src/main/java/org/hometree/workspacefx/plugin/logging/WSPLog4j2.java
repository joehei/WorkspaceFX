package org.hometree.workspacefx.plugin.logging;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.spi.StandardLevel;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.hometree.workspacefx.utils.WorkspaceFXUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WSPLog4j2 extends AWorkspaceFXPlugin implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(WSPLog4j2.class);
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
    private final ObservableList<WSPLog4j2LogMessage> messages = FXCollections.observableArrayList();
    private final FilteredList<WSPLog4j2LogMessage> filteredList = new FilteredList<>(messages);
    private final IntegerProperty infoCounter = new SimpleIntegerProperty();
    private final IntegerProperty warnCounter = new SimpleIntegerProperty();
    private final IntegerProperty errCounter = new SimpleIntegerProperty();
    private final IntegerProperty debugCounter = new SimpleIntegerProperty();

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
    @FXML
    private Button clearAll;
    @FXML
    private Button clearFilter;
    @FXML
    private ToggleButton scrollToEnd;
    @FXML
    private ToggleButton toggleDebugMode;

    public WSPLog4j2() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getClass().getSimpleName() + ".fxml"));
            loader.setController(this);
            setContent(loader.load());
            setShortDescription("Logging");
            setToggleButtonContent(MaterialDesignF.FORMAT_LIST_TEXT);

            // Define when a notification should be shown
            getMessages().addListener((ListChangeListener<WSPLog4j2LogMessage>) c -> {
                c.next();
                // When no log entries are in the list, hide the notification circle
                if (getMessages().isEmpty()) {
                    getButtonGraphic().setShowNotification(false);
                    return;
                }
                // If something was added and the plugin is not shown, show the circle, otherwise hide
                if (c.wasAdded()) {
                    getButtonGraphic().setShowNotification(!getPluginSelected());
                }
            });

            // When the plugin is shown the notification should be hidden
            pluginSelectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    getButtonGraphic().setShowNotification(false);
                }
            });

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Integer getInfoCounter() {
        return infoCounter.getValue();
    }

    public void setInfoCounter(Integer infoCounter) {
        this.infoCounter.setValue(infoCounter);
    }

    public IntegerProperty infoCounterProperty() {
        return this.infoCounter;
    }

    public Integer getWarnCounter() {
        return warnCounter.getValue();
    }

    public void setWarnCounter(Integer warnCounter) {
        this.warnCounter.setValue(warnCounter);
    }

    public IntegerProperty warnCounterProperty() {
        return this.warnCounter;
    }

    public Integer getErrCounter() {
        return errCounter.getValue();
    }

    public void setErrCounter(Integer errCounter) {
        this.errCounter.setValue(errCounter);
    }

    public IntegerProperty errCounterProperty() {
        return this.errCounter;
    }

    public Integer getDebugCounter() {
        return debugCounter.getValue();
    }

    public void setDebugCounter(Integer debugCounter) {
        this.debugCounter.setValue(debugCounter);
    }

    public IntegerProperty debugCounterProperty() {
        return this.debugCounter;
    }

    public ObservableList<WSPLog4j2LogMessage> getMessages() {
        return messages;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO : Add detail log view

        Styles.addStyleClass(table, Styles.DENSE);
        Styles.addStyleClass(table, Styles.BORDERED);
        initSearch();

        clearAll.setOnAction(event -> messages.clear());
        clearFilter.setOnAction(event -> {
            searchTF.setText("");
            showInfo.setSelected(true);
            showWarn.setSelected(true);
            showError.setSelected(true);
            showDebug.setSelected(true);
        });

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
                            setGraphic(WorkspaceFXUtils.successIcon());
                        } else if (level.intLevel() == StandardLevel.WARN.intLevel()) {
                            setGraphic(WorkspaceFXUtils.warnIcon());
                        } else if (level.intLevel() == StandardLevel.ERROR.intLevel()) {
                            setGraphic(WorkspaceFXUtils.errorIcon());
                        } else if (level.intLevel() == StandardLevel.DEBUG.intLevel()) {
                            setGraphic(WorkspaceFXUtils.debugIcon());
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        colTS.setCellValueFactory(new PropertyValueFactory<>("ts"));
        colTS.setCellFactory(new Callback<>() {
            @Override
            public TableCell<WSPLog4j2LogMessage, ZonedDateTime> call(TableColumn<WSPLog4j2LogMessage, ZonedDateTime> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(ZonedDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(null);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(dtf.format(item));
                        }
                    }
                };
            }
        });
        colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        colException.setCellValueFactory(new PropertyValueFactory<>("throwable"));

        toggleDebugMode.selectedProperty().addListener((obs, ov, nv) -> {
            if (nv != null) {
                if (nv) {
                    Configurator.setRootLevel(Level.DEBUG);
                } else {
                    Configurator.setRootLevel(Level.INFO);
                }
            }
        });

        messages.addListener((ListChangeListener<WSPLog4j2LogMessage>) change -> {
            change.next();
            int i = 0;
            int w = 0;
            int e = 0;
            int d = 0;
            if (change.wasAdded()) {
                for (WSPLog4j2LogMessage message : change.getAddedSubList()) {
                    int level = message.getLevel().intLevel();
                    if (level == StandardLevel.INFO.intLevel()) {
                        i++;
                    }
                    if (level == StandardLevel.WARN.intLevel()) {
                        w++;
                    }
                    if (level == StandardLevel.ERROR.intLevel()) {
                        e++;
                    }
                    if (level == StandardLevel.DEBUG.intLevel()) {
                        d++;
                    }
                }

                if (scrollToEnd.isSelected() &&
                        !table.getItems().isEmpty() &&
                        table.getItems().contains(table.getItems().getLast())) {
                    table.scrollTo(table.getItems().indexOf(table.getItems().getLast()));
                }
            }
            if (change.wasRemoved()) {
                for (WSPLog4j2LogMessage message : change.getRemoved()) {
                    int level = message.getLevel().intLevel();
                    if (level == StandardLevel.INFO.intLevel()) {
                        i--;
                    }
                    if (level == StandardLevel.WARN.intLevel()) {
                        w--;
                    }
                    if (level == StandardLevel.ERROR.intLevel()) {
                        e--;
                    }
                    if (level == StandardLevel.DEBUG.intLevel()) {
                        d--;
                    }
                }
            }

            setInfoCounter(getInfoCounter() + i);
            setWarnCounter(getWarnCounter() + w);
            setErrCounter(getErrCounter() + e);
            setDebugCounter(getDebugCounter() + d);
        });

        infoCounterProperty().addListener((obs, ov, nv) -> updateCounter(nv.intValue(), totalInfo));
        warnCounterProperty().addListener((obs, ov, nv) -> updateCounter(nv.intValue(), totalWarn));
        errCounterProperty().addListener((obs, ov, nv) -> updateCounter(nv.intValue(), totalError));
        debugCounterProperty().addListener((obs, ov, nv) -> updateCounter(nv.intValue(), totalDebug));

    }

    private void updateCounter(int value, Label label) {
        label.setText(String.format("%,d", value));
        setShortDescription("Logging\n" +
                "Info   : " + infoCounter.getValue() + "\n" +
                "Warning: " + warnCounter.getValue() + "\n" +
                "Error  : " + errCounter.getValue() + "\n" +
                "Debug  : " + debugCounter.getValue());
    }

    private boolean filterMessages(WSPLog4j2LogMessage message) {
        boolean show;
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
        FontIcon iconRight = new FontIcon(MaterialDesignC.CLOSE_THICK);
        FontIcon iconLeft = new FontIcon(MaterialDesignC.CARD_SEARCH_OUTLINE);
        iconRight.setOnMouseClicked(event -> searchTF.setText(""));
        searchTF.setRight(iconRight);
        searchTF.setLeft(iconLeft);
        showInfo.selectedProperty().addListener((obs, ov, nv) -> filteredList.setPredicate(this::filterMessages));
        showWarn.selectedProperty().addListener((obs, ov, nv) -> filteredList.setPredicate(this::filterMessages));
        showError.selectedProperty().addListener((obs, ov, nv) -> filteredList.setPredicate(this::filterMessages));
        showDebug.selectedProperty().addListener((obs, ov, nv) -> filteredList.setPredicate(this::filterMessages));
    }
}
