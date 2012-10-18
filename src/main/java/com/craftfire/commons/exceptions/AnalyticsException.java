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
package com.craftfire.commons.exceptions;

import com.craftfire.commons.Util;
import com.craftfire.commons.classes.AnalyticsData;
import com.craftfire.commons.managers.AnalyticsManager;

public class AnalyticsException extends Exception {
    private AnalyticsManager manager;

    public AnalyticsException(AnalyticsManager manager, String message) {
        super(message);
        this.manager = manager;
    }

    public AnalyticsManager getManager() {
        return this.manager;
    }

    public AnalyticsData getData() {
        return getManager().getData();
    }

    public int getResponseCode() {
        return Util.getResponseCode(getManager().getURL());
    }
}