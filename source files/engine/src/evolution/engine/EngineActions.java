package evolution.engine;

import java.io.IOException;

public interface EngineActions<T>
{
    int i_numberOfGenerations = 0;
    void ReadFromFile() throws Exception;
    void ExecuteEvolution() throws Exception;

    void ShowDataInformation() throws Exception;
    void showProcess() throws Exception;
    void ShowBestSolution() throws Exception;
    void SaveSystemToFile() throws Exception;
    void LoadFileToSystem() throws IOException;

}
