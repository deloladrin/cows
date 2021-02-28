package com.deloladrin.cows.activities.diagnosis;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.diagnosis.dialogs.DiagnosisEditDialog;
import com.deloladrin.cows.data.DiagnosisTemplate;
import com.deloladrin.cows.dialogs.YesNoDialog;

public class DiagnosisEntry implements View.OnClickListener
{
    private DiagnosisActivity parent;
    private DiagnosisTemplate template;

    private View view;

    private TextView shortName;
    private TextView name;

    private ImageButton edit;
    private ImageButton delete;

    public DiagnosisEntry(DiagnosisActivity parent, DiagnosisTemplate template, LayoutInflater inflater)
    {
        this.parent = parent;
        this.template = template;

        this.view = inflater.inflate(R.layout.entry_diagnosis, null);

        /* Load all children */
        this.shortName = this.view.findViewById(R.id.entry_short_name);
        this.name = this.view.findViewById(R.id.entry_name);

        this.edit = this.view.findViewById(R.id.entry_edit);
        this.delete = this.view.findViewById(R.id.entry_delete);

        /* Add events */
        this.view.setOnClickListener(this);
        this.edit.setOnClickListener(this);
        this.delete.setOnClickListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        /* Company image */
        String shortName = this.template.getShortName();
        this.shortName.setText(shortName);

        String name = this.template.getNewName();
        this.name.setText(name);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.edit))
        {
            /* Show edit dialog */
            DiagnosisEditDialog dialog = new DiagnosisEditDialog(this.parent, this.template);

            dialog.setOnSubmitListener((DiagnosisTemplate t) ->
            {
                /* Update and refresh */
                this.template.update();
                this.parent.refresh();
            });

            dialog.show();
            return;
        }

        if (view.equals(this.delete))
        {
            /* Request to delete template */
            YesNoDialog dialog = new YesNoDialog(this.parent);
            dialog.setText(R.string.dialog_diagnosis_delete, this.template.getNewName());

            dialog.setOnYesListener((YesNoDialog d) ->
            {
                /* Delete and refresh */
                this.template.delete();
                this.parent.refresh();
            });

            dialog.show();
            return;
        }
    }

    public DiagnosisActivity getParent()
    {
        return this.parent;
    }

    public DiagnosisTemplate getTemplate()
    {
        return this.template;
    }

    public View getView()
    {
        return this.view;
    }
}
