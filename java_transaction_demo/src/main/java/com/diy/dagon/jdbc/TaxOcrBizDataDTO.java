package com.diy.dagon.jdbc;


import java.util.List;


/**
 * 学历ocr识别明细信息
 * @author admin
 * @date 2019/12/9.
 **/
public class TaxOcrBizDataDTO{

    private static final long serialVersionUID = 2632663584655210334L;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobileNo;
    /**
     * 受雇日期
     */
    private String hireDate;
    /**
     * 今年年收入
     */
    private String curYearIncome;
    /**
     * 最近月收入
     */
    private String latestMonIncome;
    /**
     * 最近缴纳月份
     * 2022-04
     */
    private String lastMonth;
    /**
     * 受雇公司
     */
    private String companyName;
    /**
     * 今年度最近月份
     * 04
     */
    private String curMth;
    /**
     * 去年最近月份
     * 04
     */
    private String lstMth;
    /**
     * 去年年收入合计
     */
    private String lstYearIncome;
    /**
     * 其它扣除金额
     */
    private String taxOcrOtherTaxPay;


    /**
     * 用户个税图片id
     * 用户完成个税认证时有值
     */
    private String taxImgId;


    public String getCurMth() {
        return curMth;
    }

    public void setCurMth(String curMth) {
        this.curMth = curMth;
    }

    public String getLstMth() {
        return lstMth;
    }

    public void setLstMth(String lstMth) {
        this.lstMth = lstMth;
    }

    public String getLstYearIncome() {
        return lstYearIncome;
    }

    public void setLstYearIncome(String lstYearIncome) {
        this.lstYearIncome = lstYearIncome;
    }

    public String getTaxImgId() {
        return taxImgId;
    }

    public void setTaxImgId(String taxImgId) {
        this.taxImgId = taxImgId;
    }

    public String getTaxOcrOtherTaxPay() {
        return taxOcrOtherTaxPay;
    }

    public void setTaxOcrOtherTaxPay(String taxOcrOtherTaxPay) {
        this.taxOcrOtherTaxPay = taxOcrOtherTaxPay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getCurYearIncome() {
        return curYearIncome;
    }

    public void setCurYearIncome(String curYearIncome) {
        this.curYearIncome = curYearIncome;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLatestMonIncome() {
        return latestMonIncome;
    }

    public void setLatestMonIncome(String latestMonIncome) {
        this.latestMonIncome = latestMonIncome;
    }

    public String getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(String lastMonth) {
        this.lastMonth = lastMonth;
    }
}
