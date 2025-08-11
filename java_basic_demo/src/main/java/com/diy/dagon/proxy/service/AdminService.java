package com.diy.dagon.proxy.service;

import java.util.List;

/**
 * @author dagon
 * @description: 目标接口
 * @date 2021/3/18-8:49
 */
public interface AdminService {

    Object find();

    void update();

    String testArgs(int count, List<String> strList);

}
