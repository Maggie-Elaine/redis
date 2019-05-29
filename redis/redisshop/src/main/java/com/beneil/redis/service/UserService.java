package com.beneil.redis.service;

import com.beneil.redis.mapper.UserMapper;
import com.beneil.redis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisCluster jedisCluster;

    public void removeUserCache(User user){
        jedisCluster.del(user.getId().toString());
    }
    public void addUserCache(User user){
        jedisCluster.set(user.getId().toString(),user.getUname());
    }
    public void updateUser(User user){
        userMapper.updateUser(user);
    }

    public User queryUser(int uid){
        return userMapper.queryUser(uid);
    }
    public User queryUserCache(int uid){
        String s = jedisCluster.get(String.valueOf(uid));
        if(s!=null&&!"".equals(s)){
            return new User(uid,s);
        }
        return null;
    }

}
