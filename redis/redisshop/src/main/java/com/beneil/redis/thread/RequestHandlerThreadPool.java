package com.beneil.redis.thread;

import com.beneil.redis.request.Request;
import com.beneil.redis.request.RequestQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池:单例
 */
public class RequestHandlerThreadPool {
    /**
     * 用绝对线程安全的方式初始化
     * 静态内部类:
     *      不管多少个线程并发初始化,美不美只会初始化一次
     *      外部类加载时并不需要立即加载内部类，内部类不被加载则不去初始化INSTANCE，故而不占内存
     */
    private ExecutorService threadpool= Executors.newFixedThreadPool(10);//线程池


    private RequestHandlerThreadPool(){//私有化构造
        for(int i=0;i<10;i++){
            RequestQueue queues = RequestQueue.getInstance();
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(100);
            queues.addQueue(queue);//保存所有内存队列
            threadpool.submit(new RequestHandlerThread(queue));
        }

    }

    public static RequestHandlerThreadPool getInstance(){//返回单例
        return Singlento.instance;
    }
    public static void init(){
        getInstance();
    }

    private static class Singlento{//单例
        private static RequestHandlerThreadPool instance=new RequestHandlerThreadPool();
    }
}
