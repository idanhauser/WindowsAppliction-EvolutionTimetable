package scenes.init.handles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import scenes.init.ChangeMutationController;
import time.table.problem.configurations.Crossover;
import time.table.problem.configurations.Selection;
import time.table.problem.configurations.mutation.Mutation;

import java.util.List;

public class HandleChangeableFields
{
    //FXML DATA
    private Label changeableFieldTitle_Label;
    private Button MutationSetting_Button;
    private CheckBox Elitism_cb;
    private TextField Elitism_textbox;
    private Label Selection_Label;
    private Label Crossover_Label;
    private ComboBox<?> Selection_comboBox;
    private ComboBox<?> Crossover_comboBox;
    // END FXML DATA

    private int m_Elitism;

    //MutationChange needed field:
    Runnable showMutationChange;
    ChangeMutationController MutationController;

    public HandleChangeableFields(Label changeableFieldTitle_label, Button mutationSetting_Button, CheckBox elitism_cb,
                                  TextField elitism_textbox, Label selection_label, Label crossover_label, ComboBox<?> selection_comboBox, ComboBox<?> crossover_comboBox,
                                  Selection selection, Crossover crossover, List<Mutation> mutation, Runnable showMutationChange, ChangeMutationController MutationController)
    {
        this.changeableFieldTitle_Label = changeableFieldTitle_label;
        this.MutationSetting_Button = mutationSetting_Button;
        this.Elitism_cb = elitism_cb;
        this.Elitism_textbox = elitism_textbox;
        this.Selection_Label = selection_label;
        this.Crossover_Label = crossover_label;
        this.Selection_comboBox = selection_comboBox;
        this.Crossover_comboBox = crossover_comboBox;
        this.showMutationChange = showMutationChange;
        this.MutationController = MutationController;
        setComboBoxes(selection,crossover);
        elitism_cb.setOnAction((e) -> elitismClicked());
        MutationController.initialize(mutation);

        elitism_textbox.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d*"))
                elitism_textbox.setText(newValue.replaceAll("[^\\d]", ""));
        });

        mutationSetting_Button.onActionProperty().setValue(event -> showMutationChange.run());

        elitism_textbox.textProperty().setValue(String.valueOf(selection.getElitismCount()));
    }

    private void setComboBoxes(Selection selection, Crossover crossover)
    {
        ObservableList selectionList = FXCollections.observableArrayList(Selection.type.values());
        Selection_comboBox.getItems().setAll(selectionList);
        Selection_comboBox.getSelectionModel().select(Selection.type.valueOf(selection.getType().toString()).ordinal());

        ObservableList crossoverList = FXCollections.observableArrayList(Crossover.type.values());
        Crossover_comboBox.getItems().setAll(crossoverList);
        Crossover_comboBox.getSelectionModel().select(Crossover.type.valueOf(crossover.getEnumCrossoverType().toString()).ordinal());

        setComboBoxesEvent(selection,crossover);
    }


    private void setComboBoxesEvent(Selection selection, Crossover crossover)
    {
        Selection_comboBox.onActionProperty().setValue((e)->
        {
            Selection.type ety = Enum.valueOf(Selection.type.class, Selection_comboBox.getValue().toString());
            selection.setEnumSelectType(ety);

        });

        Crossover_comboBox.onActionProperty().setValue((e)->
        {
            Crossover.type ety = Enum.valueOf(Crossover.type.class, Crossover_comboBox.getValue().toString());
            crossover.setEnumCrossoverType(ety);
        });
    }



    private void elitismClicked()
    {
        Elitism_textbox.setDisable(!Elitism_cb.isSelected());
    }


    public void setAllVisibility(boolean status)
    {
        changeableFieldTitle_Label.visibleProperty().setValue(status);
        MutationSetting_Button.visibleProperty().setValue(status);
        Elitism_cb.visibleProperty().setValue(status);
        Elitism_textbox.visibleProperty().setValue(status);
        Selection_Label.visibleProperty().setValue(status);
        Crossover_Label.visibleProperty().setValue(status);
        Selection_comboBox.visibleProperty().setValue(status);
        Crossover_comboBox.visibleProperty().setValue(status);
    }

    public void enableAll()
    {
        MutationSetting_Button.setDisable(false);
        Elitism_cb.setDisable(false);
        Selection_comboBox.setDisable(false);
        Crossover_comboBox.setDisable(false);

        if(Elitism_cb.isSelected() == true) Elitism_textbox.setDisable(false);
    }

    public void disableAll()
    {
        MutationSetting_Button.setDisable(true);
        //Probability_cb.setDisable(true);
        Elitism_cb.setDisable(true);
        Selection_comboBox.setDisable(true);
        Crossover_comboBox.setDisable(true);
        //Probability_textbox.setDisable(true);
        Elitism_textbox.setDisable(true);
    }

    public float getProbability()
    {
        return MutationController.getProbability();
    }

    public int getElitism()
    {
        if(Elitism_cb.isSelected() == false)
            m_Elitism = -1;
        else
            m_Elitism = Integer.parseInt(Elitism_textbox.textProperty().getValue());

        return m_Elitism;
    }
}
