package com.diy.dagon.proxy.service;

import java.util.List;

/**
 * @author dagon
 * @description: 目标接口实现类
 * @date 2021/3/18-8:49
 */
public class AdminServiceImpl implements AdminService {
    @Override
    public Object find() {
        System.out.println("查看管理系统数据");
        return new Object();
    }

    @Override
    public void update() {
        System.out.println("更新管理系统数据");
    }

    @Override
    public String testArgs(int count, List<String> strList) {
        System.out.println("开始执行测试参数类方法，count=" + count + "strList={}" + strList.toArray());
        System.out.println("结束执行测试参数类方法");
        return "SUCCESS";
    }
}
