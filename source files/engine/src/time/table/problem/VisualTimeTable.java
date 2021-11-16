package time.table.problem;

import time.table.problem.objects.StudyClass;
import time.table.problem.objects.Teacher;

import java.util.ArrayList;
import java.util.List;

public class VisualTimeTable {
    private List<Quintet> m_resList;
    List<Quintet>[][] m_Table = null;
    private int m_maxHours;
    private int m_maxDays;

    public VisualTimeTable(List<Quintet> i_TimeTableList, int i_days, int i_hours) {
        m_resList = i_TimeTableList;
        m_maxHours = i_hours + 1;
        m_maxDays = i_days + 1;
    }

    public List<Quintet>[][] ArrangeByHoursDays() {
        List<Quintet>[][] qArr = new ArrayList[m_maxHours][m_maxDays];
        for (Quintet quintet : m_resList) {
            int day = quintet.getDay();
            int hour = quintet.getHour();

            if (qArr[hour][day] == null) qArr[hour][day] = new ArrayList<>();
            qArr[hour][day].add(quintet);
        }

        return qArr;
    }

    public VisualTimeTable showAsTeacher(Teacher i_Teacher) {
        List<Quintet>[][] hourdayList = ArrangeByHoursDays();
        m_Table = new ArrayList[m_maxHours][m_maxDays];

        for (int i = 0; i < m_maxHours; i++) {
            for (int j = 0; j < m_maxDays; j++) {
                if (hourdayList[i][j] != null) {
                    for (Quintet q : hourdayList[i][j]) {

                        if (q.getTeacher().equals(i_Teacher)) {
                            int hour = q.getHour();
                            int day = q.getDay();
                            if (m_Table[hour][day] == null) m_Table[hour][day] = new ArrayList<>();
                            m_Table[hour][day].add(q);
                        }
                    }
                }

            }
        }

        return this;
    }

    public VisualTimeTable showAsClass(StudyClass i_StudyClass) {
        List<Quintet>[][] hourdayList = ArrangeByHoursDays();
        m_Table = new ArrayList[m_maxHours][m_maxDays];

        for (int i = 0; i < m_maxHours; i++) {
            for (int j = 0; j < m_maxDays; j++) {
                if (hourdayList[i][j] != null) {
                    for (Quintet q : hourdayList[i][j]) {

                        if (q.getStudyClass().equals(i_StudyClass)) {
                            int hour = q.getHour();
                            int day = q.getDay();
                            if (m_Table[hour][day] == null) m_Table[hour][day] = new ArrayList<>();
                            m_Table[hour][day].add(q);
                        }
                    }
                }

            }
        }

        return this;
    }

    @Override
    public String toString() {
        StringBuilder TimeTableString = new StringBuilder();
        String formatting = "| %-30s |";

        int qmaxsize = 0;
        for (int j = 1; j < m_maxDays; j++) {
            for (int i = 1; i < m_maxHours; i++) {
                if (m_Table[i][j] != null) {
                    if (qmaxsize < m_Table[i][j].size()) qmaxsize = m_Table[i][j].size();
                }
            }
        }

        for (int i = 1; i < m_maxHours + qmaxsize; i++) {
            TimeTableString.append(String.format(formatting, "------------------------------"));
        }

        TimeTableString.append(System.lineSeparator());

        for (int j = 1; j < m_maxDays; j++) {

            TimeTableString.append(System.lineSeparator());

            for (int i = 1; i < m_maxHours; i++) {
                if (m_Table[i][j] != null) {
                    for (Quintet q : m_Table[i][j]) {
                        TimeTableString.append(String.format(formatting, "Day " + q.getDay() + ",Hour " + q.getHour()));
                    }
                } else {
                    TimeTableString.append(String.format(formatting, " "));
                }
            }

            TimeTableString.append(System.lineSeparator());

            for (int i = 1; i < m_maxHours; i++) {
                if (m_Table[i][j] != null) {
                    for (Quintet q : m_Table[i][j]) {
                        TimeTableString.append(String.format(formatting, "Subject " + q.getSubject()));
                    }
                } else {
                    TimeTableString.append(String.format(formatting, " "));
                }
            }

            TimeTableString.append(System.lineSeparator());

            for (int i = 1; i < m_maxHours; i++) {
                if (m_Table[i][j] != null) {
                    for (Quintet q : m_Table[i][j]) {
                        TimeTableString.append(String.format(formatting, "Class " + q.getStudyClass().toString()));
                    }
                } else {
                    TimeTableString.append(String.format(formatting, " "));
                }
            }

            TimeTableString.append(System.lineSeparator());

            for (int i = 1; i < m_maxHours; i++) {
                if (m_Table[i][j] != null) {
                    for (Quintet q : m_Table[i][j]) {
                        TimeTableString.append(String.format(formatting, "Teacher " + q.getTeacher()));
                    }
                } else {
                    TimeTableString.append(String.format(formatting, " "));
                }
            }

            TimeTableString.append(System.lineSeparator());

            for (int i = 1; i < m_maxHours + qmaxsize; i++) {

                TimeTableString.append(String.format(formatting, "------------------------------"));
            }
            TimeTableString.append(System.lineSeparator());
        }

        return TimeTableString.toString();
    }

}


