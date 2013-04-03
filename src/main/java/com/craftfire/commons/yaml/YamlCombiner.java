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
package com.craftfire.commons.yaml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.craftfire.commons.util.LoggingManager;

/**
 * A YamlManager that combines multiple YamlManagers into one.
 * <p>
 * It allows to load yaml nodes from multiple files and treat them as one document, and then save them back to the files they came from.
 */
public class YamlCombiner implements YamlManager {
    private Set<YamlManager> managers = new HashSet<YamlManager>();
    private YamlManager defaultManager;
    private Settings settings = new Settings();

    /**
     * Creates a new YamlCombiner
     */
    public YamlCombiner() {
    }

    /**
     * Returns set of all YamlManagers used by the combiner.
     * 
     * @return set of YamlManagers
     */
    public Set<YamlManager> getYamlManagers() {
        return new HashSet<YamlManager>(this.managers);
    }

    /**
     * Sets the set of YamlManagers to use.
     * <p>
     * If {@code managers} is null, sets the set of YamlManagers to an empty set.
     * <p>
     * Sets the first YamlManager returned by iterator of {@code managers} as the default manager, or null if {@code managers} is empty.
     * 
     * @param managers  collection of YamlManagers
     */
    public void setYamlManagers(Collection<YamlManager> managers) {
        if (managers == null || managers.isEmpty()) {
            this.managers = new HashSet<YamlManager>();
            this.defaultManager = null;
        } else {
            this.managers = new HashSet<YamlManager>(managers);
            this.defaultManager = managers.iterator().next();
        }
    }

