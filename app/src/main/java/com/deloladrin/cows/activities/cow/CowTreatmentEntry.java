package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.views.YesNoDialog;

import java.time.format.DateTimeFormatter;

public class CowTreatmentEntry implements View.OnClickListener
{
    private CowTreatmentHistory parent;
    private Treatment treatment;

    private View view;
    private TextView type;
    private TextView date;
    private TextView time;
    private TextView user;
    private ImageButton show;
    private ImageButton delete;

    public CowTreatmentEntry(CowTreatmentHistory parent, LayoutInflater inflater)
    {
        this.parent = parent;
        this.view = inflater.inflate(R.layout.activity_cow_entry, null);

        /* Load all children */
        this.type = this.view.findViewById(R.id.entry_type);
        this.date = this.view.findViewById(R.id.entry_date);
        this.time = this.view.findViewById(R.id.entry_time);
        this.user = this.view.findViewById(R.id.entry_user);
        this.show = this.view.findViewById(R.id.entry_show);
        this.delete = this.view.findViewById(R.id.entry_delete);

        /* Add events */
        this.show.setOnClickListener(this);
        this.delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        /* On show click */
        if (view.equals(this.show))
        {
            this.parent.getActivity().getEditor().setTreatment(this.treatment);
        }

        /* On delete click */
        if (view.equals(this.delete))
        {
            YesNoDialog dialog = new YesNoDialog(this.view.getContext());
            Resources resources = this.view.getResources();

            /* Treatment date */
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String date = this.treatment.getDate().format(dateFormatter);

            dialog.setText(R.string.dialog_treatment_delete_text, date);
            dialog.setNoText(R.string.dialog_treatment_delete_no);
            dialog.setYesText(R.string.dialog_treatment_delete_yes);

            dialog.setOnYesListener(new YesNoDialog.OnYesListener()
            {
                @Override
                public void onYesClick(YesNoDialog dialog)
                {
                    /* Remove treatment and restart */
                    for (Diagnosis diagnosis : treatment.getDiagnoses())
                    {
                        diagnosis.delete();
                    }

                    treatment.delete();
                    parent.getActivity().refresh();
                }
            });

            dialog.show();
        }
    }

    public CowTreatmentHistory getParent()
    {
        return this.parent;
    }

    public View getView()
    {
        return this.view;
    }

    public Treatment getTreatment()
    {
        return this.treatment;
    }

    public void setTreatment(Treatment treatment)
    {
        this.treatment = treatment;

        /* Update all views */
        Context context = this.view.getContext();

        String type = treatment.getType().getShortName(context);
        this.type.setText(type);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = treatment.getDate().format(dateFormatter);
        this.date.setText(date);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = treatment.getDate().format(timeFormatter);
        this.time.setText(time);

        String user = treatment.getUser();
        this.user.setText(user);
    }
}
