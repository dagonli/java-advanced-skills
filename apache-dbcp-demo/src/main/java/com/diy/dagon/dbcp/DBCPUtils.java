package com.diy.dagon.dbcp;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DBCP 工具类
 *

 *
 * Created by Dagon on 2020/4/11 - 9:33
 */
public class DBCPUtils {

    private static Properties properties = new Properties();
    private static DataSource dataSource;

    //加载DBCP配置文件
    static {
        try {
            InputStream inputStream = Object.class.getResourceAsStream("/dbcp-config.properties");
            properties.load(inputStream);
            System.out.println(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public static void main(String[] args) {
        Connection connection = getConnection();

        System.out.println(connection);

    }





}
