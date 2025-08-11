package com.upcredit.utils.data;

public class ResponseParameters {
    private String applyStatus;
    private String loanAmount;
    private String duration;
    private double apr;
    private boolean aprGuaranteed;
    private double interestRate;
    private double arrangementFee;
    private double total_repayable;
    private String approvalStatus;
    private int eligibility;
    private String webApplyUrl;
    private String h5ApplUrl;

    // Getters and Setters
    public String getApplyStatus() { return applyStatus; }
    public void setApplyStatus(String applyStatus) { this.applyStatus = applyStatus; }

    public String getLoanAmount() { return loanAmount; }
    public void setLoanAmount(String loanAmount) { this.loanAmount = loanAmount; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public double getApr() { return apr; }
    public void setApr(double apr) { this.apr = apr; }

    public boolean isAprGuaranteed() { return aprGuaranteed; }
    public void setAprGuaranteed(boolean aprGuaranteed) { this.aprGuaranteed = aprGuaranteed; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public double getArrangementFee() { return arrangementFee; }
    public void setArrangementFee(double arrangementFee) { this.arrangementFee = arrangementFee; }

    public double getTotal_repayable() { return total_repayable; }
    public void setTotal_repayable(double total_repayable) { this.total_repayable = total_repayable; }

    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }

    public int getEligibility() { return eligibility; }
    public void setEligibility(int eligibility) { this.eligibility = eligibility; }

    public String getWebApplyUrl() { return webApplyUrl; }
    public void setWebApplyUrl(String webApplyUrl) { this.webApplyUrl = webApplyUrl; }

    public String getH5ApplUrl() { return h5ApplUrl; }
    public void setH5ApplUrl(String h5ApplUrl) { this.h5ApplUrl = h5ApplUrl; }

    @Override
    public String toString() {
        return "ResponseParameters{" +
                "applyStatus='" + applyStatus + '\'' +
                ", loanAmount='" + loanAmount + '\'' +
                ", duration='" + duration + '\'' +
                ", apr=" + apr +
                ", aprGuaranteed=" + aprGuaranteed +
                ", interestRate=" + interestRate +
                ", arrangementFee=" + arrangementFee +
                ", total_repayable=" + total_repayable +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", eligibility=" + eligibility +
                ", webApplyUrl='" + webApplyUrl + '\'' +
                ", h5ApplUrl='" + h5ApplUrl + '\'' +
                '}';
    }
}