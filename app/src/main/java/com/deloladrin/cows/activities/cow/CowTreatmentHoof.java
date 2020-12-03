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
    private List<Diagnosis> diagnoses;

    private int maskLeft;
    private int maskRight;
    private int maskAll;

    private CowTreatmentFinger left;
    private CowTreatmentFinger right;

    private DiagnosisContainer diagnosesLeft;
    private DiagnosisContainer diagnosesRight;
    private DiagnosisContainer diagnosesAll;

    private HoofResourceContainer resourcesLeft;
    private HoofResourceContainer resourcesRight;
    private HoofResourceContainer resourcesAll;

    public CowTreatmentHoof(ChildActivity<CowActivity> parent, int mask, int layout)
    {
        super(parent, layout);
        this.getMasks(mask);

        /* Load all children */
        this.left = new CowTreatmentFinger(this, R.id.hoof_left);
        this.right = new CowTreatmentFinger(this, R.id.hoof_right);

        this.diagnosesLeft = this.findViewById(R.id.hoof_diagnoses_left);
        this.diagnosesRight = this.findViewById(R.id.hoof_diagnoses_right);
        this.diagnosesAll = this.findViewById(R.id.hoof_diagnoses_all);

        this.resourcesLeft = this.findViewById(R.id.hoof_resources_left);
        this.resourcesRight = this.findViewById(R.id.hoof_resources_right);
        this.resourcesAll = this.findViewById(R.id.hoof_resources_all);
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

        this.resourcesLeft.clear();
        this.resourcesRight.clear();
        this.resourcesAll.clear();

        this.left.setState(DiagnosisState.NONE, true);
        this.right.setState(DiagnosisState.NONE, true);

        if (diagnoses != null)
        {
            for (Diagnosis diagnosis : diagnoses)
            {
                /* Load only valid diagnoses */
                if ((diagnosis.getTarget() & this.maskAll) != 0)
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
        if ((target & this.maskAll) == this.maskAll)
        {
            this.left.setState(state);
            this.right.setState(state);

            this.diagnosesAll.add(diagnosis);
        }

        else if ((target & this.maskLeft) == this.maskLeft)
        {
            this.left.setState(state);
            this.diagnosesLeft.add(diagnosis);
        }

        else if ((target & this.maskRight) == this.maskRight)
        {
            this.right.setState(state);
            this.diagnosesRight.add(diagnosis);
        }

        this.addResources(diagnosis, target);
    }

    private void addResources(Diagnosis diagnosis, int target)
    {
        for (Resource resource : diagnosis.getResources())
        {
            /* Show resource on correct location */
            switch (resource.getType())
            {
                case FINGER:
                    if ((target & this.maskLeft) == this.maskLeft)
                    {
                        this.resourcesLeft.add(resource);
                    }

                    else if ((target & this.maskRight) == this.maskRight)
                    {
                        this.resourcesRight.add(resource);
                    }

                    break;

                case FINGER_INVERTED:
                    if ((target & this.maskLeft) == this.maskLeft)
                    {
                        this.resourcesRight.add(resource);
                    }

                    else if ((target & this.maskRight) == this.maskRight)
                    {
                        this.resourcesLeft.add(resource);
                    }

                    break;

                case HOOF:
                    this.resourcesAll.add(resource);

                    break;

                case COW:

                    break;
            }
        }
    }

    private void getMasks(int mask)
    {
        int left = -1;
        int right = -1;

        for (int i = 0; i < Integer.BYTES * 8; i++)
        {
            boolean on = (mask & (1 << i)) != 0;

            if (on && right == -1)
            {
                right = i;
            }

            else if (on)
            {
                left = i;
                break;
            }
        }

        this.maskLeft = (1 << left);
        this.maskRight = (1 << right);
        this.maskAll = mask;
    }
}
