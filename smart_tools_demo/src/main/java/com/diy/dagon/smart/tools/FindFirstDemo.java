package com.diy.dagon.smart.tools;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Description TODO
 * @Date 2024/6/11 18:56
 * @Author by liyu-jk
 */
public class FindFirstDemo {

    public static void main(String[] args) throws InterruptedException {
        List<String> appList = Arrays.asList("app1", "app2", "app3","app4");

        Optional<String> firstApp = appList.stream()
                .map(FindFirstDemo::doSth) // 这里不会立即执行 doSth(n)
                .filter(v ->!"app1".equals(v))
                .findFirst();

        if (firstApp.isPresent()) {
            System.out.println("First app is: " + firstApp.get());
        } else {
            System.out.println("No apps found.");
        }
        Thread.sleep(10000);
    }

    public static String doSth(String app) {
        // 这里只是示例代码，实际中可能会有耗时的操作
        System.out.println("Processing app: " + app);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return app;
    }

}
