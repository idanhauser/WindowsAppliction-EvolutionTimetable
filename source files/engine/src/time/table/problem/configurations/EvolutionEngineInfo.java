package time.table.problem.configurations;

import evolution.engine.EvolutionEngineInformation;
import time.table.problem.configurations.mutation.Mutation;
import time.table.problem.configurations.rules.Rule;
import time.table.problem.configurations.rules.RuleIdEnum;
import time.table.problem.jaxb.schema.generated.ETTEvolutionEngine;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EvolutionEngineInfo implements EvolutionEngineInformation, Serializable {
    private static final long serialVersionUID = 100L;
    private int m_InitialPopulation;
    private Crossover m_Crossover;
    private Selection m_Selection;
    private List<Mutation> m_Mutations;
    private Map<Enum, Rule> m_Rules;


    public EvolutionEngineInfo(ETTEvolutionEngine i_EvolutionEngine, List<Mutation> i_Mutations, Map<Enum, Rule> i_Rules) throws Exception {
        m_Rules = i_Rules;
        m_Mutations = i_Mutations;
        m_InitialPopulation = i_EvolutionEngine.getETTInitialPopulation().getSize();
        m_Crossover = new Crossover(i_EvolutionEngine.getETTCrossover());
        m_Selection = new Selection(i_EvolutionEngine.getETTSelection(),m_InitialPopulation);
    }


    public Selection getSelection() {
        return m_Selection;
    }

    public Crossover getCrossover() {
        return m_Crossover;
    }

    public int getInitialPopulation() {
        return m_InitialPopulation;
    }

    public List<Mutation> getMutations() {
        return m_Mutations;
    }

    public Map<Enum, Rule> GetRules() {
        return m_Rules;
    }

    @Override
    public int getMaxEval() {
        return RuleIdEnum.getMaxEval();
    }
}
