package time.table.problem.objects;

import time.table.problem.LoadData;
import time.table.problem.jaxb.schema.generated.ETTStudy;

import java.io.Serializable;
import java.util.Objects;

public class Requirement implements Serializable
{

    private static final long serialVersionUID = 100L;
    private Subject m_Subject = null;
    private int m_SubjectId;
    private int m_Hours;

    public Requirement(ETTStudy requirement) throws NullPointerException
    {
        m_SubjectId = requirement.getSubjectId();
        m_Hours = requirement.getHours();
        m_Subject = LoadData.FindSubject(m_SubjectId);
        if (m_Subject == null)
        {
           throw new NullPointerException("StudyClass Requirement can not contain subject that doesn't exist on subject list, please check subjects list and try again");
        }
    }

    public int getSubjectId() {
        return m_SubjectId;
    }

    public void setSubjectId(int m_SubjectId) {
        this.m_SubjectId = m_SubjectId;
    }

    public int getHours() {
        return m_Hours;
    }

    public void setHours(int m_Hours) {
        this.m_Hours = m_Hours;
    }

    public Subject getSubject() {
        return m_Subject;
    }

    public void setSubject(Subject m_Subject) {
        this.m_Subject = m_Subject;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requirement that = (Requirement) o;
        return m_SubjectId == that.m_SubjectId && m_Hours == that.m_Hours && m_Subject.equals(that.m_Subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_Subject, m_SubjectId, m_Hours);
    }
}
