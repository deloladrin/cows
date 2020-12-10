package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.deloladrin.cows.activities.cow.views.FingerDialog;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.FingerMask;
import com.deloladrin.cows.data.Treatment;

public class CowTreatmentFinger implements View.OnClickListener
{
    private CowTreatmentHoof parent;
    private FingerMask mask;

    private ImageView view;
    private DiagnosisState state;

    public CowTreatmentFinger(CowTreatmentHoof parent, FingerMask mask, int view)
    {
        this.parent = parent;
        this.mask = mask;

        this.view = parent.findViewById(view);
        this.view.setOnClickListener(this);

        this.setState(DiagnosisState.NONE, true);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.view))
        {
            /* Open finger dialog */
            Context context = this.parent.getContext();
            Treatment treatment = this.parent.getTreatment();

            FingerDialog dialog = new FingerDialog(context, treatment, this.mask);

            dialog.setOnSubmitListener(new FingerDialog.OnSubmitListener()
            {
                @Override
                public void onSubmit(FingerDialog dialog)
                {
                    /* Refresh on submit */
                    CowTreatmentFinger.this.parent.getActivity().refresh();
                }
            });

            dialog.show();
        }
    }

    public CowTreatmentHoof getParent()
    {
        return this.parent;
    }

    public FingerMask getMask()
    {
        return this.mask;
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
