package time.table.problem.objects;

import time.table.problem.LoadData;
import time.table.problem.configurations.EvolutionEngineInfo;
import time.table.problem.configurations.rules.Rule;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class TimeTable implements Serializable
{

    private static final long serialVersionUID = 100L;
    private int m_Days;
    private int m_Hours;

    private  Map<Integer,Subject> m_Subjects;
    private  Map<Integer,StudyClass> m_StudyClasses;
    private  Map<Integer,Teacher> m_Teachers;
    private  Map<Enum, Rule> m_Rules;
    private EvolutionEngineInfo EngineInfo;

    public TimeTable(LoadData i_LoadData)
    {
        this.m_Days = i_LoadData.getDays();
        this.m_Hours = i_LoadData.getHours();
        this.m_Subjects = i_LoadData.getSubjects();
        this.m_StudyClasses = i_LoadData.getStudyClasses();
        this.m_Teachers = LoadData.getTeachers();
        this.m_Rules = i_LoadData.getRules();
        this.EngineInfo = i_LoadData.getEngineInfo();
    }

    public Map<Integer, Subject> getSubjects() {
        return m_Subjects;
    }

    public Map<Integer, StudyClass> getStudyClasses()
    {
        return m_StudyClasses;
    }

    public Map<Integer, Teacher> getTeachers() {
        return m_Teachers;
    }

    public Map<Enum, Rule> getRules() {
        return m_Rules;
    }

    public EvolutionEngineInfo getEngineInfo() {
        return EngineInfo;
    }

    public int getDays() {  return m_Days; }

    public int getHours() {
        return m_Hours;
    }

    public void setHours(int hours) {
        m_Hours = hours;
    }

    public int GetWeeklyHours() {return m_Hours * m_Days; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTable timeTable = (TimeTable) o;
        return m_Days == timeTable.m_Days && m_Hours == timeTable.m_Hours;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_Days, m_Hours);
    }

    public int getGeneLength()
    {
        int res = 0;
        for (Map.Entry<Integer, StudyClass> entry : m_StudyClasses.entrySet())
        {
            res += entry.getValue().getRequirementHoursSum();
        }
        return res;
    }
}
