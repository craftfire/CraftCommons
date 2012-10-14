package com.craftfire.commons.classes;

import java.net.Inet6Address;

public class IPv6Address extends IPAddress {
    short[] data;
    
    public IPv6Address(Inet6Address address) {
        System.arraycopy(address.getAddress(), 0, this.data, 0, 16);
    }

    public IPv6Address(String address) {
        this.data = parseIP(address);
    }

    public IPv6Address(short a, short b, short c, short d, short e, short f, short g, short h) {
        this.data = new short[] { a, b, c, d, e, f, g, h };
    }

    @Override
    public boolean isIPv4() {
        return false;
    }

    @Override
    public boolean isIPv6() {
        return true;
    }

    @Override
    public boolean isInRange() {
        // TODO Auto-generated method stub
        return false;
    }

    public static short[] parseIP(String ip) {
        String[] split = ip.split(":");
        if (split.length != 8) {
            return null;
        }
        short[] shorts = { 0, 0, 0, 0 };
        for (int i = 0; i < 4; ++i) {
            shorts[i] = Short.parseShort(split[i], 16);
        }
        return shorts;
    }
}
