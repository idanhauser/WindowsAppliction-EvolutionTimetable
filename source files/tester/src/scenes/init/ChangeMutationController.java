package scenes.init;

import dialog.ErrorDialog;
import dialog.InformationDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import time.table.problem.configurations.mutation.EComponent;
import time.table.problem.configurations.mutation.Mutation;
import time.table.problem.configurations.mutation.MutationEType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ChangeMutationController {
    float m_Probability;
    int m_Tupples;
    Mutation m_Mutation;
    @FXML
    private ComboBox<?> Mutation_ComboBox;
    @FXML
    private CheckBox Component_cb;
    @FXML
    private ComboBox<?> Component_ComboBox;
    @FXML
    private CheckBox Probability_cb;
    @FXML
    private TextField Probability_textbox;
    @FXML
    private Button Save_Button;
    @FXML
    private TextField Tupples_textbox;
    @FXML
    private CheckBox Tupples_cb;
    private List<Mutation> MutationList;


    public void initialize(List<Mutation> mutation) {
        MutationList = mutation;
        Probability_cb.setOnAction((e) -> Probability_textbox.setDisable(!Probability_cb.isSelected()));
        Tupples_cb.setOnAction((e) -> Tupples_textbox.setDisable(!Tupples_cb.isSelected()));
        Component_cb.setOnAction((e) -> Component_ComboBox.setDisable(!Component_cb.isSelected()));

        Probability_textbox.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d*(\\.\\d*)?"))
                Probability_textbox.setText(oldValue);
        });

        Tupples_textbox.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d*"))
                Tupples_textbox.setText(newValue.replaceAll("[^\\d]", ""));
        });

        setComboBoxes();
    }

    private void setComboBoxes() {
        ObservableList componentList = FXCollections.observableArrayList(EComponent.values());
        Component_ComboBox.getItems().setAll(componentList);

        ObservableList mutationList = FXCollections.observableArrayList(MutationEType.values());//todo:fix
        Mutation_ComboBox.getItems().setAll(mutationList);
    }

    @FXML
    void MutationComboBox_OnAction(ActionEvent event) {
        try {
            m_Mutation = getSelected();
            SetDisability(false);
            setVisibility();
            initFieldValues();
        } catch (Exception ex) {
        }
    }

    private Mutation getSelected() {
        String selected = Mutation_ComboBox.getSelectionModel().getSelectedItem().toString();
        AtomicReference<Mutation> res = new AtomicReference<>();
        MutationList.forEach(m ->
        {
            if (m.getEnumType().toString().equals(selected)) res.set(m);
        });

        return res.get();
    }

    private void initFieldValues() {
        try {
            Probability_textbox.textProperty().setValue(String.valueOf(m_Mutation.getProbability()));
            Tupples_textbox.textProperty().setValue(String.valueOf(m_Mutation.getTupples()));
            Component_ComboBox.getSelectionModel().select(EComponent.valueOf(m_Mutation.getComponent().toString()).ordinal());
        } catch (Exception ex) {
        }
    }

    private void SetDisability(boolean b) {
        Probability_cb.disableProperty().setValue(b);
        Tupples_cb.disableProperty().setValue(b);
        Component_cb.disableProperty().setValue(b);
        Save_Button.disableProperty().setValue(b);
    }


    private void setVisibility() {
        if (m_Mutation.getEnumType().toString() == "Sizer") {
            Component_cb.visibleProperty().setValue(false);
            Component_ComboBox.visibleProperty().setValue(false);
        } else {
            Component_cb.visibleProperty().setValue(true);
            Component_ComboBox.visibleProperty().setValue(true);
        }
    }


    @FXML
    void Save_OnAction(ActionEvent event) {
        boolean isError = false;
        try {
            if (Probability_cb.isSelected())
                m_Mutation.setProbability(getProbability());
            if (Tupples_cb.isSelected())
                m_Mutation.setTupples(getTupples());
            if (Component_cb.isSelected())
                m_Mutation.setComponent(getComponent());

        } catch (Exception ex) {
            new ErrorDialog("Changed didn't saved: "+ ex.getMessage());
            isError = true;
        }

        if (!isError) {
            new InformationDialog("Selection Saved!");
        }
    }

    public float getProbability() //todo fix in engine
    {
        if (Probability_cb.isSelected() == false)
            m_Probability = -1;
        else
            m_Probability = Float.parseFloat(Probability_textbox.textProperty().getValue());

        return m_Probability;
    }


    private int getTupples() {
        if (Tupples_cb.isSelected() == false)
            m_Tupples = -1;
        else
            m_Tupples = Integer.parseInt(Tupples_textbox.textProperty().getValue());

        return m_Tupples;
    }


    public EComponent getComponent() {
        AtomicReference<EComponent> res = new AtomicReference<>();
        String selected = Mutation_ComboBox.getSelectionModel().getSelectedItem().toString();
        Arrays.stream(EComponent.values()).forEach(comp ->
        {
            if (comp.name().equals(selected)) res.set(comp);

        });
        return res.get();
    }

    public void onExit() {
        Mutation_ComboBox.selectionModelProperty().getValue().clearSelection();
        SetDisability(true);
        Probability_textbox.textProperty().setValue("");
        Probability_textbox.disableProperty().setValue(true);
        Tupples_textbox.textProperty().setValue("");
        Tupples_textbox.disableProperty().setValue(true);
        Component_ComboBox.getSelectionModel().clearSelection();
        Component_ComboBox.disableProperty().setValue(true);
        Probability_cb.setSelected(false);
        Tupples_cb.setSelected(false);
        Component_cb.setSelected(false);
    }
}
