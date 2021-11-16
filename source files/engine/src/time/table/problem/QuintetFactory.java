package time.table.problem;

import evolution.engine.Factory;
import time.table.problem.objects.StudyClass;
import time.table.problem.objects.Subject;
import time.table.problem.objects.Teacher;
import time.table.problem.objects.TimeTable;

import java.io.Serializable;
import java.util.Map;

public class QuintetFactory implements Factory<Quintet>, Serializable {
    private final TimeTable m_TimeTable;

    public QuintetFactory(TimeTable i_TimeTable) {
        m_TimeTable = i_TimeTable;
    }

    @Override
    public Quintet Create() {
        return new Quintet(m_TimeTable);
    }

    public int getTotalDays() {
        return m_TimeTable.getDays();
    }

    public int getTotalHours() {
        return m_TimeTable.getHours();
    }

    public Map<Integer, Subject> getSubjects() {
        return m_TimeTable.getSubjects();
    }

    public int getTotalNumberOfClasses() {
        return m_TimeTable.getStudyClasses().size();
    }

    public int getTotalNumberOfTeachers() {
        return m_TimeTable.getTeachers().size();
    }

    public Map<Integer, Teacher> getTeachers() {
        return m_TimeTable.getTeachers();
    }

    public Map<Integer, StudyClass> getStudyClasses() {
        return m_TimeTable.getStudyClasses();
    }


}
