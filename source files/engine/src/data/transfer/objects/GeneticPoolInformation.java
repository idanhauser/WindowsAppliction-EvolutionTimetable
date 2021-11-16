package data.transfer.objects;
import evolution.engine.Population;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.Map;

public class GeneticPoolInformation<T> implements Serializable {
    private static final long serialVersionUID = 100L;

    private Map<Integer, Pair<Integer, Population>> m_historyOfGenerations;
    private int m_gensToShow;

    public GeneticPoolInformation() {
    }

    public Map<Integer, Pair<Integer, Population>> getHistoryOfGenerations() {
        return m_historyOfGenerations;
    }

    public void setHistoryOfGenerations(Map<Integer, Pair<Integer, Population>> m_historyOfGenerations) {
        this.m_historyOfGenerations = m_historyOfGenerations;
    }

    public int getGensToShow() {
        return m_gensToShow;
    }

    public void setGensToShow(int m_gensToShow) {
        this.m_gensToShow = m_gensToShow;
    }
}

