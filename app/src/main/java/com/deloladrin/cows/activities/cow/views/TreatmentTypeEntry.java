package com.deloladrin.cows.activities.cow.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.TreatmentType;

public class TreatmentTypeEntry extends LinearLayout
{
    private TreatmentType value;

    private TextView icon;
    private TextView name;

    public TreatmentTypeEntry(Context context, TreatmentType value)
    {
        super(context);
        inflate(this.getContext(), R.layout.entry_cow_treatment_type, this);

        /* Load all children */
        this.icon = this.findViewById(R.id.entry_icon);
        this.name = this.findViewById(R.id.entry_name);

        this.setValue(value);
    }

    public TreatmentType getValue()
    {
        return this.value;
    }

    public void setValue(TreatmentType value)
    {
        this.value = value;
        Context context = this.getContext();

        /* Update children */
        this.icon.setText(value.getShortName(context));
        this.name.setText(value.getName(context));
    }
}
