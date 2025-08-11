package com.diy.dagon.proxy.service.jdk;

import com.diy.dagon.proxy.service.AdminService;
import com.diy.dagon.proxy.service.AdminServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author dagon
 * @description: java动态代理
 * 为解决静态代理对象必须实现接口的所有方法的问题，java给出了动态代理，具有以下特点：
 * 1.Proxy对象不需要implements接口。
 * 2.要求target对象是一个接口的实现对象
 * 2.Proxy对象的生成是利用JDK的Api，在JVM内存中动态的构建Proxy对象,
 * 需要使用到{@link java.lang.reflect.Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}
 * 方法参数说明：
 * a.ClassLoader loader：指定当前target对象使用类加载器，获取加载器的方法是固定的；
 * b.Class<?>[] interfaces：target对象实现的接口的类型，使用泛型方式确认类型；
 * c.InvocationHandler invocationHandler:事件处理,执行target对象的方法时，会触发事件处理器的方法，会把当前执行target对象的方法作为参数传入。
 *
 * @date 2021/3/29-7:52
 */
public class JdkProxyDemoTest {
    public static void main(String[] args) {
        final AdminService adminService = new AdminServiceImpl();
        AdminService jdkProxyAdminService = (AdminService) Proxy.newProxyInstance(adminService.getClass().getClassLoader(), adminService.getClass().getInterfaces(), new InvocationHandler() {
            /**
             * @param   proxy the proxy instance that the method was invoked on
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("开始执行，" + method.getName());
                method.invoke(adminService, args);
                if (args!=null && args.length>0) {
                    boolean isWarpClass = ((Class) args[0].getClass().getField("TYPE").get(null)).isPrimitive();
                    if (isWarpClass) {
                        System.err.println("当前参数是原始类型，isWarpClass={}" + args[0].getClass());
                    }

                }
                System.out.println("结束执行，" + method.getName());
                return null;
            }
        });

        jdkProxyAdminService.update();
        jdkProxyAdminService.find();
        jdkProxyAdminService.testArgs(100, Arrays.asList("我", "是", "我啦"));
    }
}
