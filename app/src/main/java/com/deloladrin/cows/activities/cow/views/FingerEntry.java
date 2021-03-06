package com.deloladrin.cows.activities.cow.views;

import android.content.Context;

import androidx.appcompat.widget.AppCompatButton;

import com.deloladrin.cows.data.Diagnosis;

public class FingerEntry extends AppCompatButton
{
    private Diagnosis diagnosis;

    public FingerEntry(Context context)
    {
        super(context);
    }

    public Diagnosis getDiagnosis()
    {
        return this.diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis)
    {
        this.diagnosis = diagnosis;
    }
}
