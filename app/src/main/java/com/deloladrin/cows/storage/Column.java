package com.deloladrin.cows.storage;

import android.content.ContentValues;
import android.util.Log;

import java.lang.reflect.Field;

public class Column<T>
{
    private static final String TAG = Column.class.getSimpleName();

    private Table<T> table;
    private Field field;
    private Class<?> type;

    private ColumnInfo info;
    private ColumnType innerType;

    public Column(Table<T> table, Field field)
    {
        Log.d(TAG, "Loading column '" + field.getName() + "' ...");
        this.table = table;
        this.field = field;
        this.type = field.getType();

        this.info = this.field.getAnnotation(ColumnInfo.class);
        this.innerType = ColumnType.get(this.type);

        if (this.innerType == null)
        {
            Log.e(TAG, "Column does not have a valid type!");
            return;
        }

        this.field.setAccessible(true);
    }

    public void put(ContentValues values, T object)
    {
        try
        {
            Object value = this.field.get(object);
            this.innerType.put(values, this.info.name(), value);
        }
        catch (IllegalAccessException e)
        {
            Log.e(TAG, "Failed to get value from column '" + this.info.name() + "'!", e);
        }
    }

    public void get(ContentValues values, T object)
    {
        try
        {
            Object value = this.innerType.get(values, this.info.name(), this.type);
            this.field.set(object, value);
        }
        catch (IllegalAccessException e)
        {
            Log.e(TAG, "Failed to put value into column '" + this.info.name() + "'!", e);
        }
    }

    public Table<T> getTable()
    {
        return this.table;
    }

    public Field getField()
    {
        return this.field;
    }

    public Class<?> getType()
    {
        return this.type;
    }

    public ColumnInfo getInfo()
    {
        return this.info;
    }

    public ColumnType getInnerType()
    {
        return this.innerType;
    }

    @Override
    public String toString()
    {
        String name = this.info.name() + " " + this.innerType.name();

        if (this.info.primary())
        {
            name += " PRIMARY KEY";
        }

        if (this.info.autoIncrement())
        {
            name += " AUTOINCREMENT";
        }

        return name;
    }
}
