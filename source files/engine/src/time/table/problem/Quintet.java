package time.table.problem;


import time.table.problem.objects.StudyClass;
import time.table.problem.objects.Subject;
import time.table.problem.objects.Teacher;
import time.table.problem.objects.TimeTable;

import java.io.Serializable;
import java.util.Map;
import java.util.Random;

public class Quintet implements Comparable<Quintet>, Serializable
{
    private static final long serialVersionUID = 100L;

    private TimeTable m_TimeTable;
    private StudyClass m_C;
    private Subject m_S;
    private Teacher m_T;
    private int m_H;
    private int m_D;

    public Quintet(TimeTable i_TimeTable)
    {
        m_TimeTable = i_TimeTable;
        Random rn = new Random();
        setTeacher(rn);
        setSubject(rn);
        setClass(rn);
        setDay(rn);
        setHour(rn);
    }

    public Quintet(int c, int s, int h, int d, int t)
    {
        m_C = m_TimeTable.getStudyClasses().get(c);
        m_S = m_TimeTable.getSubjects().get(s);
        m_T = m_TimeTable.getTeachers().get(t);
        m_H = h;
        m_D = d;
    }

    public int getHour() {
        return m_H;
    }

    public int getDay() {
        return m_D;
    }

    public StudyClass getStudyClass() {   return m_C;    }
    public Teacher getTeacher() {   return m_T;    }
    public Subject getSubject() {   return m_S;  }

    public int getTotalDays()
    {
       return m_TimeTable.getDays();
    }

    public int getTotalHours()
    {
        return m_TimeTable.getHours();
    }

    public Map<Integer, Subject> getSubjects()
    {
        return m_TimeTable.getSubjects();
    }

    public Map<Integer, StudyClass> getStudyClasses()
    {
        return m_TimeTable.getStudyClasses();
    }

    public Map<Integer, Teacher> getTeachers()
    {
        return m_TimeTable.getTeachers();
    }

    @Override
    public int compareTo(Quintet other) {
        return this.m_D - other.m_D;
    }
    public void setTeacher(Random rn)
    {
        int tSize = m_TimeTable.getTeachers().size();
        m_T = m_TimeTable.getTeachers().get((rn.nextInt(tSize)) + 1);
    }

    public void setClass(Random rn)
    {
        int cSize = m_TimeTable.getStudyClasses().size();
        m_C = m_TimeTable.getStudyClasses().get(rn.nextInt(cSize) + 1);
    }

    public void setSubject(Random rn)
    {
        int sSize = m_TimeTable.getSubjects().size();
        m_S = m_TimeTable.getSubjects().get(rn.nextInt(sSize) + 1);
    }

    public void setDay(Random rn)
    {
        m_D = rn.nextInt(m_TimeTable.getDays() - 1) + 1;
    }
    public void setHour(Random rn)
    {
        m_H = rn.nextInt(m_TimeTable.getHours() - 1) + 1;
    }


    @Override
    public String toString()
    {
        String output = String.format("<day = %d, hour = %d, Class = ",m_D,m_H);
        output += m_C + "Teacher = " + m_T + "Subject = " + m_S + ">";
        return output;
    }
}
