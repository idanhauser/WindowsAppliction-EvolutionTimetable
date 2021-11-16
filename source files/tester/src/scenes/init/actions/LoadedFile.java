package scenes.init.actions;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import time.table.problem.LoadData;

import java.io.File;

public class LoadedFile
{
    File m_file = null;
    final StringProperty m_title = new SimpleStringProperty();
    boolean m_isLoaded;

    public LoadedFile(StringProperty i_title)
    {
        m_title.setValue("");
        i_title.bind(m_title);
    }

    public SystemData setFile(File i_file) throws Exception {
        m_file = i_file;
        m_title.setValue(m_file.getName());
        LoadData data = new LoadData(i_file.getPath());
        return new SystemData(data);
    }

    public boolean isLoaded() {     return m_isLoaded;    }
}
