package com.beneil.redis.mapper;

import com.beneil.redis.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface UserMapper {
    User queryUser(int uid);
    void updateUser(User user);
}
