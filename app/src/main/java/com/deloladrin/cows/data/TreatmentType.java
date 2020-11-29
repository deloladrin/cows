package com.deloladrin.cows.data;

import android.content.Context;

import com.deloladrin.cows.R;

public enum TreatmentType
{
    NONE(0, 0),
    LIMP(1, R.string.treatment_limp),
    WHOLE(2, R.string.treatment_whole),
    REPEAT(3, R.string.treatment_repeat);

    private int id;
    private int name;

    TreatmentType(int id, int name)
    {
        this.id = id;
        this.name = name;
    }

    public static TreatmentType parse(int typeID)
    {
        for (TreatmentType type : values())
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

    public String getName(Context context)
    {
        return context.getString(this.name);
    }

    public String getShortName(Context context)
    {
        String fullName = this.getName(context);
        return Character.toString(fullName.charAt(0));
    }
}
