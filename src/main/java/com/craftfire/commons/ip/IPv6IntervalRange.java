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
        // TODO: Check if address is IPv6 and decide what to do if it's not.
        return new VersionRange(getMin().toString(), getMax().toString(), ":").inVersionRange(new Version(address.toIPv6().toString(), ":"));
    }

    @Override
    public String toString() {
        return "[" + getMin() + " - " + getMax() + "]";
    }
}
