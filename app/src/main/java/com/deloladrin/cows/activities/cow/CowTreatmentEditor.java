package com.deloladrin.cows.activities.cow;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.Treatment;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CowTreatmentEditor extends ChildActivity<CowActivity> implements View.OnClickListener
{
    private Treatment treatment;

    private TextView date;

    private CowTreatmentHoof frontLeft;
    private CowTreatmentHoof frontRight;
    private CowTreatmentHoof backLeft;
    private CowTreatmentHoof backRight;

    private Button cancel;
    private Button save;

    public CowTreatmentEditor(CowActivity parent, int layoutID)
    {
        super(parent, layoutID);

        /* Load all children */
        this.date = this.findViewById(R.id.editor_date);

        this.frontLeft = new CowTreatmentHoof(this, 0b11000000, R.id.editor_front_left);
        this.frontRight = new CowTreatmentHoof(this, 0b00110000, R.id.editor_front_right);
        this.backLeft = new CowTreatmentHoof(this, 0b00001100, R.id.editor_back_left);
        this.backRight = new CowTreatmentHoof(this, 0b00000011, R.id.editor_back_right);

        this.cancel = this.findViewById(R.id.editor_cancel);
        this.save = this.findViewById(R.id.editor_save);

        /* Add events */
        this.cancel.setOnClickListener(this);
        this.save.setOnClickListener(this);
    }


    @Override
    public void onClick(View view)
    {

    }

    public Treatment getTreatment()
    {
        return this.treatment;
    }

    public void setTreatment(Treatment treatment)
    {
        this.treatment = treatment;

        if (treatment != null)
        {
            /* Set treatment date */
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");
            String date = treatment.getDate().format(dateFormatter);
            this.date.setText(date);

            /* Update children */
            List<Diagnosis> diagnoses = treatment.getDiagnoses();
            this.frontLeft.setDiagnoses(diagnoses);
            this.frontRight.setDiagnoses(diagnoses);
            this.backLeft.setDiagnoses(diagnoses);
            this.backRight.setDiagnoses(diagnoses);
        }
        else
        {
            this.date.setText("");

            this.frontLeft.setDiagnoses(null);
            this.frontRight.setDiagnoses(null);
            this.backLeft.setDiagnoses(null);
            this.backRight.setDiagnoses(null);
        }
    }
}
