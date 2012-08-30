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
package com.craftfire.commons.managers;

import com.boxysystems.jgoogleanalytics.FocusPoint;
import com.boxysystems.jgoogleanalytics.JGoogleAnalyticsTracker;
import com.boxysystems.jgoogleanalytics.LoggingAdapter;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsManager {
    protected final JGoogleAnalyticsTracker tracker;
    private String title, version, id;
    private List<FocusPoint> points = new ArrayList<FocusPoint>();
    private LoggingManager loggingManager;

    public AnalyticsManager(String title, String version, String id) {
        this.title = title;
        this.version = version;
        this.id = id;
        this.tracker = new JGoogleAnalyticsTracker(title, version, id);
        this.loggingManager = new LoggingManager("CraftFire.AnalyticsManager", "[AnalyticsManager]");
        LoggingAdapter loggingAdapter = new LoggingAdapter() {
            @Override
            public void logError(String s) {
                loggingManager.error(s);
            }

            @Override
            public void logMessage(String s) {
                loggingManager.debug(s);
            }
        };
        this.tracker.setLoggingAdapter(loggingAdapter);
    }

    public LoggingManager getLoggingManager() {
        return this.loggingManager;
    }

    public String getTitle() {
        return this.title;
    }

    public String getVersion() {
        return this.version;
    }

    public String getID() {
        return this.id;
    }

    public void addPoint(String name) {
        this.points.add(new FocusPoint(name));
    }

    public List<FocusPoint> getPoints() {
        return this.points;
    }

    public void start() {
        for (FocusPoint focusPoint : this.points) {
            this.tracker.trackAsynchronously(focusPoint);
        }
    }
}
