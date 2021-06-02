package com.diy.dagon.spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * @author dagon
 * @description: TODO
 * @date 2021/5/14-8:59
 */
@Service
public class CloudNoteTestServiceImpl1 implements CloudNoteTestService1{

    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public boolean update(String name) throws Exception{
        try {
            //this.insert(1, "axe");
            runWithThrowUncheckedException();
        } catch (Exception e) {
//            throw new RuntimeException("insert error.");
//            System.err.println("run with a error.");
            throw new RuntimeException("insert error.");
        }
        jdbcTemplate.execute("update cn_test set name ='" + name + "' where id='1';");
        return false;
    }

    @Override
    @Transactional/*(propagation = Propagation.REQUIRES_NEW)*/
    public boolean insert(int id, String name) {
        try {
            jdbcTemplate.execute("insert into cn_test values (1,'axe');");
        } catch (Exception e) {
//            System.err.println("error inside.");
            throw new RuntimeException();
        }
        return true;
    }

    private void runWithThrowUncheckedException(){
        throw new RuntimeException();
    }
}
