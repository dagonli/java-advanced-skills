package com.diy.dagon;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 1.事务的 ACID
 *
 * 事务的原子性：表示事务执行过程中的任何失败都将导致事务所做的任何修改失效。
 * 事务的一致性：表示当事务执行失败时，所有被该事务影响的数据都应该恢复到事务执行前的状态。
 * 事务的隔离性：表示在事务执行过程中对数据的修改，在事务提交之前对其他事务不可见。
 * 事务的持久性：表示已提交的数据在事务执行失败时，数据的状态都应该正确。
 *
 * 通俗的理解，事务是一组原子操作单元，从数据库角度说，就是一组SQL指令，要么全部执行成功，若因为某个原因其中一条指令执行有错误，
 * 则撤销先前执行过的所有指令。更简答的说就是：要么全部执行成功，要么撤销不执行
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );


        String property = System.getProperty("jdbc.drivers");
        System.out.println(property);

        ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
        Iterator<Driver> driversIterator = loadedDrivers.iterator();

        /* Load these drivers, so that they can be instantiated.
         * It may be the case that the driver class may not be there
         * i.e. there may be a packaged driver with the service class
         * as implementation of java.sql.Driver but the actual class
         * may be missing. In that case a java.util.ServiceConfigurationError
         * will be thrown at runtime by the VM trying to locate
         * and load the service.
         *
         * Adding a try catch block to catch those runtime errors
         * if driver not available in classpath but it's
         * packaged as service and that service is there in classpath.
         */
        try{
            while(driversIterator.hasNext()) {
                Driver next = driversIterator.next();
                System.out.println(next);
            }
        } catch(Throwable t) {
            // Do nothing
        }
    }
}
