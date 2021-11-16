package scenes.init.actions;

import data.transfer.objects.UIAdapter;
import javafx.application.Platform;
import time.table.problem.configurations.rules.Rule;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UIAdapterImpl implements UIAdapter
{
    private Runnable OnFinish;
    private Consumer<Double> GenerationProgressbar;
    private Consumer<Double> FitnessProgressbar;
    private Consumer<Double> TimeProgressbar;
    private Consumer<Integer> PopulationUpdate;
    private Consumer<Integer> FitnessUpdate;
    private Consumer<List> TableUpdate;
    private Consumer<Map<Rule, Integer>> updateRules;

    public UIAdapterImpl(Consumer<List> TableUpdate,
                         Consumer<Double> GenerationProgressbar, Consumer<Double> FitnessProgressbar, Consumer<Double> TimeProgressbar,
                         Consumer<Integer> PopulationUpdate, Consumer<Integer> FitnessUpdate, Consumer<Map<Rule, Integer>> ruleConsumer, Runnable onFinish)
    {
        this.TableUpdate = TableUpdate;
        this.GenerationProgressbar = GenerationProgressbar;
        this.FitnessProgressbar = FitnessProgressbar;
        this.TimeProgressbar = TimeProgressbar;
        this.FitnessUpdate = FitnessUpdate;
        this.PopulationUpdate = PopulationUpdate;
        this.updateRules = ruleConsumer;
        this.OnFinish = onFinish;
    }


    @Override
    public void UpdateRulesPane(Map Grades)
    {
        Platform.runLater(()->{
            updateRules.accept(Grades);
        });
    }

    @Override
    public void UpdateProgressbar(double Generation, double Fitness, double Time)
    {
        Platform.runLater(()->{
            GenerationProgressbar.accept(Generation);
            FitnessProgressbar.accept(Fitness);
            TimeProgressbar.accept(Time);
        });
    }

    @Override
    public void UpdatePopulationAndFitness(int PopulationInformation, int FitnessInformation)
    {
        Platform.runLater(()->PopulationUpdate.accept(PopulationInformation));
        Platform.runLater(()->FitnessUpdate.accept(FitnessInformation));
    }

    @Override
    public void UpdateBestTable(List arrangeByHoursDays)
    {
        Platform.runLater(()->TableUpdate.accept(arrangeByHoursDays));
    }

    @Override
    public void onFinish()
    {
        Platform.runLater(()->OnFinish.run());
    }

}
