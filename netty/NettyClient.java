package com.beneil.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws Exception{
        //1.创建一个线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2.创建客户端启动助手
        Bootstrap bs = new Bootstrap();
        bs.group(group)//设置线程组
          .channel(NioSocketChannel.class)//设置客户端通道实现
          .handler(new ChannelInitializer<SocketChannel>() {//创建通道初始化对象

              @Override
              protected void initChannel(SocketChannel socketChannel) throws Exception {
                  socketChannel.pipeline().addLast(new NettyClientHandler());//往pipelie中添加自定义handler
              }
          });
        System.out.println("ok");
        //3.连接  异步非阻塞
        ChannelFuture cf = bs.connect("127.0.0.1", 9999).sync();//connect是异步的 sync是同步阻塞
        //4.关闭连接
        cf.channel().closeFuture().sync();

    }
}
