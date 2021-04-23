package com.deloladrin.cows.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.deloladrin.cows.Async;
import com.deloladrin.cows.AsyncVoid;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper
{
    private static final String TAG = Database.class.getSimpleName();

    private Context context;
    private String name;

    private List<Table<?>> tables;

    public Database(Context context, String name)
    {
        super(context, name, null, 1);

        Log.i(TAG, "Opening database '" + name + "' ...");
        this.context = context;
        this.name = name;

        this.tables = new ArrayList<>();
    }

    public void register(Class<?> tableType)
    {
        Table<?> table = new Table<>(this, tableType);
        this.tables.add(table);
    }

    public void initialize()
    {
        Log.i(TAG, "Initializing database " + this.name + " ...");

        for (Table<?> table : this.tables)
        {
            table.create();
        }
    }

    public void initializeAsync(AsyncVoid.Result then)
    {
        AsyncVoid.run(then, () ->
        {
            this.initialize();
        });
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public Context getContext()
    {
        return this.context;
    }

    public String getName()
    {
        return this.name;
    }

    public List<Table<?>> getTables()
    {
        return this.tables;
    }

    public <T> Table<T> getTable(Class<T> type)
    {
        for (Table<?> table : this.tables)
        {
            if (table.getType().equals(type))
            {
                return (Table<T>) table;
            }
        }

        Log.w(TAG, "Table of type '" + type.getSimpleName() + "' is not registered!");
        return null;
    }
}
