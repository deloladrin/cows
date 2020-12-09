package com.deloladrin.cows.activities.cow;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.HoofMask;
import com.deloladrin.cows.data.Resource;

import java.util.List;

public class CowTreatmentHoof extends ChildActivity<CowActivity>
{
    private List<Diagnosis> diagnoses;
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

    public List<Diagnosis> getDiagnoses()
    {
        return this.diagnoses;
    }

    public void setDiagnoses(List<Diagnosis> diagnoses)
    {
        this.diagnoses = diagnoses;

        /* Reset diagnosis */
        this.diagnosesLeft.clear();
        this.diagnosesRight.clear();
        this.diagnosesAll.clear();
        this.resources.clear();

        this.left.setState(DiagnosisState.NONE, true);
        this.right.setState(DiagnosisState.NONE, true);

        if (diagnoses != null)
        {
            for (Diagnosis diagnosis : diagnoses)
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
