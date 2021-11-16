package scenes.init;

import dialog.ErrorDialog;
import dialog.InformationDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import scenes.init.actions.Execute;
import scenes.init.actions.SystemData;
import scenes.init.actions.UIAdapterImpl;
import scenes.init.handles.HandleChangeableFields;
import scenes.init.handles.HandleConditions;
import scenes.init.handles.table.HandleTimeTable;
import time.table.problem.configurations.Crossover;
import time.table.problem.configurations.Selection;
import time.table.problem.configurations.mutation.Mutation;
import time.table.problem.configurations.rules.Rule;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class executeController {

    @FXML private Label totalPopulation;
    @FXML private Label highestFitness;
    @FXML private Button ActionExecute_Button;
    @FXML private Button clearButton;
    @FXML private TableView<?> ViewTimeTable;
    @FXML private ProgressBar GenerationProgressBar;
    @FXML private ProgressBar FitnessProgressBar;
    @FXML private ProgressBar TimeProgressBar;

    public enum ActionButton { Start, Pause, Resume }
    private Execute m_executeSystem;
    private Scene m_executeScene;
    private HandleConditions m_Conditions;
    private HandleChangeableFields m_ChangeableField;
    private HandleTimeTable m_TimeTable;
    UIAdapterImpl uiAdapter;
    private Runnable ShowMutationChange;
    private boolean alreadyInit;

    //to delete after done debug:
    private Crossover mcrossover;
    private Selection mselection;
    private List<Mutation> mmutation;

    public void initialize(SystemData i_systemData, ChangeMutationController i_mController, Consumer<Map<Rule,Integer>> ruleConsumer)
    {
        m_Conditions = new HandleConditions(ProcessTitleCondition_Label, timeCondition_cb, fitnessCondition_cb, generationCondition_cb,
                generationCondition_textbox, fitnessCondition_testbox, timeCondition_textbox, ConditionData_Label);

        uiAdapter = createAdapter(ruleConsumer);
        m_executeSystem = new Execute(i_systemData, uiAdapter);
        m_TimeTable = new HandleTimeTable(ViewTimeTable,i_systemData.getTimeTable().getHours(),i_systemData.getTimeTable().getDays(),
        ShowAs_ComboBox, FilterBySpecificName_ComboBox, i_systemData.getTimeTable().getTeachers(), i_systemData.getTimeTable().getStudyClasses());

        Selection selection =  i_systemData.getEngineInfo().getSelection();
        Crossover crossover =  i_systemData.getEngineInfo().getCrossover();
        List<Mutation> mutation = i_systemData.getEngineInfo().getMutations();

        m_ChangeableField = new HandleChangeableFields(changeableFieldTitle_Label, mutationSetting_Button, Elitism_cb,
                Elitism_textbox, Selection_Label, Crossover_Label, Selection_comboBox, Crossover_comboBox,selection, crossover, mutation,
                ShowMutationChange, i_mController);

        //to delete:
        mmutation = mutation;
        mselection = selection;
        mcrossover = crossover;
        //
        alreadyInit = true;
    }

    public executeController()
    {
        alreadyInit = false;
    }


    public void setScene(Scene i_scene)
    {
        m_executeScene = i_scene;
    }

    public Scene getScene() { return m_executeScene;  }

    @FXML
    void ActionExecuteButton_onAction(ActionEvent event)
    {
        if(ActionExecute_Button.textProperty().getValue().equals(ActionButton.Start.toString()))
        {
            if (m_Conditions.isValidInput() == true)
            {StartAction();}
            else
            {
                new ErrorDialog("Must enter stop condition!");

            }
        }

        else if(ActionExecute_Button.textProperty().getValue() == ActionButton.Pause.toString())
        {
            PauseAction();
            clearButton.setDisable(false);
        }

        else if(ActionExecute_Button.textProperty().getValue() == ActionButton.Resume.toString())
        {
            if (m_Conditions.isValidInput() == true)  ResumeAction();
            clearButton.setDisable(true);
        }
    }

    private void StartAction()
    {
        if(m_Conditions.isValidInput() == true)
        {
            ActionExecute_Button.textProperty().setValue(ActionButton.Pause.toString());
            m_Conditions.setAllVisibility(false);
            m_Conditions.setConditionDataVisible();
            m_ChangeableField.setAllVisibility(true);
            m_ChangeableField.disableAll();

            m_executeSystem.Start(m_Conditions.getGenerationCondition(),m_Conditions.getFitnessCondition(),m_Conditions.getTimeCondition());
        }
    }

    @FXML
    void clearTaskButton_onAction(ActionEvent event)
    {
        if(m_Conditions != null)
        {
            m_Conditions.setAllVisibility(true);
            m_Conditions.clear();
        }
        if(m_ChangeableField != null) m_ChangeableField.setAllVisibility(false);
        if(m_executeSystem != null) m_executeSystem.clear();
        if(m_TimeTable != null) m_TimeTable.ClearTable();
        clearProgressBar();
        clearFitnessAndGeneration();
        ActionExecute_Button.textProperty().setValue(ActionButton.Start.toString());
        clearButton.setDisable(true);
    }

    private void ResumeAction()
    {
        try
        {
            m_executeSystem.Resume(m_ChangeableField.getElitism());
            ActionExecute_Button.textProperty().setValue(ActionButton.Pause.toString());
            clearButton.setDisable(true);
            m_ChangeableField.disableAll();
        }
        catch(NumberFormatException ex)
        {
            new ErrorDialog("please enter valid value of number");
        }
        catch(IllegalArgumentException ex)
        {
            new ErrorDialog(ex.getMessage());
        }
    }

    private void PauseAction()
    {

        ActionExecute_Button.textProperty().setValue(ActionButton.Resume.toString());
        clearButton.setDisable(false);
        m_ChangeableField.enableAll();
        m_executeSystem.Pause();
    }

    private UIAdapterImpl createAdapter(Consumer<Map<Rule, Integer>> ruleConsumer)
    {
        return new UIAdapterImpl(
                (table)->m_TimeTable.setTimeTable(table),
                (g)->GenerationProgressBar.setProgress(g/(float)m_Conditions.getGenerationCondition()),
                (f)->
                {
                    int fitness = m_Conditions.getFitnessCondition();
                    if(fitness != Integer.MAX_VALUE) FitnessProgressBar.setProgress(f/(float)m_Conditions.getFitnessCondition());
                },
                (t)->
                {
                    float time = m_Conditions.getTimeCondition();
                    if(time != Integer.MAX_VALUE) TimeProgressBar.setProgress(t/m_Conditions.getTimeCondition());
                },
                (info)->totalPopulation.textProperty().setValue(info.toString()), (info)-> highestFitness.textProperty().setValue(info.toString()),
                ruleConsumer,
                ()-> new InformationDialog("Execute Done")

        );
    }

    public void setMutationStage(Runnable action)
    {
        ShowMutationChange = action;
    }

    public void clearFitnessAndGeneration()
    {
        totalPopulation.textProperty().setValue("0");
        highestFitness.textProperty().setValue("0");
    }

    private void clearProgressBar()
    {
        GenerationProgressBar.setProgress(0);
        FitnessProgressBar.setProgress(0);
        TimeProgressBar.setProgress(0);
    }


    public boolean alreadyInit()
    {
        return alreadyInit;
    }


    public void reset()
    {
        clearTaskButton_onAction(null);
        alreadyInit = false;
        m_executeSystem = null;
        m_TimeTable = null;
    }

    //Execute part1 controls
    @FXML private Label ProcessTitleCondition_Label;
    @FXML private CheckBox timeCondition_cb;
    @FXML private CheckBox fitnessCondition_cb;
    @FXML private CheckBox generationCondition_cb;
    @FXML private TextField generationCondition_textbox;
    @FXML private TextField fitnessCondition_testbox;
    @FXML private TextField timeCondition_textbox;
    @FXML private Label ConditionData_Label;

    //Execute part2 controls
    @FXML private Label changeableFieldTitle_Label;
    @FXML private Button mutationSetting_Button;
    @FXML private CheckBox Elitism_cb;
    @FXML private TextField Elitism_textbox;
    @FXML private Label Selection_Label;
    @FXML private Label Crossover_Label;
    @FXML private ComboBox<?> Selection_comboBox;
    @FXML private ComboBox<?> Crossover_comboBox;


    @FXML private ComboBox<?> ShowAs_ComboBox;
    @FXML private ComboBox<?> FilterBySpecificName_ComboBox;

}
