package com.craftfire.commons.database;

import java.util.ArrayList;
import java.util.Iterator;

public class DataList extends ArrayList<DataField> {
    public DataField get(String fieldName) {
        Iterator<DataField> iterator = iterator();
        while (iterator.hasNext()) {
            DataField dataField = iterator.next();
            if (dataField.getFieldName().equalsIgnoreCase(fieldName)) {
                return dataField;
            }
        }
        return null;
    }
}
