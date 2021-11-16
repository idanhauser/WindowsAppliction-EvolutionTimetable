package time.table.problem.configurations.mutation;

import evolution.engine.Individual;
import time.table.problem.Quintet;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public enum MutationEType implements Serializable {
    Flipping {
        @Override
        public void Mutate(Individual<Quintet> i_Individual, EComponent m_Component, int i_NumberOfTupples) {
            //i_NumberOfTupples = maxTupples.
            Random rn = new Random();
            int max = rn.nextInt(i_NumberOfTupples);
            //random max to change
            for (int i = 0; i < max; i++) {
                int randomQuintet = rn.nextInt(i_Individual.GetGeneListSize());
                Quintet quintet = i_Individual.getListOfGenes().get(randomQuintet);
                m_Component.setComponent(quintet);
            }

        }
    },


    Sizer {
        @Override
        public void Mutate(Individual i_Individual, EComponent m_Component, int i_NumberOfTupples) {
            //i_NumberOfTupples = TotalTupples.
            if (i_NumberOfTupples < 0) {
                deleteRandomGenes(i_Individual.getListOfGenes(), i_NumberOfTupples);
            } else if (i_NumberOfTupples > 0) {
                addRandomGenes(i_Individual, i_NumberOfTupples);

            }
        }

        private void addRandomGenes(Individual i_GenesList, int i_NumberOfTupples) {
            try {
                Individual temp = (Individual) i_GenesList.clone();
                temp.setGeneListLength(i_NumberOfTupples);
                temp.CreateRandomGeneList();
                i_GenesList.getListOfGenes().addAll(temp.getListOfGenes());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        private void deleteRandomGenes(List i_GenesList, int i_NumberOfTupples) {
            Random rnd = new Random();
            int idx;
            for (int i = 0; i < i_NumberOfTupples; i++) {
                idx = rnd.nextInt(i_GenesList.size());
                i_GenesList.remove(idx);
            }
        }
    };


    private static final long serialVersionUID = 100L;

    public abstract void Mutate(Individual<Quintet> i_Individual, EComponent m_Component, int i_NumberOfTupples);
}


