package com.diy.dagon.proxy.service.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author dagon
 * @description: TODO
 * @date 2021/3/29-8:47
 */
public class AdminServiceCglibProxy implements MethodInterceptor {
    private Object target;

    public AdminServiceCglibProxy(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {
        //工具类
        Enhancer enhancer = new Enhancer();
        //设置父类
        enhancer.setSuperclass(target.getClass());
        //设置回调函数
        enhancer.setCallback(this);
        //创建子类代理对象
        return enhancer.create();

    }



    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("cglib开始执行");
        Object invoke = method.invoke(target, args);
        System.out.println("cglib执行完成");
        return invoke;

    }
}
