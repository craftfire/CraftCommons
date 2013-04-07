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

import com.craftfire.commons.util.LoggingManager;
import com.craftfire.commons.util.Util;

import java.io.*;
import java.net.*;

/**
 * The manager for analytics reporting.
 * <p>
 * To submit the data use the {@link #submit()} method.
 *
 * @see AnalyticsData
 * TODO: create a PHP script for reporting.
 */
public class AnalyticsManager {
    private URL url;
    private AnalyticsData data;
    private LoggingManager loggingManager = new LoggingManager("CraftFire.AnalyticsManager", "[AnalyticsManager]");

    /**
     * Constructor for AnalyticsManager class.
     *
     * @param url      URL to where the PHP script is located.
     * @param name     name of the item that is being reported
     * @param version  version of the item that is being reported
     */
    public AnalyticsManager(URL url, String name, String version) {
        this.url = url;
        this.data = new AnalyticsData(name, version);
    }

    /**
     * Constructor for AnalyticsManager class.
     *
     * @param url      String url to where the PHP script is located.
     * @param name     name of the item that is being reported
     * @param version  version of the item that is being reported
     */
    public AnalyticsManager(String url, String name, String version) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            getLogger().stackTrace(e);
        }
        this.data = new AnalyticsData(name, version);
    }

    /**
     * Submit the data to the {@link #url} without throwing any exceptions.
     */
    public void submitVoid() {
        if (Util.isURLOnline(getURL())) {
            try {
                String dataString = getParameters();
                HttpURLConnection connection = (HttpURLConnection) getURL().openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(dataString.getBytes().length));
                connection.setUseCaches(false);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
                wr.writeBytes(dataString);
                wr.flush();
                wr.close();
                connection.disconnect();
            } catch (IOException e) {
                getLogger().stackTrace(e);
            }
        }
        String error = getURL().toString() + " did not return HTTP Status 200, status returned was: " +
                       Util.getResponseCode(getURL()) + ".";
        getLogger().error(error);
    }

    /**
     * Submit the data to the {@link #url}.
     *
     * @throws AnalyticsException  if submitting the data failed
     * @throws IOException         if submitting the data failed
     */
    public void submit() throws AnalyticsException, IOException {
        if (Util.isURLOnline(getURL())) {
            String dataString = getParameters();
            HttpURLConnection connection = (HttpURLConnection) getURL().openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(dataString.getBytes().length));
            connection.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.writeBytes(dataString);
            wr.flush();
            wr.close();
            connection.disconnect();
        }
        String error = getURL().toString() + " did not return HTTP Status 200, status returned was: " +
                Util.getResponseCode(getURL()) + ".";
        getLogger().error(error);
        throw new AnalyticsException(this, error);
    }

    /**
     * Returns a UTF-8 formatted string of all the data for the POST request.
     *
     * @return UTF-8 formatted string of all the data
     */
    public String getParameters() {
        String dataString = getData().getName().getKeyUTF8() + "=" + getData().getName().getValueUTF8() + "&"
                    + getData().getVersion().getKeyUTF8() + "=" + getData().getVersion().getValueUTF8() + "&"
                    + getData().getOSName().getKeyUTF8() + "=" + getData().getOSName().getValueUTF8() + "&"
                    + getData().getOSVersion().getKeyUTF8() + "=" + getData().getOSVersion().getValueUTF8() + "&"
                    + getData().getOSArch().getKeyUTF8() + "=" + getData().getOSArch().getValueUTF8() + "&"
                    + getData().getMaxMemory().getKeyUTF8() + "=" + getData().getMaxMemory().getValueUTF8() + "&"
                    + getData().getTotalMemory().getKeyUTF8() + "=" + getData().getTotalMemory().getValueUTF8() + "&"
                    + getData().getJavaVersion().getKeyUTF8() + "=" + getData().getJavaVersion().getValueUTF8() + "&";
        for (AnalyticsItem item : getData().getCustomData().values()) {
            dataString += item.getKeyUTF8() + "=" + item.getValueUTF8() + "&";
        }
        return dataString.substring(0, dataString.length() - 1);
    }

    /**
     * Returns the {@link LoggingManager} of the manager, creates a new one if one doesn't exist already.
     *
     * @return the LoggingManager
     */
    public LoggingManager getLogger() {
        if (this.loggingManager == null) {
            this.loggingManager = new LoggingManager("CraftFire.AnalyticsManager", "[AnalyticsManager]");
        }
        return this.loggingManager;
    }

    /**
     * Sets the LoggingManager.
     *
     * @param loggingManager  the LoggingManager
     */
    public void setLoggingManager(LoggingManager loggingManager) {
        if (loggingManager == null) {
            throw new IllegalArgumentException("Parameter 'loggingManager' cannot be null.");
        }
        this.loggingManager = loggingManager;
    }

    /**
     * Returns the data that is being reported.
     *
     * @return data that is being reported
     */
    public AnalyticsData getData() {
        return this.data;
    }

    /**
     * Adds a data item to the reporting.
     *
     * @param key    key of the data item
     * @param value  value of the data item
     */
    public void addData(String key, String value) {
        getData().add(key, value);
    }

    /**
     * Returns the submit URL.
     *
     * @return submit URL
     */
    public URL getURL() {
        return this.url;
    }

    /**
     * Sets the submit URL.
     *
     * @param url  submit URL
     */
    public void setURL(URL url) {
        this.url = url;
    }
}
