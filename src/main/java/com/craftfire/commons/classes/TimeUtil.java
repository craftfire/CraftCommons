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

import java.util.Arrays;
import java.util.List;

public class TimeUtil {
    private final TimeUnit unit;
    private final int amount;

    enum TimeUnit {
        SECOND(new String[]{"seconds", "sec", "secs", "s"}, 20, 1),
        MINUTE(new String[]{"minutes", "min", "mins", "m"}, 1200, 60),
        HOUR(new String[]{"hours", "hr", "hrs", "h"}, 72000, 3600),
        DAY(new String[]{"days", "d"}, 1728000, 86400);

        private String[] aliases;
        private int seconds;
        private int ticks;
        TimeUnit(String[] aliases, int seconds, int ticks) {
            this.aliases = aliases;
            this.seconds = seconds;
            this.ticks = ticks;
        }

        public List<String> getAliases() {
            return Arrays.asList(this.aliases);
        }

        public String getName() {
            return this.name();
        }

        public int getSeconds() {
            return this.seconds;
        }

        public int getTicks() {
            return this.ticks;
        }

        public static TimeUnit getUnit(String name) {
            for (TimeUnit unit : TimeUnit.values()) {
                if (unit.getAliases().contains(name.toLowerCase())) {
                    return unit;
                }
            }
            return null;
        }
    }

    public TimeUtil(TimeUnit unit, int amount) {
        this.unit = unit;
        this.amount = amount;
    }

    public TimeUtil(String unit, int amount) {
        this.unit = TimeUnit.getUnit(unit.toLowerCase());
        this.amount = amount;
    }

    public TimeUnit getUnit() {
        return this.unit;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getTicks() {
        return getUnit().getTicks() * getAmount();
    }

    public int getSeconds() {
        return getUnit().getSeconds() * getAmount();
    }
}
