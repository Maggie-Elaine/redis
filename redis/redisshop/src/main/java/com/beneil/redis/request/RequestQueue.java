package com.beneil.redis.request;

import com.beneil.redis.thread.RequestHandlerThreadPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求内存队列
 */
public class RequestQueue {
    private List<ArrayBlockingQueue> queues=new ArrayList<ArrayBlockingQueue>();//内存队列

    private Map<Integer,Boolean> map=new ConcurrentHashMap<>();
    public void addQueue(ArrayBlockingQueue<Request> queue){
        queues.add(queue);
    }

    public int queueSize(){
        return queues.size();
    }

    public ArrayBlockingQueue<Request> getQueue(int index){
        return queues.get(index);
    }

    public Map<Integer,Boolean> getFlagMap(){
        return map;
    }


    public static void init(){
        getInstance();
    }

    public static RequestQueue getInstance(){//返回单例
        return Singlento.instance;
    }

    private static class Singlento{//单例
        private static RequestQueue instance=new RequestQueue();
    }


}
