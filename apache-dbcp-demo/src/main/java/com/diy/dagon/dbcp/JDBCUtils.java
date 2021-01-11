package com.diy.dagon.dbcp;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  JDBC 示例
 * 和DBCP连接池做对比
 *
 * Created by Dagon on 2020/4/11 - 10:43
 */
public class JDBCUtils {
    private static Connection connection = null;

    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.registerDriver(new Driver());

            String dbUrl = "jdbc:mysql://192.168.1.10:3306/cloud_note";
            connection = DriverManager.getConnection(dbUrl, "root", "liyu5200");


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
