package com.deloladrin.cows.activities.cow;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.cow.views.DiagnosisContainer;
import com.deloladrin.cows.activities.cow.views.ResourceContainer;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.HoofMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.TargetMask;
import com.deloladrin.cows.data.Treatment;

import java.util.ArrayList;
import java.util.List;

public class EditorHoof extends ChildActivity<CowActivity>
{
    private Treatment treatment;
    private HoofMask mask;

    private EditorFinger left;
    private EditorFinger right;

    private List<Diagnosis> diagnoses;
    private List<Resource> resources;

    private DiagnosisContainer diagnosisContainer;
    private ResourceContainer resourceContainer;

    public EditorHoof(ChildActivity<CowActivity> parent, HoofMask mask, int layout)
    {
        super(parent, layout);
        this.mask = mask;

        /* Load all children */
        this.left = new EditorFinger(this, mask.getLeftFinger(), R.id.hoof_diagnoses_left, R.id.hoof_resources, R.id.hoof_left);
        this.right = new EditorFinger(this, mask.getRightFinger(), R.id.hoof_diagnoses_right, R.id.hoof_resources, R.id.hoof_right);

        this.diagnosisContainer = this.findViewById(R.id.hoof_diagnoses_hoof);
        this.resourceContainer = this.findViewById(R.id.hoof_resources);
        this.resourceContainer.setMask(this.mask);
    }

    public Treatment getTreatment()
    {
        return this.treatment;
    }

    public void setTreatment(Treatment treatment)
    {
        this.treatment = treatment;

        this.diagnoses = new ArrayList<>();
        this.resources = new ArrayList<>();

        /* Reset diagnoses & resources */
        this.diagnosisContainer.clear();
        this.resourceContainer.clear();

        /* Reset fingers */
        this.left.reset();
        this.right.reset();

        if (treatment != null)
        {
            /* Get all diagnoses */
            List<Diagnosis> diagnosesLeft = new ArrayList<>();
            List<Diagnosis> diagnosesRight = new ArrayList<>();

            for (Diagnosis diagnosis : treatment.getDiagnoses())
            {
                if (diagnosis.getTemplate() != null)
                {
                    TargetMask target = diagnosis.getTarget();

                    if (target == this.mask)
                    {
                        this.diagnoses.add(diagnosis);
                        this.diagnosisContainer.add(diagnosis);

                        /* Affects both fingers */
                        DiagnosisState state = diagnosis.getState();
                        this.left.setState(state);
                        this.right.setState(state);
                    }

                    else if (target == this.left.getMask())
                    {
                        diagnosesLeft.add(diagnosis);
                    }

                    else if (target == this.right.getMask())
                    {
                        diagnosesRight.add(diagnosis);
                    }
                }
            }

            /* Update finger diagnoses */
            this.left.setDiagnoses(diagnosesLeft);
            this.right.setDiagnoses(diagnosesRight);

            /* Get all resources */
            List<Resource> resourcesLeft = new ArrayList<>();
            List<Resource> resourcesRight = new ArrayList<>();

            for (Resource resource : treatment.getResources())
            {
                TargetMask target = resource.getTarget();

                if (target == this.mask)
                {
                    this.resources.add(resource);
                    this.resourceContainer.add(resource);
                }

                else if (target == this.left.getMask())
                {
                    resourcesLeft.add(resource);
                }

                else if (target == this.right.getMask())
                {
                    resourcesRight.add(resource);
                }
            }

            /* Update finger resources */
            this.left.setResources(resourcesLeft);
            this.right.setResources(resourcesRight);

            this.resourceContainer.sort();
        }
        else
        {
            this.left.setDiagnoses(null);
            this.left.setResources(null);

            this.right.setDiagnoses(null);
            this.right.setResources(null);
        }
    }

    public EditorFinger getLeftFinger()
    {
        return this.left;
    }

    public EditorFinger getRightFinger()
    {
        return this.right;
    }

    public List<Diagnosis> getDiagnoses()
    {
        return this.diagnoses;
    }

    public List<Resource> getResources()
    {
        return this.resources;
    }
}
