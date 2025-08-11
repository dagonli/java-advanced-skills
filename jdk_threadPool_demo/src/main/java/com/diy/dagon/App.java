package com.diy.dagon;

import java.util.concurrent.*;

/**
 * Hello world!
 *
 * 参考博客 {@see https://www.jianshu.com/p/a166944f1e73}
 *
 */
public class App implements Runnable
{
    public static void main( String[] args ) /*throws InterruptedException */{
        //1.jdk自带是无界队列
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
        ExecutorService executorService = new ThreadPoolExecutor(3, 5,
                2000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(5));

        for (int i = 0; i < 20; i++) {
            try {
                executorService.execute(new App(i));
            } catch (Exception e) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
                int activeCount = threadPoolExecutor.getActiveCount();

                System.err.println(threadPoolExecutor + " tasks:" + threadPoolExecutor.getQueue());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        executorService.shutdown();



    }

    private int index;
    public App(int index){
        this.index = index;
    }

    @Override
    public String toString() {
        return "APP-" + index;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started..." + index);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" over......" + index);
    }
}
