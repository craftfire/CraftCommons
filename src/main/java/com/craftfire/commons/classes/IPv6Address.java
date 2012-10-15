package com.craftfire.commons.classes;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class IPv6Address extends IPAddress {
    private final short[] data;
    
    public IPv6Address(Inet6Address address) {
        ByteBuffer buffer = ByteBuffer.wrap(address.getAddress());
        this.data = buffer.asShortBuffer().array();
    }

    public IPv6Address(short... address) {
        if (address.length != 8) {
            throw new IllegalArgumentException("IPv6 Address must have 8 parts!");
        }
        this.data = address.clone();
    }

    public IPv6Address(int... address) {
        if (address.length != 8) {
            throw new IllegalArgumentException("IPv6 Address must have 8 parts!");
        }
        this.data = new short[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < 8; ++i) {
            this.data[i] = (short) address[i];
        }
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
    
    @Override
    public InetAddress getInetAddress() {
        try {
            return InetAddress.getByName(toString());
        } catch (UnknownHostException ignore) {
        }
        return null;
    }

    @Override
    public IPv4Address toIPv4() {
        if (this.data[0] == 0x2002 || this.data[0] == 0xfe80) {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putShort(this.data[6]).putShort(this.data[7]);
            return new IPv4Address(buffer.array());
        }
        return null;
    }

    @Override
    public IPv6Address toIPv6() {
        return this;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.asShortBuffer().put(this.data);
        return buffer.array();
    }

    public short[] getAddress() {
        return this.data.clone();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            builder.append(Integer.toHexString(this.data[i]));
            builder.append(":");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IPv6Address) {
            return Arrays.equals(this.data, ((IPv6Address) obj).getAddress());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }
}
