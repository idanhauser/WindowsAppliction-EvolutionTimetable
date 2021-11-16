package evolution.engine.conditions;

public class FitnessCondition
{
    private boolean isMarked; // if this condition chose as exit condition
    private int m_ExitFitness;
    //SimpleIntegerProperty m_BestFitness;

    public FitnessCondition(int Fitness)
    {
        if(Fitness == Integer.MAX_VALUE)
        {
            isMarked = false;
            m_ExitFitness = 0;
        }
        else
        {
            isMarked = true;
            m_ExitFitness = Fitness;
        }
    }


    public boolean isEnded(int m_BestFitness)
    {
        return m_BestFitness >= m_ExitFitness;
    }

    public void setExitFitness(int exitFitness)
    {
        m_ExitFitness = exitFitness;
    }

}
