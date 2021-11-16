import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class LoadFXML
{
    private String SCENE_FXML_PATH;
    private FXMLLoader fxml;

    public LoadFXML(String i_SceneFxmlPath)
    {
        SCENE_FXML_PATH = i_SceneFxmlPath;
        fxml = getFXMLLoader();
    }

    private FXMLLoader getFXMLLoader() {
        FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader();
        URL url = getClass().getResource(SCENE_FXML_PATH);
        fxmlLoader.setLocation(url);
        return fxmlLoader;
    }

    public Parent getMainRoot() throws IOException
    {
        return fxml.load(fxml.getLocation().openStream());
    }

    public Object getController()
    {
        return fxml.getController();
    }

}
