package scenes.init.actions;

import evolution.engine.EvolutionEngine;
import javafx.beans.property.SimpleIntegerProperty;
import time.table.problem.Quintet;
import time.table.problem.configurations.mutation.Mutation;

import java.util.List;

public class Execute {
    List<Mutation> m_mutation;
    int m_PopSize;
    private boolean m_alreadyExecuted = false;
    private boolean m_isRunning = false;
    //EvolutionEngine Execute
    private Thread m_trEngine = null;
    private EvolutionEngine<Quintet> m_EvolutionEngine = null;
    //Condition Properties:
    private SimpleIntegerProperty m_ExitGeneration = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty m_ExitFitness = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty m_ExitTime = new SimpleIntegerProperty(0);
    //Changeable Fields Properties: //todo:change this method:s
    private int m_Elitism;
    //UpdateEngineInformation
    private UIAdapterImpl uiAdapter;


    public Execute(SystemData i_data, UIAdapterImpl uiAdapter) {
        this.uiAdapter = uiAdapter;
        m_EvolutionEngine = new EvolutionEngine<>(i_data.getFactory(), i_data.getEngineInfo(), i_data.getTimeTable().getGeneLength()
                , i_data.getDTOEngine(), this.uiAdapter, m_ExitGeneration.getValue(), m_ExitFitness.getValue(), m_ExitTime.getValue(), 1, 1);

        m_Elitism = -1;
        m_PopSize = i_data.getEngineInfo().getInitialPopulation();
    }

    public boolean isRunning() {
        if (m_trEngine == null) return false;
        else return m_trEngine.isAlive();
    }

    public boolean isAlreadyExecuted() {
        return m_alreadyExecuted;
    }

    public void Start(int generationCondition, int fitnessCondition, float timeCondition) {
        m_ExitGeneration.setValue(generationCondition);
        m_ExitFitness.setValue(fitnessCondition);
        m_ExitTime.setValue(timeCondition);

        m_EvolutionEngine.setConditions(generationCondition, fitnessCondition, timeCondition);
        m_trEngine = new Thread(m_EvolutionEngine);
        m_trEngine.start();
        m_alreadyExecuted = true;
    }

    public void Resume(int i_Elitism/*float i_PTE*/) {

        if (i_Elitism != -1) {
            if (i_Elitism < 0 || i_Elitism >= m_PopSize) {
                throw new IllegalArgumentException("Elitism count must be non-negative and less than population size which is "+m_PopSize );
            } else {
                m_EvolutionEngine.setElitismCount(i_Elitism);
            }
        }
        /*if (i_PTE != -1) {
            if (i_PTE > 1 || i_PTE < 0) {
                throw new IllegalArgumentException("Predefined tournament equalizer should be between 0 to 1");
            } else {
               m_EvolutionEngine.setPredefinedTournamentEqualizer(i_PTE);
            }
        }*/
        m_EvolutionEngine.Resume();

    }

    public synchronized void Pause()
    {
        m_EvolutionEngine.Pause();
    }

    public void clear() {
        m_EvolutionEngine.reset();
    }
}
