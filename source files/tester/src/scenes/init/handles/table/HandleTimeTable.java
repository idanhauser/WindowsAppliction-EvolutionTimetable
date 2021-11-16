package scenes.init.handles.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import time.table.problem.Quintet;
import time.table.problem.VisualTimeTable;
import time.table.problem.objects.StudyClass;
import time.table.problem.objects.Teacher;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class HandleTimeTable {
    List<Quintet>[][] RawQuintetList;
    Filter TeacherFilter;
    Filter ClassFilter;
    Filter RawFilter;
    ComboBox<String> ShowAs_ComboBox;
    ComboBox<String> FilterBySpecificName_ComboBox;
    private TableView<String[]> ViewTimeTable;
    private int maxDays;
    private int maxHours;
    private List<String> m_Teachers;
    private List<String> m_StudyClasses;

    public HandleTimeTable(TableView<?> ViewTimeTable, int hours, int days, ComboBox ShowAs_ComboBox, ComboBox FilterBySpecificName_ComboBox, Map<Integer, Teacher> teachers, Map<Integer, StudyClass> studyClasses) {
        this.ViewTimeTable = (TableView<String[]>) ViewTimeTable;
        maxHours = hours;
        maxDays = days;
        this.ShowAs_ComboBox = ShowAs_ComboBox;
        this.FilterBySpecificName_ComboBox = FilterBySpecificName_ComboBox;


        ViewTimeTable.visibleProperty().setValue(true);
        CreateTable();
        CreateTeachersAndClassesNameList(teachers, studyClasses);
        initShowAsComboBox();
        initFilters();
    }

    private void initFilters() {
        TeacherFilter = q -> q.stream().filter(t -> t.getTeacher().getName().equals(FilterBySpecificName_ComboBox.getValue())).findFirst().map(t -> cellFormation(t,false)).orElse(null);
        ClassFilter = q -> q.stream().filter(t -> t.getStudyClass().getName().equals(FilterBySpecificName_ComboBox.getValue())).findFirst().map(t -> cellFormation(t,false)).orElse(null);
        //RawFilter = q -> cellFormation(q.get(0));
    }

    public void setTimeTable(List<Quintet> TimeTableList) {
        RawQuintetList = new VisualTimeTable(TimeTableList, maxDays, maxHours).ArrangeByHoursDays();
        setTable();
    }

    private void setTable() {
        ClearTable();
        ObservableList<String[]> data = FXCollections.observableArrayList();
        String[][] staffArray = GetStringArray();
        data.addAll(Arrays.asList(staffArray));
        ViewTimeTable.setItems(data);
    }

    private void CreateTable() {
        for (int i = 1; i <= maxDays; i++) {
            TableColumn tc = new TableColumn("Day " + i);
            final int colNo = i;

            tc.setCellValueFactory((Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>)
                    p -> new SimpleStringProperty((p.getValue()[colNo])));

            ViewTimeTable.getColumns().add(tc);
        }
    }

    private String[][] GetStringArray() {
        if (ShowAs_ComboBox.getValue().equals("Raw")) return RawStringArrayCreation();
        if (ShowAs_ComboBox.getValue().equals("Teacher")) return StringArrayCreation(TeacherFilter);
        if (ShowAs_ComboBox.getValue().equals("Class")) return StringArrayCreation(ClassFilter);

        return null;
    }

    private String[][] StringArrayCreation(Filter FilterBy) {
        HideHeader(false);
        String[][] retArr = new String[maxDays + 1][maxHours + 1];

        for (int i = 1; i <= maxHours; i++) {
            for (int j = 1; j <= maxDays; j++) {
                if (RawQuintetList[i][j] != null) {
                    retArr[j][i] = FilterBy.filter(RawQuintetList[i][j]);
                }
            }
        }

        return retArr;
    }

    private String[][] RawStringArrayCreation() {
        HideHeader(true);
        int size = getRawArrSize();
        String[][] retArr = new String[maxDays + 1][size+1];

        for (int i = 1, hour = 0; i <= maxHours; i++) {
            for (int j = 1, day = 0; j <= maxDays; j++) {
                if (RawQuintetList[i][j] != null)
                {
                    for (Iterator<Quintet> itr = RawQuintetList[i][j].iterator(); itr.hasNext(); )
                    {
                        String sFormat = cellFormation(itr.next(),true);
                        retArr[day][hour] = sFormat;
                        day++;
                        if (day == maxDays) {day = 0; hour++;}
                    }
                }
            }
        }

        return retArr;
    }

    private int getRawArrSize()
    {
        int size = 0;
        for (int i = 1; i <= maxHours; i++) {
            for (int j = 1; j <= maxDays; j++) {
                if(RawQuintetList[i][j] != null)
                size += RawQuintetList[i][j].stream().count();
            }
        }
        return size/maxDays +1;
    }


    String cellFormation(Quintet quintet, boolean withDaysHours) {
        StringBuilder resStr = new StringBuilder();
        resStr.append(quintet.getTeacher().getName());
        resStr.append(System.lineSeparator());
        resStr.append(quintet.getStudyClass().getName());
        resStr.append(System.lineSeparator());
        resStr.append(quintet.getSubject().getName());

        if(withDaysHours == true)
        {
            resStr.append(System.lineSeparator());
            resStr.append("day " + quintet.getDay());
            resStr.append(" hour " + quintet.getHour());
        }

        return resStr.toString();
    }


    private void initShowAsComboBox() {
        ObservableList<String> selectionList = FXCollections.observableArrayList("Raw", "Teacher", "Class");
        ShowAs_ComboBox.getItems().setAll(selectionList);
        ShowAs_ComboBox.getSelectionModel().selectFirst();
        ShowAs_ComboBox.onActionProperty().setValue(e ->
        {
            String choice = ShowAs_ComboBox.getValue();
            if (choice.equals("Raw")) {
                FilterBySpecificName_ComboBox.visibleProperty().setValue(false);
                setTable();
            }
            if (choice.equals("Teacher")) SetFiltered(m_Teachers);
            if (choice.equals("Class")) SetFiltered(m_StudyClasses);
        });

        FilterBySpecificName_ComboBox.onActionProperty().setValue(e -> setTable());
    }


    private void SetFiltered(List<String> list) {
        FilterBySpecificName_ComboBox.visibleProperty().setValue(true);
        FilterBySpecificName_ComboBox.getItems().clear();
        FilterBySpecificName_ComboBox.setItems(FXCollections.observableArrayList(list));
    }

    private void CreateTeachersAndClassesNameList(Map<Integer, Teacher> teachers, Map<Integer, StudyClass> studyClasses) {
        m_Teachers = new ArrayList<>();
        teachers.forEach((key1, value1) -> m_Teachers.add(value1.getName()));
        m_StudyClasses = new ArrayList<>();
        studyClasses.forEach((key, value) -> m_StudyClasses.add(value.getName()));
    }

    private void HideHeader(boolean val)
    {
        AtomicInteger x = new AtomicInteger(1);
        if(val == true)
        ViewTimeTable.getColumns().forEach(c->c.textProperty().setValue(""));
        else
            ViewTimeTable.getColumns().forEach(c->
            {
                c.textProperty().setValue("Day " + x);
                x.getAndIncrement();
            });
    }

    public void ClearTable() {
        ViewTimeTable.getItems().clear();
        ViewTimeTable.refresh();
    }

}
