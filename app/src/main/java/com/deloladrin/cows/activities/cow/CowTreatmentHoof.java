package com.deloladrin.cows.activities.cow;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.cow.views.DiagnosisContainer;
import com.deloladrin.cows.activities.cow.views.HoofResourceContainer;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.HoofMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.Treatment;

public class CowTreatmentHoof extends ChildActivity<CowActivity>
{
    private Treatment treatment;
    private HoofMask mask;

    private CowTreatmentFinger left;
    private CowTreatmentFinger right;

    private DiagnosisContainer diagnosesLeft;
    private DiagnosisContainer diagnosesRight;
    private DiagnosisContainer diagnosesAll;
    private HoofResourceContainer resources;

    public CowTreatmentHoof(ChildActivity<CowActivity> parent, HoofMask mask, int layout)
    {
        super(parent, layout);
        this.mask = mask;

        /* Load all children */
        this.left = new CowTreatmentFinger(this, mask.getLeftFinger(), R.id.hoof_left);
        this.right = new CowTreatmentFinger(this, mask.getRightFinger(), R.id.hoof_right);

        this.diagnosesLeft = this.findViewById(R.id.hoof_diagnoses_left);
        this.diagnosesRight = this.findViewById(R.id.hoof_diagnoses_right);
        this.diagnosesAll = this.findViewById(R.id.hoof_diagnoses_all);

        this.resources = this.findViewById(R.id.hoof_resources);
        this.resources.setMask(this.mask);
    }

    public Treatment getTreatment()
    {
        return this.treatment;
    }

    public void setTreatment(Treatment treatment)
    {
        this.treatment = treatment;

        /* Reset diagnosis */
        this.diagnosesLeft.clear();
        this.diagnosesRight.clear();
        this.diagnosesAll.clear();
        this.resources.clear();

        this.left.setState(DiagnosisState.NONE, true);
        this.right.setState(DiagnosisState.NONE, true);

        if (treatment != null)
        {
            for (Diagnosis diagnosis : treatment.getDiagnoses())
            {
                /* Load only valid diagnoses */
                if (this.mask.contains(diagnosis.getTarget()))
                {
                    this.addDiagnosis(diagnosis);
                }
            }
        }
    }

    private void addDiagnosis(Diagnosis diagnosis)
    {
        DiagnosisState state = diagnosis.getState();
        int target = diagnosis.getTarget();

        /* Add diagnosis to correct finger */
        if (target == this.mask.getMask())
        {
            this.left.setState(state);
            this.right.setState(state);

            this.diagnosesAll.add(diagnosis);
        }

        else if (target == this.mask.getLeftFinger().getMask())
        {
            this.left.setState(state);
            this.diagnosesLeft.add(diagnosis);
        }

        else if (target == this.mask.getRightFinger().getMask())
        {
            this.right.setState(state);
            this.diagnosesRight.add(diagnosis);
        }

        /* Add all resources */
        for (Resource resource : diagnosis.getResources())
        {
            this.resources.add(resource, target);
        }
    }
}
