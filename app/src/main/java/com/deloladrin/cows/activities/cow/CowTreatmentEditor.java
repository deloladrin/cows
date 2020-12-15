package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.cow.views.TreatmentTypeEntry;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.HoofMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.views.SelectDialog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CowTreatmentEditor extends ChildActivity<CowActivity> implements View.OnClickListener, View.OnFocusChangeListener
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

        this.frontLeft = new CowTreatmentHoof(this, HoofMask.LF, R.id.editor_front_left);
        this.frontRight = new CowTreatmentHoof(this, HoofMask.RF, R.id.editor_front_right);
        this.backLeft = new CowTreatmentHoof(this, HoofMask.LB, R.id.editor_back_left);
        this.backRight = new CowTreatmentHoof(this, HoofMask.RB, R.id.editor_back_right);

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

                Treatment treatment = new Treatment(activity.getDatabase());
                treatment.setCow(activity.getCow());
                treatment.setType(entry.getValue());
                treatment.setDate(LocalDateTime.now());
                treatment.setUser(activity.getUser());
                treatment.insert();

                /* Copy previous diagnosis */
                for (Diagnosis diagnosis : this.treatment.getDiagnoses())
                {
                    Diagnosis copy = new Diagnosis(activity.getDatabase());
                    copy.setTreatment(treatment);
                    copy.setHealedName(diagnosis.getHealedName());
                    copy.setTreatedName(diagnosis.getTreatedName());
                    copy.setNewName(diagnosis.getNewName());
                    copy.setShortName(diagnosis.getShortName());
                    copy.setState(diagnosis.getState());
                    copy.setTarget(diagnosis.getTarget());

                    /* Copy some resources */
                    List<Resource> resources = new ArrayList<>();

                    for (Resource resource : diagnosis.getResources())
                    {
                        Resource resourceCopy = resource.getCopy();

                        /* Copyable resources */
                        if (resourceCopy != null)
                        {
                            resources.add(resourceCopy);
                        }

                        /* Copy-typed resources */
                        else if (resource.getType() == ResourceType.COPY)
                        {
                            resources.add(resource);
                        }
                    }

                    copy.setResources(resources);
                    copy.insert();
                }

                activity.refreshFull();
            });

            dialog.show();
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
            String date = treatment.timestampToDate().format(dateFormatter);
            this.date.setText(date);

            /* Set comment */
            String comment = treatment.getComment();
            this.comment.setText(comment);

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

            this.status.setTreatment(null);

            this.frontLeft.setTreatment(null);
            this.frontRight.setTreatment(null);
            this.backLeft.setTreatment(null);
            this.backRight.setTreatment(null);
        }
    }
}
