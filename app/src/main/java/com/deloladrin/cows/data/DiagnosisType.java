package com.deloladrin.cows.data;

import android.content.Context;

import com.deloladrin.cows.R;

public enum DiagnosisType
{
    NONE(0, R.string.diagnosis_type_none),
    FINGER(1, R.string.diagnosis_type_finger),
    HOOF(3, R.string.diagnosis_type_hoof);

    private int id;
    private int name;

    DiagnosisType(int id, int name)
    {
        this.id = id;
        this.name = name;
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

    public String getName(Context context)
    {
        return context.getString(this.name);
    }
}
