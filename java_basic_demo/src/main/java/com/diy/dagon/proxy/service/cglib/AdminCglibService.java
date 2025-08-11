package com.diy.dagon.proxy.service.cglib;

/**
 * @author dagon
 * @description: TODO
 * @date 2021/3/29-8:55
 */
public class AdminCglibService {
        public void update() {
            System.out.println("修改管理系统数据");
        }

        public Object find() {
            System.out.println("查看管理系统数据");
            return new Object();
        }
}
