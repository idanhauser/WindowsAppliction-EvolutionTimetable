package dialog;

import javafx.scene.control.Alert;

public class InformationDialog
{
    String i_infoMessage;

    public InformationDialog(String ex)
    {
        i_infoMessage = ex;
        showInformationDialog();
    }

    private void showInformationDialog() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(i_infoMessage);
        alert.showAndWait();
    }
}
