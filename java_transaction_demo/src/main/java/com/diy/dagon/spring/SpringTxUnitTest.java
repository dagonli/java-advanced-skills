package com.diy.dagon.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author dagon
 * @description: spring 事务单元测试类
 * @date 2021/5/14-8:36
 */
/**
 * RunWith的value属性指定以spring test的SpringJUnit4ClassRunner作为启动类
 * 如果不指定启动类，默认启用的junit中的默认启动类
 */
@RunWith(SpringJUnit4ClassRunner.class)
/**
 * 指定在单元测试启动的时候创建spring的工厂类对象
 */
@ContextConfiguration(classes = ApplicationContext.class)
public class SpringTxUnitTest {

    @Resource
    UserService userService;

    @Test
    public void test() {
        System.out.println("login");
        userService.register("sofia");
        userService.register("dagon");
        System.err.println("over");
    }
}
