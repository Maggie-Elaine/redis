package com.beneil.redis;

import com.beneil.redis.config.ClusterConfigurationProperties;
import com.beneil.redis.listener.InitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.EventListener;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    /**
     * 监听器加载,初始化加载线程池和任务队列(每条线程处理一个队列 自动:循环)
     * 根据user或者id 创建不同request
     * 根据不同的id 计算hash并取模 丢进相应队列,自动:出队运行request.handler()(自己的处理方法)
     *
     * thread-->list[hash].queue.take()-->handler
     *
     * 用一个大list装所有队列 和 用一个大map装所有商品的flag(修改了true(key=id 同时删除缓存),查询设置回false)
     */


    @Autowired
    private ClusterConfigurationProperties properties;//获取所有node
    @Bean
    public JedisCluster jedisClusterFactory(){
        HashSet<HostAndPort> jedisClusterNodes = new HashSet<>();
        List<String> nodes = properties.getNodes();
        for (String node : nodes) {
            String[] split = node.split(":");
            jedisClusterNodes.add(new HostAndPort(split[0],Integer.parseInt(split[1])));
        }
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes);//可以设置超时&跳转
        return jedisCluster;
    }

    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){//注册监听器
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean<>();
        bean.setListener(new InitListener());
        return bean;
    }
}
