package com.diy.dagon.proxy.service;

/**
 * @author dagon
 * @description: TODO
 * @date 2021/3/18-8:51
 */
public class AdminServiceProxy implements AdminService {

    private AdminService adminService;

    public AdminServiceProxy(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public Object find() {
        System.out.println("判断用户是否有权限进行find操作");
        System.out.println("记录用户执行find操作的用户信息、查看内容和时间等");
        return adminService.find();
    }

    @Override
    public void update() {
        System.out.println("判断用户是否有权限进行update操作");
        adminService.update();
        System.out.println("记录用户执行update操作的用户信息、更改内容和时间等");
    }
}
