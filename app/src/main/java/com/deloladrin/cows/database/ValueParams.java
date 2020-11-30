package com.deloladrin.cows.database;

import java.util.HashMap;

public class ValueParams extends HashMap<TableColumn, Object>
{
    public ValueParams()
    {
        super();
    }

    public String getValueString(TableColumn column)
    {
        Object result = this.get(column);

        if (result != null)
        {
            String resultString = result.toString();

            if (column.getType() == ValueType.TEXT)
            {
                return "\"" + resultString + "\"";
            }

            if (column.getType() == ValueType.BLOB)
            {
                return "x\'" + resultString + "\'";
            }

            return resultString;
        }

        return null;
    }
}
