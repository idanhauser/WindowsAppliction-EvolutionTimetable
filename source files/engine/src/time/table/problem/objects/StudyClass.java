package time.table.problem.objects;

import time.table.problem.LoadData;
import time.table.problem.jaxb.schema.generated.ETTClass;
import time.table.problem.jaxb.schema.generated.ETTStudy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudyClass implements Serializable
{
    private static final long serialVersionUID = 100L;
    private String m_Name;
    private int m_Id;
    private List<Requirement> m_requirements;

    public StudyClass(ETTClass ettClass)
    {
        m_Id = ettClass.getId();
        m_Name= ettClass.getETTName();
        setRequirements(ettClass.getETTRequirements().getETTStudy());
    }

    public String getName()
    {
        return m_Name;
    }

    public int getId() {
        return m_Id;
    }

    public void setId(int m_id) {
        this.m_Id = m_id;
    }

    public List<Requirement> getRequirements() {
        return m_requirements;
    }

    public void setRequirements(List<ETTStudy> ett_requirements)
    {
        m_requirements = new ArrayList<>();
        int sum = 0;
        int days = LoadData.getDays();
        int hours = LoadData.getHours();
        for (ETTStudy req :ett_requirements)
        {
            Requirement newReq = new Requirement(req);
            m_requirements.add(newReq);
            sum += newReq.getHours();
        }
        if(sum > days*hours)
        {
            throw new IllegalArgumentException("studyClass weekly hours is greater then the timeTable weekly hours (day*hours)");
        }
    }

    public int getRequirementHoursSum()
    {
        int res = 0;
        for (Requirement r :m_requirements)
        {
            res += r.getHours();
        }
        return res;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyClass that = (StudyClass) o;
        return m_Id == that.m_Id && m_Name.equals(that.m_Name) && m_requirements.equals(that.m_requirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_Name, m_Id, m_requirements);
    }

    @Override
    public String toString() {
        return "Id=" + m_Id+") "+ m_Name + ".";
    }

}
