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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import com.craftfire.commons.util.LoggingManager;

public class SimpleYamlManager implements YamlManager {
    private LoggingManager loggingManager;
    private File file = null;
    private String resource = null;
    private Reader reader = null;
    protected final Yaml yaml;
    private final boolean caseSensitive;
    @SuppressWarnings("unused")
    private final boolean multiDocument; // May be used in the future
    private final String separator;
    private YamlNode root;

    /**
     * Creates a new SimpleYamlManager with default settings and given file to load the document from.
     * <p>
     * Note that the constructor won't load the yaml itself. Do do this, you should use {@link #load()}.
     * 
     * @param  file        file to load yaml from
     */
    public SimpleYamlManager(File file) {
        this(file, new Settings());
    }

    /**
     * Creates a new SimpleYamlManager with given settings and file to load the document from.
     * <p>
     * Note that the constructor won't load the yaml itself. Do do this, you should use {@link #load()}.
     * 
     * @param  file        file to load yaml from
     * @param  settings    settings to use
     */
    public SimpleYamlManager(File file, Settings settings) {
        this.file = file;
        this.yaml = settings.createYaml();
        this.caseSensitive = settings.isCaseSensitive();
        this.multiDocument = settings.isMultiDocument();
        this.separator = settings.getSeparator();
        setLoggingManager(settings.getLogger());
    }

    /**
     * Creates a new SimpleYamlManager with default settings and given classpath resource to load the document from.
     * <p>
     * Note that the constructor won't load the yaml itself. Do do this, you should use {@link #load()}.
     * 
     * @param  path        path to the resource in classpath to load the yaml from
     */
    public SimpleYamlManager(String path) {
        this(path, new Settings());
    }

    /**
     * Creates a new SimpleYamlManager with given settings and loads yaml from the given classpath resource.
     * <p>
     * Note that the constructor won't load the yaml itself. Do do this, you should use {@link #load()}.
     * 
     * @param  path        path to the resource in classpath to load the yaml from
     * @param  settings    settings to use
     */
    public SimpleYamlManager(String path, Settings settings) {
        this.resource = path;
        this.yaml = settings.createYaml();
        this.caseSensitive = settings.isCaseSensitive();
        this.multiDocument = settings.isMultiDocument();
        this.separator = settings.getSeparator();
        setLoggingManager(settings.getLogger());
    }

    /**
     * Creates a new SimpleYamlManager with default settings and given input stream to load the document from.
     * <p>
     * Note that the constructor won't load the yaml itself. Do do this, you should use {@link #load()}.
     * 
     * @param  stream      stream to load the yaml from
     * @throws IOException if an IOException occurred
     */
    public SimpleYamlManager(InputStream stream) throws IOException {
        this(stream, new Settings());
    }

    /**
     * Creates a new SimpleYamlManager with given settings and input stream to load the document from.
     * <p>
     * Note that the constructor won't load the yaml itself. Do do this, you should use {@link #load()}.
     * 
     * @param  stream      stream to load the yaml from
     * @param  settings    settings to use
     * @throws IOException if an IOException occurred
     */
    public SimpleYamlManager(InputStream stream, Settings settings) throws IOException {
        this(new InputStreamReader(stream, "UTF-8"), settings);
    }

    /**
     * Creates a new SimpleYamlManager with default settings and given reader to load the yaml from.
     * <p>
     * Note that the constructor won't load the yaml itself. Do do this, you should use {@link #load()}.
     * 
     * @param  reader      reader to load the yaml from
     */
    public SimpleYamlManager(Reader reader) {
        this(reader, new Settings());
    }

