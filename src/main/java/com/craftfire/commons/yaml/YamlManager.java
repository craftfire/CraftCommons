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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import com.craftfire.commons.util.LoggingManager;

public class YamlManager {
    private LoggingManager loggingManager = new LoggingManager("CraftFire.YamlManager", "[YamlManager]");
    private File file = null;
    private final Yaml yaml;
    private final boolean caseSensitive;
    @SuppressWarnings("unused")
    private final boolean multiDocument; // May be used in the future
    private final String separator;
    private YamlNode root;

    public YamlManager(File file) throws IOException {
        this(file, new Settings());
    }

    public YamlManager(File file, Settings settings) throws IOException {
        this.file = file;
        this.yaml = settings.createYaml();
        this.caseSensitive = settings.isCaseSensitive();
        this.multiDocument = settings.isMultiDocument();
        this.separator = settings.getSeparator();
        load(file);
    }

    public YamlManager(String path) throws IOException {
        this(path, new Settings());
    }

    public YamlManager(String path, Settings settings) throws IOException {
        this.yaml = settings.createYaml();
        this.caseSensitive = settings.isCaseSensitive();
        this.multiDocument = settings.isMultiDocument();
        this.separator = settings.getSeparator();
        load(path);
    }

    public YamlManager(InputStream stream) throws IOException {
        this(stream, new Settings());
    }

    public YamlManager(InputStream stream, Settings settings) throws IOException {
        this(new InputStreamReader(stream), settings);
    }

    public YamlManager(Reader reader) throws IOException {
        this(reader, new Settings());
    }

