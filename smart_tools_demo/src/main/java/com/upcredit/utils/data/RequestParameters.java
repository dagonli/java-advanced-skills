package com.upcredit.utils.data;

import java.util.List;

public class RequestParameters {
    private String partnerUserNo;
    private String productCode;
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String birthDate;
    private String emailAddress;
    private String employmentStatus;
    private String residentialStatus;
    private double incomeAmount;
    private double householdIncome;
    private boolean cashAdvance;
    private List<Address> addressList;
    private UnSecuredFinData unSecuredFinData;
    private SecuredFinData securedFinData;
    private EmploymentInfo employmentInfo;
    private DependentInfo dependentInfo;
    private OpenBankingInfo openBankingInfo;
    private AdditionalInfo additionalInfo;

    public String getPartnerUserNo() {
        return partnerUserNo;
    }

    public void setPartnerUserNo(String partnerUserNo) {
        this.partnerUserNo = partnerUserNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getResidentialStatus() {
        return residentialStatus;
    }

    public void setResidentialStatus(String residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    public double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public double getHouseholdIncome() {
        return householdIncome;
    }

    public void setHouseholdIncome(double householdIncome) {
        this.householdIncome = householdIncome;
    }

    public boolean isCashAdvance() {
        return cashAdvance;
    }

    public void setCashAdvance(boolean cashAdvance) {
        this.cashAdvance = cashAdvance;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public UnSecuredFinData getUnSecuredFinData() {
        return unSecuredFinData;
    }

    public void setUnSecuredFinData(UnSecuredFinData unSecuredFinData) {
        this.unSecuredFinData = unSecuredFinData;
    }

    public SecuredFinData getSecuredFinData() {
        return securedFinData;
    }

    public void setSecuredFinData(SecuredFinData securedFinData) {
        this.securedFinData = securedFinData;
    }

    public EmploymentInfo getEmploymentInfo() {
        return employmentInfo;
    }

    public void setEmploymentInfo(EmploymentInfo employmentInfo) {
        this.employmentInfo = employmentInfo;
    }

    public DependentInfo getDependentInfo() {
        return dependentInfo;
    }

    public void setDependentInfo(DependentInfo dependentInfo) {
        this.dependentInfo = dependentInfo;
    }

    public OpenBankingInfo getOpenBankingInfo() {
        return openBankingInfo;
    }

    public void setOpenBankingInfo(OpenBankingInfo openBankingInfo) {
        this.openBankingInfo = openBankingInfo;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    // Getters and Setters
    // toString &#x65B9;&#x6CD5;&#x53EF;&#x4EE5;&#x7701;&#x7565;&#xFF0C;&#x6216;&#x81EA;&#x5B9A;&#x4E49;&#x8F93;&#x51FA;&#x65B9;&#x4FBF;&#x8C03;&#x8BD5;
}

