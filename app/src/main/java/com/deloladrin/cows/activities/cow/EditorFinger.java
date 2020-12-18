package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.deloladrin.cows.activities.cow.views.DiagnosisContainer;
import com.deloladrin.cows.activities.cow.views.FingerDialog;
import com.deloladrin.cows.activities.cow.views.ResourceContainer;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.FingerMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.Treatment;

import java.util.List;

public class EditorFinger implements View.OnClickListener
{
    private EditorHoof parent;
    private FingerMask mask;

    private List<Diagnosis> diagnoses;
    private List<Resource> resources;

    private DiagnosisContainer diagnosisContainer;
    private ResourceContainer resourceContainer;
    private ImageView view;

    private DiagnosisState state;

    public EditorFinger(EditorHoof parent, FingerMask mask, int diagnosisContainer, int resourceContainer, int view)
    {
        this.parent = parent;
        this.mask = mask;

        /* Load all children */
        this.diagnosisContainer = parent.findViewById(diagnosisContainer);
        this.resourceContainer = parent.findViewById(resourceContainer);
        this.view = parent.findViewById(view);

        /* Add events */
        this.view.setOnClickListener(this);

        this.reset();
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.view))
        {
            /* Open finger dialog */
            Treatment treatment = this.parent.getTreatment();

            if (treatment != null)
            {
                FingerDialog dialog = new FingerDialog(this.parent, treatment, this.mask);

                dialog.setOnSubmitListener((FingerDialog d) ->
                {
                    /* Refresh on submit */
                    this.parent.getActivity().refresh();
                });

                dialog.show();
            }
        }
    }

    public void reset()
    {
        /* Force reset finger state */
        this.setState(DiagnosisState.NONE, true);
    }

    public EditorHoof getParent()
    {
        return this.parent;
    }

    public FingerMask getMask()
    {
        return this.mask;
    }

    public List<Diagnosis> getDiagnoses()
    {
        return this.diagnoses;
    }

    public void setDiagnoses(List<Diagnosis> diagnoses)
    {
        this.diagnoses = diagnoses;
        this.diagnosisContainer.clear();

        if (diagnoses != null)
        {
            /* Add all to container */
            for (Diagnosis diagnosis : diagnoses)
            {
                this.diagnosisContainer.add(diagnosis);
                this.setState(diagnosis.getState());
            }
        }
    }

    public List<Resource> getResources()
    {
        return this.resources;
    }

    public void setResources(List<Resource> resources)
    {
        this.resources = resources;

        if (resources != null)
        {
            /* Add all to container */
            for (Resource resource : resources)
            {
                this.resourceContainer.add(resource);
            }
        }
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
