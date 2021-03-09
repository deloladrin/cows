package com.deloladrin.cows.activities.status;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.resource.dialogs.ResourceEditDialog;
import com.deloladrin.cows.activities.status.dialogs.StatusEditDialog;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.dialogs.YesNoDialog;

public class StatusEntry implements View.OnClickListener
{
    private StatusActivity parent;
    private StatusTemplate template;

    private View view;

    private ImageView image;
    private TextView name;

    private ImageButton edit;
    private ImageButton delete;

    public StatusEntry(StatusActivity parent, StatusTemplate template, LayoutInflater inflater)
    {
        this.parent = parent;
        this.template = template;

        this.view = inflater.inflate(R.layout.entry_status, null);

        /* Load all children */
        this.image = this.view.findViewById(R.id.entry_image);
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
        /* Resource data */
        DatabaseBitmap image = this.template.getImage();
        this.image.setImageBitmap(image.getBitmap());

        String name = this.template.getName();
        this.name.setText(name);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.edit))
        {
            /* Show edit dialog */
            StatusEditDialog dialog = new StatusEditDialog(this.parent, this.template);

            dialog.setOnSubmitListener((StatusTemplate t) ->
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
            dialog.setText(R.string.dialog_status_delete, this.template.getName());

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

    public StatusActivity getParent()
    {
        return this.parent;
    }

    public StatusTemplate getTemplate()
    {
        return this.template;
    }

    public View getView()
    {
        return this.view;
    }
}
