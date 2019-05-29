package com.beneil.nettyrpc.serverStub;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reflections.Reflections;
import sun.reflect.Reflection;

import java.lang.reflect.Method;
import java.util.Set;

public class InvokeHandler extends ChannelInboundHandlerAdapter {
    //得到实现类
    private String getImplClassName(ClassInfo classInfo) throws Exception{
        //拼接包路径
        String interfacePath="com.beneil.nettyrpc.server";
        int lastDot = classInfo.getClassName().lastIndexOf(".");
        String interfaceName = classInfo.getClassName().substring(lastDot);
        Class<?> superClass = Class.forName(interfacePath + interfaceName);
        Reflections reflections = new Reflections(interfacePath);
        //得到实现类
        Set<Class<?>> ImplClassSet = (Set<Class<?>>) reflections.getSubTypesOf(superClass);
        if(ImplClassSet.size()==0){
            System.out.println("未找到实现类");
            return null;
        }else if(ImplClassSet.size()>1){
            System.out.println("找到多个实现类");
            return null;
        }else{
            Class[] classes = ImplClassSet.toArray(new Class[0]);
            return classes[0].getName();//得到实现类 类名
        }

    }

    //反射
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo= (ClassInfo) msg;
        Object clazz = Class.forName(getImplClassName(classInfo)).newInstance();
        Method method = clazz.getClass().getMethod(classInfo.getMethodName(), classInfo.getTpes());
        Object result = method.invoke(clazz, classInfo.getObjects());
        ctx.writeAndFlush(result);

    }
}
