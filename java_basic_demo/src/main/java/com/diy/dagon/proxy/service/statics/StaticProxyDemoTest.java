package com.diy.dagon.proxy.service.statics;

import com.diy.dagon.proxy.service.AdminService;
import com.diy.dagon.proxy.service.AdminServiceImpl;

/**
 * @author dagon
 * @description: 静态代理模式demo.
 * 特点：代理类也需要实现目标接口。
 *
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
