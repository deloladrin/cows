package com.deloladrin.cows.activities.cow;

import android.widget.ImageView;

import com.deloladrin.cows.data.DiagnosisState;

public class CowTreatmentFinger
{
    private CowTreatmentHoof parent;

    private ImageView view;
    private DiagnosisState state;

    public CowTreatmentFinger(CowTreatmentHoof parent, int viewID)
    {
        this.parent = parent;
        this.view = parent.findViewById(viewID);

        this.setState(DiagnosisState.NONE, true);
    }

    public CowTreatmentHoof getParent()
    {
        return this.parent;
    }

    public ImageView getView()
    {
        return this.view;
    }

    public DiagnosisState getState()
    {
        return this.state;
    }

    public void setState(DiagnosisState state)
    {
        this.setState(state, false);
    }

    public void setState(DiagnosisState state, boolean force)
    {
        /* Without force, check if worse */
        if (!force)
        {
            state = DiagnosisState.worse(this.state, state);
        }

        /* Update state and color */
        this.state = state;

        int color = this.state.getColor(this.view.getContext());
        this.view.setColorFilter(color);
    }
}
