package com.craftfire.commons.database;

import java.util.ArrayList;
import java.util.Iterator;

public class DataList<E> extends ArrayList<E> {
    public DataField get(String fieldName) {
        Iterator iterator = iterator();
        while (iterator.hasNext()) {
            DataField dataField = (DataField) iterator.next();
            if (dataField.getFieldName().equalsIgnoreCase(fieldName)) {
                return dataField;
            }
        }
        return null;
    }
}
