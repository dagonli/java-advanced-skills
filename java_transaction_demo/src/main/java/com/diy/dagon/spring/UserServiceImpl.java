package com.diy.dagon.spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author dagon
 * @description: TODO
 * @date 2021/5/14-9:00
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    JdbcTemplate jdbcTemplate;
    @Resource
    CloudNoteTestService cloudNoteTestService;

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public boolean register(String userName) {
        jdbcTemplate.execute("update cn_user set cn_user_name ='" + userName + "' where cn_user_id = '008' ");
        try {
            cloudNoteTestService.insert(1,"ddd");
        } catch (Exception e) {
            System.err.println("update error.");
        }

        System.err.println("run success!");
        return true;
    }

    @Override
    public User login(String userName) {
        return null;
    }
}
