package com.deloladrin.cows.loader;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class AttributeRegistry implements AutoCloseable
{
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final int DEFAULT_INTEGER = 0;
    private static final float DEFAULT_FLOAT = 0.0f;
    private static final int DEFAULT_COLOR = 0xFFFFFFFF;
    private static final float DEFAULT_DIMENSION = 0.0f;
    private static final int DEFAULT_DIMENSION_PIXEL_OFFSET = 0;
    private static final int DEFAULT_DIMENSION_PIXEL_SIZE = 0;
    private static final int DEFAULT_LAYOUT_DIMENSION = 0;
    private static final float DEFAULT_FRACTION = 0.0f;
    private static final int DEFAULT_RESOURCE = 0;
    private static final int DEFAULT_THEME_ATTRIBUTE = 0;
    private static final int DEFAULT_SOURCE_RESOURCE = 0;

    private Context context;
    private TypedArray array;

    public AttributeRegistry(Context context, AttributeSet attrs, int[] styleableIDs)
    {
        this.context = context;
        this.array = context.obtainStyledAttributes(attrs, styleableIDs);
    }

    public int getIndex(int at)
    {
        return this.array.getIndex(at);
    }

    public CharSequence getText(int index)
    {
        return this.array.getText(index);
    }

    public String getString(int index)
    {
        return this.array.getString(index);
    }

    public String getNonResourceString(int index)
    {
        return this.array.getNonResourceString(index);
    }

    public boolean getBoolean(int index, boolean defValue)
    {
        return this.array.getBoolean(index, defValue);
    }

    public boolean getBoolean(int index)
    {
        return this.getBoolean(index, DEFAULT_BOOLEAN);
    }

    public int getInteger(int index, int defValue)
    {
        return this.array.getInt(index, defValue);
    }

    public int getInteger(int index)
    {
        return this.getInteger(index, DEFAULT_INTEGER);
    }

    public float getFloat(int index, float defValue)
    {
        return this.array.getFloat(index, defValue);
    }

    public float getFloat(int index)
    {
        return this.getFloat(index, DEFAULT_FLOAT);
    }

    public int getColor(int index, int defValue)
    {
        return this.array.getColor(index, defValue);
    }

    public int getColor(int index)
    {
        return this.getColor(index, DEFAULT_COLOR);
    }

    public ColorStateList getColorStateList(int index)
    {
        return this.array.getColorStateList(index);
    }

    public float getDimension(int index, float defValue)
    {
        return this.array.getDimension(index, defValue);
    }

    public float getDimension(int index)
    {
        return this.getDimension(index, DEFAULT_DIMENSION);
    }

    public int getDimensionPixelOffset(int index, int defValue)
    {
        return this.array.getDimensionPixelOffset(index, defValue);
    }

    public int getDimensionPixelOffset(int index)
    {
        return this.getDimensionPixelOffset(index, DEFAULT_DIMENSION_PIXEL_OFFSET);
    }

    public int getDimensionPixelSize(int index, int defValue)
    {
        return this.array.getDimensionPixelSize(index, defValue);
    }

    public int getDimensionPixelSize(int index)
    {
        return this.getDimensionPixelSize(index, DEFAULT_DIMENSION_PIXEL_SIZE);
    }

    public int getLayoutDimension(int index, String name)
    {
        return this.array.getLayoutDimension(index, name);
    }

    public int getLayoutDimension(int index, int defValue)
    {
        return this.array.getLayoutDimension(index, defValue);
    }

    public int getLayoutDimension(int index)
    {
        return this.getLayoutDimension(index, DEFAULT_LAYOUT_DIMENSION);
    }

    public float getFraction(int index, int base, int pbase, float defValue)
    {
        return this.array.getFraction(index, base, pbase, defValue);
    }

    public float getFraction(int index, int base, int pbase)
    {
        return this.array.getFraction(index, base, pbase, DEFAULT_FRACTION);
    }

    public int getResourceID(int index, int defValue)
    {
        return this.array.getResourceId(index, defValue);
    }

    public int getResourceID(int index)
    {
        return this.getResourceID(index, DEFAULT_RESOURCE);
    }

    public int getThemeAttributeID(int index, int defValue)
    {
        return this.array.getResourceId(index, defValue);
    }

    public int getThemeAttributeID(int index)
    {
        return this.getThemeAttributeID(index, DEFAULT_THEME_ATTRIBUTE);
    }

    public Drawable getDrawable(int index)
    {
        return this.array.getDrawable(index);
    }

    public Typeface getFont(int index)
    {
        return this.array.getFont(index);
    }

    public CharSequence[] getTextArray(int index)
    {
        return this.array.getTextArray(index);
    }

    public int getSourceResourceID(int index, int defValue)
    {
        return this.array.getSourceResourceId(index, defValue);
    }

    public int getSourceResourceID(int index)
    {
        return this.getSourceResourceID(index, DEFAULT_SOURCE_RESOURCE);
    }

    public boolean hasValue(int index)
    {
        return this.array.hasValue(index);
    }

    public boolean hasValueOrEmpty(int index)
    {
        return this.array.hasValueOrEmpty(index);
    }

    public int length()
    {
        return this.array.length();
    }

    public int getIndexCount()
    {
        return this.array.getIndexCount();
    }

    public Resources getResources()
    {
        return this.array.getResources();
    }

    public Context getContext()
    {
        return this.context;
    }

    @Override
    public void close()
    {
        this.array.recycle();
    }
}
