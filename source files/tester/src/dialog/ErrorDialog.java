package dialog;

import javafx.scene.control.Alert;

public class ErrorDialog
{
    String i_errorMessage;

    public ErrorDialog(String ex)
    {
        i_errorMessage = ex;
        showErrorDialog();
    }

    private void showErrorDialog()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Error: " + i_errorMessage);
        alert.showAndWait();
    }
}
