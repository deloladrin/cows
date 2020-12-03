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
    private TextView comment;

    private CowTreatmentStatus status;

    private CowTreatmentHoof frontLeft;
    private CowTreatmentHoof frontRight;
    private CowTreatmentHoof backLeft;
    private CowTreatmentHoof backRight;

    private Button add;

    public CowTreatmentEditor(CowActivity parent, int layout)
    {
        super(parent, layout);

        /* Load all children */
        this.date = this.findViewById(R.id.editor_date);
        this.comment = this.findViewById(R.id.editor_comment);

        this.status = new CowTreatmentStatus(this, R.id.editor_status);

        this.frontLeft = new CowTreatmentHoof(this, 0b11000000, R.id.editor_front_left);
        this.frontRight = new CowTreatmentHoof(this, 0b00110000, R.id.editor_front_right);
        this.backLeft = new CowTreatmentHoof(this, 0b00001100, R.id.editor_back_left);
        this.backRight = new CowTreatmentHoof(this, 0b00000011, R.id.editor_back_right);

        /* Add events */
        this.add = this.findViewById(R.id.editor_add);
        this.add.setOnClickListener(this);
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

            /* Set comment */
            String comment = treatment.getComment();
            this.comment.setText(comment);

            /* Update children */
            this.status.setTreatment(treatment);

            List<Diagnosis> diagnoses = treatment.getDiagnoses();
            this.frontLeft.setDiagnoses(diagnoses);
            this.frontRight.setDiagnoses(diagnoses);
            this.backLeft.setDiagnoses(diagnoses);
            this.backRight.setDiagnoses(diagnoses);
        }
        else
        {
            this.date.setText("");
            this.comment.setText("");

            this.status.setTreatment(null);

            this.frontLeft.setDiagnoses(null);
            this.frontRight.setDiagnoses(null);
            this.backLeft.setDiagnoses(null);
            this.backRight.setDiagnoses(null);
        }
    }
}
