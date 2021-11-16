package data.transfer.objects;

import time.table.problem.configurations.rules.Rule;

import java.util.List;
import java.util.Map;

public interface UIAdapter<T>
{
    void UpdateRulesPane(Map<Rule, Integer> Grades);
    void UpdateProgressbar(double Generation, double Fitness, double Time);
    void UpdatePopulationAndFitness(int PopulationInformation, int FitnessInformation);
    void UpdateBestTable(List<T> arrangeByHoursDays);

    void onFinish();
}
