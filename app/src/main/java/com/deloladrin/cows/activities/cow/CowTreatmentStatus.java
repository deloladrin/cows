package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.view.View;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.cow.views.ToggleResourceContainer;
import com.deloladrin.cows.activities.cow.views.TreatmentTypeEntry;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.views.CircleTextView;
import com.deloladrin.cows.views.SelectDialog;
import com.deloladrin.cows.views.YesNoDialog;

import java.util.List;

public class CowTreatmentStatus extends ChildActivity<CowActivity> implements View.OnClickListener, ToggleResourceContainer.OnToggleListener
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
        this.type.setOnClickListener(this);
        this.resources.setOnToggleListener(this);

        /* Load resources from database */
        CowActivity activity = this.getActivity();
        this.resources.refresh(this.getDatabase());
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

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.type))
        {
            /* Select different type */
            Context context = this.getContext();
            SelectDialog<TreatmentTypeEntry> dialog = new SelectDialog<>(context);

            dialog.setText(R.string.dialog_treatment_type);

            for (TreatmentType type : TreatmentType.values())
            {
                if (type != TreatmentType.NONE)
                {
                    TreatmentTypeEntry entry = new TreatmentTypeEntry(context, type);
                    dialog.add(entry);
                }
            }

            dialog.setOnSelectListener((View v) ->
            {
                /* Update and refresh */
                TreatmentTypeEntry entry = (TreatmentTypeEntry) v;

                this.treatment.setType(entry.getValue());
                this.treatment.update();
                this.activity.refresh();
            });

            dialog.show();
        }
    }

    @Override
    public void onToggle(final Resource resource)
    {
        /* Toggle after secondary check */
        YesNoDialog dialog = new YesNoDialog(this.getContext());

        final List<Resource> treatmentResources = this.treatment.getResources();
        final boolean contains = treatmentResources.contains(resource);

        if (contains)
        {
            dialog.setText(R.string.dialog_resource_toggle_remove, resource.getName());
        }
        else
        {
            dialog.setText(R.string.dialog_resource_toggle_add, resource.getName());
        }

        dialog.setOnYesListener((YesNoDialog d) ->
        {
            /* Toggle and refresh */
            if (contains)
            {
                treatmentResources.remove(resource);
            }
            else
            {
                treatmentResources.add(resource);
            }

            this.treatment.update();
            this.activity.refresh();
        });

        dialog.show();
    }
}