    /**
     * Creates a new SimpleYamlManager with given settings and reader to load the yaml from.
     * <p>
     * Note that the constructor won't load the yaml itself. Do do this, you should use {@link #load()}.
     * 
     * @param  reader      reader to load the yaml from
     * @param  settings    settings to use
     */
    public SimpleYamlManager(Reader reader, Settings settings) {
        this.reader = reader;
        this.yaml = settings.createYaml();
        this.caseSensitive = settings.isCaseSensitive();
        this.multiDocument = settings.isMultiDocument();
        this.separator = settings.getSeparator();
        setLoggingManager(settings.getLogger());
    }

    /**
     * Returns the state of case-sensitive option (off by default).
     * <p>
     * If this option is on, node names will be case-sensitive.
     * 
     * @return true if on, otherwise false
     * @see Settings#isCaseSensitive()
     */
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    /**
     * Returns the path separator to be used (default {@code "."}).
     * 
     * @return the path separator
     * @see Settings#getSeparator()
     */
    public String getSeparator() {
        return this.separator;
    }

    /**
     * Returns path to the classpath resource used by this SimpleYamlManager, or null if not using a classpath resource.
     * 
     * @return path to the resource, or null
     */
    public String getResource() {
        return this.resource;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getFiles()
     */
    @Override
    public Set<File> getFiles() {
        Set<File> set = new HashSet<File>();
        if (this.file != null) {
            set.add(this.file);
        }
        return set;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getLogger()
     */
    @Override
    public LoggingManager getLogger() {
        /*if (this.loggingManager == null) {
            this.loggingManager = new LoggingManager("CraftFire.YamlManager", "[YamlManager]");
        }*/
        return this.loggingManager;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#setLoggingManager(com.craftfire.commons.util.LoggingManager)
     */
    @Override
    public void setLoggingManager(LoggingManager loggingManager) {
        if (loggingManager == null) {
            throw new IllegalArgumentException("Parameter 'loggingManager' cannot be null.");
        }
        this.loggingManager = loggingManager;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getRootNode()
     */
    @Override
    public YamlNode getRootNode() {
        return this.root;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#setRootNode(com.craftfire.commons.yaml.YamlNode)
     */
    @Override
    public YamlNode setRootNode(YamlNode node) {
        this.root = new YamlNode(this, null, node.getValue());
        return this.root;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#exist(java.lang.String)
     */
    @Override
    public boolean exist(String node) {
        boolean result = this.root.hasNode(node);
        getLogger().debug("Checking if node '" + node + "' exists: '" + result + "'.");
        return result;
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
    public boolean getBoolean(String node, boolean defaultValue) {
        if (exist(node)) {
            try {
                boolean value = this.root.getNode(node).getBool(defaultValue);
                getLogger().debug("Found node '" + node + "' with Boolean value '" + value + "', default value is '" + defaultValue + "'.");
                return value;
            } catch (YamlException e) {
                getLogger().stackTrace(e);
            }
        }
        getLogger().debug("Could not find node '" + node + "', returning default value instead '" + defaultValue + "'.");
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
    public String getString(String node, String defaultValue) {
        if (exist(node)) {
            try {
                String value = this.root.getNode(node).getString(defaultValue);
                getLogger().debug("Found node '" + node + "' with String value '" + value +
                        "', default value is '" + defaultValue + "'.");
                return value;
            } catch (YamlException e) {
                getLogger().stackTrace(e);
            }
        }
        getLogger().debug("Could not find node '" + node + "', returning default value instead '" + defaultValue + "'.");
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
    public int getInt(String node, int defaultValue) {
        if (exist(node)) {
            try {
                int value = this.root.getNode(node).getInt(defaultValue);
                getLogger().debug("Found node '" + node + "' with Integer value '" + value +
                        "', default value is '" + defaultValue + "'.");
                return value;
            } catch (YamlException e) {
                getLogger().stackTrace(e);
            }
        }
        getLogger().debug("Could not find node '" + node + "', returning default value instead '" + defaultValue + "'.");
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
    public long getLong(String node, long defaultValue) {
        if (exist(node)) {
            try {
                long value = this.root.getNode(node).getLong(defaultValue);
                getLogger().debug("Found node '" + node + "' with Long value '" + value +
                        "', default value is '" + defaultValue + "'");
                return value;
            } catch (YamlException e) {
                getLogger().stackTrace(e);
            }
        }
        getLogger().debug("Could not find node '" + node + "', returning default value instead '" + defaultValue + "'.");
        return defaultValue;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#addNodes(com.craftfire.commons.yaml.YamlManager)
     */
    @Override
    public void addNodes(YamlManager yamlManager) throws YamlException {
        getLogger().debug("Adding node list to current node list: '" + yamlManager.getRootNode().getChildrenMap().toString() + "'.");
        this.root.addChildren(yamlManager.getRootNode().getChildrenList());
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#addNodes(java.util.Map)
     */
    @Override
    public void addNodes(Map<String, Object> map) throws YamlException {
        getLogger().debug("Adding node list to current node list: '" + map.toString() + "'.");
        this.root.addChildren(map);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#setNode(java.lang.String, java.lang.Object)
     */
    @Override
    public void setNode(String node, Object value) throws YamlException {
        getLogger().debug("Setting node '" + node + "' to value '" + value + "'.");
        this.root.getNode(node, true).setValue(value);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getNode(java.lang.String)
     */
    @Override
    public YamlNode getNode(String node) throws YamlException {
        getLogger().debug("Getting node '" + node + "'.");
        return this.root.getNode(node);
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
        return getRootNode().getFinalNodeList();
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getFinalNodeCount()
     */
    @Override
    public int getFinalNodeCount() {
        return getRootNode().getFinalNodeCount();
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#save()
     */
    @Override
    public boolean save() {
        if (this.file == null) {
            return false;
        }
        try {
            this.yaml.dump(this.root.dump(), new FileWriter(this.file));
        } catch (IOException e) {
            getLogger().stackTrace(e);
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#reload()
     */
    @Override
    public boolean load() {
        try {
            if (this.reader != null) {
                load(this.reader);
                this.reader = null;
                return true;
            }
            if (this.resource != null) {
                load(this.resource);
                return true;
            }
            if (this.file != null) {
                load(this.file);
                return true;
            }
        } catch (IOException e) {
            getLogger().stackTrace(e);
        }
        return false;
    }

    /**
     * Loads yaml from the given file and sets is as the root node.
     * <p>
     * Tries to create the file if doesn't exist.
     * 
     * @param  file        file to load the yaml from
     * @throws IOException if an IOException occurred
     */
    protected void load(File file) throws IOException {
        if (file == null) {
            getLogger().error("File '" + file + "' is null, nodes could not be loaded from the yaml file.");
            return;
        } else if (!file.exists()) {
            try {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                getLogger().error("Unable to create file '" + file);
                getLogger().stackTrace(e);
                return;
            }
        }
        getLogger().debug("Loading nodes from file '" + file.getAbsoluteFile() + "'.");
        load(new FileInputStream(file));
    }

    /**
     * Loads yaml from the given classpath resource and sets it as the root node.
     * 
     * @param  path        path to the resource in classpaht to load the yaml from
     * @throws IOException if an IOException occurred
     */
    protected void load(String path) throws IOException {
        getLogger().debug("Loading nodes from local file '" + path + "'.");
        load(getClass().getClassLoader().getResourceAsStream(path));
    }

    /**
     * Loads yaml from the given input stream and sets is as the root node.
     * 
     * @param stream  input stream to load the yaml from
     */
    protected void load(InputStream stream) {
        load(new InputStreamReader(stream));
    }

    /**
     * Loads yaml from the given reader and sets is as the root node.
     * 
     * @param reader  reader to load the yaml from
     */
    protected void load(Reader reader) {
        // TODO: Replace all tabs in the document before parsing.
        Object tree = this.yaml.load(reader);
        this.root = new YamlNode(this, null, tree);
    }
}
