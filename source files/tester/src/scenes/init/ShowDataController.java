package scenes.init;

import data.transfer.objects.DTOEngineInformation;
import data.transfer.objects.TimeTableInformation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import scenes.init.actions.SystemData;
import time.table.problem.objects.StudyClass;

public class ShowDataController {
    private static Timeline m_timeline;
    private static boolean m_isAnimOn;
    DTOEngineInformation m_engingInfoDto;
    TimeTableInformation m_TimeTableInfoDto;
    StringBuilder m_Sb = new StringBuilder();
    @FXML
    private TextFlow TextFlowPane;
    private Scene m_ShowDataScene;

    public static void StopAnimation(boolean isSelected) {
        m_isAnimOn = isSelected;
        if (m_timeline != null) {
            if (isSelected) {
                m_timeline.play();
            } else {

                m_timeline.pause();
            }
        }
    }

    public void initialize(SystemData i_systemData) {
        m_engingInfoDto = new DTOEngineInformation(i_systemData.getDTOEngine());
        m_TimeTableInfoDto = new TimeTableInformation(i_systemData.getTimeTable());
        StringBuilder m_Sb = new StringBuilder("");
        showInformation();

    }

    private void showInformation() {
        TextFlowPane.setTextAlignment(TextAlignment.LEFT);

        Text text1;


        //  m_Sb.append("-----------------------------");
        m_Sb.append("  Time Table Information");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("-----------------------------");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("Subjects:");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("-----------");

        m_Sb.append(System.lineSeparator());
        m_TimeTableInfoDto.getSubjects().values().forEach(subject ->
        {
            m_Sb.append(subject.getId()).append(" : ").append(subject.getName()).append(System.lineSeparator());
        });

        m_Sb.append(System.lineSeparator());
        m_Sb.append("Teachers:");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("-----------");
        m_Sb.append(System.lineSeparator());

        m_TimeTableInfoDto.getSubjects().values().forEach(teacher -> {
            m_Sb.append(teacher.getId()).append(" : ").append(teacher.getName()).append(System.lineSeparator());
        });
        m_Sb.append(System.lineSeparator());
        m_Sb.append("Classes:");
        m_Sb.append(System.lineSeparator());

        m_Sb.append("-----------");
        m_Sb.append(System.lineSeparator());


        m_TimeTableInfoDto.getStudyClasses().values().forEach(this::PrintStudyClass);
        m_Sb.append(System.lineSeparator());
        m_Sb.append("Rules:");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("-----------");
        m_Sb.append(System.lineSeparator());
        m_TimeTableInfoDto.getRules().values().forEach(t -> m_Sb.append(t.toString()));
        m_Sb.append(System.lineSeparator());
        m_Sb.append("==============================");
        printEngineInformation();
        m_Sb.append("==============================");
        m_Sb.append(System.lineSeparator());
        text1 = new Text(m_Sb.toString());


        double sceneWidth = m_ShowDataScene.getHeight();
        double msgWidth = text1.getLayoutBounds().getWidth();

        KeyValue initKeyValue = new KeyValue(text1.translateXProperty(), sceneWidth);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);

        KeyValue endKeyValue = new KeyValue(text1.translateXProperty(), 1.0 * msgWidth);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(3), endKeyValue);

        m_timeline = new Timeline(initFrame, endFrame);

        m_timeline.setCycleCount(Timeline.INDEFINITE);
        if (m_isAnimOn) {
            m_timeline.play();
        }
        TextFlowPane.getChildren().addAll(text1);
        TextFlowPane.setLineSpacing(15);

    }

    private void printEngineInformation() {
        m_Sb.append(System.lineSeparator());
        m_Sb.append("-----------------------------");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("  Engine Information");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("-----------------------------");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("Size of population:");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("--------------------");
        m_Sb.append(System.lineSeparator());
        m_Sb.append(m_engingInfoDto.getInitialPopulation());
        m_Sb.append(System.lineSeparator());
        m_Sb.append("Selection:");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("--------------------");
        m_Sb.append(System.lineSeparator());
        m_Sb.append(m_engingInfoDto.getSelection().getType().toString()).append(":").append(m_engingInfoDto.getSelection().getTopPercent()).append("%").append(System.lineSeparator());
        m_Sb.append(System.lineSeparator());
        m_Sb.append("CrossOver:");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("--------------------");
        m_Sb.append(System.lineSeparator());
        m_Sb.append(m_engingInfoDto.getCrossOver().toString());
        m_Sb.append(System.lineSeparator());
        m_Sb.append("Mutations:");
        m_Sb.append(System.lineSeparator());
        m_Sb.append("--------------------");
        m_Sb.append(System.lineSeparator());
        m_engingInfoDto.getMutations().forEach(mutation -> {
            if (mutation != null)
                m_Sb.append(mutation).append(System.lineSeparator());
        });


    }


    private void PrintStudyClass(StudyClass studyClass) {
        m_Sb.append(studyClass.getId()).append(" : ").append(studyClass.getName()).append(System.lineSeparator());
        studyClass.getRequirements().forEach(req ->
                m_Sb.append("Subject ").append(req.getSubject().toString()).append(" And ").append(req.getHours()).append("hours are required.").append(System.lineSeparator()));

    }


    public Scene getScene() {
        return m_ShowDataScene;
    }

    public void setScene(Scene i_scene) {
        m_ShowDataScene = i_scene;
    }

}
