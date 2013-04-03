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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import com.craftfire.commons.util.LoggingManager;

public class TestSettings {
    private Settings settings;

    @Before
    public void setup() {
        this.settings = new Settings();
    }

    @Test
    public void testLogger() {
        LoggingManager logger = this.settings.getLogger();
        assertEquals("[YamlManager]", logger.getPrefix());
        assertSame(Logger.getLogger("CraftFire.YamlManager"), logger.getLogger());

        logger = mock(LoggingManager.class);
        assertSame(this.settings, this.settings.setLogger(logger));
        assertSame(logger, this.settings.getLogger());

        try {
            this.settings.setLogger(null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testResolver() {
        assertEquals(Resolver.class, this.settings.getResolver().getClass());

        Resolver mock = mock(Resolver.class);
        assertSame(this.settings, this.settings.setResolver(mock));
        assertSame(mock, this.settings.getResolver());
    }

    @Test
    public void testDumperOptions() {
        DumperOptions options = this.settings.getDumperOptions();
        assertEquals(FlowStyle.BLOCK, options.getDefaultFlowStyle());
        assertEquals(ScalarStyle.PLAIN, options.getDefaultScalarStyle());
        assertEquals(4, options.getIndent());
        assertEquals(LineBreak.UNIX, options.getLineBreak());
        assertNull(options.getTags());
        assertNull(options.getTimeZone());
        assertNull(options.getVersion());
        assertEquals(80, options.getWidth());
        assertFalse(options.isAllowReadOnlyProperties());
        assertTrue(options.isAllowUnicode());
        assertFalse(options.isCanonical());
        assertFalse(options.isExplicitEnd());
        assertFalse(options.isExplicitStart());
        assertFalse(options.isPrettyFlow());

        options = mock(DumperOptions.class);
        assertSame(this.settings, this.settings.setDumperOptions(options));
        assertSame(options, this.settings.getDumperOptions());
    }

    @Test
    public void testRepresenter() {
        assertEquals(EmptyNullRepresenter.class, this.settings.getRepresenter().getClass());

        Representer mock = mock(Representer.class);
        assertSame(this.settings, this.settings.setRepresenter(mock));
        assertSame(mock, this.settings.getRepresenter());
    }

    @Test
    public void testConstructor() {
        assertEquals(Constructor.class, this.settings.getConstructor().getClass());

        BaseConstructor mock = mock(BaseConstructor.class);
        assertSame(this.settings, this.settings.setConstructor(mock));
        assertSame(mock, this.settings.getConstructor());
    }

    @Test
    public void testSeparator() {
        assertEquals(".", this.settings.getSeparator());

        assertSame(this.settings, this.settings.setSeparator("::"));
        assertEquals("::", this.settings.getSeparator());
    }

    @Test
    public void testMultiDocument() {
        assertFalse(this.settings.isMultiDocument());

        assertSame(this.settings, this.settings.setMultiDocument(true));
        assertTrue(this.settings.isMultiDocument());
    }

    @Test
    public void testCaseSensitive() {
        assertFalse(this.settings.isCaseSensitive());

        assertSame(this.settings, this.settings.setCaseSensitive(true));
        assertTrue(this.settings.isCaseSensitive());
    }

    @Test
    public void testCreateYaml() throws NoSuchFieldException, IllegalAccessException {
        Yaml yaml = this.settings.createYaml();
        Field resolver = Yaml.class.getDeclaredField("resolver");
        Field constructor = Yaml.class.getDeclaredField("constructor");
        Field representer = Yaml.class.getDeclaredField("representer");
        Field dumperOptions = Yaml.class.getDeclaredField("dumperOptions");

        resolver.setAccessible(true);
        constructor.setAccessible(true);
        representer.setAccessible(true);
        dumperOptions.setAccessible(true);

        assertSame(this.settings.getResolver(), resolver.get(yaml));
        assertSame(this.settings.getConstructor(), constructor.get(yaml));
        assertSame(this.settings.getRepresenter(), representer.get(yaml));
        assertSame(this.settings.getDumperOptions(), dumperOptions.get(yaml));
    }
}
