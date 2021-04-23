package com.deloladrin.cows.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Selection<T> implements Iterable<T>
{
    private static final String TAG = Selection.class.getSimpleName();

    private Table<T> table;
    private Iterator<T> iterator;

    public Selection(Table<T> table, Cursor cursor)
    {
        this.table = table;
        this.iterator = new Iterator<>(table, cursor);
    }

    public void reset()
    {
        this.iterator.reset();
    }

    public T get(int index)
    {
        return this.iterator.get(index);
    }

    public int size()
    {
        return this.iterator.getCount();
    }

    public Table<T> getTable()
    {
        return this.table;
    }

    @Override
    public Iterator<T> iterator()
    {
        return this.iterator;
    }

    public Stream<T> stream()
    {
        return StreamSupport.stream(this.spliterator(), true);
    }

    public static class Iterator<T> implements java.util.Iterator<T>
    {
        private Table<T> table;
        private Cursor cursor;

        public Iterator(Table<T> table, Cursor cursor)
        {
            this.table = table;
            this.cursor = cursor;

            this.reset();
        }

        public void reset()
        {
            this.cursor.moveToFirst();
        }

        @Override
        public boolean hasNext()
        {
            return !this.cursor.isAfterLast();
        }

        public T get(int index)
        {
            this.cursor.moveToPosition(index);
            return this.next(false);
        }

        public T next(boolean close)
        {
            ContentValues values = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(this.cursor, values);

            T object = this.newInstance();

            for (Column<T> column : this.table.getColumns())
            {
                column.get(values, object);
            }

            this.cursor.moveToNext();

            if (close && !this.hasNext())
            {
                this.cursor.close();
            }

            return object;
        }

        @Override
        public T next()
        {
            return this.next(true);
        }

        private T newInstance()
        {
            try
            {
                Constructor<T> ctor = this.table.getType().getConstructor();
                return ctor.newInstance();
            }
            catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e)
            {
                Log.e(TAG, "Failed to create an instance of type '" + this.table.getType().getSimpleName() + "'!", e);
                return null;
            }
        }

        public Table<T> getTable()
        {
            return this.table;
        }

        public Cursor getCursor()
        {
            return this.cursor;
        }

        public int getCount()
        {
            return this.cursor.getCount();
        }
    }
}
