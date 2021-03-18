package com.diy.dagon.proxy.service;

/**
 * @author dagon
 * @description: TODO
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
}
