package time.table.problem.configurations;

import evolution.engine.Individual;
import time.table.problem.Quintet;
import time.table.problem.jaxb.schema.generated.ETTSelection;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Selection implements Serializable {
    private static final long serialVersionUID = 100L;
    List<Individual> m_ElitismList;
    private float m_TopPercent;
    private int m_ElitismCount;


    private type m_EnumSelectType;

    public Selection(ETTSelection i_EttSelection, int i_PopSize) {
        setElitismCount(i_EttSelection.getETTElitism(), i_PopSize);
        m_EnumSelectType = type.valueOf(i_EttSelection.getType());
        if (m_EnumSelectType == type.Truncation) {
            m_TopPercent = (float) (setTopPercentFromConfiguration(i_EttSelection.getConfiguration()) / 100f);
        } else if (m_EnumSelectType == type.Tournament) {
            float percent = setTopPercentFromConfiguration(i_EttSelection.getConfiguration());
            if (percent > 1 || percent < 0) {
                throw new IllegalArgumentException("Probability should be between 0 to 1");
            } else {
                m_TopPercent = percent;
            }
        } else {
            m_TopPercent = 0.5f;
        }
    }

    public type getEnumSelectType() {
        return m_EnumSelectType;
    }

    public void setEnumSelectType(type m_EnumSelectType) {
        this.m_EnumSelectType = m_EnumSelectType;
    }

    public List<Individual> get_ElitismList() {
        return m_ElitismList;
    }


    public void setPredefinedTournamentEqualizer (float i_PredefinedTournamentEqualizer) {
        if (i_PredefinedTournamentEqualizer > 1 || i_PredefinedTournamentEqualizer < 0) {
            throw new IllegalArgumentException("Probability should be between 0 to 1");
        } else {
            m_TopPercent = i_PredefinedTournamentEqualizer;
        }
    }

    public void setTopPercent(float m_TopPercent) {
        this.m_TopPercent = m_TopPercent;
    }

    private float setTopPercentFromConfiguration(String configuration) {
        int indexEquals = configuration.indexOf("=");
        String number = configuration.substring(++indexEquals);
        float percent = Float.parseFloat(number);
        return percent;
    }

    public List<Individual> doSelection(List<Individual> i_Individuals) {
        List<Individual> individualsWithoutElite = new ArrayList<>();
        m_ElitismList = null;
        if (m_ElitismCount > 0) {
            List<Individual> temp = null;
            m_ElitismList = new ArrayList<>();

            chooseElitism(i_Individuals, individualsWithoutElite);
            temp = m_EnumSelectType.Select(individualsWithoutElite, m_TopPercent);
            temp.addAll(m_ElitismList);
            return temp;
        }
        return m_EnumSelectType.Select(i_Individuals, m_TopPercent);
    }

    private void chooseElitism(List<Individual> i_Individuals, List<Individual> i_IndividualsWithoutElite) {

        i_Individuals.sort(Individual::compareFittness);
        m_ElitismList.addAll(i_Individuals.subList(0, m_ElitismCount));
        i_IndividualsWithoutElite.addAll(i_Individuals.stream().skip(m_ElitismCount).collect(Collectors.toList()));

    }

    public float getTopPercent() {
        return m_TopPercent;
    }

    public type getType() {
        return m_EnumSelectType;
    }

    public int getElitismCount() {
        return m_ElitismCount;
    }

    public void setElitismCount(int i_Elitism, int i_PopulationSize) {
        if (i_Elitism < 0 || i_Elitism >= i_PopulationSize) {
            throw new IllegalArgumentException("Elite count must be non-negative and less than population size.");
        } else {
            m_ElitismCount = i_Elitism;
        }
    }

    public enum type implements Serializable {

        Truncation {
            @Override
            public List<Individual> Select(List<Individual> i_individuals, float i_selectionProbability) {
                int newSize = (int) (i_individuals.size() * i_selectionProbability);
                //       Comparator<Individual> comp = (ind1, ind2) -> ind1.compareFittness(ind2);
                //Comparator<Individual> comp = ;
                List<Individual> res = i_individuals.stream().sorted(Individual::compareFittness).limit(newSize).collect(Collectors.toList());
                return res;
            }
        },
        Tournament {
            @Override
            public List<Individual> Select(List<Individual> i_Individuals, float i_SelectionProbability) {
                if (i_SelectionProbability > 1 || i_SelectionProbability < 0) {
                    throw new IllegalArgumentException("Probability should be between 0 to 1");
                } else {
                    Random rng = new Random();
                    List<Individual> selections = new ArrayList<>(i_Individuals.size());
                    for (int i = 0; i < i_Individuals.size(); i++) {
                        // Pick two candidates at random.
                        Individual candidate1 = i_Individuals.get(rng.nextInt(i_Individuals.size()));
                        Individual candidate2 = i_Individuals.get(rng.nextInt(i_Individuals.size()));

                        double selectFitter = Math.random();
                        if (selectFitter > i_SelectionProbability) {
                            // Select the fitter candidate.
                            selections.add(candidate2.getFitness() > candidate1.getFitness() ? candidate2 : candidate1);
                        } else {
                            // Select the less fit candidate.
                            selections.add(candidate2.getFitness() > candidate1.getFitness() ? candidate1 : candidate2);
                        }
                    }
                    return selections;
                }
            }
        },


        RouletteWheel {
            private int selectionSize;

            @Override
            public List<Individual> Select(List<Individual> i_individuals, float i_topPercent) {
                return RouletteWheel(i_individuals);
            }

            public List<Individual> RouletteWheel(List<Individual> i_individuals) {

                Random rng = new Random();
                // Record the cumulative fitness scores.  It doesn't matter whether the
                // population is sorted or not.  We will use these cumulative scores to work out
                // an index into the population.  The cumulative array itself is implicitly
                // sorted since each element must be greater than the previous one.  The
                // numerical difference between an element and the previous one is directly
                // proportional to the probability of the corresponding candidate in the population
                // being selected.
                double[] cumulativeFitnesses = new double[i_individuals.size()];
                cumulativeFitnesses[0] = i_individuals.get(0).getFitness();

                for (int i = 1; i < i_individuals.size(); i++) {
                    double fitness = i_individuals.get(i).getFitness();
                    cumulativeFitnesses[i] = cumulativeFitnesses[i - 1] + fitness;
                }

                List<Individual> selection = new ArrayList<>(selectionSize);
                for (int i = 0; i < i_individuals.size(); i++) {
                    double randomFitness = rng.nextDouble() * cumulativeFitnesses[cumulativeFitnesses.length - 1];
                    int index = Arrays.binarySearch(cumulativeFitnesses, randomFitness);
                    if (index < 0) {
                        // Convert negative insertion point to array index.
                        index = Math.abs(index + 1);
                    }
                    selection.add(i_individuals.get(index));
                }
                return selection;
            }


        };

        public abstract List<Individual> Select(List<Individual> i_individuals, float i_topPercent);
    }

}
