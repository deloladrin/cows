package com.deloladrin.cows.storage;

import android.content.ContentValues;

import java.util.Arrays;
import java.util.List;

public enum ColumnType
{
    INTEGER(new Converter()
    {
        @Override
        public void put(ContentValues values, String column, Object value)
        {
            if (value instanceof Byte)
            {
                values.put(column, (byte) value);
                return;
            }

            if (value instanceof Short)
            {
                values.put(column, (short) value);
                return;
            }

            if (value instanceof Integer)
            {
                values.put(column, (int) value);
                return;
            }

            if (value instanceof Long)
            {
                values.put(column, (long) value);
                return;
            }

            values.putNull(column);
        }

        @Override
        public Object get(ContentValues values, String column, Class<?> type)
        {
            if (type.equals(byte.class))
            {
                return values.getAsByte(column);
            }

            if (type.equals(short.class))
            {
                return values.getAsShort(column);
            }

            if (type.equals(int.class))
            {
                return values.getAsInteger(column);
            }

            if (type.equals(long.class))
            {
                return values.getAsLong(column);
            }

            return null;
        }

    }, byte.class, short.class, int.class, long.class),

    REAL(new Converter()
    {
        @Override
        public void put(ContentValues values, String column, Object value)
        {
            if (value instanceof Float)
            {
                values.put(column, (float) value);
                return;
            }

            if (value instanceof Double)
            {
                values.put(column, (double) value);
                return;
            }

            values.putNull(column);
        }

        @Override
        public Object get(ContentValues values, String column, Class<?> type)
        {
            if (type.equals(float.class))
            {
                return values.getAsFloat(column);
            }

            if (type.equals(double.class))
            {
                return values.getAsDouble(column);
            }

            return null;
        }

    }, float.class, double.class),

    TEXT(new Converter()
    {
        @Override
        public void put(ContentValues values, String column, Object value)
        {
            if (value instanceof String)
            {
                values.put(column, (String) value);
                return;
            }

            values.putNull(column);
        }

        @Override
        public Object get(ContentValues values, String column, Class<?> type)
        {
            if (type.equals(String.class))
            {
                return values.getAsString(column);
            }

            return null;
        }

    }, String.class),

    BLOB(new Converter()
    {
        @Override
        public void put(ContentValues values, String column, Object value)
        {
            if (value instanceof byte[])
            {
                values.put(column, (byte[]) value);
                return;
            }

            values.putNull(column);
        }

        @Override
        public Object get(ContentValues values, String column, Class<?> type)
        {
            if (type.equals(byte[].class))
            {
                return values.getAsByteArray(column);
            }

            return null;
        }

    }, byte[].class);

    private Converter converter;
    private List<Class<?>> supported;

    ColumnType(Converter converter, Class<?>... supported)
    {
        this.converter = converter;
        this.supported = Arrays.asList(supported);
    }

    public static ColumnType get(Class<?> type)
    {
        for (ColumnType value : ColumnType.values())
        {
            if (value.isSupportedType(type))
            {
                return value;
            }
        }

        return null;
    }

    public void put(ContentValues values, String column, Object value)
    {
        this.converter.put(values, column, value);
    }

    public Object get(ContentValues values, String column, Class<?> type)
    {
        return this.converter.get(values, column, type);
    }

    public Converter getConverter()
    {
        return this.converter;
    }

    public List<Class<?>> getSupportedTypes()
    {
        return this.supported;
    }

    public boolean isSupportedType(Class<?> type)
    {
        return this.supported.contains(type);
    }

    public static interface Converter
    {
        void put(ContentValues values, String column, Object value);
        Object get(ContentValues values, String column, Class<?> type);
    }
}
