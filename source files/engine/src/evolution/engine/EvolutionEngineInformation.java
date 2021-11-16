package evolution.engine;

import time.table.problem.configurations.Crossover;
import time.table.problem.configurations.mutation.Mutation;
import time.table.problem.configurations.rules.Rule;
import time.table.problem.configurations.Selection;

import java.util.List;
import java.util.Map;

public interface EvolutionEngineInformation
{
   public Selection getSelection();
   public Crossover getCrossover();
   public int getInitialPopulation();
   public List<Mutation> getMutations();
   public Map<Enum, Rule> GetRules();
   public int getMaxEval();
}
