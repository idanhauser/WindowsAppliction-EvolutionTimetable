package scenes.init;

import dialog.ErrorDialog;
import dialog.InformationDialog;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import scenes.init.actions.LoadedFile;
import scenes.init.actions.SystemData;
import time.table.problem.configurations.rules.Rule;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class mainController {

    private boolean m_isAnimOn;
    private Scene m_mainScene;
    private StringProperty m_title = new SimpleStringProperty();
    private LoadedFile m_xmlFile = null;
    private SystemData m_systemData = null;

    private Timeline m_rotationAnimation;
    private Runnable executeReset;
    @FXML
    private Accordion Rule_Accordion;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private CheckMenuItem lightMode_checkMenuItem;
    @FXML
    private CheckMenuItem darkMode_checkMenuItem;
    @FXML
    private CheckMenuItem greenMode_checkMenuItem;
    @FXML
    private Button showBestResButton;
    @FXML
    private Button showProcessButton;
    @FXML
    private Button executeButton;
    @FXML
    private Button showSettingButton;
    @FXML
    private CheckBox Animation_cb;
    @FXML
    private ImageView Calendar_Icon;

    @FXML
    void Animation_OnAction(ActionEvent event) {
        AnimationPlay(Animation_cb.isSelected());
        ShowDataController.StopAnimation(Animation_cb.isSelected());

    }

    private void AnimationPlay(boolean selected) {
        if (selected) {
            Node square;
            try { // grab a smurf from the path if it is there otherwise just use a green square.
                square = Calendar_Icon;

            } catch (Exception e) {
                square = new Rectangle(100, 100, Color.FORESTGREEN);
            }
            square.setTranslateZ(150);
            square.setOpacity(0.7);
            square.setMouseTransparent(true);

            // create a rotation transform starting at 0 degrees, rotating about pivot point 50, 50.
            final Rotate rotationTransform = new Rotate(10, 50, 50);
            square.getTransforms().add(rotationTransform);

            // rotate a square using timeline attached to the rotation transform's angle property.
            m_rotationAnimation = new Timeline();
            m_rotationAnimation.getKeyFrames()
                    .add(
                            new KeyFrame(
                                    Duration.seconds(5),
                                    new KeyValue(
                                            rotationTransform.angleProperty(),
                                            360
                                    )
                            )
                    );

            m_rotationAnimation.setCycleCount(Animation.INDEFINITE);
            m_rotationAnimation.play();
        } else {
            m_rotationAnimation.pause();
        }
    }


    public void setScene(Scene i_Scene) {
        m_mainScene = i_Scene;

    }


    @FXML
    public void Open_onAction(ActionEvent actionEvent) {
        File file = getFile("TXT files (*.txt)", "*.txt");
        if (file == null) new ErrorDialog("invalid file");
        m_systemData.LoadFileToSystem(file.getPath());
    }

    @FXML
    public void Save_onAction(ActionEvent actionEvent) {
        File file = getFile("TXT files (*.txt)", "*.txt");
        if (file == null) new ErrorDialog("invalid file");
        m_systemData.SaveSystemToFile(file.getPath());
    }

    @FXML
    public void LoadXml_onAction(ActionEvent actionEvent) {
        File file = getFile("XML files (*.xml)", "*.Xml");
        if (file == null) new ErrorDialog("invalid file");

        try {
           if (m_systemData != null) resetApp();
           if (m_systemData != null) resetApp();
            m_systemData = m_xmlFile.setFile(file);
            new InformationDialog("xml Loaded successfully");
            activeButtonsAfterLoad();
            setRules();

        }
        catch (Exception ex) {
            new ErrorDialog(ex.getMessage());
        }
    }

    private void resetApp() {
        executeReset.run();
        mainBorderPane.setCenter(new ScrollPane());
        Rule_Accordion.getPanes().clear();
    }

    File getFile(String Description, String Extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(Description, Extension);
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(new Stage());
    }


    @FXML
    void ShowBestRes_onAction(ActionEvent event) {

    }

    @FXML
    void ShowProcess_onAction(ActionEvent event) {

    }


    @FXML
    void GreenMode_onAction(ActionEvent event) {
        m_mainScene.getStylesheets().add(getClass().getResource("resource/css/green-theme.css").toExternalForm());
        unMarkAllThemeField();
        greenMode_checkMenuItem.setSelected(true);
    }

    @FXML
    void DarkMode_onAction(ActionEvent event) {
        m_mainScene.getStylesheets().add(getClass().getResource("resource/css/dark-theme.css").toExternalForm());
        unMarkAllThemeField();
        darkMode_checkMenuItem.setSelected(true);
    }

    @FXML
    void LightMode_onAction(ActionEvent event) {
        m_mainScene.getStylesheets().clear();
        unMarkAllThemeField();
        lightMode_checkMenuItem.setSelected(true);
    }

    @FXML
    public void Quit_onAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    private void activeButtonsAfterLoad() {
        executeButton.setDisable(false);
        showSettingButton.setDisable(false);
    }

    private void unMarkAllThemeField() {
        darkMode_checkMenuItem.setSelected(false);
        lightMode_checkMenuItem.setSelected(false);
        greenMode_checkMenuItem.setSelected(false);
    }

    public void bindTitle(StringProperty i_title) {
        m_xmlFile = new LoadedFile(i_title);
    }

    public void setExecuteButtonAction(executeController i_eController, ChangeMutationController i_mController) {
        executeReset = i_eController::reset;
        executeButton.setOnAction((e) ->
        {
            if (i_eController.alreadyInit() == false)
                i_eController.initialize(m_systemData, i_mController, (g) -> updateRulesData(g));
            mainBorderPane.setCenter(i_eController.getScene().getRoot());
        });
    }

    public void setShowSettingButtonAction(ShowDataController i_ShowDataController) {
        showSettingButton.setOnAction((e) ->
        {
            mainBorderPane.setCenter(i_ShowDataController.getScene().getRoot());
            i_ShowDataController.initialize(m_systemData);
        });
    }

    public void setRules() {
        m_systemData.getTimeTable().getRules().entrySet().forEach(t ->
        {
            TitledPane pane = new TitledPane(t.getKey().toString(), new Label(""));
            Rule_Accordion.getPanes().add(pane);
        });
    }

    public void updateRulesData(Map<Rule, Integer> Grades) {
        Rule_Accordion.getPanes().forEach(r ->
        {
            //int item = Grades.get(r.textProperty().toString());
            Map.Entry<Rule, Integer> rule = findRule(r.textProperty(), Grades);
            String content = FormatRule(rule);
            ((Label) r.getContent()).textProperty().setValue(content);
        });
    }

    private Map.Entry<Rule, Integer> findRule(StringProperty r, Map<Rule, Integer> grades) {
        AtomicReference<Map.Entry> res = new AtomicReference<>();
        grades.entrySet().forEach(g ->
        {
            if (g.getKey().GetRuleId().toString().equals(r.getValue()))
                res.set(g);
        });
        return res.get();
    }

    private String FormatRule(Map.Entry<Rule, Integer> rule) {
        StringBuilder res = new StringBuilder();
        res.append("Type: ");
        res.append(rule.getKey().getType().toString());
        res.append(System.lineSeparator());
        res.append("Grade: ");
        res.append(rule.getValue());
        return res.toString();
    }
}

