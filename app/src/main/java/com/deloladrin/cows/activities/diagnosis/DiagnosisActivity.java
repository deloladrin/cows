package com.deloladrin.cows.activities.diagnosis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.company.dialogs.CompanyEditDialog;
import com.deloladrin.cows.activities.diagnosis.dialogs.DiagnosisEditDialog;
import com.deloladrin.cows.activities.template.TemplateActivity;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.DiagnosisTemplate;
import com.deloladrin.cows.database.SelectValues;

public class DiagnosisActivity extends TemplateActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Update header */
        this.image.setImageResource(R.drawable.icon_diagnosis);
        this.name.setText(R.string.activity_diagnosis);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.add))
        {
            /* Add new template */
            DiagnosisTemplate template = new DiagnosisTemplate(this.database);
            DiagnosisEditDialog dialog = new DiagnosisEditDialog(this, template);

            dialog.setOnSubmitListener((DiagnosisTemplate edited) ->
            {
                edited.insert();
                this.refresh();
            });

            dialog.show();
        }
    }

    @Override
    public void refresh()
    {
        super.refresh();

        /* Refresh templates */
        LayoutInflater inflater = this.getLayoutInflater();
        SelectValues values = new SelectValues();

        for (DiagnosisTemplate template : this.database.getDiagnosisTemplateTable().selectAll(values))
        {
            DiagnosisEntry entry = new DiagnosisEntry(this, template, inflater);
            this.add(entry.getView());
        }
    }
}
