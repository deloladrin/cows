package com.deloladrin.cows.activities.cow;

import android.view.LayoutInflater;

import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.data.Treatment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CowTreatmentHistory extends ChildActivity<CowActivity>
{
    private List<Treatment> treatments;
    private List<CowTreatmentEntry> views;

    public CowTreatmentHistory(CowActivity parent, int layout)
    {
        super(parent, layout);
    }

    public List<Treatment> getTreatments()
    {
        return this.treatments;
    }

    public void setTreatments(List<Treatment> treatments)
    {
        this.treatments = treatments;
        this.clearViews();

        /* Create all views */
        this.views = new ArrayList<>();
        LayoutInflater inflater = this.activity.getLayoutInflater();

        if (treatments != null)
        {
            for (int i = treatments.size() - 1; i >= 0; i--)
            {
                Treatment treatment = treatments.get(i);

                CowTreatmentEntry entry = new CowTreatmentEntry(this, inflater);
                entry.setTreatment(treatment);

                this.views.add(entry);
                this.layout.addView(entry.getView());
            }
        }
    }

    private void clearViews()
    {
        /* Skip the first view */
        int lastIndex = this.layout.getChildCount();

        for (int i = lastIndex - 1; i >= 1; i--)
        {
            this.layout.removeViewAt(i);
        }
    }
}
