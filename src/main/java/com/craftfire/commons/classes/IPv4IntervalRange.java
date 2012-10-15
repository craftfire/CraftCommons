package com.craftfire.commons.classes;

public class IPv4IntervalRange implements IPRange {
    private final IPv4Address min, max;

    public IPv4IntervalRange(IPv4Address min, IPv4Address max) {
        this.min = min;
        this.max = max;
    }

    public IPv4Address getMin() {
        return this.min;
    }

    public IPv4Address getMax() {
        return this.max;
    }

    @Override
    public boolean isInRange(IPAddress address) {
        return new VersionRange(this.min.toString(), this.max.toString()).inVersionRange(address.toString());
    }

    @Override
    public String toString() {
        return "[" + getMin() + " - " + getMax() + "]";
    }
}
