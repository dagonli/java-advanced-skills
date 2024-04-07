package com.diy.dagon.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.PatternMatchUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

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
    @Resource
    JdbcTemplate jdbcTemplate;
    @Resource
    CloudNoteTestService cloudNoteTestService;

    @Test
    public void testRollback() {
        cloudNoteTestService.insert(1, "liyu");
    }

    @Test
    public void testTx() {
        userService.register("yyds1");
    }

    @Test
    public void testNewTx() {
        userService.login("never_more");
    }

    @Test
    public void test() {
        System.out.println("login");
        userService.register("sofia");
        userService.register("dagon");
        System.err.println("over");
    }

    @Test
    public void testJdbcTemplate() {
        jdbcTemplate.execute("update cloud_test set name='sofia' where id=2;");
        System.err.println("over");
    }

    @Test
    public void testMethodMatched() throws NoSuchMethodException {
        Class<? extends UserService> aClass = userService.getClass();
        Method method = aClass.getMethod("register", String.class);
        String methodName = method.getName();

        String mappedName = "*NewTrx";
        boolean b = PatternMatchUtils.simpleMatch(mappedName, "saveLoanSuccBillIncomeNew");
        System.out.println(b);
    }

}
