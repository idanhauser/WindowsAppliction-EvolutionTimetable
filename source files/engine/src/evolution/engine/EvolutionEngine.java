package evolution.engine;

import data.transfer.objects.DTOEngineInformation;
import data.transfer.objects.GeneticPoolInformation;
import data.transfer.objects.UIAdapter;
import evolution.engine.conditions.FitnessCondition;
import evolution.engine.conditions.GenerationCondition;
import evolution.engine.conditions.TimeCondition;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class EvolutionEngine<T> implements Serializable, Runnable {

    private static final long serialVersionUID = 100L;

    private final DTOEngineInformation<T> m_DTOEngine;
    private final EvolutionEngineInformation m_EngineInfo;
    Individual bestSolutionInAllGens;
    int m_ElitismCount;
    private Population<T> m_Population;
    private Map<Integer, Pair<Integer, Population>> m_OldPopulationList;
    private double m_Probability;
    private int m_Days; //todo delete?
    private int m_Hours;
    private Long m_timeLeft;
    private boolean isPause = false;

    private UIAdapter m_Adapter;
    private int m_ProcessesGenerationToShow;

    //Exit Conditions:
    private int m_GenerationCount;

    //Exit Conditions:
    private long m_MinutesToStop;

    private FitnessCondition m_fitnessCondition;
    private GenerationCondition m_generationCondition;
    private TimeCondition m_timeCondition;

    public EvolutionEngine(Factory<T> i_Factory, EvolutionEngineInformation i_EngineInfo, int i_geneListLength, DTOEngineInformation<T> i_DTOEngine, int i_Days, int i_Hours) throws InstantiationException, IllegalAccessException {
        m_Population = new Population<>(i_Factory, i_EngineInfo, i_geneListLength);
        m_OldPopulationList = new TreeMap<>();
        m_EngineInfo = i_EngineInfo;
        m_GenerationCount = 0;
        m_DTOEngine = i_DTOEngine;
        m_ElitismCount = m_EngineInfo.getSelection().getElitismCount();
    }

    public EvolutionEngine(Factory<T> i_Factory, EvolutionEngineInformation i_EngineInfo, int i_geneListLength, DTOEngineInformation<T> i_DTOEngine, UIAdapter i_Adapter,
                           int i_ExitGeneration, int i_ExitFitness, int i_ExitTime, int i_ProcessesGenerationToShow, long i_MinutesToStop) {
        m_Population = new Population<>(i_Factory, i_EngineInfo, i_geneListLength);
        m_OldPopulationList = new TreeMap<>();
        m_EngineInfo = i_EngineInfo;
        m_GenerationCount = 0;
        m_DTOEngine = i_DTOEngine;
        m_ProcessesGenerationToShow = i_ProcessesGenerationToShow;

        m_generationCondition = new GenerationCondition(i_ExitGeneration);
        m_fitnessCondition = new FitnessCondition(i_ExitFitness);
        m_timeCondition = new TimeCondition(i_ExitTime);
        m_ElitismCount = m_EngineInfo.getSelection().getElitismCount();
        if (m_EngineInfo.getMutations().get(0) != null) {
            m_Probability = m_EngineInfo.getMutations().get(0).getProbability();
        }
        m_Adapter = i_Adapter;
    }

    public Map<Integer, Pair<Integer, Population>> getOldPopulationList() {
        return m_OldPopulationList;
    }

    public void run() {

        m_timeCondition.StartTimer();

        try {
            m_Population.calculateFitness();
        } catch (Exception e) {
            System.out.println("Could not continue with the process: " + e.getMessage());
        }

        Individual bestSolutionInCurrentGen = m_Population.getFittestScore();
        bestSolutionInAllGens = m_Population.getFittestScore();
        m_DTOEngine.setBestResult(bestSolutionInAllGens.getListOfGenes());

        UpdateAdapter();
        int bestGen = 0; //todo delete?

        while (ToContinue()) {
            isPause();

            ++m_GenerationCount;
            if (m_GenerationCount % m_ProcessesGenerationToShow == 0) {
                m_OldPopulationList.put(m_GenerationCount, new Pair<>(m_GenerationCount, m_Population));
                m_DTOEngine.setOldPopulationList(m_OldPopulationList);
                //System.out.println("DEBUGGER: gen: " + m_GenerationCount + " fitness: " + m_Population.getBestFitnessScore());
            }
            try {
                //Selection:
                m_Population.copyAndSetIndividualList(m_EngineInfo.getSelection().doSelection(m_Population.getIndividuals()));
                //CrossOver:
                m_Population.setIndividuals(m_EngineInfo.getCrossover().runCrossover(m_Population.getIndividuals(),
                        m_EngineInfo.getInitialPopulation() - m_ElitismCount));
                //Mutation
                m_EngineInfo.getMutations().forEach((mutation) -> mutation.doMutation(m_Population.getIndividuals(), m_Days, m_Hours));
                //Adding back the elitism solutions.
                if (m_ElitismCount > 0) {
                    m_Population.getIndividuals().addAll(m_EngineInfo.getSelection().get_ElitismList());
                }
            } catch (Exception ex) {
                System.out.println("There was some problem in EvolutionEngine: " + ex.getMessage());
            }
            try {
                m_Population.calculateFitness();
            } catch (Exception e) {
                System.out.println("Could not continue with the process: " + e.getMessage());
            }
            bestSolutionInCurrentGen = m_Population.getFittestScore();

            if (bestSolutionInCurrentGen.getFitness() > bestSolutionInAllGens.getFitness()) {
                bestGen = m_GenerationCount + 1;
                bestSolutionInAllGens = bestSolutionInCurrentGen;
                m_DTOEngine.setBestResult(bestSolutionInAllGens.getListOfGenes());
                m_DTOEngine.setBestGen(bestGen);
                m_Adapter.UpdateBestTable(bestSolutionInAllGens.getListOfGenes());
                m_Adapter.UpdateRulesPane(bestSolutionInAllGens.getRulesGrade());
            }

            UpdateAdapter();
        }

        m_Adapter.onFinish();
    }

    private void isPause() {
        if (isPause) {
            synchronized (this) {
                try {
                    if (m_timeCondition.isTimeConditionIsOn()) {
                        m_timeCondition.pauseTimer();
                    }
                    this.wait();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void Pause() {
        isPause = true;
    }

    public void Resume() {
        synchronized (this) {
            isPause = false;
            if (m_timeCondition.isTimeConditionIsOn()) {
                m_timeCondition.resumeTimer();
            }
            notifyAll();
        }
    }

    private void UpdateAdapter() {
        m_Adapter.UpdatePopulationAndFitness(m_OldPopulationList.size(), bestSolutionInAllGens.getFitness());
        m_Adapter.UpdateProgressbar(m_GenerationCount, bestSolutionInAllGens.getFitness(), m_timeCondition.getDuration().getSeconds() / 60f);
    }

    private boolean ToContinue() {
        return !m_generationCondition.isEnded(m_GenerationCount) && !m_fitnessCondition.isEnded(bestSolutionInAllGens.getFitness()) && !m_timeCondition.isEnded();
    }

    public GeneticPoolInformation<T> requestGeneticsPool() throws Exception {
        GeneticPoolInformation<T> geneticPoolDto = new GeneticPoolInformation<>();
        if (m_OldPopulationList.size() != 0) {
            geneticPoolDto.setGensToShow(m_ProcessesGenerationToShow);
            geneticPoolDto.setHistoryOfGenerations(m_OldPopulationList);
        } else {
            throw new Exception("There is no data, You have to run the engine or load the data first.");
        }

        return geneticPoolDto;
    }

    public void reset() {
        m_OldPopulationList = new TreeMap<>();
        m_GenerationCount = 0;
        m_Population.reset();
        isPause = false;
    }

    public long getGenerationCount() {
        return m_GenerationCount;
    }

    public void setConditions(int i_Generation, int i_Fitness, float i_Time) {
        m_generationCondition.setExitGeneration(i_Generation);
        m_fitnessCondition.setExitFitness(i_Fitness);
        m_timeCondition.setExitTime(i_Time);
    }

    public int getElitismCount() {
        return m_ElitismCount;
    }

    public void setElitismCount(int i_Elitism) {

        if (i_Elitism < 0 || i_Elitism >= m_EngineInfo.getInitialPopulation()) {
            throw new IllegalArgumentException("Elite count must be non-negative and less than population size.");
        } else {
            this.m_ElitismCount = i_Elitism;
            m_EngineInfo.getSelection().setElitismCount(m_ElitismCount, m_EngineInfo.getInitialPopulation());
        }
    }

    public DTOEngineInformation<T> getDTOEngine() {
        return m_DTOEngine;
    }

//    public void setProbability(double i_Probability) {
//        if (i_Probability < 0 || i_Probability > 1) {
//            throw new IllegalArgumentException("The probability must be range of 0 to 1");
//        } else {
//            m_Probability = i_Probability;
//            m_EngineInfo.getMutations().forEach(mutation -> mutation.setProbability(m_Probability));
//        }
//    }

    public void setPredefinedTournamentEqualizer(float i_Probability) {
        if (i_Probability < 0 || i_Probability > 1) {
            throw new IllegalArgumentException("The probability must be range of 0 to 1");
        } else {
            m_EngineInfo.getSelection().setPredefinedTournamentEqualizer(i_Probability);
        }
    }
}


