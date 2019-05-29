package com.beneil.redis.request;

import com.beneil.redis.pojo.User;
import com.beneil.redis.service.UserService;

/**
 * 1.查数据库
 * 2.存缓存
 */
public class QuaryRequest implements Request{
    private int uid;

    private UserService userService;

    public QuaryRequest(int uid, UserService userService) {
        this.uid = uid;
        this.userService = userService;
    }

    @Override
    public void handler() {
        System.out.println("QuaryRequest");
        //1
        User user = userService.queryUser(this.uid);
        //2
        userService.addUserCache(user);
    }

    @Override
    public Integer getUserId() {
        return uid;
    }
}
