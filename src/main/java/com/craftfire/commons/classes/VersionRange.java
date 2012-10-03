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
package com.craftfire.commons.classes;


/**
 * A class representing a version range.
 */
public class VersionRange {
    private Version min, max;

    /**
     * Creates a version range with given minimal version and maximal version in string format, with default separator (<code>.</code>).
     * 
     * @param minVersion  the minimal version of the range
     * @param maxVersion  the maximal version of the range
     * @see               Version#Version(String)
     */
    public VersionRange(String minVersion, String maxVersion) {
        this.min = new Version(minVersion);
        this.max = new Version(maxVersion);
    }
    
    /**
     * Creates a version range with given minimal version and maximal version in string format, with the given <code>separator</code>
     * 
     * @param minVersion  the minimal version of the range
     * @param maxVersion  the maximal version of the range
     * @param separator   the separator to use
     * @see               Version#Version(String, String)
     */
    public VersionRange(String minVersion, String maxVersion, String separator) {
        this.min = new Version(minVersion, separator);
        this.max = new Version(maxVersion, separator);
    }
    
    /**
     * Creates a version range with given minimal version and maximal version.
     * 
     * @param minVersion  the minimal version of the range
     * @param maxVersion  the maximal version of the range
     */
    public VersionRange(Version minVersion, Version maxVersion) {
        this.min = minVersion;
        this.max = maxVersion;
    }

    /**
     * Returns the minimal version of the range
     * 
     * @return the minimal version of the range
     */
    public Version getMin() {
        return this.min;
    }

    /**
     * Returns the maximal version of the range
     * 
     * @return the maximal version of the range
     */
    public Version getMax() {
        return this.max;
    }

    /**
     * Checks if the given <code>version</code> in string format is within the range (between minimal and maximal version).
     * 
     * @param version  the version to check
     * @return         <code>true</code> if the version is within range, <code>false</code> if not
     */
    public boolean inVersionRange(String version) {
        return this.inVersionRange(new Version(version));
    }

    /**
     * Checks if the given <code>version</code> is within the range (between minimal and maximal version).
     * 
     * @param version  the version to check
     * @return         <code>true</code> if the version is within range, <code>false</code> if not
     */
    public boolean inVersionRange(Version version) {
        return (this.min.compareTo(version) <= 0) && (this.max.compareTo(version) >= 0);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + this.getMin() + " - " + this.getMax() + "]";
    }
}
