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

import com.craftfire.commons.util.VersionRange;

/**
 * An IPRange implementation for IPv4 intervals.
 */
public class IPv4IntervalRange implements IPRange {
    private final IPv4Address min, max;

    /**
     * Creates an IPv4IntervalRange object with given minimum and maximum address.
     * 
     * @param min  the minimum address
     * @param max  the maximum address
     */
    public IPv4IntervalRange(IPv4Address min, IPv4Address max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the minimum address of the interval.
     * 
     * @return the minimum address
     */
    public IPv4Address getMin() {
        return this.min;
    }

    /**
     * Returns the maximum address of the interval.
     * 
     * @return the maximum address
     */
    public IPv4Address getMax() {
        return this.max;
    }

    /**
     * Returns true, if the given IP address is an IPv4 address and is between min and max (including).
     */
    @Override
    public boolean isInRange(IPAddress address) {
        if (!address.isIPv4()) {
            return false;
        }
        return new VersionRange(this.min.toString(), this.max.toString()).inVersionRange(address.toIPv4().toString());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + getMin() + " - " + getMax() + "]";
    }
}
