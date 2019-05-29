package com.beneil.redis.controller;

import com.beneil.redis.mapper.UserMapper;
import com.beneil.redis.pojo.User;
import com.beneil.redis.request.QuaryRequest;
import com.beneil.redis.request.Request;
import com.beneil.redis.request.UpdateRequest;
import com.beneil.redis.response.Response;
import com.beneil.redis.service.RequestAsyncService;
import com.beneil.redis.service.UserService;
import org.hibernate.cache.spi.QueryResultsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;

@RestController
public class UserController {
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private JedisCluster jedisCluster;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestAsyncService requestAsyncService;


    @PostConstruct
    public void init(){

    }

//    @RequestMapping("/getone")
//    public String getOne(){
//        String key1 = jedisCluster.get("key1");
//        System.out.println(key1);
//
//        return "ok";
//    }

    @RequestMapping("/update")
    public Response update(User user){
        user=new User(16,"good");//手动测试
        Response response=null;
        try {
            Request request=new UpdateRequest(user,userService);
            requestAsyncService.handler(request);
            response=new Response(Response.SUCCESS);
            System.out.println("update++++");
        }catch (Exception e){
            e.printStackTrace();
            response=new Response(Response.FAILURE);
        }
        return response;
    }
    @RequestMapping("/query")
    public User query(Integer uid){
        Response response=null;
        try {
            Request request=new QuaryRequest(uid,userService);
            requestAsyncService.handler(request);//这个是request.handler操作的是查数据库,并添加缓存
            //将请求仍给service处理后,hang主一小会,等待前面缓存刷新操作
            long starttime = System.currentTimeMillis();
            long endtime=0L;
            long waittime=0L;
            while(true){
                //查询缓存-->查询数据库
                User user = userService.queryUserCache(uid);//只查缓存
                if(user!=null){
                    System.out.println("走缓存");
                    return user;
                }else{
                    Thread.sleep(20);//睡眠200ms
                    endtime=System.currentTimeMillis();
                    waittime=endtime-starttime;
                }
                if(waittime>200L){
                    System.out.println("超时数据库");
                    return userService.queryUser(uid);//超时 直接查询数据库
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new User(uid,"none exist");
    }

}
