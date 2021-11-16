import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import scenes.init.ChangeMutationController;
import scenes.init.ShowDataController;
import scenes.init.executeController;
import scenes.init.mainController;

public class myApp extends Application
{
    private static final String MAIN_SCENE_FXML_PATH = "scenes/init/mainScene.fxml";
    private static final String EXECUTE_SCENE_FXML_PATH = "scenes/init/executeScene.fxml";
    private static final String SHOW_DATA_FXML_PATH = "scenes/init/ShowDataScene.fxml";
    private static final String CHANGE_MUTATION_FXML_PATH = "scenes/init/ChangeMutation.fxml";

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        LoadFXML main_fxml = new LoadFXML(MAIN_SCENE_FXML_PATH);
        Parent mainRoot = main_fxml.getMainRoot();
        Scene scene = new Scene(mainRoot);

        mainController mainController = (mainController)main_fxml.getController();
        mainController.setScene(scene);
        mainController.bindTitle(primaryStage.titleProperty());

        LoadFXML exe_fxml = new LoadFXML(EXECUTE_SCENE_FXML_PATH);
        Parent exeRoot = exe_fxml.getMainRoot();
        executeController exeController = (executeController)exe_fxml.getController();
        exeController.setScene(new Scene(exeRoot));

        LoadFXML showData_fxml = new LoadFXML(SHOW_DATA_FXML_PATH);
        Parent showDataRoot = showData_fxml.getMainRoot();
        ShowDataController showDataController = (ShowDataController)showData_fxml.getController();
        showDataController.setScene(new Scene(showDataRoot));
        mainController.setShowSettingButtonAction(showDataController);

        LoadFXML mutation_fxml = new LoadFXML(CHANGE_MUTATION_FXML_PATH);
        Parent mutationRoot = mutation_fxml.getMainRoot();
        Scene s = new Scene(mutationRoot);
        Stage stage = new Stage();
        stage.setScene(s);
        exeController.setMutationStage(()->stage.show());
        ChangeMutationController mController = (ChangeMutationController) mutation_fxml.getController();
        stage.setOnHiding( event -> mController.onExit());

        mainController.setExecuteButtonAction(exeController,mController);
        primaryStage.getIcons().add(new Image("scenes/init/resource/TTicon.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
