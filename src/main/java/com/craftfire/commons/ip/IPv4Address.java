/*
 * This file is part of CraftCommons.
 *
 * Copyright (c) 2011-2012, CraftFire <http://www.craftfire.com/>
 * CraftCommons is licensed under the GNU Lesser General Public License.
 *
 * CraftCommons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CraftCommons is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftfire.commons.ip;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A class that represents an IPv4 address.
 */
public class IPv4Address extends IPAddress {
    private final byte[] data;

    /**
     * Creates an IPv4Address object for given Inet4Address.
     * 
     * @param address  the Inet4Address
     * @see            IPAddress#valueOf(InetAddress)
     */
    public IPv4Address(Inet4Address address) {
        this.data = address.getAddress();
    }

    /**
     * Creates an IPv4Address object form given byte representation.
     * 
     * @param  address                   byte representation of the IPv4 address, <b>must be 4 byte long</b>
     * @throws IllegalArgumentException  if the number of bytes is different than 4
     */
    public IPv4Address(byte... address) {
        if (address.length != 4) {
            throw new IllegalArgumentException("IPv4 Address must have 4 parts!");
        }
        this.data = address.clone();
    }

    /**
     * Creates an IPv4Address object from four integers corresponding to four parts of IPv4 address (usually seperated by dots).
     * 
     * @param  address                   integer parts of the IPv4 address, <b>must be exactly 4 elements</b>
     * @throws IllegalArgumentException  if the number of ints is different than 4
     */
    public IPv4Address(int... address) {
        if (address.length != 4) {
            throw new IllegalArgumentException("IPv4 Address must have 4 parts!");
        }
        this.data = new byte[] { 0, 0, 0, 0 };
        for (int i = 0; i < 4; ++i) {
            this.data[i] = (byte) address[i];
        }
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#isIPv4()
     */
    @Override
    public boolean isIPv4() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#isIPv6()
     */
    @Override
    public boolean isIPv6() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#getInetAddress()
     */
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

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#toIPv4()
     */
    @Override
    public IPv4Address toIPv4() {
        return this;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#toIPv6()
     */
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
        buffer.position(12);
        buffer.put(this.data);
        short[] shorts = new short[8];
        buffer.flip();
        buffer.asShortBuffer().get(shorts, 0, 8);
        return new IPv6Address(shorts);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#getBytes()
     */
    @Override
    public byte[] getBytes() {
        return getAddress();
    }

    /**
     * Returns four bytes, one for each part of IPv4 address (usually separated by dots).
     * <p>
     * In this case it returns the same value as {@link #getBytes()}
     * 
     * @return  four bytes, one for each part of IPv4 address
     */
    public byte[] getAddress() {
        return this.data.clone();
    }

    /**
     * Returns the value of this address in single integer, which contains four bytes of the address.
     * 
     * @return  single integer representation of this address
     */
    public int getInt() {
        return (byteToUint(this.data[0]) << 24) | (byteToUint(this.data[1]) << 16) | (byteToUint(this.data[2]) << 8) | byteToUint(this.data[3]);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            builder.append(this.data[i] + ((this.data[i] < 0) ? 256 : 0));
            if (i < 3) {
                builder.append(".");
            }
        }
        return builder.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof IPv4Address) {
            return Arrays.equals(this.data, ((IPv4Address) obj).getAddress());
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    private static int byteToUint(byte b) {
        return b + ((b < 0) ? 256 : 0);
    }
}
