package com.deloladrin.cows.activities.cow;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.views.CircleTextView;

public class CowTreatmentStatus extends ChildActivity<CowTreatmentEditor>
{
    private Treatment treatment;

    private CircleTextView type;
    private ToggleResourceContainer resources;

    public CowTreatmentStatus(CowTreatmentEditor parent, int layoutID)
    {
        super(parent, layoutID);

        /* Load all children */
        this.type = this.findViewById(R.id.status_type);
        this.resources = this.findViewById(R.id.status_resources);

        /* Load resources from database */
        CowActivity activity = this.parent.getParent();
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
                this.resources.set(resource, true);
            }
        }
        else
        {
            this.type.setText("");
        }
    }
}