    public YamlManager(Reader reader, Settings settings) throws IOException {
        this.yaml = settings.createYaml();
        this.caseSensitive = settings.isCaseSensitive();
        this.multiDocument = settings.isMultiDocument();
        this.separator = settings.getSeparator();
        load(reader);
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    public String getSeparator() {
        return this.separator;
    }

    public Set<File> getFiles() {
        Set<File> set = new HashSet<File>();
        if (this.file != null) {
            set.add(this.file);
        }
        return set;
    }

    public LoggingManager getLogger() {
        if (this.loggingManager == null) {
            this.loggingManager = new LoggingManager("CraftFire.YamlManager", "[YamlManager]");
        }
        return this.loggingManager;
    }

    public void setLoggingManager(LoggingManager loggingManager) {
        if (loggingManager == null) {
            throw new IllegalArgumentException("Parameter 'loggingManager' cannot be null.");
        }
        this.loggingManager = loggingManager;
    }

    public YamlNode getRootNode() {
        return this.root;
    }

    public YamlNode setRootNode(YamlNode node) {
        this.root = new YamlNode(this, null, node.getValue());
        return this.root;
    }

    public boolean exist(String node) {
        boolean result = this.root.hasNode(node);
        getLogger().debug("Checking if node '" + node + "' exists: '" + result + "'.");
        return result;
    }

    public boolean getBoolean(String node) {
        return getBoolean(node, false);
    }

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

    public String getString(String node) {
        return getString(node, null);
    }

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

    public int getInt(String node) {
        return getInt(node, 0);
    }

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

    public long getLong(String node) {
        return getLong(node, 0);
    }

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

    public void addNodes(YamlManager yamlManager) throws YamlException {
        getLogger().debug("Adding node list to current node list: '" + yamlManager.getRootNode().getChildrenMap().toString() + "'.");
        this.root.addChildren(yamlManager.getRootNode().getChildrenList().toArray(new YamlNode[0]));
    }

    public void addNodes(Map<String, Object> map) throws YamlException {
        getLogger().debug("Adding node list to current node list: '" + map.toString() + "'.");
        this.root.addChildren(map);
    }

    public void setNode(String node, Object value) throws YamlException {
        getLogger().debug("Setting node '" + node + "' to value '" + value + "'.");
        this.root.getNode(node, true).setValue(value);
    }

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

    public boolean reload() {
        if (this.file == null) {
            return false;
        }
        try {
            load(this.file);
        } catch (IOException e) {
            getLogger().stackTrace(e);
            return false;
        }
        return true;
    }

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
            }
        }
        getLogger().debug("Loading nodes from file '" + file.getAbsoluteFile() + "'.");
        load(new FileInputStream(file));
    }

    protected void load(String path) throws IOException {
        getLogger().debug("Loading nodes from local file '" + path + "'.");
        load(getClass().getClassLoader().getResourceAsStream(path));
    }

    protected void load(InputStream stream) {
        load(new InputStreamReader(stream));
    }

    protected void load(Reader reader) {
        // TODO: Replace all tabs in the document before parsing.
        Object tree = this.yaml.load(reader);
        this.root = new YamlNode(this, null, tree);
    }

    /*    private void load(InputStream yamlStream) throws IOException {
            if (yamlStream != null) {
                try {
                    InputStreamReader yamlStreamReader = new InputStreamReader(yamlStream);
                    BufferedReader buffer = new BufferedReader(yamlStreamReader);
                    String line, node = "";
                    int lastBlank = 0;
                    boolean isNode = false;
                    while  ((line = buffer.readLine()) != null) {
                        int blank = 0;
                        if (line.length() > 0 && line.charAt(line.length() - 1) == ':') {
                            for (int i = 0; i < line.length(); i++) {
                                if (Character.isWhitespace(line.charAt(i))) {
                                    blank++;
                                }
                            }
                            line = line.replaceAll("\\s+", "");
                            line = line.replaceAll(":", "");
                            if (blank == 0) {
                                node = line + ".";
                            } else if (blank > lastBlank) {
                                node += line + ".";
                            } else if (blank <= lastBlank) {
                                String[] split = node.split("\\.");
                                if ((lastBlank - blank) > 0) {
                                    for (int i = 1; ((lastBlank - blank) / 4) >= i; i++) {
                                        node = node.replace("." + split[split.length - i], "");
                                    }
                                } else {
                                    node = node.replace("." + split[split.length - 1], "");
                                }
                                node += line + ".";
                            }
                            lastBlank = blank;
                            isNode = true;
                        } else if (line.length() > 0) {
                            boolean set = false;
                            for (int i=0; i < line.length() && ! set; i++) {
                                if (Character.isWhitespace(line.charAt(i))) {
                                    blank++;
                                } else {
                                    set = true;
                                }
                            }

                            String[] split = line.split(":");
                            String finalNode = split[0].replaceAll("\\s+", "");
                            if (finalNode.startsWith("#")) {
                                continue;
                            }
                            if (!isNode && blank > lastBlank) {
                                node += finalNode + ".";
                            } else if (!isNode && blank < lastBlank) {
                                String[] spl = node.split("\\.");
                                node = node.replace("." + spl[spl.length - 1], "");
                            }
                            lastBlank = blank;
                            String temp = split[1].substring(1);
                            if (split.length > 1) {
                                for(int i=2; split.length > i; i++){
                                    temp += ":" + split[i];
                                }
                            }
                            int index = temp.lastIndexOf('#');
                            if (index != -1 && Character.isWhitespace(temp.charAt(index - 1))) {
                                temp = temp.substring(0, index - 1);
                            }
                            String value = "";
                            char last = 0;
                            for (int i = 0; i < temp.length(); i++) {
                                if (Character.isWhitespace(temp.charAt(i)) && Character.isWhitespace(last)) {
                                    continue;
                                }
                                value += temp.charAt(i);
                                last = temp.charAt(i);
                            }
                            if (Character.isWhitespace(value.charAt(value.length() - 1))) {
                                value = value.substring(0, value.length() - 1);
                            }
                            //System.out.println(node + finalNode + " = " + value);
                            if (value.equalsIgnoreCase("true")) {
                                getLogger().debug("Adding node '" + node + finalNode + "' " +
                                        "to the node list with Boolean value 'true'.");
                                this.yaml.put(node + finalNode, true);
                            } else if (value.equalsIgnoreCase("false")) {
                                getLogger().debug("Adding node '" + node + finalNode + "' " +
                                        "to the node list with Boolean value 'false'.");
                                this.yaml.put(node + finalNode, false);
                            } else if (Util.isInteger(value)) {
                                getLogger().debug("Adding node '" + node + finalNode + "' " +
                                        "to the node list with Integer value '" + value + "'.");
                                this.yaml.put(node + finalNode, Integer.parseInt(value));
                            } else if (Util.isLong(value)) {
                                getLogger().debug("Adding node '" + node + finalNode + "' " +
                                        "to the node list with Long value '" + value + "'.");
                                this.yaml.put(node + finalNode, Long.parseLong(value));
                            } else if (value instanceof String && value != null && !value.equalsIgnoreCase("null")) {
                                getLogger().debug("Adding node '" + node + finalNode + "' " +
                                        "to the node list with String value '" + value + "'.");
                                this.yaml.put(node + finalNode, value);
                            } else {
                                getLogger().error("Could not add node '" + node + finalNode + "' " + "to the node list because the value is '" + value + "'.");
                            }
                            isNode = false;
                        }
                    }
                } finally  {
                    yamlStream.close();
                }
            }
        }*/

    static class Settings {
        private boolean caseSensitive = false;
        private boolean multiDocument = false;
        private String separator = ".";
        private BaseConstructor constructor;
        private Representer representer;
        private DumperOptions options;
        private Resolver resolver;

        public Settings() {
            this.constructor = new Constructor();
            this.representer = new EmptyNullRepresenter();
            this.resolver = new Resolver();
            this.options = new DumperOptions();
            this.options.setDefaultFlowStyle(FlowStyle.BLOCK);
            this.options.setIndent(4);
        }

        public Yaml createYaml() {
            return new Yaml(this.constructor, this.representer, this.options, this.resolver);
        }

        public boolean isCaseSensitive() {
            return this.caseSensitive;
        }

        public void setCaseSensitive(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }

        public boolean isMultiDocument() {
            return this.multiDocument;
        }

        public void setMultiDocument(boolean multiDocument) {
            this.multiDocument = multiDocument;
        }

        public String getSeparator() {
            return this.separator;
        }

        public void setSeparator(String separator) {
            this.separator = separator;
        }

        public BaseConstructor getConstructor() {
            return this.constructor;
        }

        public void setConstructor(BaseConstructor constructor) {
            this.constructor = constructor;
        }

        public Representer getRepresenter() {
            return this.representer;
        }

        public void setRepresenter(Representer representer) {
            this.representer = representer;
        }

        public DumperOptions getDumperOptions() {
            return this.options;
        }

        public void setDumperOptions(DumperOptions options) {
            this.options = options;
        }

        public Resolver getResolver() {
            return this.resolver;
        }

        public void setResolver(Resolver resolver) {
            this.resolver = resolver;
        }

    }
}
