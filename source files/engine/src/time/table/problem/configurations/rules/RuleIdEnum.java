package time.table.problem.configurations.rules;

import evolution.engine.Individual;
import javafx.util.Pair;
import time.table.problem.Quintet;
import time.table.problem.QuintetFactory;
import time.table.problem.objects.Requirement;
import time.table.problem.objects.StudyClass;
import time.table.problem.objects.Subject;
import time.table.problem.objects.Teacher;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RuleIdEnum {
    //A teacher is only a human being.
    // It is not possible that he teaches at the same
    // time and today in several classes
    TeacherIsHuman {
        public <T> int Eval(Individual<T> i_Individual, int i_totalHour) {
            int res = i_Individual.getGeneListLength();
            int maxDays = ((QuintetFactory) i_Individual.getIndividualFactory()).getTotalDays();
            int maxHours = ((QuintetFactory) i_Individual.getIndividualFactory()).getTotalHours();
            List<Teacher>[][] TeacherArr = new ArrayList[maxHours][maxDays];
            List<Quintet> list = (List<Quintet>) i_Individual.getListOfGenes();

            for (Quintet quintet : list) {
                List<Teacher> curTeacher = TeacherArr[quintet.getTotalHours() - 1][quintet.getDay() - 1];

                if (curTeacher == null) {
                    TeacherArr[quintet.getTotalHours() - 1][quintet.getDay() - 1] = new ArrayList<>();
                    TeacherArr[quintet.getTotalHours() - 1][quintet.getDay() - 1].add(quintet.getTeacher());
                } else {
                    if (curTeacher.contains(quintet.getTeacher())) {
                        res--;
                    } else {
                        curTeacher.add(quintet.getTeacher());
                    }
                }
            }
            return (int) (((float) res / (float) i_Individual.getGeneListLength()) * 100f);
        }
    },
    //At the time and day graduating in a particular class can teach at most one teacher one subject (or not at all).
    Singularity {
        public <T> int Eval(Individual<T> i_Individual, int i_totalHour) {
            int res = i_Individual.getGeneListLength();
            int maxDays = ((QuintetFactory) i_Individual.getIndividualFactory()).getTotalDays();
            int maxHours = ((QuintetFactory) i_Individual.getIndividualFactory()).getTotalHours();
            List<StudyClass>[][] classArr = new ArrayList[maxHours][maxDays];
            List<Quintet> list = (List<Quintet>) i_Individual.getListOfGenes();

            for (Quintet quintet : list) {
                List<StudyClass> curClass = classArr[quintet.getTotalHours() - 1][quintet.getDay() - 1];

                if (curClass == null) {
                    classArr[quintet.getTotalHours() - 1][quintet.getDay() - 1] = new ArrayList<>();
                    classArr[quintet.getTotalHours() - 1][quintet.getDay() - 1].add(quintet.getStudyClass());
                } else {
                    if (curClass.contains(quintet.getStudyClass())) {
                        res--;
                    } else {
                        curClass.add(quintet.getStudyClass());
                    }
                }
            }
            return (int) (((float) res / (float) i_Individual.getGeneListLength()) * 100f);
        }
    },
    //A teacher teaches only the professions for which he is qualified
    Knowledgeable {
        public <T> int Eval(Individual<T> i_Individual, int i_totalHour) {
            int res = i_Individual.getGeneListLength();
            List<Quintet> list = (List<Quintet>) i_Individual.getListOfGenes();

            for (Quintet quintet : list) {

                if (!quintet.getTeacher().isTeachingSubject(quintet.getSubject())) {
                    res--;
                }
            }

            return (int) (((float) res / (float) i_Individual.getGeneListLength()) * 100f);
        }
    },

    //A class receives exactly the stock of study hours it needs for each and every subject
    Satisfactory {
        public <T> int Eval(Individual<T> i_Individual, int i_totalHour) {
            int res = i_Individual.getGeneListLength();
            int maxStudyClasses = ((QuintetFactory) i_Individual.getIndividualFactory()).getTotalNumberOfClasses();
            Map<Integer, StudyClass> studyClassesById = ((QuintetFactory) i_Individual.getIndividualFactory()).getStudyClasses();
            List<Quintet> quintets = (List<Quintet>) i_Individual.getListOfGenes();

            for (StudyClass sClass : studyClassesById.values()) {
                List<Requirement> sClassReqs = sClass.getRequirements();
                Map<Subject, Integer> checkingReqs = new HashMap<>(sClassReqs.size());
                for (Requirement classReq : sClassReqs) {
                    checkingReqs.put(classReq.getSubject(), classReq.getHours());
                }

                Stream<Quintet> sClassQuintets = quintets.stream().filter(t -> t.getStudyClass().equals(sClass));
                sClassQuintets.filter(quintet -> checkingReqs.containsKey(quintet.getSubject())).forEach(quintet -> {
                    checkingReqs.replace(quintet.getSubject(), checkingReqs.get(quintet.getSubject()) - 1);
                });

                for (int hoursSubjectBeingTaught : checkingReqs.values()) {
                    if (hoursSubjectBeingTaught != 0) {
                        res--;
                    }
                }


            }
            return (int) (((float) res / (float) i_Individual.getGeneListLength()) * 100f);
        }


    },

    //Done:it was for Ex2
    //Teacher has at least one day off where she does not study at all.
    DayOffTeacher {
        public <T> int Eval(Individual<T> i_Individual, int i_totalHour) {
            int res = i_Individual.getGeneListLength();
            int maxDays = ((QuintetFactory) i_Individual.getIndividualFactory()).getTotalDays();

            Map<Integer, Teacher> teachers = ((QuintetFactory) i_Individual.getIndividualFactory()).getTeachers();
            int maxTeachers = teachers.size();
            List<Quintet> listOfGenes = (List<Quintet>) i_Individual.getListOfGenes();
            Map<Teacher, Boolean[]> dayTeacherOff = new HashMap<>(maxTeachers);

            for (Teacher sTeacher : teachers.values()) {
                Boolean[] days = new Boolean[maxDays];
                Arrays.fill(days, false);
                dayTeacherOff.put(sTeacher, days);
            }

            for (Quintet quintet : listOfGenes) {
                dayTeacherOff.get(quintet.getTeacher())[quintet.getDay()] = true;
            }

            res = CountOffDays(res, dayTeacherOff);

            return (int) (((float) res / (float) i_Individual.getGeneListLength()) * 100f);
        }
    },

    //Done this is for Ex3
    //The class has at least one day off where she does not study at all.
    DayOffClass {
        public <T> int Eval(Individual<T> i_Individual, int i_totalHour) /*throws Exception*/ {
            int res = i_Individual.getGeneListLength();
            int maxDays = ((QuintetFactory) i_Individual.getIndividualFactory()).getTotalDays();
            int maxStudyClasses = ((QuintetFactory) i_Individual.getIndividualFactory()).getTotalNumberOfClasses();
            Map<Integer, StudyClass> studyClasses = ((QuintetFactory) i_Individual.getIndividualFactory()).getStudyClasses();
            List<Quintet> listOfGenes = (List<Quintet>) i_Individual.getListOfGenes();
            Map<StudyClass, Boolean[]> dayClassOff = new HashMap<>(maxStudyClasses);

            for (StudyClass sClass : studyClasses.values()) {
                Boolean[] days = new Boolean[maxDays]; //new Boolean[sClass.getRequirementHoursSum()];
                Arrays.fill(days, false);
                dayClassOff.put(sClass, days);
            }

            for (Quintet quintet : listOfGenes) {
                dayClassOff.get(quintet.getStudyClass())[quintet.getDay()] = true;
            }

            res = CountOffDays(res, dayClassOff);
            return (int) (((float) res / (float) i_Individual.getGeneListLength()) * 100f);


        }
    },

    //A subject is not taught for more than a continuous number of hours. The number of hours will be given as an external parameter.
    //Calculating the sequence of hours of course speaks as part of a particular day (rather than crossing days).
    //The parameter in a member named TotalHours.
    //we put every quintet in the time table organized by days X hours.
    //and then we check if there is a subject which is being taught continuously over i_totalHours
    Sequentiality {
        public <T> int Eval(Individual<T> i_Individual, int i_totalHour) {
            int res = i_Individual.getGeneListLength();
            int maxDays = ((QuintetFactory) i_Individual.getIndividualFactory()).getTotalDays();
            int maxHours = ((QuintetFactory) i_Individual.getIndividualFactory()).getTotalHours();
            List<Pair<StudyClass, Subject>>[][] subjectsByClassArr = new ArrayList[maxDays][maxHours];
            List<Quintet> quintets = (List<Quintet>) i_Individual.getListOfGenes();

            for (Quintet quintet : quintets) {
                List<Pair<StudyClass, Subject>> currentSubjectsByClass = subjectsByClassArr[quintet.getDay() - 1][quintet.getHour() - 1];

                if (currentSubjectsByClass == null) {
                    subjectsByClassArr[quintet.getDay() - 1][quintet.getHour() - 1] = new ArrayList<>();
                }
                subjectsByClassArr[quintet.getDay() - 1][quintet.getHour() - 1].add(
                        new Pair<StudyClass, Subject>(quintet.getStudyClass(), quintet.getSubject()));
            }
            Pair<StudyClass, Subject> next = null, current = null;
            int hoursForSub = 0;
            for (int i = 0; i < maxDays; i++) {

                List<Pair<StudyClass, Subject>>[] days = subjectsByClassArr[i];
                for (List<Pair<StudyClass, Subject>> day : days) {
                    hoursForSub = 0;
                    if (day != null) {
                        for (int j = 0; j < day.size() - 1; j++) {

                            current = day.get(j);
                            next = day.get(j + 1);
                            if (current.getValue().equals(next.getValue())) {
                                if (current.getKey() == next.getKey()) {
                                    if (hoursForSub == 0) {
                                        hoursForSub += 2;
                                    } else {
                                        hoursForSub++;
                                    }
                                } else {
                                    hoursForSub = 0;
                                }
                            } else {
                                hoursForSub = 0;
                            }

                            if (hoursForSub > i_totalHour) {
                                res -= hoursForSub;//relax the res referenced by hoursForSub
                            }
                        }
                    }
                }
            }
            return (int) (((float) res / (float) i_Individual.getGeneListLength()) * 100f);
        }


    };
    //for EX3
//Each teacher is assigned a desired amount of hours to work during the entire work week.
//The number of hours is defined in the XML file as part of each teacher's information.
  /* WorkingHoursPreference {
        public <T> int Eval(Individual<T> individual) {
            return m_MaxFitness;
        }
    };
*/
    private final static int m_MaxFitness = 100;

    //This method is for DayOffClass/Teacher checking that each of the Class/Teacher has at least one day off.
//counting how many days they don't have a day off and then compare it to how many days they learn in a week.
    private static <T> int CountOffDays(int i_Eval, Map<T, Boolean[]> i_DaysOff) {
        int counter = 0;
        for (Boolean[] Days : i_DaysOff.values()) {
            counter = 0;
            for (int i = 1; i < Days.length; i++) {
                try {
                    if (Days[i]) {
                        counter++;
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (counter == Days.length) {
                i_Eval--;
            }
        }
        return i_Eval;
    }

    public static int getMaxEval() {
        return m_MaxFitness;
    }

    public abstract <T> int Eval(Individual<T> i_Individual, int i_TotalHour);
}
