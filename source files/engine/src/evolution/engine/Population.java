package evolution.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Population class
public class Population<T> implements Serializable, Cloneable {
    private static final long serialVersionUID = 100L;
    int m_geneLength;
    private int m_PopSize;
    private List<Individual> m_Individuals;
    private EvolutionEngineInformation m_EngineInfo = null;
    private int m_FittestScore = 0;

    public Population(Factory<T> i_Factory, EvolutionEngineInformation i_EngineInfo, int i_geneListLength)
    {
        m_geneLength = i_geneListLength;
        m_EngineInfo = i_EngineInfo;
        m_PopSize = m_EngineInfo.getInitialPopulation();
        m_Individuals = new ArrayList<>();

        //Create a first population pool
        for (int i = 0; i < m_PopSize; i++) {
            Individual ind = new Individual(i_Factory, i_EngineInfo.GetRules(), i_geneListLength);
            ind.CreateRandomGeneList();
            m_Individuals.add(ind);
        }
    }

    public int getBestFitnessScore() {
        return m_FittestScore;
    }

    public Individual getFittestScore() {
        Individual maxFit = m_Individuals.get(0);

        for (Individual ind : m_Individuals) {
            if (maxFit.getFitness() < ind.getFitness()) {
                maxFit = ind;
            }
        }
        m_FittestScore = maxFit.getFitness();
        return maxFit;
    }

    public void calculateFitness() throws Exception {
        for (Individual individual : m_Individuals) {
            individual.calcFitness();
        }
    }

    public List<Individual> getIndividuals() {
        return m_Individuals;
    }

    public void setIndividuals(List<Individual> i_individuals) {
        m_Individuals = i_individuals;
    }

    public void copyAndSetIndividualList(List<Individual> other) {
        m_Individuals = new ArrayList<>();

        other.forEach(q -> {
            try {
                m_Individuals.add((Individual) q.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });
    }

    public void reset()
    {
        initIndividuals();
        m_FittestScore = 0;
    }

    private void initIndividuals()
    {
        Map rules = m_Individuals.get(0).getRules();
        Factory factory = m_Individuals.get(0).getIndividualFactory();
        m_Individuals = new ArrayList<>();

        //Create a first population pool
        for (int i = 0; i < m_PopSize; i++) {
            Individual ind = new Individual(factory, rules, m_geneLength);
            ind.CreateRandomGeneList();
            m_Individuals.add(ind);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}