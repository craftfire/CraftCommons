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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileDownloader {
    private Set<String> urls = new HashSet<String>();
    private URL url = null;
    private File ouputFile;
    private boolean successful = false;
    private float elapsedSeconds = 0;

    public FileDownloader(Set<String> urls, File outputFile) {
        this.urls = urls;
        this.ouputFile = outputFile;
        setURL();
    }

    public FileDownloader(Set<String> urls, String outputFile) {
        this(urls, new File(outputFile));
    }

    public FileDownloader(String url, String outputFile) {
        this(new HashSet<String>(Arrays.asList(new String[]{url})), outputFile);
    }

    public FileDownloader(String url, File outputFile) {
        this(new HashSet<String>(Arrays.asList(new String[]{url})), outputFile);
    }

    public void addURL(String url) {
        this.urls.add(url);
    }

    public URL getURL () {
        return this.url;
    }

    public File getOuputFile() {
        return this.ouputFile;
    }

    public boolean hasMirror() {
        return getURL() != null;
    }

    public boolean successfulDownload() {
        return this.successful;
    }

    public float getElapsedSeconds() {
        return this.elapsedSeconds;
    }

    public long getFileSize() {
        return getOuputFile().length();
    }

    public void download() throws IOException {
        long start = System.currentTimeMillis();
        if (hasMirror()) {
            BufferedInputStream in = null;
            FileOutputStream fout = null;
            try {
                in = new BufferedInputStream(getURL().openStream());
                fout = new FileOutputStream(getOuputFile());

                byte data[] = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1) {
                    fout.write(data, 0, count);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (fout != null) {
                    fout.close();
                }
                this.successful = true;
            }
        } else {
            //TODO: logging, throw exception?
        }
        this.elapsedSeconds = (System.currentTimeMillis() - start) / 1000F;
    }

    private void setURL() {
        URL chosen = null;
        for (String url : this.urls) {
            try {
                chosen = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) chosen.openConnection();
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    break;
                }
            } catch (MalformedURLException ignored) {
                chosen = null;
            } catch (IOException ignored) {
                chosen = null;
            }
        }
        this.url = chosen;
    }
}