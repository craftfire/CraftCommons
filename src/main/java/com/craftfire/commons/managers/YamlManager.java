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

import java.io.*;
import java.util.HashMap;

import com.craftfire.commons.CraftCommons;

public class YamlManager {
    //TODO: Make it possible to save to a file
    private HashMap<String, Object> yaml = new HashMap<String, Object>();

    public YamlManager(File file) throws IOException {
        this.load(new FileInputStream(file));
    }

    public YamlManager(String path) throws IOException {
        this.load(getClass().getClassLoader().getResourceAsStream(path));
    }

    public boolean exist(String node) {
        return this.yaml.containsKey(node.toLowerCase());
    }

    public boolean getBoolean(String node) {
        node = node.toLowerCase();
        if (exist(node) && this.yaml.get(node) instanceof Boolean) {
            return (Boolean) this.yaml.get(node);
        }
        return false;
    }

    public String getString(String node) {
        node = node.toLowerCase();
        if (exist(node) && this.yaml.get(node) instanceof String) {
            return (String) this.yaml.get(node);
        }
        return null;
    }

    public int getInt(String node) {
        node = node.toLowerCase();
        if (exist(node) && this.yaml.get(node) instanceof Integer) {
            return (Integer) this.yaml.get(node);
        }
        return 0;
    }

    public Long getLong(String node) {
        node = node.toLowerCase();
        if (exist(node) && this.yaml.get(node) instanceof Long) {
            return (Long) this.yaml.get(node);
        }
        return null;
    }

    public HashMap<String, Object> getNodes() {
        return this.yaml;
    }

    private void load(InputStream yamlStream) throws IOException {
        this.yaml = new HashMap<String, Object>();
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
                        int index = temp.lastIndexOf("#");
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
                            this.yaml.put(node + finalNode, true);
                        } else if (value.equalsIgnoreCase("false")) {
                            this.yaml.put(node + finalNode, false);
                        } else if (CraftCommons.isInteger(value)) {
                            this.yaml.put(node + finalNode, Integer.parseInt(value));
                        } else if (CraftCommons.isLong(value)) {
                            this.yaml.put(node + finalNode, Long.parseLong(value));
                        } else {
                            this.yaml.put(node + finalNode, value);
                        }
                        isNode = false;
                    }
                }
            } finally  {
                yamlStream.close();
            }
        }
    }
}
