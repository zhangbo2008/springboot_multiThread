package com.example.visit_record.asp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 描述:创建一个定长线程池，支持定时及周期性任务执行。延迟执行
 *
 * @author yanpenglei
 * @create 2017-10-12 11:53
 **/
public class TestNewScheduledThreadPool {

    public static void main(String[] args) {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        scheduledThreadPool.schedule(new Runnable() {

            @Override
            public void run() {
                System.out.println("表示延迟3秒执行。");
            }
        }, 0, TimeUnit.SECONDS);


        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                System.out.println("表示延迟1秒后每3秒执行一次。");
            }
        }, 1, 3, TimeUnit.SECONDS);








    }

}