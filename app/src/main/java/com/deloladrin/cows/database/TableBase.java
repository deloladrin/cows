package com.deloladrin.cows.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    protected abstract ValueParams getParams(T object);
    protected abstract T getObject(Cursor cursor);

    public void create(SQLiteDatabase db)
    {
        List<String> columns = new ArrayList<>();

        for (TableColumn column : this.columns)
        {
            columns.add(column.toString());
        }

        db.execSQL("CREATE TABLE " + this.getName() + " (" + String.join(", ", columns) + ")");
    }

    public void drop(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + this.getName());
    }

    public int insert(T object)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        return this.insert(db, object);
    }

    public int insert(SQLiteDatabase db, T object)
    {
        ValueParams params = this.getParams(object);

        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (TableColumn column : params.keySet())
        {
            if (!column.isAutoIncrement())
            {
                columns.add(column.getName());
                values.add(params.getValueString(column));
            }
        }

        db.execSQL("INSERT INTO " + this.getName() + "(" + String.join(", ", columns) + ") VALUES(" + String.join(", ", values) + ")");

        return this.getLastID(db);
    }

    public void insertAll(List<T> objects)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        this.insertAll(db, objects);
    }

    public void insertAll(SQLiteDatabase db, List<T> objects)
    {
        for (T object : objects)
        {
            this.insert(db, object);
        }
    }

    public void update(T object)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        this.update(db, object);
    }

    public void update(SQLiteDatabase db, T object)
    {
        ValueParams params = this.getParams(object);

        List<String> values = new ArrayList<>();
        String condition = "";

        for (TableColumn column : params.keySet())
        {
            String assignment = column.getName() + " = " + params.getValueString(column);

            if (column.isPrimary())
            {
                condition = assignment;
            }
            else if (!column.isAutoIncrement())
            {
                values.add(assignment);
            }
        }

        db.execSQL("UPDATE " + this.getName() + " SET " + String.join(", ", values) + " WHERE " + condition);
    }

    public void updateAll(List<T> objects)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        this.updateAll(db, objects);
    }

    public void updateAll(SQLiteDatabase db, List<T> objects)
    {
        for (T object : objects)
        {
            this.update(db, object);
        }
    }

    public void delete(T object)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        this.delete(db, object);
    }

    public void delete(SQLiteDatabase db, T object)
    {
        ValueParams params = this.getParams(object);
        String condition = "";

        for (TableColumn column : params.keySet())
        {
            if (column.isPrimary())
            {
                condition = column.getName() + " = " + params.getValueString(column);
            }
        }

        db.execSQL("DELETE FROM " + this.getName() + " WHERE " + condition);
    }

    public void deleteAll(List<T> objects)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        this.deleteAll(db, objects);
    }

    public void deleteAll(SQLiteDatabase db, List<T> objects)
    {
        for (T object : objects)
        {
            this.delete(db, object);
        }
    }

    public T select(Object id)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        return this.select(db, id);
    }

    public T select(SQLiteDatabase db, Object id)
    {
        ValueParams params = new ValueParams();

        for (TableColumn column : this.columns)
        {
            if (column.isPrimary())
            {
                params.put(column, id);
            }
        }

        return this.select(db, params);
    }

    public T select(ValueParams params)
    {
        return this.select(params, SelectOrder.NONE);
    }

    public T select(ValueParams params, SelectOrder order)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        return this.select(db, params, order);
    }

    public T select(SQLiteDatabase db, ValueParams params)
    {
        return this.select(db, params, SelectOrder.NONE);
    }

    public T select(SQLiteDatabase db, ValueParams params, SelectOrder order)
    {
        Cursor cursor = this.selectValues(db, params, order);

        if (cursor != null)
        {
            return this.getObject(cursor);
        }

        return null;
    }

    public List<T> selectAll()
    {
        return this.selectAll(SelectOrder.NONE);
    }

    public List<T> selectAll(SelectOrder order)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        return this.selectAll(db, order);
    }

    public List<T> selectAll(SQLiteDatabase db)
    {
        return this.selectAll(db, SelectOrder.NONE);
    }

    public List<T> selectAll(SQLiteDatabase db, SelectOrder order)
    {
        ValueParams empty = new ValueParams();
        return this.selectAll(db, empty, order);
    }

    public List<T> selectAll(ValueParams params)
    {
        return this.selectAll(params, SelectOrder.NONE);
    }

    public List<T> selectAll(ValueParams params, SelectOrder order)
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        return this.selectAll(db, params, order);
    }

    public List<T> selectAll(SQLiteDatabase db, ValueParams params)
    {
        return this.selectAll(db, params, SelectOrder.NONE);
    }

    public List<T> selectAll(SQLiteDatabase db, ValueParams params, SelectOrder order)
    {
        Cursor cursor = this.selectValues(db, params, order);
        List<T> values = new ArrayList<>();

        if (cursor != null)
        {
            do
            {
                values.add(this.getObject(cursor));
            }
            while (cursor.moveToNext());
        }

        return values;
    }

    private Cursor selectValues(SQLiteDatabase db, ValueParams params, SelectOrder order)
    {
        List<String> values = new ArrayList<>();

        for (TableColumn column : params.keySet())
        {
            values.add(column.getName() + " = " + params.getValueString(column));
        }

        String orderString = "";

        if (order != SelectOrder.NONE)
        {
            orderString = " ORDER BY " + this.getPrimaryColumn().getName();

            switch (order)
            {
                case ASCENDING:
                    orderString += " ASC";
                    break;

                case DESCENDING:
                    orderString += " DESC";
                    break;
            }
        }

        Cursor cursor = db.rawQuery("SELECT * FROM " + this.getName() + (values.size() > 0 ? " WHERE " + String.join(" AND ", values) : "") + orderString, null);

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return cursor;
        }

        return null;
    }

    public int getLastID()
    {
        SQLiteDatabase db = this.database.getWritableDatabase();
        return this.getLastID(db);
    }

    public int getLastID(SQLiteDatabase db)
    {
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
