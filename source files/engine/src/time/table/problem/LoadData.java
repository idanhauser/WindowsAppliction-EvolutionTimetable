package time.table.problem;

import time.table.problem.configurations.EvolutionEngineInfo;
import time.table.problem.configurations.rules.Rule;
import time.table.problem.jaxb.schema.XmlParser;
import time.table.problem.objects.StudyClass;
import time.table.problem.objects.Subject;
import time.table.problem.objects.Teacher;

import java.util.Map;
/*
/ each loading user must create new LoadData object
 */

public class
LoadData
{
    private static XmlParser s_XmlParser = null;

    private static int s_Day;
    private static int s_Hour;
    private static Map<Integer, Subject> s_Subjects = null;
    private static Map<Integer, StudyClass> s_StudyClasses = null;
    private static Map<Integer, Teacher> s_Teachers = null;
    private static Map<Enum, Rule> s_Rules = null;
    private static EvolutionEngineInfo s_EngineInfo = null;

    public LoadData(String i_fileName) throws Exception
    {
        //s_XmlParser = new XmlParser("engine/src/time/table/problem/resources/EX2-small.xml");
        s_XmlParser = new XmlParser(i_fileName); //temporary use the top line to debug (all the fields are private and cant be view from main.. should debug here
        s_Day = s_XmlParser.LoadDays();
        s_Hour = s_XmlParser.LoadHours();

        s_Subjects = s_XmlParser.LoadSubjects();
        s_Teachers = s_XmlParser.LoadTeachers();
        s_StudyClasses = s_XmlParser.GetClasses();
        s_Rules = s_XmlParser.LoadRules();
        s_EngineInfo = s_XmlParser.LoadEngineInfo();
    }


    public static int getDays() {   return s_Day;    }

    public static int getHours() {  return s_Hour;    }

    public static Map<Integer, Subject> getSubjects() { return s_Subjects;  }

    public static Map<Integer, StudyClass> getStudyClasses() {  return s_StudyClasses;    }

    public static Map<Integer, Teacher> getTeachers() { return s_Teachers;  }

    public static Map<Enum, Rule> getRules() {  return s_Rules; }

    public static EvolutionEngineInfo getEngineInfo() { return s_EngineInfo; }





    public static Subject FindSubject(int i_id)
    {
        return s_Subjects.get(i_id);
    }

}
