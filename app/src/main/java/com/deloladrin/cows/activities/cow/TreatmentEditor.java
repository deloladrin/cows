package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.cow.views.TreatmentTypeEntry;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.HoofMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.views.SelectDialog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TreatmentEditor extends ChildActivity<CowActivity> implements View.OnClickListener, View.OnFocusChangeListener
{
    private Treatment treatment;

    private TextView date;
    private TextView comment;

    private TreatmentStatus status;

    private EditorHoof frontLeft;
    private EditorHoof frontRight;
    private EditorHoof backLeft;
    private EditorHoof backRight;

    private Button add;

    public TreatmentEditor(CowActivity parent, int layout)
    {
        super(parent, layout);

        /* Load all children */
        this.date = this.findViewById(R.id.editor_date);
        this.comment = this.findViewById(R.id.editor_comment);

        this.status = new TreatmentStatus(this, R.id.editor_status);

        this.frontLeft = new EditorHoof(this, HoofMask.LF, R.id.editor_front_left);
        this.frontRight = new EditorHoof(this, HoofMask.RF, R.id.editor_front_right);
        this.backLeft = new EditorHoof(this, HoofMask.LB, R.id.editor_back_left);
        this.backRight = new EditorHoof(this, HoofMask.RB, R.id.editor_back_right);

        this.add = this.findViewById(R.id.editor_add);

        /* Add events */
        this.comment.setOnFocusChangeListener(this);
        this.add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.add))
        {
            Cow cow = this.activity.getCow();

            if (cow != null)
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

                dialog.setOnSelectListener((View v) ->
                {
                    /* Create and refresh */
                    TreatmentTypeEntry entry = (TreatmentTypeEntry) v;

                    CowActivity activity = this.getActivity();
                    Database database = activity.getDatabase();

                    Treatment treatmentCopy = new Treatment(database);
                    treatmentCopy.setCow(cow);
                    treatmentCopy.setType(entry.getValue());
                    treatmentCopy.setDate(LocalDateTime.now());
                    treatmentCopy.setUser(activity.getUser());
                    treatmentCopy.insert();

                    if (this.treatment != null)
                    {
                        /* Copy diagnoses */
                        for (Diagnosis diagnosis : this.treatment.getDiagnoses())
                        {
                            Diagnosis diagnosisCopy = new Diagnosis(database);
                            diagnosisCopy.setTreatment(treatmentCopy);
                            diagnosisCopy.setTemplate(diagnosis.getTemplate());
                            diagnosisCopy.setTarget(diagnosis.getTarget());
                            diagnosisCopy.setState(diagnosis.getState());
                            diagnosisCopy.insert();
                        }

                        /* Copy some resources */
                        for (Resource resource : this.treatment.getResources())
                        {
                            if (resource.getTemplate().isCopying())
                            {
                                Resource resourceCopy = new Resource(database);
                                resourceCopy.setTreatment(treatmentCopy);
                                resourceCopy.setTemplate(resource.getTemplate());
                                resourceCopy.setTarget(resource.getTarget());
                                resourceCopy.setCopy(true);
                                resourceCopy.insert();
                            }
                        }
                    }

                    activity.refreshFull();
                });

                dialog.show();
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus)
    {
        if (view.equals(this.comment) && !hasFocus)
        {
            /* Save comment every time it loses focus */
            String comment = this.comment.getText().toString();
            this.treatment.setComment(comment);
            this.treatment.update();
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
            this.comment.setEnabled(true);

            /* Update children */
            this.status.setTreatment(treatment);

            this.frontLeft.setTreatment(treatment);
            this.frontRight.setTreatment(treatment);
            this.backLeft.setTreatment(treatment);
            this.backRight.setTreatment(treatment);
        }
        else
        {
            this.date.setText("—");

            this.comment.setText("");
            this.comment.setEnabled(false);

            this.status.setTreatment(null);

            this.frontLeft.setTreatment(null);
            this.frontRight.setTreatment(null);
            this.backLeft.setTreatment(null);
            this.backRight.setTreatment(null);
        }
    }
}
