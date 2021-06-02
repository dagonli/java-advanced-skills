package com.diy.dagon.spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author dagon
 * @description: TODO
 * @date 2021/5/14-8:59
 */
@Service
public class CloudNoteTestServiceImpl implements CloudNoteTestService{

    @Resource
    JdbcTemplate jdbcTemplate;
    @Resource
    CloudNoteTestService1 cloudNoteTestService1;

    @Override
    @Transactional
    public boolean update(String name) {
        try {
//            cloudNoteTestService1.insert(1, "axe");
            cloudNoteTestService1.update("999");
        } catch (Exception e) {
//            throw new RuntimeException("insert error.");
            System.err.println("run with a error.");
        }
        jdbcTemplate.execute("update cn_test set name ='" + name + "' where id='12';");
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public boolean insert(int id, String name) {
        jdbcTemplate.execute("insert into cn_test values (1,'axe');");
        if (1==1)
        throw new RuntimeException();
        return true;
    }
}
