package com.deloladrin.cows.export;

import android.content.Context;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class TableColumn
{
    private String name;

    private float width;
    private float height;

    private boolean bold;
    private boolean italic;

    private HorizontalAlignment horizontal;
    private VerticalAlignment vertical;

    public TableColumn(String name)
    {
        this.name = name;

        this.bold = false;
        this.italic = false;

        this.horizontal = HorizontalAlignment.LEFT;
        this.vertical = VerticalAlignment.TOP;
    }

    public TableColumn(Context context, int resource)
    {
        this(context.getString(resource));
    }

    public String getName()
    {
        return this.name;
    }

    public float getWidth()
    {
        return this.width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return this.height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public boolean isBold()
    {
        return this.bold;
    }

    public void setBold(boolean bold)
    {
        this.bold = bold;
    }

    public boolean isItalic()
    {
        return this.italic;
    }

    public void setItalic(boolean italic)
    {
        this.italic = italic;
    }

    public HorizontalAlignment getHorizontalAlignment()
    {
        return this.horizontal;
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontal)
    {
        this.horizontal = horizontal;
    }

    public VerticalAlignment getVerticalAlignment()
    {
        return this.vertical;
    }

    public void setVerticalAlignment(VerticalAlignment vertical)
    {
        this.vertical = vertical;
    }
}
