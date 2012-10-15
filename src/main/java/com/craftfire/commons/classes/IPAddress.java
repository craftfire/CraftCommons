package com.craftfire.commons.classes;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class IPAddress {
    public abstract boolean isIPv4();

    public abstract boolean isIPv6();

    public abstract InetAddress getInetAddress();

    public abstract IPv4Address toIPv4();

    public abstract IPv6Address toIPv6();

    abstract byte[] getBytes();

    public boolean isInRange(IPRange range) {
        return range.isInRange(this);
    }

    public static IPAddress valueOf(InetAddress address) {
        if (address instanceof Inet4Address) {
            return new IPv4Address((Inet4Address) address);
        } else if (address instanceof Inet6Address) {
            return new IPv6Address((Inet6Address) address);
        }
        return null;
    }

    public static IPAddress valueOf(String address) {
        try {
            return valueOf(InetAddress.getByName(address));
        } catch (UnknownHostException ignore) {
        }
        return null;
    }
}
