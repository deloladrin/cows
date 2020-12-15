package com.deloladrin.cows.data;

public enum ResourceType
{
    NONE(0),
    FINGER(1),
    FINGER_INVERTED(2),
    HOOF(3),
    COW(4),
    COPY(5);

    private int id;

    ResourceType(int id)
    {
        this.id = id;
    }

    public static ResourceType parse(int typeID)
    {
        for (ResourceType type : values())
        {
            if (typeID == type.id)
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
}
