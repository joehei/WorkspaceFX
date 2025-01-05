package org.hometree.workspacefx.plugin.fxproperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Font;
import org.hometree.workspacefx.plugin.AWorkspaceFXPlugin;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WSPFXProperty extends AWorkspaceFXPlugin implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(WSPFXProperty.class);

    @FXML
    TextField tfVarName;
    @FXML
    ChoiceBox<String> choiceVariableType;
    @FXML
    ComboBox<String> comboObjectType;
    @FXML
    TextArea codeSnippet;
    @FXML
    Button copyToClipboard;
    @FXML
    TextArea codeSnippetLazy;
    @FXML
    Button copyToClipboardLazy;

    public WSPFXProperty() {
        FontIcon icon = new FontIcon(MaterialDesignS.SWAP_HORIZONTAL);
        icon.setIconSize(16);
        setToggleButtonContent(icon);
        setShortDescription("JavaFX Property generator");

        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(WSPFXProperty.class.getResource(WSPFXProperty.class.getSimpleName() + ".fxml")));
            loader.setController(this);
            setContent(loader.load());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private void updateTextField() {

        String varName = tfVarName.getText();
        String varNameUpper = "";
        if (!varName.isEmpty()) {
            varNameUpper = varName.substring(0, 1).toUpperCase() + varName.substring(1);
        }

        String varType = choiceVariableType.getSelectionModel().getSelectedItem();
        String varTypeSimple = varType.replace("Property", "");

        if (varType.contains("ObjectProperty")) {
            varType = "ObjectProperty<" + comboObjectType.getSelectionModel().getSelectedItem() + ">";
            varTypeSimple = comboObjectType.getSelectionModel().getSelectedItem();
        }

        String generalContent = """
                private final {{varType}} {{varName}} = new Simple{{varType}}();
                                
                public {{varTypeSimple}} get{{varNameUpper}}() {
                 return {{varName}}.getValue();
                }
                               
                public void set{{varNameUpper}}({{varTypeSimple}} {{varName}}) {
                 this.{{varName}}.setValue({{varName}});
                }
                               
                public {{varType}} {{varName}}Property(){
                 return this.{{varName}};
                }
                """;
        generalContent = generalContent.replace("{{varType}}", varType)
                .replace("{{varName}}", varName)
                .replace("{{varTypeSimple}}", varTypeSimple)
                .replace("{{varNameUpper}}", varNameUpper);

        String lazyContent = """
                private {{varType}} {{varName}};
                                
                public {{varTypeSimple}} get{{varNameUpper}}() {
                 if ({{varName}} == null) {
                  this.{{varName}} = new Simple{{varType}}(this, "{{varName}}");
                 }
                 return {{varName}}.getValue();
                }
                                
                public void set{{varNameUpper}}({{varTypeSimple}} {{varName}}) {
                 if ({{varName}} == null) {
                  this.{{varName}} = new Simple{{varType}}(this, "{{varName}}");
                 }
                 this.{{varName}}.setValue({{varName}});
                }
                               
                public {{varType}} {{varName}}Property() {
                 if ({{varName}} == null) {
                  this.{{varName}} = new Simple{{varType}}(this, "{{varName}}");
                 }
                 return this.{{varName}};
                }
                """;
        lazyContent = lazyContent.replace("{{varType}}", varType)
                .replace("{{varName}}", varName)
                .replace("{{varTypeSimple}}", varTypeSimple)
                .replace("{{varNameUpper}}", varNameUpper);

        codeSnippet.setText(generalContent);
        codeSnippetLazy.setText(lazyContent);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {


        tfVarName.textProperty().addListener((arg01, arg11, arg2) -> updateTextField());
        ObservableList<String> variableTypeList = FXCollections.observableArrayList("StringProperty",
                "BooleanProperty",
                "DoubleProperty",
                "IntegerProperty",
                "FloatProperty",
                "LongProperty",
                "ObjectProperty<>");

        choiceVariableType.setItems(variableTypeList);
        choiceVariableType.getSelectionModel().select(0);
        choiceVariableType.getSelectionModel().selectedItemProperty().addListener((arg012, arg112, arg2) -> {
            comboObjectType.setDisable(!arg2.equals("ObjectProperty<>"));
            updateTextField();
        });

        ObservableList<String> list = FXCollections.observableArrayList("Path");
        comboObjectType.setItems(list);
        comboObjectType.getSelectionModel().select(0);
        comboObjectType.setDisable(true);
        comboObjectType.setEditable(true);
        comboObjectType.valueProperty().addListener((obs, ov, nv) -> updateTextField());

        copyToClipboard.setOnAction(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(codeSnippet.getText());
            clipboard.setContent(content);
        });

        copyToClipboardLazy.setOnAction(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(codeSnippetLazy.getText());
            clipboard.setContent(content);
        });

        updateTextField();

        Font font = Font.font("MonoSpaced", 10);
        codeSnippet.setFont(font);
        codeSnippetLazy.setFont(font);

    }

}
