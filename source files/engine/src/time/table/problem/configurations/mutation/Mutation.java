package time.table.problem.configurations.mutation;

import evolution.engine.Individual;
import time.table.problem.jaxb.schema.generated.ETTMutation;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Mutation implements Serializable {
    private static final long serialVersionUID = 100L;

    private MutationEType m_EnumType;


    private double m_Probability;
    private int m_NumberOfTupples;

    private EComponent m_Component;
    //Factory<Individual> i_Factory;

    public Mutation(ETTMutation ettMutation) {
        m_EnumType = MutationEType.valueOf(ettMutation.getName());
        setProbability(ettMutation.getProbability());
        setConfiguration(m_EnumType, ettMutation.getConfiguration());
    }

    public List<Individual> doMutation(List<Individual> i_Individuals, int i_Days, int i_Hours) {
        Random rn = new Random();

        for (Individual ind : i_Individuals) {
            if (rn.nextFloat() <= m_Probability) {
                if (m_EnumType == MutationEType.Sizer) {
                    if ((m_NumberOfTupples > 0) && (ind.getListOfGenes().size() + m_NumberOfTupples > (i_Days * i_Hours))) {
                        m_NumberOfTupples = ind.getListOfGenes().size() - (i_Days * i_Hours);
                    } else if ((m_NumberOfTupples < 0) && (ind.getListOfGenes().size() + m_NumberOfTupples >= (i_Days))) {
                        System.out.println("DEBUG: check that.");
                        m_NumberOfTupples = ind.getListOfGenes().size() + (i_Days);
                        if (m_NumberOfTupples > 0) {
                            System.out.println("DEBUG: check that.");
                        }
                    }
                }
                m_EnumType.Mutate(ind, m_Component, m_NumberOfTupples);
            }
        }

        return i_Individuals;
    }

    private void setConfiguration(MutationEType i_EnumType, String i_ConfigurationStr) {
        int indexEquals;
        int indexComma;
        String maxTupplesStr;
        String EnumCompStr;
        EComponent eComp;
        int maxTupplesInt;

        indexEquals = i_ConfigurationStr.indexOf("=");
        indexComma = i_ConfigurationStr.indexOf(',');
        if (indexComma != -1) {
            maxTupplesStr = i_ConfigurationStr.substring(indexEquals + 1, indexComma);
            maxTupplesInt = Integer.parseInt(maxTupplesStr);
            indexEquals = i_ConfigurationStr.lastIndexOf("=");
            EnumCompStr = i_ConfigurationStr.substring(indexEquals + 1);
            try {
                eComp = Enum.valueOf(EComponent.class, EnumCompStr);
            } catch (IllegalArgumentException E) {
                throw new IllegalArgumentException("The value of the component in mutation is Illegal." +
                        System.lineSeparator() + "The only valid values are : T, S, D, H ,C");
            }
            m_Component = eComp;
        } else {
            maxTupplesStr = i_ConfigurationStr.substring(indexEquals + 1);
            maxTupplesInt = Integer.parseInt(maxTupplesStr);
        }
        m_NumberOfTupples = maxTupplesInt;

    }

    public double getProbability() {
        return m_Probability;
    }

    public void setProbability(double i_Probability) {
        if (i_Probability < 0 || i_Probability > 1) {
            throw new IllegalArgumentException("The probability must be range of 0 to 1");
        } else {
            m_Probability = i_Probability;
        }
    }

    public void setTupples(int Tupples) {
        m_NumberOfTupples = Tupples;
    }

    public void setComponent(EComponent Component) {
        m_Component = Component;
    }

    public EComponent getComponent()
    {
        return m_Component;
    }

    public MutationEType getEnumType()
    {
        return m_EnumType;
    }

    public int getTupples()
    {
        return m_NumberOfTupples;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(m_EnumType.toString()).append(": probability ").append(m_Probability).append(System.lineSeparator()).append("Configuration: ").append(System.lineSeparator()).append("\t Max Tupples: ").append(this.m_NumberOfTupples).append(System.lineSeparator());
        if (m_Component != null) {
            sb.append("\t Component: ").append(this.m_Component.toString());
        }
        return sb.toString();
    }
}
