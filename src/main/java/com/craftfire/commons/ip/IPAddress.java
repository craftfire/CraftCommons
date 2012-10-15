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
