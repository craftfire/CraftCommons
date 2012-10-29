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
package com.craftfire.commons.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util {

    /**
     * Checks if the string is an email.
     *
     * @param string The email.
     * @return true, if the string is an email.
     */
    public static boolean isEmail(String string) {
        if (string != null && ! string.isEmpty()) {
            Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher m = p.matcher(string);
            if (m.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if string is a valid IP. Wildcards (*) are allowed.
     *
     * @param string The IP as a string.
     * @return true, if string is an IP.
     */
    public static boolean isIP(String string) {
        if (string != null && ! string.isEmpty()) {
            String[] parts = string.split("\\.");
            if (parts.length == 4) {
                for (String s : parts) {
                    if (s.equals("*")) {
                        continue;
                    }
                    if (! isInteger(s)) {
                        return false;
                    }
                    int i = Integer.parseInt(s);
                    if (i < 0 || i > 255) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if input is an integer.
     *
     * @param input The string to parse/check.
     * @return true, if the input is an integer.
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if input is a Long object.
     *
     * @param input The string to parse/check.
     * @return true, if the input is a Long object.
     */
    public static boolean isLong(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isURLOnline(URL url) {
        return Util.getResponseCode(url) == 200;
    }

    public static boolean isURLOnline(String urlString) {
        URL url = isValidURL(urlString);
        return url != null && isURLOnline(url);
    }

    public static URL isValidURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static int getResponseCode(String urlString) {
        try {
            return getResponseCode(new URL(urlString));
        } catch (MalformedURLException e) {
            return 0;
        }
    }

    public static int getResponseCode(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
            connection.connect();
            return connection.getResponseCode();
        } catch (IOException e) {
            return 0;
        }
    }
}
