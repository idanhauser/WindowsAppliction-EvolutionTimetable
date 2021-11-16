package time.table.problem.configurations.rules;

import evolution.engine.Individual;
import time.table.problem.jaxb.schema.generated.ETTRule;

import java.io.Serializable;
import java.util.Objects;

public class Rule implements Serializable {
    private static final long serialVersionUID = 100L;
    private int m_HardRulesWeight;
    private RuleTypeEnum m_Type;
    private RuleIdEnum m_RuleId;
    private int m_TotalHours;
    private int m_MaxEval;

    public Rule(ETTRule ettRule, int hardRulesWeight) {
        m_HardRulesWeight = hardRulesWeight;
        m_Type = RuleTypeEnum.valueOf(ettRule.getType());
        m_RuleId = RuleIdEnum.valueOf(ettRule.getETTRuleId());
        m_MaxEval = RuleIdEnum.getMaxEval();
        m_TotalHours = -1;

    }

    public void setTotalHours(int i_TotalHours) {
        this.m_TotalHours = i_TotalHours;
    }

    public void SetConfiguration(String i_ConfigurationStr) {
        int indexEquals = i_ConfigurationStr.indexOf("=");
        String totalHoursStr = i_ConfigurationStr.substring(indexEquals + 1);
        int totalHours = Integer.parseInt(totalHoursStr);
        setTotalHours(totalHours);
    }

    public <T> int Eval(Individual<T> i_individual) {
        int res;
        try {
            res = m_RuleId.Eval(i_individual, m_TotalHours);
        } catch (Exception e) {
            e.printStackTrace();
            res = m_RuleId.Eval(i_individual, m_TotalHours);
        }

        if (m_Type == RuleTypeEnum.Hard) {
            res = (int) ((float) res * (m_HardRulesWeight / 100f));
        } else {
            res = (int) ((float) res * (1 - m_HardRulesWeight / 100f));
        }

        return res;
    }

    public int GetRulesWeight() {
        return m_HardRulesWeight;
    }

    public void SetRulesWeight(int m_rulesWeight) {
        m_HardRulesWeight = m_rulesWeight;
    }

    public RuleTypeEnum GetType() {
        return m_Type;
    }

    public RuleIdEnum GetRuleId() {
        return m_RuleId;
    }

    public int getMaxEval() {
        return m_MaxEval;
    }

    public RuleTypeEnum getType() {
        return m_Type;
    }

    @Override
    public String toString() {
        return "Rule " + this.m_RuleId.toString() + ", " + "type : " + this.m_Type + System.lineSeparator();
    }

    public void SetRuleId(RuleIdEnum i_RuleId) {
        m_RuleId = i_RuleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return m_Type == rule.m_Type && m_RuleId == rule.m_RuleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_Type, m_RuleId);
    }

    public enum RuleTypeEnum {
        Hard,
        Soft
    }
}
