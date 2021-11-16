package data.transfer.objects;

import time.table.problem.configurations.rules.Rule;
import time.table.problem.objects.StudyClass;
import time.table.problem.objects.Subject;
import time.table.problem.objects.Teacher;
import time.table.problem.objects.TimeTable;

import java.io.Serializable;
import java.util.Map;

public class TimeTableInformation implements Serializable {
    private static final long serialVersionUID = 100L;
    private int m_Days;
    private int m_Hours;
    private Map<Integer, Subject> m_Subjects;
    private Map<Integer, StudyClass> m_StudyClasses;
    private Map<Integer, Teacher> m_Teachers;
    private Map<Enum, Rule> m_Rules;

    public TimeTableInformation(int i_Days, int i_Hours, Map<Integer, Subject> i_Subjects, Map<Integer, StudyClass> i_StudyClasses,
                                Map<Integer, Teacher> i_Teachers, Map<Enum, Rule> i_Rules) {
        this.m_Days = i_Days;
        this.m_Hours = i_Hours;
        this.m_Subjects = i_Subjects;
        this.m_StudyClasses = i_StudyClasses;
        this.m_Teachers = i_Teachers;
        this.m_Rules = i_Rules;
    }

    public TimeTableInformation(TimeTable timeTable) {
        this.m_Days = timeTable.getDays();
        this.m_Hours = timeTable.getHours();
        this.m_Subjects = timeTable.getSubjects();
        this.m_StudyClasses = timeTable.getStudyClasses();
        this.m_Teachers = timeTable.getTeachers();
        this.m_Rules = timeTable.getRules();
    }

    public int getDays() {
        return m_Days;
    }

    public int getHours() {
        return m_Hours;
    }

    public Map<Integer, Subject> getSubjects() {
        return m_Subjects;
    }

    public Map<Integer, StudyClass> getStudyClasses() {
        return m_StudyClasses;
    }

    public Map<Integer, Teacher> getTeachers() {
        return m_Teachers;
    }

    public Map<Enum, Rule> getRules() {
        return m_Rules;
    }

}
