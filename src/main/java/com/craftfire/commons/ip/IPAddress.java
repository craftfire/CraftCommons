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
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A class that represents an IP address (either IPv4 or IPv6).
 */
public abstract class IPAddress {
    /**
     * Checks if this address is an IPv4 address.
     * 
     * @return true if IPv4 address, false if not
     */
    public abstract boolean isIPv4();

    /**
     * Checks if this address is an IPv6 address.
     * 
     * @return true if IPv6 address, false if not
     */
    public abstract boolean isIPv6();

    /**
     * Returns an {@link InetAddress} object for this address.
     * 
     * @return an InetAddress object
     */
    public abstract InetAddress getInetAddress();

    /**
     * Converts this address (if necessary) to IPv4 address.
     * 
     * @return an IPv4 address
     */
    public abstract IPv4Address toIPv4();

    /**
     * Converts this address (if necessary) to IPv6 address.
     * 
     * @return an IPv6 address
     */
    public abstract IPv6Address toIPv6();

    /**
     * Returns a byte representation of this IP address
     * 
     * @return a byte representation
     */
    abstract byte[] getBytes();

    /**
     * Checks if this address is in specified range.
     * 
     * @param range  the range to check
     * @return       true if in range, false if not
     */
    public boolean isInRange(IPRange range) {
        return range.isInRange(this);
    }

    /**
     * Returns an IPAddress object for given InetAddress.
     * 
     * @param address  the InetAddress to convert
     * @return         an IPAddress object
     */
    public static IPAddress valueOf(InetAddress address) {
        if (address instanceof Inet4Address) {
            return new IPv4Address((Inet4Address) address);
        } else if (address instanceof Inet6Address) {
            return new IPv6Address((Inet6Address) address);
        }
        return null;
    }

    /**
     * Parses an IP address from string.
     * 
     * @param address  the string representation of IP address
     * @return         an IPAddress object
     */
    public static IPAddress valueOf(String address) {
        try {
            return valueOf(InetAddress.getByName(address));
        } catch (UnknownHostException ignore) {
        }
        return null;
    }
}
