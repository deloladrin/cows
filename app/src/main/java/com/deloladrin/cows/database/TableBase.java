package com.deloladrin.cows.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class TableBase<T>
{
    protected Database database;

    protected String name;
    protected List<TableColumn> columns;

    public TableBase(Database database, String name)
    {
        this.database = database;

        this.name = name;
        this.columns = new ArrayList<>();
    }

    protected abstract ContentValues getValues(T object);
    protected abstract T getObject(ContentValues values);

    public void create(SQLiteDatabase db)
    {
        List<String> columns = new ArrayList<>();

        for (TableColumn column : this.columns)
        {
            columns.add(column.toString());
        }

        db.execSQL("CREATE TABLE " + this.name + " (" + String.join(", ", columns) + ")");
    }

    public void drop(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + this.name);
    }

    public int insert(T object)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        ContentValues cv = this.getValues(object);

        /* Remove auto increment columns */
        for (TableColumn column : this.columns)
        {
            if (column.isAutoIncrement())
            {
                cv.remove(column.getName());
            }
        }

        db.insert(this.name, null, cv);
        return this.getLastID();
    }

    public void insertAll(List<T> objects)
    {
        for (T object : objects)
        {
            this.insert(object);
        }
    }

    public void update(T object)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        ContentValues cv = this.getValues(object);

        /* Where clause */
        TableColumn primary = this.getPrimaryColumn();
        String where = primary.getName() + " = ?";
        String whereArg = cv.get(primary.getName()).toString();

        db.update(this.name, cv, where, new String[] { whereArg });
    }

    public void updateAll(List<T> objects)
    {
        for (T object : objects)
        {
            this.update(object);
        }
    }

    public void delete(T object)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        ContentValues cv = this.getValues(object);

        /* Where clause */
        TableColumn primary = this.getPrimaryColumn();
        String where = primary.getName() + " = ?";
        String whereArg = cv.get(primary.getName()).toString();

        db.delete(this.name, where, new String[] { whereArg });
    }

    public void deleteAll(List<T> objects)
    {
        for (T object : objects)
        {
            this.delete(object);
        }
    }

    public T select(SelectValues values)
    {
        /* Return only single value */
        values.limit(1);
        List<T> objects = this.selectAll(values);

        if (objects != null && objects.size() > 0)
        {
            return objects.get(0);
        }

        return null;
    }

    public List<T> selectAll(SelectValues values)
    {
        SQLiteDatabase db = this.database.getReadableDatabase();
        String query = values.getQuery(this);

        Cursor cursor = db.rawQuery(query, null);

        /* Convert cursor to objects */
        if (cursor != null)
        {
            List<T> objects = new ArrayList<>();
            cursor.moveToFirst();

            if (cursor.getCount() > 0)
            {
                do
                {
                    ContentValues cv = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, cv);

                    T object = this.getObject(cv);
                    objects.add(object);
                }
                while (cursor.moveToNext());
            }

            cursor.close();
            return objects;
        }

        return null;
    }

    public int getLastID()
    {
        SQLiteDatabase db = this.database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }

        return -1;
    }

    public String getName()
    {
        return this.name;
    }

    public Database getDatabase()
    {
        return this.database;
    }

    public List<TableColumn> getColumns()
    {
        return this.columns;
    }

    public TableColumn getPrimaryColumn()
    {
        for (TableColumn column : this.columns)
        {
            if (column.isPrimary())
            {
                return column;
            }
        }

        return null;
    }
}
