package com.beneil.nettyrpc.client;

import com.beneil.nettyrpc.clientStub.NettyRPCProxy;

public class TestRPC {
    public static void main(String[] args) {
        HelloNetty helloNetty= (HelloNetty) NettyRPCProxy.create(HelloNetty.class);
        System.out.println(helloNetty.hello());

        HelloRPC helloRPC= (HelloRPC) NettyRPCProxy.create(HelloRPC.class);
        System.out.println(helloRPC.hello("RPC"));
    }
}
