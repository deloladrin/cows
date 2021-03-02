package com.deloladrin.cows.activities.resource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.diagnosis.dialogs.DiagnosisEditDialog;
import com.deloladrin.cows.activities.resource.dialogs.ResourceEditDialog;
import com.deloladrin.cows.activities.template.TemplateActivity;
import com.deloladrin.cows.data.DiagnosisTemplate;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.database.SelectValues;

public class ResourceActivity extends TemplateActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Update header */
        this.image.setImageResource(R.drawable.icon_resource);
        this.name.setText(R.string.activity_resource);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.add))
        {
            /* Add new template */
            ResourceTemplate template = new ResourceTemplate(this.database);
            ResourceEditDialog dialog = new ResourceEditDialog(this, template);

            dialog.setOnSubmitListener((ResourceTemplate edited) ->
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

        for (ResourceTemplate template : this.database.getResourceTemplateTable().selectAll(values))
        {
            ResourceEntry entry = new ResourceEntry(this, template, inflater);
            this.add(entry.getView());
        }
    }
}
