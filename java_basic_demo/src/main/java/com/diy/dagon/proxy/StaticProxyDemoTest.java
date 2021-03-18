package com.diy.dagon.proxy;

import com.diy.dagon.proxy.service.AdminService;
import com.diy.dagon.proxy.service.AdminServiceImpl;
import com.diy.dagon.proxy.service.AdminServiceProxy;

/**
 * @author dagon
 * @description: TODO
 * @date 2021/3/18-8:54
 */
public class StaticProxyDemoTest {

    public static void main(String[] args) {
        AdminService adminService = new AdminServiceImpl();
        AdminServiceProxy proxy = new AdminServiceProxy(adminService);

        proxy.find();

        proxy.update();
    }
}
