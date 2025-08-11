package com.upcredit.utils;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upcredit.utils.data.BizDataWrapper;
import com.upcredit.utils.data.RequestParameters;
import com.upcredit.utils.data.ResponseParameters;

public class JsonRequestParser {
    public static void main(String[] args) throws Exception {
        String requestParamaters = "{\"partnerUserNo\":\"UR6582321817442062338\",\"productCode\":\"UpCredit-PL\",\"title\":\"Mr\",\"firstName\":\"John\",\"middleName\":\"Michael\",\"lastName\":\"Doe\",\"birthDate\":\"1990-01-01\",\"emailAddress\":\"john.doe@example.com\",\"employmentStatus\":\"FT_EMPLOYED\",\"residentialStatus\":\"HOMEOWNER\",\"incomeAmount\":50000,\"householdIncome\":55000,\"cashAdvance\":true,\"addressList\":[{\"houseName\":\"Maple Cottage\",\"houseNumber\":\"123\",\"flatNumber\":\"4B\",\"streetName\":\"Main Street\",\"townName\":\"London\",\"countryName\":\"UK\",\"postcode\":\"SW1A1AA\",\"yearsAtAddress\":2,\"monthsAtAddress\":6,\"moveInDate\":\"2018-01-01\"}],\"unSecuredFinData\":{\"loanAmount\":10000,\"duration\":36,\"loanPurpose\":\"debt\",\"residentialAmount\":1200,\"expenditureAmount\":2000},\"securedFinData\":{\"loanAmount\":20000,\"duration\":120,\"loanPurpose\":\"home-improvements\",\"residentialAmount\":1500,\"expenditureAmount\":2500,\"houseValue\":300000,\"mortgageStatus\":\"Yes\",\"mortgageBalance\":150000},\"employmentInfo\":{\"occupation\":\"Engineer\",\"occupationId\":\"ENG001\",\"industry\":\"Engineering\",\"industryId\":\"IND001\",\"employer\":\"ABC Corp\"},\"dependentInfo\":{\"householdDependents\":2,\"dependantsCosts\":500},\"openBankingInfo\":{\"provider\":\"data_one\",\"report_id\":\"RPT123456\"},\"additionalInfo\":{\"partnerExtData\":\"{\\\"customField\\\":\\\"value\\\"}\"}}";

        RequestParameters requestParameters1 = JSONObject.parseObject(requestParamaters, RequestParameters.class);

        System.err.println(requestParameters1.toString());

        String responseData = "{\"applyStatus\":\"PS\",\"loanAmount\":\"10000\",\"duration\":\"36\",\"apr\":5.9,\"aprGuaranteed\":true,\"interestRate\":0.059,\"arrangementFee\":200,\"total_repayable\":304.15,\"approvalStatus\":\"PREAPPROVED\",\"eligibility\":100,\"webApplyUrl\":\"https://example.com/apply\",\"h5ApplUrl\":\"https://m.example.com/apply\"}";

        ResponseParameters responseParameters = JSONObject.parseObject(responseData, ResponseParameters.class);
        System.err.println(responseParameters.toString());


    }
}