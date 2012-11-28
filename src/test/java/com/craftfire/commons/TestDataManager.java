package com.craftfire.commons;

import org.junit.BeforeClass;

import com.craftfire.commons.database.DataManager;

public class TestDataManager {
    private static DataManager datamanager;

    @BeforeClass
    public void init() {
        datamanager = new DataManager("usr", "pss");
    }

    //TODO: Add some tests here.
}
