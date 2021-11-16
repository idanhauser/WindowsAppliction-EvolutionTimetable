package evolution.engine.conditions;

import java.time.Duration;
import java.time.Instant;

public class TimeCondition {
    private boolean isMarked; // if this condition chose as exit condition
    private float m_ExitTime;
    private Instant m_StartTime;
    private Instant m_PauseTime;

    public TimeCondition(int Time) {
        setExitTime(Time);
    }

    public boolean isTimeConditionIsOn() {
        return isMarked;
    }

    public void StartTimer() {
        m_StartTime = Instant.now();
    }

    public boolean isEnded() {
        boolean ended;
        if (!isMarked)
            ended = false;

        else
            ended = getDuration().getSeconds() >= m_ExitTime;

        return ended;
    }


    public Duration getDuration() {
        return Duration.between(m_StartTime, Instant.now());
    }

    public void setExitTime(float exitTime)
    {
        isMarked = (exitTime != Integer.MAX_VALUE);
        m_ExitTime = exitTime * 60;
    }

    public void pauseTimer() {
        m_PauseTime = Instant.now();
    }

    public void resumeTimer()
    {
        Duration PauseDuration = Duration.between(m_PauseTime, Instant.now());
        m_StartTime = m_StartTime.plusSeconds(PauseDuration.getSeconds());
    }
}
