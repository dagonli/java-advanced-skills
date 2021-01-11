import com.diy.dagon.dbcp.DBCPUtils;
import com.diy.dagon.dbcp.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Dagon on 2020/4/11 - 11:16
 */
public class DBCPTest {

    //测试,每写一条数据前,就新建一个连接
    @Test
    public void testWriteDBByEveryConn() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            writeDBByEveryConn(i);
        }
        System.err.println("DONE,cost ms:" + (System.currentTimeMillis() - start));//133251ms

    }

    //测试,使用连接池,每写一条数据前,从连接池中获取一个连接
    @Test
    public void testWriteDBByDBCP() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            writeDBByDBCP(i);
        }
        System.err.println("DONE,cost ms:" + (System.currentTimeMillis() - start));//77375ms
    }

    /**
     * TODO:多线程调用时，会报错【Cannot get a connection, pool error Timeout waiting for idle object】！！！
     * @throws IOException
     */
    @Test
    public void testDBCPConcurrent() throws IOException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 200; j++) {
                    writeDBByDBCP(j);
                }
            }, "myThread-" + i);
        }

        System.in.read();
    }

    //测试,只建一条连接,写入所有数据
    @Test
    public void testWriteDBByOneConn() throws Exception {
        long start = System.currentTimeMillis();
        Connection conn = JDBCUtils.getConnection();
        Statement stat = conn.createStatement();
        for (int i = 0; i < 2000; i++) {
            writeDBByOneConn(i, stat);
        }
        conn.close();
        System.err.println("DONE,cost ms:" + (System.currentTimeMillis() - start));//71422ms
    }


    //不使用连接池写数据库,每写一条数据创建一个连接
    public void writeDBByEveryConn(int data) {
        String sql = "insert into dbcp values (" + data + ")";
        Connection conn = JDBCUtils.getConnection();
        try {
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    //不使用连接池写数据库,只用一个连接,写所有数据
    public void writeDBByOneConn(int data, Statement stat) {
        String sql = "insert into dbcp values (" + data + ")";
        try {
            stat.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通过DBCP连接池写数据库
    public void writeDBByDBCP(int data) {
        long start = System.currentTimeMillis();

        String sql = "insert into dbcp values (" + data + ")";
        try {
            Connection conn = DBCPUtils.getConnection();
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            conn.commit();
            /**
             * 并不是手动将该连接关闭，而是将该连接交回给DBCP连接池，由连接池管理该连接。
             * 即用完连接后显示的将数据库连接提交至DBCP连接池。
             *
             * 若注释掉：则removeAbandonedOnMaintenance=true/removeAbandonedOnBorrow=true 开始生效
             */
//            conn.close();


            System.err.println(Thread.currentThread().getName() + " DONE,cost ms:" + (System.currentTimeMillis() - start));//71422ms

            /**
             * 多线程测试时：防止抛出异常->【Cannot get a connection, pool error Timeout waiting for idle object】
             * 让connection有足够时间被回收。
             *
             * 网络解释：如果用到了多线程，当线程1和线程2还没跑完的时候，线程3也开始运行并且调用service的时候就会导致取不到session，抛出如上异常
             */
            Thread.sleep(1000);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
