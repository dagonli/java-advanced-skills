package com.upcredit.utils.data;

public class UnSecuredFinData {
    private double loanAmount;
    private int duration;
    private String loanPurpose;
    private double residentialAmount;
    private double expenditureAmount;

    // Getters and Setters

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public double getResidentialAmount() {
        return residentialAmount;
    }

    public void setResidentialAmount(double residentialAmount) {
        this.residentialAmount = residentialAmount;
    }

    public double getExpenditureAmount() {
        return expenditureAmount;
    }

    public void setExpenditureAmount(double expenditureAmount) {
        this.expenditureAmount = expenditureAmount;
    }
}