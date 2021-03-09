package com.deloladrin.cows.activities.status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.resource.ResourceEntry;
import com.deloladrin.cows.activities.status.dialogs.StatusEditDialog;
import com.deloladrin.cows.activities.template.TemplateActivity;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.database.SelectValues;

public class StatusActivity extends TemplateActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Update header */
        this.image.setImageResource(R.drawable.icon_status);
        this.name.setText(R.string.activity_status);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.add))
        {
            /* Add new template */
            StatusTemplate template = new StatusTemplate(this.database);
            StatusEditDialog dialog = new StatusEditDialog(this, template);

            dialog.setOnSubmitListener((StatusTemplate edited) ->
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

        for (StatusTemplate template : this.database.getStatusTemplateTable().selectAll(values))
        {
            StatusEntry entry = new StatusEntry(this, template, inflater);
            this.add(entry.getView());
        }
    }
}
