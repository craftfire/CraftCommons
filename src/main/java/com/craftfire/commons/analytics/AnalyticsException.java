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
package com.craftfire.commons.analytics;

import com.craftfire.commons.util.Util;

/**
 * This exception is thrown by {@link AnalyticsManager}.
 */
public class AnalyticsException extends Exception {
    private AnalyticsManager manager;

    /**
     * Default constructor.
     *
     * @param manager  the manager that threw the exception
     * @param message  the message
     */
    public AnalyticsException(AnalyticsManager manager, String message) {
        super(message);
        this.manager = manager;
    }

    /**
     * Returns the {@link AnalyticsManager} of this exception.
     *
     * @return manager of this exception
     */
    public AnalyticsManager getManager() {
        return this.manager;
    }

    /**
     * Returns the data of this exception.
     *
     * @return data of this exception
     */
    public AnalyticsData getData() {
        return getManager().getData();
    }

    /**
     * Returns the HTTP response code of the URL used in the manager.
     *
     * @return HTTP response code of the URL used in the manager
     */
    public int getResponseCode() {
        return Util.getResponseCode(getManager().getURL());
    }
}