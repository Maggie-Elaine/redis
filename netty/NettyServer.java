package com.beneil.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.SocketChannel;


public class NettyServer {
    public static void main(String[] args) throws Exception{
        //1.创建2个线程组,接受socket连接和处理网络操作
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //2.创建服务端启动助手 [配置参数
        ServerBootstrap boot = new ServerBootstrap();
        boot.group(bossGroup,workerGroup)//设置2个线程组
                .channel(NioServerSocketChannel.class)//设置channel类型?
                .option(ChannelOption.SO_BACKLOG,128)//设置队列等待连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//保持连接
                .childHandler(new ChannelInitializer<SocketChannel>() {//创建 通道初始化对象--最关键这个
                    @Override
                    public void initChannel(SocketChannel sc) throws Exception {//往pipeline链中添加自定义handler
                        sc.pipeline().addLast(new NettyServerHandler());
                    }
                });
        //3.绑定
        ChannelFuture cf = boot.bind(9999).sync();//bind是异步的 立即返回future  sync是同步阻塞的
        System.out.println("server启动");
        //Thread.sleep(6000);
        //4.关闭通道 关闭线程
        cf.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
