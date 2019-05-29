package com.beneil.redis.listener;

import com.beneil.redis.thread.RequestHandlerThreadPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //初始化工作线程和内存队列
       RequestHandlerThreadPool.init();



    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
