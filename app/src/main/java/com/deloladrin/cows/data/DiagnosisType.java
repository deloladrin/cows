package com.deloladrin.cows.data;

public enum DiagnosisType
{
    NONE(0),
    FINGER(1),
    HOOF(3);

    private int id;

    DiagnosisType(int id)
    {
        this.id = id;
    }

    public static DiagnosisType parse(int typeID)
    {
        for (DiagnosisType type : values())
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
}
