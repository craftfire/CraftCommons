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

import com.craftfire.commons.classes.Version;
import com.craftfire.commons.classes.VersionRange;

public class IPv6IntervalRange implements IPRange {
    private final IPv6Address min, max;

    public IPv6IntervalRange(IPv6Address min, IPv6Address max) {
        this.min = min;
        this.max = max;
    }

    public IPv6Address getMin() {
        return this.min;
    }

    public IPv6Address getMax() {
        return this.max;
    }

    @Override
    public boolean isInRange(IPAddress address) {
        if (!address.isIPv6()) {
            return false;
        }
        return new VersionRange(getMin().toString(), getMax().toString(), ":").inVersionRange(new Version(address.toIPv6().toString(), ":"));
    }

    @Override
    public String toString() {
        return "[" + getMin() + " - " + getMax() + "]";
    }
}
