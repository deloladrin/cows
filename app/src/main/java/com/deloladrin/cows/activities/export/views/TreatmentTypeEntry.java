package com.deloladrin.cows.activities.export.views;

import android.content.Context;

import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.views.ToggleTextView;

public class TreatmentTypeEntry extends ToggleTextView
{
    private TreatmentType type;

    public TreatmentTypeEntry(Context context)
    {
        super(context);
    }

    public TreatmentType getType()
    {
        return this.type;
    }

    public void setType(TreatmentType type)
    {
        this.type = type;

        /* Update name */
        String name = this.type.getShortName(this.getContext());
        this.setText(name);
    }
}
