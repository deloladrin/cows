package com.deloladrin.cows.activities.cow.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.DiagnosisTemplate;

public class DiagnosisTemplateEntry extends LinearLayout
{
    private DiagnosisTemplate value;

    private TextView shortName;
    private TextView name;

    public DiagnosisTemplateEntry(Context context, DiagnosisTemplate value)
    {
        super(context);
        inflate(context, R.layout.entry_cow_diagnosis_template, this);

        /* Load all children */
        this.shortName = this.findViewById(R.id.entry_short_name);
        this.name = this.findViewById(R.id.entry_name);

        this.setValue(value);
    }

    public DiagnosisTemplate getValue()
    {
        return this.value;
    }

    public void setValue(DiagnosisTemplate value)
    {
        this.value = value;

        /* Update children */
        this.shortName.setText(value.getShortName());
        this.name.setText(value.getNewName());
    }
}
