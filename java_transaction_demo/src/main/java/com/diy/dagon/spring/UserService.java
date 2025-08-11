package com.diy.dagon.spring;

/**
 * @author dagon
 * @description: TODO
 * @date 2021/5/14-8:59
 */
public interface UserService {

    boolean register(String userName);

    User login(String userName);

}
