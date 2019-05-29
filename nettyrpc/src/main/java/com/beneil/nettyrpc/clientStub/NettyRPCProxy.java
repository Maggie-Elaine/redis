package com.beneil.nettyrpc.clientStub;

import com.beneil.nettyrpc.serverStub.ClassInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import javafx.scene.chart.PieChart;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class NettyRPCProxy {
    //创建代理对象  封装classinfo
    public  static Object create(final Class target){
        //.getClass().getInterfaces()
        return Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //封装classinfo
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassName(target.getName());
                classInfo.setMethodName(method.getName());
                classInfo.setObjects(args);
                classInfo.setTpes(method.getParameterTypes());

                //netty发送数据
                NioEventLoopGroup group = new NioEventLoopGroup();
                final ResultHandler resultHandler = new ResultHandler();
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel channel) throws Exception {
                                    ChannelPipeline pipeline = channel.pipeline();
                                    pipeline.addLast("encoder",new ObjectEncoder());
                                    pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE,
                                            ClassResolvers.cacheDisabled(null)));
                                    pipeline.addLast("handler",resultHandler);
                                }
                            });
                    ChannelFuture future = bootstrap.connect("127.0.0.1", 11111).sync();
                    future.channel().writeAndFlush(classInfo).sync();//发送数据
                    future.channel().closeFuture().sync();
                }catch (Exception e){

                }finally {
                    group.shutdownGracefully().sync();
                }

                return resultHandler.getResponse();
            }
        });
    }
}
