package com.beneil.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //通道就绪事件
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发送还钱");
        ctx.writeAndFlush(Unpooled.copiedBuffer("还钱", CharsetUtil.UTF_8));
    }

    //读取数据事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接受没钱");
        ByteBuf buf= (ByteBuf) msg;
        System.out.println("server:"+buf.toString(CharsetUtil.UTF_8));
    }
}
