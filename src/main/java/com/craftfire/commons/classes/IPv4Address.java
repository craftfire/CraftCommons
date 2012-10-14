package com.craftfire.commons.classes;

import java.net.Inet4Address;

public class IPv4Address extends IPAddress {
    byte[] data;

    public IPv4Address(Inet4Address address) {
        this.data = address.getAddress();
    }

    public IPv4Address(String address) {
        this.data = parseIP(address);
    }

    public IPv4Address(byte a, byte b, byte c, byte d) {
        this.data = new byte[] { a, b, c, d };
    }

    public IPv4Address(byte[] address) {
        this.data = address;
    }

    @Override
    public boolean isIPv4() {
        return true;
    }

    @Override
    public boolean isIPv6() {
        return false;
    }

    @Override
    public boolean isInRange() {
        // TODO Auto-generated method stub
        return false;
    }

    public static byte[] parseIP(String ip) {
        String[] split = ip.split("\\.");
        if (split.length != 4) {
            return null;
        }
        byte[] bytes = { 0, 0, 0, 0 };
        for (int i = 0; i < 4; ++i) {
            bytes[i] = Byte.parseByte(split[i]);
        }
        return bytes;
    }

}
