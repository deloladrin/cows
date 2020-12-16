package com.deloladrin.cows.data;

import android.content.Context;

import com.deloladrin.cows.R;

public enum DiagnosisState
{
    NONE(0, 0, R.color.diagnosis_none),
    HEALED(1, R.string.diagnosis_healed, R.color.diagnosis_healed),
    TREATED(2, R.string.diagnosis_treated, R.color.diagnosis_treated),
    NEW(3, R.string.diagnosis_new, R.color.diagnosis_new);

    private int id;
    private int name;
    private int color;

    DiagnosisState(int id, int name, int color)
    {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static DiagnosisState parse(int stateID)
    {
        for (DiagnosisState state : values())
        {
            if (stateID == state.id)
            {
                return state;
            }
        }

        return null;
    }

    public static DiagnosisState worse(DiagnosisState state1, DiagnosisState state2)
    {
        if (state1.id < state2.id)
        {
            return state2;
        }

        return state1;
    }

    public int getID()
    {
        return this.id;
    }

    public String getName(Context context)
    {
        return context.getString(this.name);
    }

    public int getColor(Context context)
    {
        return context.getColor(this.color);
    }

    public String getShortName(Context context)
    {
        String fullName = this.getName(context);
        return Character.toString(fullName.charAt(0));
    }
}
