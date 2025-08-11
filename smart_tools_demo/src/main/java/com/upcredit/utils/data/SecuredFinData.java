package com.upcredit.utils.data;

public class SecuredFinData {
    private double loanAmount;
    private int duration;
    private String loanPurpose;
    private double residentialAmount;
    private double expenditureAmount;
    private double houseValue;
    private String mortgageStatus;
    private double mortgageBalance;

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

    public double getHouseValue() {
        return houseValue;
    }

    public void setHouseValue(double houseValue) {
        this.houseValue = houseValue;
    }

    public String getMortgageStatus() {
        return mortgageStatus;
    }

    public void setMortgageStatus(String mortgageStatus) {
        this.mortgageStatus = mortgageStatus;
    }

    public double getMortgageBalance() {
        return mortgageBalance;
    }

    public void setMortgageBalance(double mortgageBalance) {
        this.mortgageBalance = mortgageBalance;
    }
}