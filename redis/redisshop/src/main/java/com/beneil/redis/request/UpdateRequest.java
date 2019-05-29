package com.beneil.redis.request;

import com.beneil.redis.pojo.User;
import com.beneil.redis.service.UserService;

/**
 * 1.删除缓存2.更新数据库
 */
public class UpdateRequest implements Request{
    private User user;

    private UserService userService;
    public UpdateRequest(User  user,UserService userService) {
        this.user=user;
        this.userService=userService;
    }

    @Override
    public void handler() {
        System.out.println("UpdateRequest");
        //1.删除缓存
        userService.removeUserCache(user);
        //2.更新数据库
        userService.updateUser(user);
    }

    @Override
    public Integer getUserId() {
        return user.getId();
    }
}
