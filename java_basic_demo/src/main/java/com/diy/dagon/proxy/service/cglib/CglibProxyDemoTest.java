package com.diy.dagon.proxy.service.cglib;

import com.diy.dagon.proxy.service.AdminService;
import com.diy.dagon.proxy.service.AdminServiceImpl;

import java.util.Arrays;

/**
 * @author dagon
 * @description: Cglib代理
 * JDK动态代理要求target对象是一个接口的实现对象，假如target对象只是一个单独的对象，并没有实现任何接口，
 * 这时候就会用到Cglib代理(Code Generation Library)，即通过构建一个子类对象，从而实现对target对象的代理，
 * 因此目标对象不能是final类(报错)，且目标对象的方法不能是final或static（不执行代理功能）。
 *
 *
 * 理解上述Java代理后，也就明白Spring AOP的代理实现模式，即加入Spring中的target是接口的实现时，就使用JDK动态代理，
 * 否是就使用Cglib代理。Spring也可以通过<aop:config proxy-target-class="true">强制使用Cglib代理，
 * 使用Java字节码编辑类库ASM操作字节码来实现，直接以二进制形式动态地生成 stub 类或其他代理类，性能比JDK更强。
 *
 *
 * @date 2021/3/29-8:40
 */
public class CglibProxyDemoTest {
    public static void main(String[] args) {
        //基于接口代理
        AdminService adminService = new AdminServiceImpl();
        AdminServiceCglibProxy cglibProxy = new AdminServiceCglibProxy(adminService);

        AdminService proxyInstance = (AdminService) cglibProxy.getProxyInstance();
        proxyInstance.testArgs(1, Arrays.asList("1","4"));

        System.out.println("---------------------------分割线--------------------------");

        //基于类代理
        AdminCglibService adminCglibService = new AdminCglibService();
        AdminServiceCglibProxy adminServiceCglibProxy = new AdminServiceCglibProxy(adminCglibService);

        AdminCglibService proxyInstance1 = (AdminCglibService) adminServiceCglibProxy.getProxyInstance();
        proxyInstance1.update();
    }
}
