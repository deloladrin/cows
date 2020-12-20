package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.view.View;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.cow.views.StatusTemplateContainer;
import com.deloladrin.cows.activities.cow.views.TreatmentTypeEntry;
import com.deloladrin.cows.data.Status;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.views.CircleTextView;
import com.deloladrin.cows.dialogs.SelectDialog;
import com.deloladrin.cows.dialogs.YesNoDialog;

import java.util.List;

public class TreatmentStatus extends ChildActivity<CowActivity> implements View.OnClickListener, StatusTemplateContainer.OnToggleListener
{
    private Treatment treatment;
    private List<Status> statuses;

    private CircleTextView type;
    private StatusTemplateContainer templates;

    public TreatmentStatus(ChildActivity<CowActivity> parent, int layout)
    {
        super(parent, layout);

        /* Load all children */
        this.type = this.findViewById(R.id.status_type);
        this.templates = this.findViewById(R.id.status_templates);

        /* Add events */
        this.type.setOnClickListener(this);
        this.templates.setOnToggleListener(this);

        /* Load templates from database */
        this.templates.refresh(this.getDatabase());
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.type))
        {
            if (this.treatment != null)
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
    }

    @Override
    public void onToggle(StatusTemplate template)
    {
        if (this.treatment != null)
        {
            /* Toggle after secondary check */
            YesNoDialog dialog = new YesNoDialog(this.getContext());
            Status status = this.getStatus(template);

            if (status != null)
            {
                dialog.setText(R.string.dialog_resource_toggle_remove, template.getName());
            }
            else
            {
                dialog.setText(R.string.dialog_resource_toggle_add, template.getName());
            }

            dialog.setOnYesListener((YesNoDialog d) ->
            {
                /* Toggle and refresh */
                if (status != null)
                {
                    status.delete();
                }
                else
                {
                    Status newStatus = new Status(this.getDatabase(), 0, this.treatment, template);
                    newStatus.insert();
                }

                this.activity.refresh();
            });

            dialog.show();
        }
    }

    public Treatment getTreatment()
    {
        return this.treatment;
    }

    public void setTreatment(Treatment treatment)
    {
        this.treatment = treatment;
        this.templates.reset();

        if (treatment != null)
        {
            /* Set treatment type */
            String type = treatment.getType().getShortName(this.getContext());
            this.type.setText(type);

            /* Show current statuses */
            this.statuses = treatment.getStatuses();

            for (Status status : this.statuses)
            {
                this.templates.setEnabled(status.getTemplate(), true);
            }
        }
        else
        {
            this.type.setText("");
        }
    }

    public List<Status> getStatuses()
    {
        return this.statuses;
    }

    public Status getStatus(StatusTemplate template)
    {
        for (Status status : this.statuses)
        {
            if (status.getTemplate().equals(template))
            {
                return status;
            }
        }

        return null;
    }
}
