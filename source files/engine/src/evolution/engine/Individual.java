package evolution.engine;

import time.table.problem.configurations.rules.Rule;

import java.io.Serializable;
import java.util.*;


public class Individual<T> implements Cloneable, Serializable {
    private static final long serialVersionUID = 100L;
    private List<T> m_listOfGenes;
    private Factory<T> individualFactory;

    private int m_geneListLength;
    private int fitness = 0;
    private Map<Enum, Rule> m_Rules;

    private Map<Rule, Float> RulesGrade = new HashMap<>();

    public Individual(Factory<T> i_Factory, Map<Enum, Rule> i_Rules, int i_geneListLength) {
        individualFactory = i_Factory;
        m_Rules = i_Rules;
        m_geneListLength = i_geneListLength;
    }

    public void CreateRandomGeneList()
    {
        m_listOfGenes = new ArrayList<>();
        Random rn = new Random();
        int random = rn.nextInt(m_geneListLength);
        m_geneListLength += random;
        for (int i = 0; i < m_geneListLength; i++)
        {
            T obj = individualFactory.Create();
            m_listOfGenes.add(obj);
        }
    }

    public Factory<T> getIndividualFactory() {
        return individualFactory;
    }

    public Map<Enum, Rule> getRules() {
        return m_Rules;
    }

    public void calcFitness()
    {
        fitness = 0;
        float hardRes = 0;
        float softRes = 0;
        float hardCounter = 0;
        float softCounter = 0;

        for (Rule rule : m_Rules.values())
        {
            float eval = rule.Eval((this));

            if (rule.GetType() == Rule.RuleTypeEnum.Hard)
            {
                hardRes += eval;
                hardCounter++;
            }
            else
            {
                softRes += eval;
                softCounter++;
            }

            RulesGrade.put(rule,eval);
        }

        fitness = (int)(hardRes/hardCounter + softRes/softCounter);
    }

    public void setListOfGenes(List<T> m_listOfGenes) {
        this.m_listOfGenes = m_listOfGenes;
        m_geneListLength = m_listOfGenes.size();
    }


    public List<T> getListOfGenes() {
        return m_listOfGenes;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        Individual newInd = (Individual)super.clone();
        newInd.m_listOfGenes = new ArrayList(m_listOfGenes);
        return newInd;
    }

    public int GetGeneListSize() {
        return m_listOfGenes.size();
    }

    public int compareFittness(Individual<T> ind2) {
        return ind2.getFitness() - getFitness();
    }

    public Map<Rule, Float> getRulesGrade() {
        return RulesGrade;
    }

    //Getters and Setters
    public int getGeneListLength() {
        return m_geneListLength;
    }

    public void setGeneListLength(int m_geneListLength) {
        this.m_geneListLength = m_geneListLength;
    }

    public int getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "m_listOfGenes=" + m_listOfGenes +
                ", individualFactory=" + individualFactory +
                ", m_geneListLength=" + m_geneListLength +
                ", fitness=" + fitness +
                ", m_Rules=" + m_Rules +
                '}';
    }
}
