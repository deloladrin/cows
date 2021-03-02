package com.deloladrin.cows.data;

import android.content.Context;

import com.deloladrin.cows.R;

public enum ResourceType
{
    NONE(0, R.string.resource_type_none),
    FINGER(1, R.string.resource_type_finger),
    FINGER_INVERTED(2, R.string.resource_type_finger_inverted),
    HOOF(3, R.string.resource_type_hoof);

    private int id;
    private int name;

    ResourceType(int id, int name)
    {
        this.id = id;
        this.name = name;
    }

    public static ResourceType parse(int typeID)
    {
        for (ResourceType type : values())
        {
            if (type.id == typeID)
            {
                return type;
            }
        }

        return null;
    }

    public int getID()
    {
        return this.id;
    }

    public String getName(Context context)
    {
        return context.getString(this.name);
    }
}
