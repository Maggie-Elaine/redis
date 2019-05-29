package com.beneil.nettyrpc.server;

public class HelloNettyImpl implements HelloNetty{
    @Override
    public String hello() {
        return "HelloNetty";
    }
}
