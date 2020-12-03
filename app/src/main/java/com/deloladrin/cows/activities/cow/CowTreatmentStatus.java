package com.deloladrin.cows.activities.cow;

import android.content.res.Resources;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.views.CircleTextView;
import com.deloladrin.cows.views.YesNoDialog;

import java.util.ArrayList;
import java.util.List;

public class CowTreatmentStatus extends ChildActivity<CowActivity> implements ToggleResourceContainer.OnClickListener
{
    private Treatment treatment;

    private CircleTextView type;
    private ToggleResourceContainer resources;

    public CowTreatmentStatus(ChildActivity<CowActivity> parent, int layout)
    {
        super(parent, layout);

        /* Load all children */
        this.type = this.findViewById(R.id.status_type);
        this.resources = this.findViewById(R.id.status_resources);

        /* Add events */
        this.resources.setOnClickListener(this);

        /* Load resources from database */
        CowActivity activity = this.getActivity();
        this.resources.refresh(activity.getDatabase());
    }

    public Treatment getTreatment()
    {
        return this.treatment;
    }

    public void setTreatment(Treatment treatment)
    {
        this.treatment = treatment;
        this.resources.reset();

        if (treatment != null)
        {
            /* Set treatment type */
            String type = treatment.getType().getShortName(this.getContext());
            this.type.setText(type);

            /* Set cow resources */
            for (Resource resource : treatment.getResources())
            {
                this.resources.setEnabled(resource, true);
            }
        }
        else
        {
            this.type.setText("");
        }
    }

    public void onResourceClick(final Resource resource)
    {
        /* Toggle after secondary check */
        YesNoDialog dialog = new YesNoDialog(this.getContext());
        Resources resources = this.getContext().getResources();

        final List<Resource> treatmentResources = this.treatment.getResources();
        final boolean contains = treatmentResources.contains(resource);

        dialog.setNoText(R.string.dialog_treatment_delete_no);
        dialog.setYesText(R.string.dialog_treatment_delete_yes);

        if (contains)
        {
            dialog.setText(R.string.dialog_resource_toggle_remove, resource.getName());
        }
        else
        {
            dialog.setText(R.string.dialog_resource_toggle_add, resource.getName());
        }

        dialog.setOnYesListener(new YesNoDialog.OnYesListener()
        {
            @Override
            public void onYesClick(YesNoDialog dialog)
            {
                if (contains)
                {
                    treatmentResources.remove(resource);
                }
                else
                {
                    treatmentResources.add(resource);
                }

                treatment.update();
                parent.getActivity().refresh();
            }
        });

        dialog.show();
    }
}
