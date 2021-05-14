package com.diy.dagon.spring;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dagon
 * @description: TODO
 * @date 2021/5/14-9:00
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    @Transactional
    public boolean register(String userName) {
        if ("dagon".equals(userName)) {
            throw new IllegalArgumentException("错误了");
        }
        System.err.println("执行成功");
        return true;
    }

    @Override
    public User login(String userName) {
        return null;
    }
}
