package time.table.problem.objects;

import time.table.problem.jaxb.schema.generated.ETTSubject;

import java.io.Serializable;
import java.util.Objects;

public class Subject implements Serializable {

    private static final long serialVersionUID = 100L;
    private String m_Name;
    private int m_Id;

    public Subject(ETTSubject ettSubject)
    {
        this.m_Id = ettSubject.getId();
        m_Name = ettSubject.getName();
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(m_Name, subject.m_Name) && Objects.equals(m_Id, subject.m_Id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(m_Name, m_Id);
    }

    public String getName()
    {
        return m_Name;
    }

    public void setName(String name)
    {
        this.m_Name = name;
    }

    public Integer getId() {
        return m_Id;
    }

    public void setId(Integer id)
    {
        this.m_Id = id;
    }

    @Override
    public String toString()
    {
        return "Id=" + m_Id+") "+ m_Name + ".";

    }
}
