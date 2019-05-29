package com.beneil.redis.service;

import com.beneil.redis.request.QuaryRequest;
import com.beneil.redis.request.Request;
import com.beneil.redis.request.RequestQueue;
import com.beneil.redis.request.UpdateRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

@Service
public class RequestAsyncServiceImpl implements RequestAsyncService{
    @Override
    public void handler(Request request) {
        //根据请求路由 获得id  路由到对应的内存队列中
        ArrayBlockingQueue<Request> queue = route(request.getUserId());
        //请求放到队列里面
        try {
            //重复的不要添加到队列里面

            //这里会有线程安全  要修改  应该在threadhandler(也就是处理队列那里处理(进入了队列单线程 但是效率下降?))
//            if(request instanceof UpdateRequest ){//更新请求 删除缓存 设置false,进入队列
//                flagMap.put(request.getUserId(),true);
//            }else if(request instanceof QuaryRequest){
//                Boolean flag = flagMap.get(request.getUserId());
//                if(flag!=null&&flag){
//                    flagMap.put(request.getUserId(),false);//(前面有更新请求)查请求 放入缓存 设置true 进入队列
//                }
//                if(flag!=null&&!flag){//(前面没有更新请求) 直接返回 不进入队列
//                    return;
//                }
//                if(flag==null){
//                    flagMap.put(request.getUserId(),false);
//                }
//            }
            queue.put(request);//阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取路由到内存的队列
     * @param uid
     * @return
     */
    private ArrayBlockingQueue<Request> route(Integer uid){
        RequestQueue requestQueue = RequestQueue.getInstance();//获取取总队列
        String key = String.valueOf(uid);//获取id
        int h;
        int hash=(key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

        //对hash取模 路由到指定队列
        int index=(requestQueue.queueSize() - 1) & hash;
        ArrayBlockingQueue<Request> queue = requestQueue.getQueue(index);
        return queue;
    }
}
