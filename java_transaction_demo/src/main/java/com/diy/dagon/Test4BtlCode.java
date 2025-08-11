package com.diy.dagon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diy.dagon.jdbc.CustGroupWelfareCouponDetectDTO;
import com.diy.dagon.jdbc.QualitySalaryItemGoodJobRuleDataVO;
import com.diy.dagon.jdbc.QualitySalaryItemHighIncomeRuleDataVO;
import com.diy.dagon.jdbc.TaxOcrBizDataDTO;
import netscape.javascript.JSObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Description TODO
 * @Date 2022/4/16 11:41
 * @Author by liyu-jk
 */
public class Test4BtlCode {
    public static void main(String[] args) {

        System.err.println(Boolean.TRUE.equals(null));

        String couponDistributeInfo = "";
        List<CustGroupWelfareCouponDetectDTO> custGroupWelfareCouponDetectDTOS =
                JSONArray.parseArray(couponDistributeInfo, CustGroupWelfareCouponDetectDTO.class);

        System.out.println(custGroupWelfareCouponDetectDTOS);

        String bizData = "3|OHrAIbOU4osSb9UWvl6lnuZAd8cUklrFLFDYb+6Ij0zuT1ejLlFsyX15bZNj/0UQu/aAYIwQtVnX+LbUMhUeiCvXpe8zC1YKURvqzIqqfK3WlC8MoeO3B0OVbuMUamqwjaO4asbx0bU07y3ZQSSV6nP0ZVmkiecfV3eG7ThImK8F5Wbqzj5tJ/KV9tghpUTGD9VD2OKaVqFYJhHCCWRVQiPphvMSElsOTpDzHfawApBoaST6adXKDUgV62ZlEaM9nGUyfpwbSfMlH9Xp99I6Rlp0F7HjyxPDY0NbiPfQMDu6zY5fXZhkY0vS9opPYn15mi5bvoIKw1qgfLtOsfE8DUDA1jM/WhvQVfrgRmHhvBPdx8toyHiTViO2M+QbFBtR3KKvekMuEB4BrjkM75bE/w==";

        TaxOcrBizDataDTO taxOcrBizDataDTO = JSONObject.parseObject(bizData, TaxOcrBizDataDTO.class);

        //System.out.println(taxOcrBizDataDTO);





        List<String> authSourceList = new ArrayList<>();
        String join = String.join(",", authSourceList);
        System.out.println(join);

        BigDecimal bigDecimal = new BigDecimal("5000");

        //evaluateIncomeLevel(Arrays.asList(bigDecimal));


        QualitySalaryItemHighIncomeRuleDataVO ruleDataVO = new QualitySalaryItemHighIncomeRuleDataVO();

        ruleDataVO.setAuthSourceList(Arrays.asList("SOCIAL_OCR","TAX"));
        ruleDataVO.setHighIncomeLevelList(Arrays.asList("A", "B", "C"));

        System.out.println(JSON.toJSONString(ruleDataVO));

        String rule = JSON.toJSONString(ruleDataVO);
        QualitySalaryItemHighIncomeRuleDataVO ruleDataVO1 = JSONObject.parseObject(rule, QualitySalaryItemHighIncomeRuleDataVO.class);
        System.out.println(ruleDataVO1);

        QualitySalaryItemGoodJobRuleDataVO jobRuleDataVO = new QualitySalaryItemGoodJobRuleDataVO();
        jobRuleDataVO.setAuthSourceList(Arrays.asList("SOCIAL_OCR","TAX"));
        jobRuleDataVO.setJobTypeList(Arrays.asList("医护","国企"));

        System.out.println(JSON.toJSONString(jobRuleDataVO));






    }


    public static String evaluateIncomeLevel(List<BigDecimal> incomeList) {
        Integer incomeBase = 7500;
        Optional<BigDecimal> bigDecimalMaxOp = incomeList.stream().max(BigDecimal::compareTo);
        if(!bigDecimalMaxOp.isPresent()){
            return null;
        }
        BigDecimal maxIncome = bigDecimalMaxOp.get();
        BigDecimal incomeBaseDecimal = new BigDecimal(incomeBase);
        BigDecimal doubleIncomeBase = incomeBaseDecimal.multiply(BigDecimal.valueOf(2));
        BigDecimal trebleIncomeBase = incomeBaseDecimal.multiply(BigDecimal.valueOf(3));
        if(maxIncome.compareTo(BigDecimal.ZERO) > 0 && maxIncome.compareTo(incomeBaseDecimal) < 0){
            return  "QualityHighIncomeLevelEnum.F.getCode()";
        }else if(maxIncome.compareTo(incomeBaseDecimal) >= 0 && maxIncome.compareTo(doubleIncomeBase) < 0){
            return  "QualityHighIncomeLevelEnum.A.getCode()";
        }else if(maxIncome.compareTo(doubleIncomeBase) >= 0 && maxIncome.compareTo(trebleIncomeBase) < 0){
            return  "QualityHighIncomeLevelEnum.B.getCode()";
        }else if(maxIncome.compareTo(trebleIncomeBase) >= 0){
            return  "QualityHighIncomeLevelEnum.C.getCode()";
        }
        return null;
    }


}
