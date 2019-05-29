# redis
redis相关问题:如高并发下,双写不一致
基于springboot2.0:
1.nginx+lua进行流量分发,把id分发到指定tomcat服务上
2.每个服务都起一个线程池,管理若干个固定大小的队列
3.把hash后的id,进入指定队列,线程负责消费,并存储改id的状态(未修改,已修改),不能在service层判断,会有并发问题
4.发现已修改的id,进入队列,更新redis;未修改的直接返回查询redis

文件结构:
1.config:redis-cluster的配置
2.controller:
3.service:
4.mapper:
5.pojo:
6.thread:线程池和线程具体操作(出入队列和改变标志位)
7.listener:监听servletContext,初始化线程池
8.request:请求接口,分别有查询和更新的实现类,对应不同的业务处理(handler)
9.response:返回响应
