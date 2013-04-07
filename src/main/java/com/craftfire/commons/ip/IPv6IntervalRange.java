/*
 * This file is part of CraftCommons.
 *
 * Copyright (c) 2011 CraftFire <http://www.craftfire.com/>
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

import com.craftfire.commons.util.Version;
import com.craftfire.commons.util.VersionRange;

/**
 * An IPRange implementation for IPv6 intervals.
 */
public class IPv6IntervalRange implements IPRange {
    private final IPv6Address min, max;

    /**
     * Creates an IPv6IntervalRange object with given minimum and maximum address.
     * 
     * @param min  the minimum address
     * @param max  the maximum address
     */
    public IPv6IntervalRange(IPv6Address min, IPv6Address max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the minimum address of the interval.
     * 
     * @return the minimum address
     */
    public IPv6Address getMin() {
        return this.min;
    }

    /**
     * Returns the minimum address of the interval.
     * 
     * @return the minimum address
     */
    public IPv6Address getMax() {
        return this.max;
    }

    /**
     * Returns true, if the given IP address is an IPv4 address and is between min and max (including).
     */
    @Override
    public boolean isInRange(IPAddress address) {
        if (!address.isIPv6()) {
            return false;
        }
        return new VersionRange(getMin().toString(), getMax().toString(), ":", 16).inVersionRange(new Version(address.toIPv6().toString(), ":", 16));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + getMin() + " - " + getMax() + "]";
    }
}
