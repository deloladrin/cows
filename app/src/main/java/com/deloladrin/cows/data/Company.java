package com.deloladrin.cows.data;

import android.graphics.Bitmap;

import com.deloladrin.cows.storage.ColumnInfo;
import com.deloladrin.cows.storage.DatabaseUtils;
import com.deloladrin.cows.storage.TableInfo;

@TableInfo(name = "companies")
public class Company
{
    @ColumnInfo(index = 0, name = "id", primary = true, autoIncrement = true)
    private int id;

    @ColumnInfo(index = 1, name = "name")
    private String name;

    @ColumnInfo(index = 2, name = "grp")
    private String group;

    @ColumnInfo(index = 3, name = "icon")
    private byte[] icon;

    @ColumnInfo(index = 4, name = "last_grp")
    private String lastGroup;

    public Company(String name, String group, Bitmap icon, String lastGroup)
    {
        this.setName(name);
        this.setGroup(group);
        this.setIcon(icon);
        this.setLastGroup(lastGroup);
    }

    public Company()
    {
    }

    public int getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGroup()
    {
        return this.group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public Bitmap getIcon()
    {
        return DatabaseUtils.bytesToBitmap(this.icon);
    }

    public void setIcon(Bitmap bitmap)
    {
        this.icon = DatabaseUtils.bitmapToBytes(bitmap);
    }

    public String getLastGroup()
    {
        return this.lastGroup;
    }

    public void setLastGroup(String lastGroup)
    {
        this.lastGroup = lastGroup;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof Company))
            return false;

        return ((Company) obj).id == this.id;
    }

    @Override
    public int hashCode()
    {
        return this.id;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
