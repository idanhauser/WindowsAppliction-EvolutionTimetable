package scenes.init.handles;

import dialog.ErrorDialog;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HandleConditions {
    private static final int BEST_FITNESS = 100;
    private CheckBox timeCondition_cb;
    private CheckBox fitnessCondition_cb;
    private CheckBox generationCondition_cb;
    private TextField generationCondition_textbox;
    private TextField fitnessCondition_testbox;
    private TextField timeCondition_textbox;
    Label processTitleCondition_Label;
    Label conditionData_Label;

    private float m_TimeCondition;
    private int m_GenerationCondition;
    private int m_FitnessCondition;

    public HandleConditions(Label processTitleCondition_Label, CheckBox timeCondition_cb, CheckBox fitnessCondition_cb, CheckBox generationCondition_cb,
                            TextField generationCondition_textbox, TextField fitnessCondition_testbox, TextField timeCondition_textbox, Label conditionData_Label) {
        this.timeCondition_cb = timeCondition_cb;
        this.fitnessCondition_cb = fitnessCondition_cb;
        this.generationCondition_cb = generationCondition_cb;
        this.generationCondition_textbox = generationCondition_textbox;
        this.fitnessCondition_testbox = fitnessCondition_testbox;
        this.timeCondition_textbox = timeCondition_textbox;
        this.processTitleCondition_Label = processTitleCondition_Label;
        this.conditionData_Label = conditionData_Label;

        timeCondition_cb.setOnAction((e) -> { timeClicked(); });
        fitnessCondition_cb.setOnAction((e) -> { fitnessClicked(); });
        generationCondition_cb.setOnAction((e) -> { generationClicked(); });

        generationCondition_textbox.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d*")) {
                generationCondition_textbox.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        fitnessCondition_testbox.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d*")) {
                fitnessCondition_testbox.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        timeCondition_textbox.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d*(\\.\\d*)?"))
                timeCondition_textbox.setText(oldValue);
        });

    }


    public void generationClicked()
    {
        generationCondition_textbox.setDisable(!generationCondition_cb.isSelected());
    }

    public void fitnessClicked()
    {
        fitnessCondition_testbox.setDisable(!fitnessCondition_cb.isSelected());
    }

    public void timeClicked()
    {
        timeCondition_textbox.setDisable(!timeCondition_cb.isSelected());
    }

    public boolean isAtLeastOneCheckBoxChecked()
    {
        return fitnessCondition_cb.isSelected() || generationCondition_cb.isSelected() || timeCondition_cb.isSelected();
    }

    public int getGenerationCondition()  throws NumberFormatException
    {
        if(generationCondition_cb.isSelected() == false) m_GenerationCondition = Integer.MAX_VALUE;

        else
        {
            String generation = generationCondition_textbox.textProperty().getValue();
            m_GenerationCondition = Integer.parseInt(generation);
        }
        return m_GenerationCondition;
    }

    public int getFitnessCondition()  throws NumberFormatException
    {
        if(fitnessCondition_cb.isSelected() == false)   m_FitnessCondition = BEST_FITNESS;

        else
        {
            String fitness = fitnessCondition_testbox.textProperty().getValue();
            m_FitnessCondition = Integer.parseInt(fitness);
        }
        return m_FitnessCondition;
    }

    public float getTimeCondition() throws NumberFormatException
    {
        if(timeCondition_cb.isSelected() == false) m_TimeCondition = Integer.MAX_VALUE;

        else
        {
            String time = timeCondition_textbox.textProperty().getValue();
            m_TimeCondition =Float.parseFloat(time);
        }

        return m_TimeCondition;
    }

    public void clear()
    {
        generationCondition_textbox.textProperty().setValue("");
        fitnessCondition_testbox.textProperty().setValue("");
        timeCondition_textbox.textProperty().setValue("");
        timeCondition_cb.setSelected(false);
        generationCondition_cb.setSelected(false);
        fitnessCondition_cb.setSelected(false);
        generationCondition_textbox.setDisable(true);
        fitnessCondition_testbox.setDisable(true);
        timeCondition_textbox.setDisable(true);
    }

    public boolean isValidInput()
    {
        boolean result = true;

        try {
            if (getGenerationCondition() < 100)
            {
                new ErrorDialog("Generation must be at least 100");
                result = false;
            }
            if(getFitnessCondition() > 100)
            {
                new ErrorDialog("fitness cannot be greater then 100");
                result = false;
            }

            getTimeCondition();
        } catch(NumberFormatException ex)
        {
            new ErrorDialog("selected field must contain numbers");
            result = false;
        }

        return isAtLeastOneCheckBoxChecked() && result;
    }

    public void setConditionDataVisible()
    {
        StringBuilder res = new StringBuilder();

        if(generationCondition_cb.isSelected() == true)
            res.append("Generation: " + m_GenerationCondition + " ");

        if(fitnessCondition_cb.isSelected() == true)
            res.append("Fitness: " + m_FitnessCondition + " ");

        if(timeCondition_cb.isSelected() == true)
            res.append("Time: " + m_TimeCondition);

        conditionData_Label.visibleProperty().setValue(true);
        conditionData_Label.textProperty().setValue(res.toString());
    }

    public void setAllVisibility(boolean status)
    {
        processTitleCondition_Label.visibleProperty().setValue(status);
        generationCondition_textbox.visibleProperty().setValue(status);
        generationCondition_cb.visibleProperty().setValue(status);
        fitnessCondition_testbox.visibleProperty().setValue(status);
        fitnessCondition_cb.visibleProperty().setValue(status);
        timeCondition_textbox.visibleProperty().setValue(status);
        timeCondition_cb.visibleProperty().setValue(status);
        conditionData_Label.visibleProperty().setValue(status);
        if (status = true)
            conditionData_Label.textProperty().setValue("");
    }

    public void setConditionDataUnVisible()
    {
        conditionData_Label.textProperty().setValue("");
        conditionData_Label.visibleProperty().setValue(false);
    }
}
