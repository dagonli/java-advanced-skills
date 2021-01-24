package com.diy.dagon.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

/**
 * @author dagon
 * @description: jdbc 操作数据库
 * 2、jdbc的缺点
 * （1）需要频繁的创建数据库连接
 * （2）涉及到的增删改查等功能需要在各个java文件中编写大量代码
 * （3）对于底层事务、数据类型转换等都需要手动处理，又是各种代码
 *
 *
 * 3、mybatis优点
 * （1）封装了jdbc对数据库的各种操作，减少代码
 * （2）增加了连接池、一、二级缓存
 * （3）可以自动生成sql语句
 *
 *
 *
 * @date 2021/1/23-23:05
 */
public class JDBCDemo {
    public static void main(String[] args) throws SQLException {
        //1、加载JDBC驱动程序：
        // 加载想要连接的数据库的驱动到JVM，成功加载后，会将Driver类的实例注册到DriverManager类中。
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //DriverManager.registerDriver(new Driver());

        //2、创建数据库的连接
        String url = "jdbc:mysql://192.168.1.103:3306/cloud_note";
        String username = "root";
        String password="liyu5200";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //3、创建一个preparedStatement
        Statement statement = connection.createStatement();

        System.err.println(connection);

        connection.setAutoCommit(false);
        //4、执行SQL语句
        int i = statement.executeUpdate("update cloud_test set name='lucy' where id=5;");
        System.out.println(i);


        String sql = "update cloud_test set msg_type = ? where id=7 ;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "8");
        int i1 = preparedStatement.executeUpdate();
        System.out.println(i1);


        connection.commit();

//        //5、遍历结果集
//        while (resultSet.next()) {
//            String resultStr = resultSet.getString(1);
//            System.out.println(resultStr);
//        }

        //6、处理异常，关闭JDBC对象资源
//        resultSet.close();
        statement.close();
        connection.close();
    }
}
