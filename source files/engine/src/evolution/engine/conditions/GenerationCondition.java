package evolution.engine.conditions;

public class GenerationCondition
{
    private boolean isMarked; // if this condition chose as exit condition
    private int m_ExitGeneration;
    //SimpleIntegerProperty m_CurrentGeneration;


    public GenerationCondition(int Generation)
    {
        if(Generation == Integer.MAX_VALUE)
        {
            isMarked = false;
            m_ExitGeneration = 0;
        }
        else
        {
            isMarked = true;
            m_ExitGeneration = Generation;
        }
    }

    public boolean isEnded(int m_CurrentGeneration)
    {
        boolean ended;
        if(isMarked == false)
            ended = false;

        else
            ended = m_CurrentGeneration >= m_ExitGeneration;

        return ended;
    }

    public void setExitGeneration(int exitGeneration)
    {
        m_ExitGeneration = exitGeneration;
    }

}
