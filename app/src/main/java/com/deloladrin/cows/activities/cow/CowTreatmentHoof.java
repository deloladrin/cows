package com.deloladrin.cows.activities.cow;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.views.DiagnosesContainer;

import java.util.List;

public class CowTreatmentHoof extends ChildActivity<CowTreatmentEditor>
{
    private List<Diagnosis> diagnoses;

    private int maskLeft;
    private int maskRight;
    private int maskAll;

    private CowTreatmentFinger left;
    private CowTreatmentFinger right;

    private DiagnosesContainer diagnosesLeft;
    private DiagnosesContainer diagnosesRight;
    private DiagnosesContainer diagnosesAll;

    private ImageView resourceLeft;
    private ImageView resourceRight;
    private ImageView resourceAll;

    public CowTreatmentHoof(CowTreatmentEditor parent, int mask, int layoutID)
    {
        super(parent, layoutID);
        this.getMasks(mask);

        /* Load all children */
        this.left = new CowTreatmentFinger(this, R.id.hoof_left);
        this.right = new CowTreatmentFinger(this, R.id.hoof_right);

        this.diagnosesLeft = this.findViewById(R.id.hoof_diagnoses_left);
        this.diagnosesRight = this.findViewById(R.id.hoof_diagnoses_right);
        this.diagnosesAll = this.findViewById(R.id.hoof_diagnoses_all);

        this.resourceLeft = this.findViewById(R.id.hoof_resource_left);
        this.resourceRight = this.findViewById(R.id.hoof_resource_right);
        this.resourceAll = this.findViewById(R.id.hoof_resource_all);
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

        this.resourceLeft.setImageDrawable(null);
        this.resourceRight.setImageDrawable(null);
        this.resourceAll.setImageDrawable(null);

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
            Bitmap bitmap = resource.getImage().getBitmap();

            switch (resource.getType())
            {
                case FINGER:
                    if ((target & this.maskLeft) == this.maskLeft)
                    {
                        this.resourceRight.setImageBitmap(bitmap);
                    }

                    else if ((target & this.maskRight) == this.maskRight)
                    {
                        this.resourceLeft.setImageBitmap(bitmap);
                    }

                    break;

                case HOOF:
                    this.resourceAll.setImageBitmap(bitmap);

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
