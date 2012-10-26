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

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A class that represents an IPv6 address.
 */
public class IPv6Address extends IPAddress {
    private final short[] data;
    
    /**
     * Creates an IPv6Address object for given Inet6Address.
     * 
     * @param address  the Inet6Address
     * @see            IPAddress#valueOf(InetAddress)
     */
    public IPv6Address(Inet6Address address) {
        ByteBuffer buffer = ByteBuffer.wrap(address.getAddress());
        this.data = new short[8];
        buffer.asShortBuffer().get(this.data);
    }

    /**
     * Creates an IPv4Address object form given byte representation.
     * 
     * @param  bytes                     byte representation of the IPv6 address, <b>must be 16 byte long</b>
     * @throws IllegalArgumentException  if the number of bytes is different than 16
     */
    public IPv6Address(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("IPv6 Address must be 16 byte long!");
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        this.data = new short[8];
        buffer.asShortBuffer().get(this.data);
    }

    /**
     * Creates an IPv4Address object from four integers corresponding to four parts of IPv6 address (usually separated by colons).
     * 
     * @param  address                   short integer parts of the IPv6 address, <b>must be exactly 8 elements</b>
     * @throws IllegalArgumentException  if the number of shorts is different than 8
     */
    public IPv6Address(short... address) {
        if (address.length != 8) {
            throw new IllegalArgumentException("IPv6 Address must have 8 parts!");
        }
        this.data = address.clone();
    }

    /**
     * Creates an IPv4Address object from four integers corresponding to four parts of IPv6 address (usually separated by colons).
     * 
     * @param  address                   integer parts of the IPv6 address, <b>must be exactly 8 elements</b>
     * @throws IllegalArgumentException  if the number of ints is different than 8
     */
    public IPv6Address(int... address) {
        if (address.length != 8) {
            throw new IllegalArgumentException("IPv6 Address must have 8 parts!");
        }
        this.data = new short[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < 8; ++i) {
            this.data[i] = (short) address[i];
        }
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#isIPv4()
     */
    @Override
    public boolean isIPv4() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#isIPv6()
     */
    @Override
    public boolean isIPv6() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#getInetAddress()
     */
    @Override
    public Inet6Address getInetAddress() {
        try {
            InetAddress address = InetAddress.getByAddress(getBytes());
            if (address instanceof Inet6Address) {
                return (Inet6Address) address;
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
        InetAddress inet = getInetAddress();
        if (inet.isLoopbackAddress()) {
            return new IPv4Address(127, 0, 0, 1);
        } else if (inet.isAnyLocalAddress()) {
            return new IPv4Address(0, 0, 0, 0);
        } else if (this.data[0] == (short) 0x2002 || this.data[0] == (short) 0xfe80) {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putShort(this.data[6]).putShort(this.data[7]);
            return new IPv4Address(buffer.array());
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#toIPv6()
     */
    @Override
    public IPv6Address toIPv6() {
        return this;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.ip.IPAddress#getBytes()
     */
    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.asShortBuffer().put(this.data);
        return buffer.array();
    }

    /**
     * Returns four shorts, one for each part of IPv6 address (usually separated by colons).
     * 
     * @return  four shorts, one for each part of IPv6 address
     */
    public short[] getAddress() {
        return this.data.clone();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            builder.append(Integer.toHexString((char) this.data[i]));
            if (i < 7) {
                builder.append(":");
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
        if (obj instanceof IPv6Address) {
            return Arrays.equals(this.data, ((IPv6Address) obj).getAddress());
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
}
