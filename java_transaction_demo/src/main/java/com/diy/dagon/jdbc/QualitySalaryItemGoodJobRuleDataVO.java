package com.diy.dagon.jdbc;


import java.util.List;

/**
 * 客户薪金资质 好单位规则配置
 *
 *
 * @author liyu
 * @date 2022-04-16
 */
public class QualitySalaryItemGoodJobRuleDataVO{

    private static final long serialVersionUID = 4749381204016811852L;

    /**
     * 是否有薪金
     */
    private List<String> isSalaryList;

    /**
     * 单位类型
     */
    private List<String> jobTypeList;

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

    public List<String> getJobTypeList() {
        return jobTypeList;
    }

    public void setJobTypeList(List<String> jobTypeList) {
        this.jobTypeList = jobTypeList;
    }

    public List<String> getAuthSourceList() {
        return authSourceList;
    }

    public void setAuthSourceList(List<String> authSourceList) {
        this.authSourceList = authSourceList;
    }
}