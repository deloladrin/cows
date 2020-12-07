package com.deloladrin.cows.activities.cow;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.Resource;

import java.util.List;

public class CowTreatmentHoof extends ChildActivity<CowActivity>
{
    public static final int MASK_ALL = 0b11;
    public static final int MASK_LEFT = 0b10;
    public static final int MASK_RIGHT = 0b01;

    private List<Diagnosis> diagnoses;
    private int mask;

    private CowTreatmentFinger left;
    private CowTreatmentFinger right;

    private DiagnosisContainer diagnosesLeft;
    private DiagnosisContainer diagnosesRight;
    private DiagnosisContainer diagnosesAll;
    private HoofResourceContainer resources;

    public CowTreatmentHoof(ChildActivity<CowActivity> parent, int mask, int layout)
    {
        super(parent, layout);
        this.mask = mask;

        /* Load all children */
        this.left = new CowTreatmentFinger(this, R.id.hoof_left);
        this.right = new CowTreatmentFinger(this, R.id.hoof_right);

        this.diagnosesLeft = this.findViewById(R.id.hoof_diagnoses_left);
        this.diagnosesRight = this.findViewById(R.id.hoof_diagnoses_right);
        this.diagnosesAll = this.findViewById(R.id.hoof_diagnoses_all);
        this.resources = this.findViewById(R.id.hoof_resources);
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
                if ((diagnosis.getTarget() & this.mask) != 0)
                {
                    this.addDiagnosis(diagnosis);
                }
            }
        }
    }

    private void addDiagnosis(Diagnosis diagnosis)
    {
        DiagnosisState state = diagnosis.getState();
        int target = this.getLocalMask(diagnosis.getTarget());

        /* Add diagnosis to correct finger */
        if (target == MASK_ALL)
        {
            this.left.setState(state);
            this.right.setState(state);

            this.diagnosesAll.add(diagnosis);
        }

        else if (target == MASK_LEFT)
        {
            this.left.setState(state);
            this.diagnosesLeft.add(diagnosis);
        }

        else if (target == MASK_RIGHT)
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

    private int getLocalMask(int mask)
    {
        if (mask != 0)
        {
            int m = this.mask;
            int n = 0;

            while ((m & 0x1) == 0)
            {
                m >>= 1;
                n++;
            }

            return (mask >> n);
        }

        return 0;
    }
}
