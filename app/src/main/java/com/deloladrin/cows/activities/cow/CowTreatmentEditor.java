package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.views.SelectDialog;

import java.time.LocalDateTime;
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

        this.add = this.findViewById(R.id.editor_add);

        /* Add events */
        this.add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.add))
        {
            /* Create new treatment */
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

            dialog.setOnSelectListener(new SelectDialog.OnSelectListener()
            {
                @Override
                public void onSelect(View view)
                {
                    /* Create and refresh */
                    TreatmentTypeEntry entry = (TreatmentTypeEntry)view;
                    CowActivity activity = CowTreatmentEditor.this.getActivity();

                    Treatment treatment = new Treatment(activity.getDatabase());
                    treatment.setCow(activity.getCow());
                    treatment.setType(entry.getValue());
                    treatment.setDate(LocalDateTime.now());
                    treatment.setUser(activity.getUser());
                    treatment.insert();

                    activity.refreshFull();
                }
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
