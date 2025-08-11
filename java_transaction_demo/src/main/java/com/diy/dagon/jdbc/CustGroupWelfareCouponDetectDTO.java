package com.diy.dagon.jdbc;


import java.util.List;

/**
 * 优质客群优惠券福利类型探测 DTO
 *
 * @author liyu-jk
 * @date 2022/04/17
 */
public class CustGroupWelfareCouponDetectDTO {

    private static final long serialVersionUID = 1734817664971278218L;

    /**
     * 福利模式
     */
    private String welfareMode;

    /**
     * 击中优质客群标
     */
    private List<String> custGroups;

    /**
     * 优质客群数
     * 枚举值1/2/3/4/空，有值时校验，为空时不做校验
     */
    private Integer hitNums;


    public String getWelfareMode() {
        return welfareMode;
    }

    public void setWelfareMode(String welfareMode) {
        this.welfareMode = welfareMode;
    }

    public List<String> getCustGroups() {
        return custGroups;
    }

    public void setCustGroups(List<String> custGroups) {
        this.custGroups = custGroups;
    }

    public Integer getHitNums() {
        return hitNums;
    }

    public void setHitNums(Integer hitNums) {
        this.hitNums = hitNums;
    }
}
