package com.deloladrin.cows.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deloladrin.cows.Async;
import com.deloladrin.cows.AsyncVoid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Table<T>
{
    private static final String TAG = Table.class.getSimpleName();

    private Database database;
    private Class<T> type;

    private TableInfo info;
    private List<Column<T>> columns;
    private Column<T> primary;

    public Table(Database database, Class<T> type)
    {
        Log.i(TAG, "Loading table '" + type.getSimpleName() + "' ...");
        this.database = database;
        this.type = type;

        this.info = this.type.getAnnotation(TableInfo.class);
        this.columns = new ArrayList<>();

        if (this.info == null)
        {
            Log.e(TAG, "Type is not valid table!");
            return;
        }

        for (Field field : this.type.getDeclaredFields())
        {
            if (field.isAnnotationPresent(ColumnInfo.class))
            {
                Column<T> column = new Column<>(this, field);
                this.columns.add(column);

                if (column.getInfo().primary())
                {
                    this.primary = column;
                }
            }
        }

        this.columns.sort((Column<T> a, Column<T> b) ->
        {
           return Integer.compare(a.getInfo().index(), b.getInfo().index());
        });

        if (this.primary == null)
        {
            Log.w(TAG, "Table has no primary column!");
            return;
        }

        if (this.primary.getInnerType() != ColumnType.INTEGER)
        {
            Log.w(TAG, "The primary column must be an Integer type!");
            return;
        }
    }

    public void create()
    {
        Log.i(TAG, "Creating table '" + this.info.name() + "' ...");

        String query = "CREATE TABLE IF NOT EXISTS " + this.toString();
        Log.d(TAG, "QUERY: `" + query + "`");

        SQLiteDatabase db = this.database.getWritableDatabase();
        db.execSQL(query);
    }

    public void createAsync(AsyncVoid.Result then)
    {
        AsyncVoid.run(then, () ->
        {
            this.create();
        });
    }

    public void drop()
    {
        Log.i(TAG, "Dropping table '" + this.info.name() + "' ...");

        String query = "DROP TABLE IF EXISTS " + this.info.name();
        Log.d(TAG, "QUERY: `" + query + "`");

        SQLiteDatabase db = this.database.getWritableDatabase();
        db.execSQL(query);
    }

    public void dropAsync(AsyncVoid.Result then)
    {
        AsyncVoid.run(then, () ->
        {
           this.drop();
        });
    }

    public void insert(T object)
    {
        Log.i(TAG, "Inserting into '" + this.info.name() + "' ...");

        ContentValues values = new ContentValues();

        for (Column<T> column : this.columns)
        {
            if (!column.getInfo().autoIncrement())
            {
                column.put(values, object);
            }
        }

        SQLiteDatabase db = this.database.getWritableDatabase();
        db.insert(this.info.name(), null, values);

        if (this.primary != null)
        {
            ContentValues lastID = new ContentValues();
            lastID.put(this.primary.getInfo().name(), this.getLastID());

            this.primary.get(lastID, object);
        }
    }

    public void insertAsync(T object, AsyncVoid.Result then)
    {
        AsyncVoid.run(then, () ->
        {
            this.insert(object);
        });
    }

    public void update(T object)
    {
        Log.i(TAG, "Updating in '" + this.info.name() + "' ...");

        ContentValues values = new ContentValues();
        ContentValues where = new ContentValues();

        for (Column<T> column : this.columns)
        {
            if (column.equals(this.primary))
            {
                column.put(where, object);
            }
            else
            {
                column.put(values, object);
            }
        }

        SQLiteDatabase db = this.database.getWritableDatabase();
        db.update(this.info.name(), values, where.toString(), null);
    }

    public void updateAsync(T object, AsyncVoid.Result then)
    {
        AsyncVoid.run(then, () ->
        {
            this.update(object);
        });
    }

    public void delete(T object)
    {
        Log.i(TAG, "Deleting from '" + this.info.name() + "' ...");

        ContentValues where = new ContentValues();
        this.primary.put(where, object);

        SQLiteDatabase db = this.database.getWritableDatabase();
        db.delete(this.info.name(), where.toString(), null);
    }

    public void deleteAsync(T object, AsyncVoid.Result then)
    {
        AsyncVoid.run(then, () ->
        {
           this.delete(object);
        });
    }

    public Selection<T> select()
    {
        Log.i(TAG, "Selecting from '" + this.info.name() + "' ...");

        String query = "SELECT * FROM " + this.info.name();
        Log.d(TAG, "QUERY: `" + query + "`");

        SQLiteDatabase db = this.database.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null)
        {
            return new Selection<>(this, cursor);
        }

        return null;
    }

    public void selectAsync(Async.Result<Selection<T>> then)
    {
        Async.run(then, () ->
        {
           return this.select();
        });
    }

    public long getLastID()
    {
        Log.i(TAG, "Selecting Last ID from " + this.info.name() + " ...");

        String query = "SELECT last_insert_rowid() FROM " + this.info.name();
        Log.d(TAG, "QUERY: `" + query + "`");

        SQLiteDatabase db = this.database.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            long lastID = cursor.getLong(0);
            cursor.close();

            return lastID;
        }

        return 0;
    }

    public void getLastIDAsync(Async.Result<Long> then)
    {
        Async.run(then, () ->
        {
            return this.getLastID();
        });
    }

    public Database getDatabase()
    {
        return this.database;
    }

    public Class<T> getType()
    {
        return this.type;
    }

    public TableInfo getInfo()
    {
        return this.info;
    }

    public List<Column<T>> getColumns()
    {
        return this.columns;
    }

    public Column<T> getPrimaryColumn()
    {
        return this.primary;
    }

    @Override
    public String toString()
    {
        List<String> columnNames = new ArrayList<>();

        for (Column<T> column : this.columns)
        {
            columnNames.add(column.toString());
        }

        return this.info.name() + " (" + String.join(", ", columnNames) + ")";
    }
}
