package com.craftfire.commons.ip;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class IPv4Address extends IPAddress {
    private final byte[] data;

    public IPv4Address(Inet4Address address) {
        this.data = address.getAddress();
    }

    public IPv4Address(byte... address) {
        if (address.length != 4) {
            throw new IllegalArgumentException("IPv4 Address must have 4 parts!");
        }
        this.data = address.clone();
    }

    public IPv4Address(int... address) {
        if (address.length != 4) {
            throw new IllegalArgumentException("IPv4 Address must have 4 parts!");
        }
        this.data = new byte[] { 0, 0, 0, 0 };
        for (int i = 0; i < 4; ++i) {
            this.data[i] = (byte) address[i];
        }
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
    public Inet4Address getInetAddress() {
        try {
            InetAddress address = InetAddress.getByAddress(this.data);
            if (address instanceof Inet4Address) {
                return (Inet4Address) address;
            }
        } catch (UnknownHostException ignore) {
        }
        return null;
    }

    @Override
    public IPv4Address toIPv4() {
        return this;
    }

    @Override
    public IPv6Address toIPv6() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        InetAddress inet = getInetAddress();
        if (inet.isAnyLocalAddress()) {
            return new IPv6Address(0, 0, 0, 0, 0, 0, 0, 0);
        } else if (inet.isLoopbackAddress()) {
            return new IPv6Address(0, 0, 0, 0, 0, 0, 0, 1);
        } else if (inet.isSiteLocalAddress()) {
            buffer.putShort((short) 0xfe80);
        } else {
            buffer.putShort((short) 0x2002);
        }
        short[] zeros = new short[5];
        Arrays.fill(zeros, (short) 0);
        buffer.asShortBuffer().put(zeros);
        buffer.put(this.data);
        return new IPv6Address(buffer.asShortBuffer().array());
    }

    @Override
    public byte[] getBytes() {
        return getAddress();
    }

    public byte[] getAddress() {
        return this.data.clone();
    }

    public int getInt() {
        return (this.data[0] << 24) | (this.data[1] << 16) | (this.data[2] << 8) | this.data[3];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            builder.append(this.data[i]);
            builder.append(".");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IPv4Address) {
            return Arrays.equals(this.data, ((IPv4Address) obj).getAddress());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }
}
