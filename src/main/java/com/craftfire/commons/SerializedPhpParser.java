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
/*
 * Copyright (c) 2007 Zsolt Szász <zsolt at lorecraft dot com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.craftfire.commons;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Deserializes a serialized PHP data structure into corresponding Java objects.
 * It supports the integer, float, boolean, string primitives that are mapped to
 * their Java equivalent, plus arrays that are parsed into <code>Map</code>
 * instances and objects that are represented by
 * {@link SerializedPhpParser.PhpObject} instances.
 * <p>
 * Example of use:
 *
 * <pre>
 *      String input = "O:8:"TypeName":1:{s:3:"foo";s:3:"bar";}";
 *      SerializedPhpParser serializedPhpParser = new SerializedPhpParser(input);
 *      Object result = serializedPhpParser.parse();
 * </pre>
 *
 * The <code>result</code> object will be a <code>PhpObject</code> with the name
 * "TypeName" and the attribute "foo" = "bar".
 */
public class SerializedPhpParser {
    private final String input;
    private int index;
    private boolean assumeUTF8 = true;
    private Pattern acceptedAttributeNameRegex = null;

    public SerializedPhpParser(String input) {
        this.input = input;
    }

    public SerializedPhpParser(String input, boolean assumeUTF8) {
        this.input = input;
        this.assumeUTF8 = assumeUTF8;
    }

    public Object parse() {
        char type = this.input.charAt(this.index);
        switch (type) {
        case 'i':
            this.index += 2;
            return this.parseInt();
        case 'd':
            this.index += 2;
            return this.parseFloat();
        case 'b':
            this.index += 2;
            return this.parseBoolean();
        case 's':
            this.index += 2;
            return this.parseString();
        case 'a':
            this.index += 2;
            return this.parseArray();
        case 'O':
            this.index += 2;
            return this.parseObject();
        case 'N':
            this.index += 2;
            return NULL;
        default:
            throw new IllegalStateException("Encountered unknown type [" + type
                    + "]");
        }
    }

    private Object parseObject() {
        PhpObject phpObject = new PhpObject();
        int strLen = this.readLength();
        phpObject.name = this.input.substring(this.index, this.index + strLen);
        this.index = this.index + strLen + 2;
        int attrLen = this.readLength();
        for (int i = 0; i < attrLen; i++) {
            Object key = this.parse();
            Object value = this.parse();
            if (this.isAcceptedAttribute(key)) {
                phpObject.attributes.put(key, value);
            }
        }
        this.index++;
        return phpObject;
    }

    private Map<Object, Object> parseArray() {
        int arrayLen = this.readLength();
        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        for (int i = 0; i < arrayLen; i++) {
            Object key = this.parse();
            Object value = this.parse();
            if (this.isAcceptedAttribute(key)) {
                result.put(key, value);
            }
        }
        this.index++;
        return result;
    }

    private boolean isAcceptedAttribute(Object key) {
        if (this.acceptedAttributeNameRegex == null) {
            return true;
        }
        if (!(key instanceof String)) {
            return true;
        }
        return this.acceptedAttributeNameRegex.matcher((String) key).matches();
    }

    private int readLength() {
        int delimiter = this.input.indexOf(':', this.index);
        int arrayLen = Integer.valueOf(this.input.substring(this.index,
                delimiter));
        this.index = delimiter + 2;
        return arrayLen;
    }

    /**
     * Assumes strings are utf8 encoded
     *
     * @return
     */
    private String parseString() {
        int strLen = this.readLength();

        int utfStrLen = 0;
        int byteCount = 0;
        while (byteCount != strLen) {
            char ch = this.input.charAt(this.index + utfStrLen++);
            if (this.assumeUTF8) {
                if ((ch >= 0x0001) && (ch <= 0x007F)) {
                    byteCount++;
                } else if (ch > 0x07FF) {
                    byteCount += 3;
                } else {
                    byteCount += 2;
                }
            } else {
                byteCount++;
            }
        }
        String value = this.input.substring(this.index, this.index + utfStrLen);
        this.index = this.index + utfStrLen + 2;
        return value;
    }

    private Boolean parseBoolean() {
        int delimiter = this.input.indexOf(';', this.index);
        String value = this.input.substring(this.index, delimiter);
        if (value.equals("1")) {
            value = "true";
        } else if (value.equals("0")) {
            value = "false";
        }
        this.index = delimiter + 1;
        return Boolean.valueOf(value);
    }

    private Double parseFloat() {
        int delimiter = this.input.indexOf(';', this.index);
        String value = this.input.substring(this.index, delimiter);
        this.index = delimiter + 1;
        return Double.valueOf(value);
    }

    private Integer parseInt() {
        int delimiter = this.input.indexOf(';', this.index);
        String value = this.input.substring(this.index, delimiter);
        this.index = delimiter + 1;
        return Integer.valueOf(value);
    }

    public void setAcceptedAttributeNameRegex(String acceptedAttributeNameRegex) {
        this.acceptedAttributeNameRegex = Pattern
                .compile(acceptedAttributeNameRegex);
    }

    public static final Object NULL = new Object() {
        @Override
        public String toString() {
            return "NULL";
        }
    };

    /**
     * Represents an object that has a name and a map of attributes
     */
    public static class PhpObject {
        public String name;
        public Map<Object, Object> attributes = new HashMap<Object, Object>();

        @Override
        public String toString() {
            return "\"" + this.name + "\" : " + this.attributes.toString();
        }
    }
}
