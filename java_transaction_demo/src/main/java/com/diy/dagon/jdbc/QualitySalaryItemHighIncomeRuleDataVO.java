package com.diy.dagon.jdbc;



import java.util.List;

/**
 * 客户薪金资质 高收入规则配置
 *
 *
 * @author liyu
 * @date 2022-04-16
 */
public class QualitySalaryItemHighIncomeRuleDataVO  {

    private static final long serialVersionUID = 4749381204016811852L;

    /**
     * 是否有薪金
     */
    private List<String> isSalaryList;

    /**
     * 薪金水平
     */
    private List<String> highIncomeLevelList;

    /**
     * 薪金认证来源
     */
    private List<String> authSourceList;

    public List<String> getIsSalaryList() {
        return isSalaryList;
    }

    public void setIsSalaryList(List<String> isSalaryList) {
        this.isSalaryList = isSalaryList;
    }

    public List<String> getHighIncomeLevelList() {
        return highIncomeLevelList;
    }

    public void setHighIncomeLevelList(List<String> highIncomeLevelList) {
        this.highIncomeLevelList = highIncomeLevelList;
    }

    public List<String> getAuthSourceList() {
        return authSourceList;
    }

    public void setAuthSourceList(List<String> authSourceList) {
        this.authSourceList = authSourceList;
    }
}