    /**
     * Adds a YamlManager to this combiner.
     * <p>
     * If the default YamlManager is null, sets it to given {@code manager}.
     * 
     * @param  manager                  the YamlManager to add
     * @throws IllegalArgumentException if the {@code manager} is null
     */
    public void addYamlManager(YamlManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("The manager must not be null!");
        }
        this.managers.add(manager);
        if (this.defaultManager == null) {
            this.defaultManager = manager;
        }
    }

    /**
     * Returns the default YamlManager of this combiner.
     * <p>
     * All new nodes that don't match elsewhere will be added to the default YamlManager.
     * <p>
     * If the default manager is not set, this method will set it to the first one in the set. If the set is empty, will return {@code null}.
     * 
     * @return the default YamlManager or null
     */
    public YamlManager getDefaultManager() {
        /*if (this.defaultManager == null) {
             if (!this.managers.isEmpty()) {
                 this.defaultManager = this.managers.iterator().next(); // TODO: Unreachable?
             }
         }*/
        return this.defaultManager;
    }

    /**
     * Sets the default YamlManager of this combiner.
     * <p>
     * All new nodes that don't match elsewhere will be added to the default YamlManager.
     * <p>
     * The given YamlManager must be already added to the set of managers to use.
     * 
     * @param  manager                  the YamlManager to set as default
     * @throws IllegalArgumentException if the manager is not in the set
     * @see    #addYamlManager(YamlManager)
     */
    public void setDefaultManager(YamlManager manager) {
        if (!this.managers.contains(manager)) {
            throw new IllegalArgumentException("You must add a the manager before setting it as default.");
        }
        this.defaultManager = manager;
    }

    /**
     * Returns the default settings used to create new YamlManagers by {@link #load(File)} and {@link #load(String)} methods.
     * 
     * @return the default settings
     */
    public Settings getDefaultSettings() {
        return this.settings;
    }

    /**
     * Sets the default settings to be used to create new YamlManagers by {@link #load(File)} and {@link #load(String)} methods.
     * 
     * @param settings  the default settings
     */
    public void setDefaultSettings(Settings settings) {
        this.settings = settings;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getFiles()
     */
    @Override
    public Set<File> getFiles() {
        Set<File> files = new HashSet<File>();
        for (YamlManager manager : this.managers) {
            files.addAll(manager.getFiles());
        }
        return files;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getLogger()
     */
    @Override
    public LoggingManager getLogger() {
        return this.settings.getLogger();
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#setLoggingManager(com.craftfire.commons.util.LoggingManager)
     */
    @Override
    public void setLoggingManager(LoggingManager loggingManager) {
        this.settings.setLogger(loggingManager);
        for (YamlManager manager : this.managers) {
            manager.setLoggingManager(loggingManager);
        }
    }

    /**
     * Returns the root node of the default YamlManager of the combiner.
     * 
     * @see #setDefaultManager(YamlManager)
     * @see #getDefaultManager()
     */
    @Override
    @Deprecated
    public YamlNode getRootNode() {
        return this.defaultManager.getRootNode();
    }

    /**
     * Sets the root node of the default YamlManager of the combiner.
     * 
     * @see #setDefaultManager(YamlManager)
     * @see #getDefaultManager()
     */
    @Override
    public YamlNode setRootNode(YamlNode node) {
        this.managers = new HashSet<YamlManager>();
        this.managers.add(this.defaultManager);
        return this.defaultManager.setRootNode(node);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#exist(java.lang.String)
     */
    @Override
    public boolean exist(String node) {
        for (YamlManager manager : this.managers) {
            if (manager.exist(node)) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getBoolean(java.lang.String)
     */
    @Override
    public boolean getBoolean(String node) {
        return getBoolean(node, false);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getBoolean(java.lang.String, boolean)
     */
    @Override
    public boolean getBoolean(String path, boolean defaultValue) {
        try {
            YamlNode node = getNode(path);
            if (node != null) {
                boolean value = node.getBool(defaultValue);
                getLogger().debug("Found node '" + path + "' with Boolean value '" + value + "', default value is '" + defaultValue + "'");
                return value;
            }
        } catch (YamlException e) {
            getLogger().stackTrace(e);
        }
        getLogger().debug("Could not find node '" + path + "', returning default value instead '" + defaultValue + "'.");
        return defaultValue;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getString(java.lang.String)
     */
    @Override
    public String getString(String node) {
        return getString(node, null);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getString(java.lang.String, java.lang.String)
     */
    @Override
    public String getString(String path, String defaultValue) {
        try {
            YamlNode node = getNode(path);
            if (node != null) {
                String value = node.getString(defaultValue);
                getLogger().debug("Found node '" + path + "' with String value '" + value + "', default value is '" + defaultValue + "'");
                return value;
            }
        } catch (YamlException e) {
            getLogger().stackTrace(e);
        }
        getLogger().debug("Could not find node '" + path + "', returning default value instead '" + defaultValue + "'.");
        return defaultValue;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getInt(java.lang.String)
     */
    @Override
    public int getInt(String node) {
        return getInt(node, 0);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getInt(java.lang.String, int)
     */
    @Override
    public int getInt(String path, int defaultValue) {
        try {
            YamlNode node = getNode(path);
            if (node != null) {
                int value = node.getInt(defaultValue);
                getLogger().debug("Found node '" + path + "' with Integer value '" + value + "', default value is '" + defaultValue + "'");
                return value;
            }
        } catch (YamlException e) {
            getLogger().stackTrace(e);
        }
        getLogger().debug("Could not find node '" + path + "', returning default value instead '" + defaultValue + "'.");
        return defaultValue;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getLong(java.lang.String)
     */
    @Override
    public long getLong(String node) {
        return getLong(node, 0);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getLong(java.lang.String, long)
     */
    @Override
    public long getLong(String path, long defaultValue) {
        try {
            YamlNode node = getNode(path);
            if (node != null) {
                long value = node.getLong(defaultValue);
                getLogger().debug("Found node '" + path + "' with Long value '" + value + "', default value is '" + defaultValue + "'");
                return value;
            }
        } catch (YamlException e) {
            getLogger().stackTrace(e);
        }
        getLogger().debug("Could not find node '" + path + "', returning default value instead '" + defaultValue + "'.");
        return defaultValue;
    }

    /**
     * Adds all nodes from given YamlManager to the default YamlManager of the combiner.
     * 
     * @see #setDefaultManager(YamlManager)
     * @see #getDefaultManager()
     */
    @Override
    public void addNodes(YamlManager yamlManager) throws YamlException {
        getDefaultManager().addNodes(yamlManager);
    }

    /**
     * Adds all nodes from given map to the default YamlManager of the combiner.
     * 
     * @see #setDefaultManager(YamlManager)
     * @see #getDefaultManager()
     */
    @Override
    public void addNodes(Map<String, Object> map) throws YamlException {
        getDefaultManager().addNodes(map);
    }

    /**
     * Sets node with the given path to given value.
     * <p>
     * If the node doesn't exist, the method tries to add it to the YamlManager with most matching node. If the node doesn't match anywhere, then it's added to the default YamlManager of the combiner.
     * 
     * @see #setDefaultManager(YamlManager)
     * @see #getDefaultManager()
     */
    @Override
    public void setNode(String node, Object value) throws YamlException {
        if (exist(node)) {
            getNode(node).setValue(value);
        } else {
            int last = node.lastIndexOf(this.settings.getSeparator());
            String left = "";
            String found = node;

            while (last >= 0) {
                left = found.substring(last) + left;
                found = found.substring(0, last);
                if (exist(found)) {
                    getNode(found).getChild(left.substring(1), true).setValue(value);
                    return;
                }
                last = found.lastIndexOf(this.settings.getSeparator());
            }
            getDefaultManager().setNode(node, value);
        }
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getNode(java.lang.String)
     */
    @Override
    public YamlNode getNode(String node) throws YamlException {
        for (YamlManager manager : this.managers) {
            if (manager.exist(node)) {
                return manager.getNode(node);
            }
        }
        return null;
    }

    @Deprecated
    @Override
    public Map<String, Object> getNodes() {
        Map<String, Object> map = new HashMap<String, Object>();
        for (YamlNode node : getFinalNodeList()) {
            map.put(node.getPath(), node.getValue());
        }
        return map;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getFinalNodeList()
     */
    @Override
    public List<YamlNode> getFinalNodeList() {
        List<YamlNode> list = new ArrayList<YamlNode>();
        for (YamlManager manager : this.managers) {
            list.addAll(manager.getFinalNodeList());
        }
        return list;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getFinalNodeCount()
     */
    @Override
    public int getFinalNodeCount() {
        int count = 0;
        for (YamlManager manager : this.managers) {
            count += manager.getFinalNodeCount();
        }
        return count;
    }

    /**
     * @return {@code true} saved successfully at least one YamlManager, {@code false} otherwise
     */
    @Override
    public boolean save() {
        boolean result = false;
        for (YamlManager manager : this.managers) {
            result = manager.save() || result;
        }
        return result;
    }

    /**
     * @return {@code true} if at least one YamlManager loaded it's document successfully, {@code false} otherwise
     */
    @Override
    public boolean load() {
        boolean result = false;
        for (YamlManager manager : this.managers) {
            result = manager.load() || result;
        }
        return result;
    }

    /**
     * Creates a new SimpleYamlManager with the given file and adds it to the set.
     * <p>
     * Uses the default settings of the combiner to create the manager.
     * 
     * @param  file        the yaml file to load
     * @throws IOException if an IOException occurred
     * @see     #addYamlManager(YamlManager)
     * @see     #setDefaultSettings(Settings)
     * @see     SimpleYamlManager#SimpleYamlManager(File, Settings)
     */
    public void load(File file) throws IOException {
        YamlManager mgr = new SimpleYamlManager(file, this.settings);
        mgr.load();
        addYamlManager(mgr);
    }

    /**
     * Creates a new SimpleYamlManager with the given classpath resource and adds it to the set.
     * <p>
     * Uses the default settings of the combiner to create the manager.
     * 
     * @param  path        path to the resource in classpath to load the yaml from
     * @throws IOException if an IOException occurred
     * @see    #addYamlManager(YamlManager)
     * @see    #setDefaultSettings(Settings)
     * @see    SimpleYamlManager#SimpleYamlManager(String, Settings)
     */
    public void load(String path) throws IOException {
        YamlManager mgr = new SimpleYamlManager(path, this.settings);
        mgr.load();
        addYamlManager(mgr);
    }

}
