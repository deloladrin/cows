package com.deloladrin.cows.export;

import com.deloladrin.cows.database.DatabaseBitmap;

public class Picture
{
    private DatabaseBitmap bitmap;
    private int x;
    private int y;
    private int width;
    private int height;
    private float xoffset;
    private float yoffset;

    public Picture(DatabaseBitmap bitmap, int x, int y)
    {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.width = 1;
        this.height = 1;
    }

    public Picture(DatabaseBitmap bitmap, int x, int y, int width, int height)
    {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public byte[] getBytes()
    {
        return this.bitmap.getBytes();
    }

    public DatabaseBitmap getBitmap()
    {
        return this.bitmap;
    }

    public void setBitmap(DatabaseBitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public int getX()
    {
        return this.x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return this.y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return this.width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public float getXOffset()
    {
        return this.xoffset;
    }

    public void setXOffset(float xoffset)
    {
        this.xoffset = xoffset;
    }

    public float getYOffset()
    {
        return this.yoffset;
    }

    public void setYOffset(float yoffset)
    {
        this.yoffset = yoffset;
    }
}
