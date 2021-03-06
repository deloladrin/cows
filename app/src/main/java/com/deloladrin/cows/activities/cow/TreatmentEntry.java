package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.dialogs.YesNoDialog;

import java.time.format.DateTimeFormatter;

public class TreatmentEntry implements View.OnClickListener
{
    private TreatmentHistory parent;
    private Treatment treatment;

    private View view;
    private TextView type;
    private TextView date;
    private TextView time;
    private ImageButton delete;

    public TreatmentEntry(TreatmentHistory parent, LayoutInflater inflater)
    {
        this.parent = parent;
        this.view = inflater.inflate(R.layout.entry_cow, null);

        /* Load all children */
        this.type = this.view.findViewById(R.id.entry_type);
        this.date = this.view.findViewById(R.id.entry_date);
        this.time = this.view.findViewById(R.id.entry_time);
        this.delete = this.view.findViewById(R.id.entry_delete);

        /* Add events */
        this.view.setOnClickListener(this);
        this.delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        /* On show click */
        if (view.equals(this.view))
        {
            this.parent.getActivity().getEditor().setTreatment(this.treatment);
        }

        /* On delete click */
        if (view.equals(this.delete))
        {
            YesNoDialog dialog = new YesNoDialog(this.view.getContext());

            /* Treatment date */
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String date = this.treatment.getDate().format(dateFormatter);

            dialog.setText(R.string.dialog_treatment_delete, date);

            dialog.setOnYesListener((YesNoDialog d) ->
            {
                /* Remove treatment and restart */
                this.treatment.delete();
                this.parent.getActivity().refreshFull();
            });

            dialog.show();
        }
    }

    public TreatmentHistory getParent()
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
    }
}
