package com.beneil.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * server业务处理类
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接受还钱");
        //读事件
        ByteBuf buf= (ByteBuf) msg;//缓冲区
        System.out.println("客户端:"+buf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发送没钱");
        //读取完毕事件
        ctx.writeAndFlush(Unpooled.copiedBuffer("没钱",CharsetUtil.UTF_8));//回写
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常");
        //异常发生事件
        ctx.close();
    }
}
