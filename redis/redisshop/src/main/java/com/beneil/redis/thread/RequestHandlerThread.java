package com.beneil.redis.thread;

import com.beneil.redis.request.QuaryRequest;
import com.beneil.redis.request.Request;
import com.beneil.redis.request.RequestQueue;
import com.beneil.redis.request.UpdateRequest;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 工作线程
 */
public class RequestHandlerThread implements Callable<Boolean> {
    /**
     * 自己监控的队列
     */
    private ArrayBlockingQueue<Request> queue;

    public RequestHandlerThread(ArrayBlockingQueue<Request> queue){
        this.queue=queue;
    }
    @Override
    public Boolean call() throws Exception {
        try {
            while(true){
                Request request = queue.take();
//--------------------------------------------------------------------------
                RequestQueue requestQueue = RequestQueue.getInstance();
                Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();
                if(request instanceof UpdateRequest){//更新请求 删除缓存 设置true,进入队列
                    flagMap.put(request.getUserId(),true);
                    System.out.println("update:true");
                }else if(request instanceof QuaryRequest){
                    Boolean flag = flagMap.get(request.getUserId());
                    if(flag!=null&&flag){
                        flagMap.put(request.getUserId(),false);//(前面有更新请求)查请求 放入缓存 设置false 进入队列
                    System.out.println("query:false");
                    }
                    if(flag!=null&&!flag){//(前面没有更新请求) 直接返回 不进入队列
                    System.out.println("query:true");
                        continue;
                    }
                    if(flag==null){
                    System.out.println("null:false");
                        flagMap.put(request.getUserId(),false);//没有缓存  添加
                    }
                }
//--------------------------------------------------------------------------
                request.handler();//处理业务
                /*poll -->【若队列为空，返回null】
                  remove >【若队列为空，抛出NoSuchElementException异常】
                  take -->【若队列为空，发生阻塞，等待有元素】*/
            }
        }catch (Exception e){
            e.printStackTrace();;
        }
        return true;
    }
}
