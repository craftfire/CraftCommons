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
package com.craftfire.commons.yaml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.yaml.snakeyaml.DumperOptions;

import com.craftfire.commons.util.LoggingManager;

public class TestYamlCombiner {
    private static Random rnd = new Random();
    private YamlCombiner combiner;

    @Before
    public void setup() {
        this.combiner = new YamlCombiner();
    }

    @Test
    public void testGetSetYamlManagers() {
        assertTrue(this.combiner.getYamlManagers().isEmpty());
        assertNull(this.combiner.getDefaultManager());

        Set<YamlManager> set = new HashSet<YamlManager>();
        set.add(mock(YamlManager.class));
        set.add(mock(YamlManager.class));
        set.add(mock(YamlManager.class));

        this.combiner.setYamlManagers(set);
        assertEquals(set, this.combiner.getYamlManagers());
        assertTrue(set.contains(this.combiner.getDefaultManager()));

        this.combiner.setYamlManagers(null);
        assertTrue(this.combiner.getYamlManagers().isEmpty());
        assertNull(this.combiner.getDefaultManager());
    }

    @Test
    public void testAddYamlManager() {
        assertTrue(this.combiner.getYamlManagers().isEmpty());
        assertNull(this.combiner.getDefaultManager());

        YamlManager mgr1 = mock(YamlManager.class);
        this.combiner.addYamlManager(mgr1);
        assertTrue(this.combiner.getYamlManagers().contains(mgr1));
        assertEquals(1, this.combiner.getYamlManagers().size());
        assertSame(mgr1, this.combiner.getDefaultManager());

        YamlManager mgr2 = mock(YamlManager.class);
        YamlManager mgr3 = mock(YamlManager.class);
        List<YamlManager> list = new ArrayList<YamlManager>();
        list.add(mgr3);
        list.add(mgr1);
        list.add(mock(YamlManager.class));

        this.combiner.setYamlManagers(list);
        assertEquals(new HashSet<YamlManager>(list), this.combiner.getYamlManagers());
        assertSame(mgr3, this.combiner.getDefaultManager());
        this.combiner.addYamlManager(mgr1);
        assertEquals(new HashSet<YamlManager>(list), this.combiner.getYamlManagers());
        assertTrue(this.combiner.getYamlManagers().contains(mgr1));
        assertSame(mgr3, this.combiner.getDefaultManager());
        this.combiner.addYamlManager(mgr2);
        assertThat(this.combiner.getYamlManagers(), not(equalTo((Set<YamlManager>) new HashSet<YamlManager>(list))));
        assertTrue(this.combiner.getYamlManagers().contains(mgr2));
        assertEquals(list.size() + 1, this.combiner.getYamlManagers().size());
        assertSame(mgr3, this.combiner.getDefaultManager());

        try {
            this.combiner.addYamlManager(null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        assertSame(mgr3, this.combiner.getDefaultManager());
        assertFalse(this.combiner.getYamlManagers().isEmpty());
    }

    @Test
    public void testSetDefaultManager() {
        YamlManager mgr1 = mock(YamlManager.class);
        YamlManager mgr2 = mock(YamlManager.class);

        try {
            this.combiner.setDefaultManager(mgr1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        assertNull(this.combiner.getDefaultManager());

        List<YamlManager> list = new ArrayList<YamlManager>();
        list.add(mock(YamlManager.class));
        list.add(mgr1);
        list.add(mock(YamlManager.class));

        this.combiner.setYamlManagers(list);
        this.combiner.setDefaultManager(mgr1);
        assertSame(mgr1, this.combiner.getDefaultManager());

        try {
            this.combiner.setDefaultManager(mgr2);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        assertSame(mgr1, this.combiner.getDefaultManager());
    }

    @Test
    public void testGetSetDefaultSettings() {
        Settings settings = this.combiner.getDefaultSettings();
        Settings defaults = new Settings();
        // Make sure they are identical
        assertEquals(defaults.getConstructor().getClass(), settings.getConstructor().getClass());
        assertEquals(defaults.getRepresenter().getClass(), settings.getRepresenter().getClass());
        compareDumperOptions(defaults.getDumperOptions(), settings.getDumperOptions());
        assertEquals(defaults.getResolver().getClass(), settings.getResolver().getClass());
        assertEquals(defaults.getLogger().getPrefix(), settings.getLogger().getPrefix());
        assertSame(defaults.getLogger().getLogger(), settings.getLogger().getLogger());
        assertEquals(defaults.getSeparator(), settings.getSeparator());
        assertEquals(defaults.isCaseSensitive(), settings.isCaseSensitive());
        assertEquals(defaults.isMultiDocument(), settings.isMultiDocument());

        settings = mock(Settings.class);
        this.combiner.setDefaultSettings(settings);
        assertSame(settings, this.combiner.getDefaultSettings());
    }

    @Test
    public void testGetFiles() {
        Set<File> set1 = new HashSet<File>();
        Set<File> set2 = new HashSet<File>();
        Set<File> set3 = new HashSet<File>();

        File file1 = mock(File.class);
        set1.add(mock(File.class));
        set1.add(file1);
        set1.add(mock(File.class));
        set2.add(mock(File.class));
        set3.add(file1);
        set3.add(mock(File.class));

        List<YamlManager> mgrs = new ArrayList<YamlManager>();
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        stub(mgrs.get(0).getFiles()).toReturn(set1);
        stub(mgrs.get(1).getFiles()).toReturn(set2);
        stub(mgrs.get(2).getFiles()).toReturn(set3);

        this.combiner.setYamlManagers(mgrs);
        Set<File> files = this.combiner.getFiles();
        assertTrue(files.containsAll(set1));
        assertTrue(files.containsAll(set2));
        assertTrue(files.containsAll(set3));

        verify(mgrs.get(0)).getFiles();
        verify(mgrs.get(1)).getFiles();
        verify(mgrs.get(2)).getFiles();
    }

    @Test
    public void testGetLogger() {
        Settings settings = mock(Settings.class);
        LoggingManager mgr = mock(LoggingManager.class);
        stub(settings.getLogger()).toReturn(mgr);

        this.combiner.setDefaultSettings(settings);
        assertSame(mgr, this.combiner.getLogger());

        verify(settings).getLogger();
    }

    @Test
    public void testSetLoggingManager() {
        List<YamlManager> mgrs = new ArrayList<YamlManager>();
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        this.combiner.setYamlManagers(mgrs);

        LoggingManager logger = mock(LoggingManager.class);
        this.combiner.setLoggingManager(logger);
        assertSame(logger, this.combiner.getDefaultSettings().getLogger());

        verify(mgrs.get(0)).setLoggingManager(logger);
        verify(mgrs.get(1)).setLoggingManager(logger);
        verify(mgrs.get(2)).setLoggingManager(logger);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testRootNode() {
        List<YamlManager> mgrs = new ArrayList<YamlManager>();
        YamlManager mgr = mock(YamlManager.class);
        mgrs.add(mgr);
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        this.combiner.setYamlManagers(mgrs);
        this.combiner.setDefaultManager(mgr);

        YamlNode node = mock(YamlNode.class);
        stub(mgr.getRootNode()).toReturn(node);

        assertSame(node, this.combiner.getRootNode());
        verify(mgr).getRootNode();

        YamlNode node1 = mock(YamlNode.class);
        this.combiner.setRootNode(node1);
        assertSame(mgr, this.combiner.getDefaultManager());
        assertEquals(1, this.combiner.getYamlManagers().size());
        assertTrue(this.combiner.getYamlManagers().contains(mgr));
        verify(mgr).setRootNode(node1);
    }

    @Test
    public void testExist() {
        List<YamlManager> mgrs = new ArrayList<YamlManager>();
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        this.combiner.setYamlManagers(mgrs);

        stub(mgrs.get(0).exist("atlas.is.a.detector")).toReturn(false);
        stub(mgrs.get(1).exist("atlas.is.a.detector")).toReturn(true);
        stub(mgrs.get(2).exist("atlas.is.a.detector")).toReturn(false);

        assertTrue(this.combiner.exist("atlas.is.a.detector"));
        verify(mgrs.get(1)).exist("atlas.is.a.detector");

        stub(mgrs.get(0).exist("cms.is.a.detector.too")).toReturn(false);
        stub(mgrs.get(1).exist("cms.is.a.detector.too")).toReturn(false);
        stub(mgrs.get(2).exist("cms.is.a.detector.too")).toReturn(false);

        assertFalse(this.combiner.exist("cms.is.a.detector.too"));
        verify(mgrs.get(0)).exist("cms.is.a.detector.too");
        verify(mgrs.get(1)).exist("cms.is.a.detector.too");
        verify(mgrs.get(2)).exist("cms.is.a.detector.too");
    }

    @Test
    public void testGetterDefaults() {
        boolean randomBool = rnd.nextBoolean();
        int randomInt = rnd.nextInt();
        long randomLong = rnd.nextLong();
        byte[] randomBytes = new byte[8];
        rnd.nextBytes(randomBytes);
        String randomString = new String(randomBytes);

        YamlCombiner spy = spy(this.combiner);

        doReturn(randomBool).when(spy).getBoolean("bob.has.no.cat", false);
        assertEquals(randomBool, spy.getBoolean("bob.has.no.cat"));
        verify(spy).getBoolean("bob.has.no.cat", false);

        doReturn(randomString).when(spy).getString("bob.has.one.cat", null);
        assertEquals(randomString, spy.getString("bob.has.one.cat"));
        verify(spy).getString("bob.has.one.cat", null);

        doReturn(randomInt).when(spy).getInt("bob.has.two.cats", 0);
        assertEquals(randomInt, spy.getInt("bob.has.two.cats"));
        verify(spy).getInt("bob.has.two.cats", 0);

        doReturn(randomLong).when(spy).getLong("bob.has.three.cats", 0);
        assertEquals(randomLong, spy.getLong("bob.has.three.cats"));
        verify(spy).getLong("bob.has.three.cats", 0);
    }

    @Test
    public void testGetBoolean() throws YamlException {
        boolean random = rnd.nextBoolean();
        YamlCombiner spy = spy(this.combiner);

        YamlNode node = mock(YamlNode.class);
        doReturn(node).when(spy).getNode("charlie.has.a.dog");
        stub(node.getBool(random)).toReturn(true);

        assertTrue(spy.getBoolean("charlie.has.a.dog", random));
        verify(spy).getNode("charlie.has.a.dog");
        verify(node).getBool(random);

        doReturn(null).when(spy).getNode("charlie.has.a.cat");
        assertEquals(random, spy.getBoolean("charlie.has.a.cat", random));
        verify(spy).getNode("charlie.has.a.cat");

        doThrow(new YamlException()).when(spy).getNode("charlie.has.a.frog");
        assertEquals(random, spy.getBoolean("charlie.has.a.frog", random));
        verify(spy).getNode("charlie.has.a.frog");
    }

    @Test
    public void testGetString() throws YamlException {
        byte[] randomBytes = new byte[8];
        rnd.nextBytes(randomBytes);
        String random = new String(randomBytes);

        YamlCombiner spy = spy(this.combiner);

        YamlNode node = mock(YamlNode.class);
        doReturn(node).when(spy).getNode("charlie.has.a.dog");
        stub(node.getString(random)).toReturn("yes, he has");

        assertEquals("yes, he has", spy.getString("charlie.has.a.dog", random));
        verify(spy).getNode("charlie.has.a.dog");
        verify(node).getString(random);

        doReturn(null).when(spy).getNode("charlie.has.a.cat");
        assertEquals(random, spy.getString("charlie.has.a.cat", random));
        verify(spy).getNode("charlie.has.a.cat");

        doThrow(new YamlException()).when(spy).getNode("charlie.has.a.frog");
        assertEquals(random, spy.getString("charlie.has.a.frog", random));
        verify(spy).getNode("charlie.has.a.frog");
    }

    @Test
    public void testGetInt() throws YamlException {
        int random = rnd.nextInt();

        YamlCombiner spy = spy(this.combiner);

        YamlNode node = mock(YamlNode.class);
        doReturn(node).when(spy).getNode("charlie.has.a.dog");
        stub(node.getInt(random)).toReturn(478);

        assertEquals(478, spy.getInt("charlie.has.a.dog", random));
        verify(spy).getNode("charlie.has.a.dog");
        verify(node).getInt(random);

        doReturn(null).when(spy).getNode("charlie.has.a.cat");
        assertEquals(random, spy.getInt("charlie.has.a.cat", random));
        verify(spy).getNode("charlie.has.a.cat");

        doThrow(new YamlException()).when(spy).getNode("charlie.has.a.frog");
        assertEquals(random, spy.getInt("charlie.has.a.frog", random));
        verify(spy).getNode("charlie.has.a.frog");
    }

    @Test
    public void testGetLong() throws YamlException {
        long random = rnd.nextLong();

        YamlCombiner spy = spy(this.combiner);

        YamlNode node = mock(YamlNode.class);
        doReturn(node).when(spy).getNode("charlie.has.a.dog");
        stub(node.getLong(random)).toReturn(400700800008007004L);

        assertEquals(400700800008007004L, spy.getLong("charlie.has.a.dog", random));
        verify(spy).getNode("charlie.has.a.dog");
        verify(node).getLong(random);

        doReturn(null).when(spy).getNode("charlie.has.a.cat");
        assertEquals(random, spy.getLong("charlie.has.a.cat", random));
        verify(spy).getNode("charlie.has.a.cat");

        doThrow(new YamlException()).when(spy).getNode("charlie.has.a.frog");
        assertEquals(random, spy.getLong("charlie.has.a.frog", random));
        verify(spy).getNode("charlie.has.a.frog");
    }

    @Test
    public void testAddNodes() throws YamlException {
        List<YamlManager> mgrs = new ArrayList<YamlManager>();
        YamlManager mgr = mock(YamlManager.class);
        mgrs.add(mgr);
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        this.combiner.setYamlManagers(mgrs);
        this.combiner.setDefaultManager(mgr);

        YamlManager mgr1 = mock(YamlManager.class);
        this.combiner.addNodes(mgr1);
        verify(mgr).addNodes(mgr1);

        @SuppressWarnings("unchecked")
        Map<String, Object> map = mock(Map.class);
        this.combiner.addNodes(map);
        verify(mgr).addNodes(map);
    }

    @Test
    public void testSetNode() throws YamlException {
        List<YamlManager> mgrs = new ArrayList<YamlManager>();
        YamlManager mgr = mock(YamlManager.class);
        mgrs.add(mgr);
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        this.combiner.setYamlManagers(mgrs);
        this.combiner.setDefaultManager(mgr);

        YamlCombiner spy = spy(this.combiner);
        Object testValue = mock(Object.class);

        doReturn(true).when(spy).exist("dante.is.in.trouble");
        YamlNode node = mock(YamlNode.class);
        doReturn(node).when(spy).getNode("dante.is.in.trouble");

        spy.setNode("dante.is.in.trouble", testValue);
        verify(spy).exist("dante.is.in.trouble");
        verify(spy).getNode("dante.is.in.trouble");
        verify(node).setValue(testValue);

        YamlNode node1 = mock(YamlNode.class);
        this.combiner.getDefaultSettings().setSeparator(".");
        doReturn(false).when(spy).exist("dante.has.no.sword");
        doReturn(false).when(spy).exist("dante.has.no");
        doReturn(true).when(spy).exist("dante.has");
        doReturn(node).when(spy).getNode("dante.has");
        stub(node.getChild("no.sword", true)).toReturn(node1);

        spy.setNode("dante.has.no.sword", testValue);
        verify(spy).exist("dante.has.no.sword");
        verify(spy).exist("dante.has.no");
        verify(spy).exist("dante.has");
        verify(spy).getNode("dante.has");
        verify(node).getChild("no.sword", true);
        verify(node1).setValue(testValue);
        verify(mgr, never()).setNode(anyString(), anyObject());

        doReturn(false).when(spy).exist("dante.has.an.axe");
        doReturn(false).when(spy).exist("dante.has.an");
        doReturn(false).when(spy).exist("dante.has");
        doReturn(false).when(spy).exist("dante");

        spy.setNode("dante.has.an.axe", testValue);
        verify(spy).exist("dante.has.an.axe");
        verify(spy).exist("dante.has.an");
        verify(spy, times(2)).exist("dante.has");
        verify(spy).exist("dante");
        verify(mgr).setNode("dante.has.an.axe", testValue);
    }

    @Test
    public void testGetNode() throws YamlException {
        List<YamlManager> mgrs = new ArrayList<YamlManager>();
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        this.combiner.setYamlManagers(mgrs);

        stub(mgrs.get(0).exist("eric.wears.a.hat")).toReturn(false);
        stub(mgrs.get(1).exist("eric.wears.a.hat")).toReturn(true);
        stub(mgrs.get(2).exist("eric.wears.a.hat")).toReturn(false);

        YamlNode node = mock(YamlNode.class);
        stub(mgrs.get(1).getNode("eric.wears.a.hat")).toReturn(node);

        assertSame(node, this.combiner.getNode("eric.wears.a.hat"));
        verify(mgrs.get(1)).exist("eric.wears.a.hat");
        verify(mgrs.get(1)).getNode("eric.wears.a.hat");

        stub(mgrs.get(0).exist("eric.wears.a.fedora")).toReturn(false);
        stub(mgrs.get(1).exist("eric.wears.a.fedora")).toReturn(false);
        stub(mgrs.get(2).exist("eric.wears.a.fedora")).toReturn(false);

        assertNull(this.combiner.getNode("eric.wears.a.fedora"));
        verify(mgrs.get(0)).exist("eric.wears.a.fedora");
        verify(mgrs.get(1)).exist("eric.wears.a.fedora");
        verify(mgrs.get(2)).exist("eric.wears.a.fedora");
    }

    @Test
    public void testFinalNodeCount() {
        List<YamlManager> mgrs = new ArrayList<YamlManager>();
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        this.combiner.setYamlManagers(mgrs);

        stub(mgrs.get(0).getFinalNodeCount()).toReturn(5);
        stub(mgrs.get(1).getFinalNodeCount()).toReturn(3);
        stub(mgrs.get(2).getFinalNodeCount()).toReturn(18);
        assertEquals(5 + 3 + 18, this.combiner.getFinalNodeCount());
    }

    @Test
    public void testSave() {
        List<YamlManager> mgrs = new ArrayList<YamlManager>();
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        this.combiner.setYamlManagers(mgrs);

        stub(mgrs.get(0).save()).toReturn(false);
        stub(mgrs.get(1).save()).toReturn(true);
        stub(mgrs.get(2).save()).toReturn(false);

        assertTrue(this.combiner.save());
        verify(mgrs.get(0)).save();
        verify(mgrs.get(1)).save();
        verify(mgrs.get(2)).save();
    }

    @Test
    public void testLoad() {
        List<YamlManager> mgrs = new ArrayList<YamlManager>();
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        mgrs.add(mock(YamlManager.class));
        this.combiner.setYamlManagers(mgrs);

        stub(mgrs.get(0).load()).toReturn(true);
        stub(mgrs.get(1).load()).toReturn(false);
        stub(mgrs.get(2).load()).toReturn(true);

        assertTrue(this.combiner.load());
        verify(mgrs.get(0)).load();
        verify(mgrs.get(1)).load();
        verify(mgrs.get(2)).load();
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = mock(File.class);
        stub(file.exists()).toReturn(false);
        doThrow(new IOException()).when(file).createNewFile();

        YamlCombiner spy = spy(this.combiner);
        spy.load(file);
        verify(spy).addYamlManager(argThat(new IsSimpleYamlManagerWithFile(file)));
    }

    @Test
    public void testLoadResource() throws IOException {
        String filePath = "testresource.txt";

        YamlCombiner spy = spy(this.combiner);
        spy.load(filePath);
        verify(spy).addYamlManager(argThat(new IsSimpleYamlManagerWithResource(filePath)));
    }

    class IsSimpleYamlManagerWithFile extends ArgumentMatcher<YamlManager> {
        private final File file;

        public IsSimpleYamlManagerWithFile(File file) {
            this.file = file;
        }

        @Override
        public boolean matches(Object argument) {
            if (argument instanceof SimpleYamlManager) {
                return ((SimpleYamlManager) argument).getFiles().contains(this.file);
            }
            return false;
        }
    }

    class IsSimpleYamlManagerWithResource extends ArgumentMatcher<YamlManager> {
        private final String resource;

        public IsSimpleYamlManagerWithResource(String resource) {
            this.resource = resource;
        }

        @Override
        public boolean matches(Object argument) {
            if (argument instanceof SimpleYamlManager) {
                return ((SimpleYamlManager) argument).getResource().equals(this.resource);
            }
            return false;
        }
    }

    private void compareDumperOptions(DumperOptions a, DumperOptions b) {
        assertEquals(a.getDefaultFlowStyle(), b.getDefaultFlowStyle());
        assertEquals(a.getDefaultScalarStyle(), b.getDefaultScalarStyle());
        assertEquals(a.getIndent(), b.getIndent());
        assertEquals(a.getLineBreak(), b.getLineBreak());
        assertEquals(a.getTags(), b.getTags());
        assertEquals(a.getTimeZone(), b.getTimeZone());
        assertEquals(a.getVersion(), b.getVersion());
        assertEquals(a.getWidth(), b.getWidth());
        assertEquals(a.isAllowReadOnlyProperties(), b.isAllowReadOnlyProperties());
        assertEquals(a.isAllowUnicode(), b.isAllowUnicode());
        assertEquals(a.isCanonical(), b.isCanonical());
        assertEquals(a.isExplicitEnd(), b.isExplicitEnd());
        assertEquals(a.isExplicitStart(), b.isExplicitStart());
        assertEquals(a.isPrettyFlow(), b.isPrettyFlow());
    }
}
