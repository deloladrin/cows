package com.deloladrin.cows.activities.template;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.database.DatabaseActivity;
import com.deloladrin.cows.database.DatabaseEntry;
import com.deloladrin.cows.dialogs.YesNoDialog;

import java.util.ArrayList;
import java.util.List;

public abstract class TemplateActivity<T extends DatabaseEntry> extends DatabaseActivity implements View.OnClickListener, TemplateEntry.OnActionListener<T>
{
    protected List<T> values;
    protected List<TemplateEntry<T>> views;

    protected ImageView image;
    protected TextView name;

    protected LinearLayout container;
    protected Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_template);

        /* Load all children */
        this.image = this.findViewById(R.id.template_image);
        this.name = this.findViewById(R.id.template_name);

        this.container = this.findViewById(R.id.template_container);
        this.add = this.findViewById(R.id.template_add);

        /* Add events */
        this.add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.add))
        {
            this.onAddClick();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        /* Refresh data */
        this.refresh();
    }

    public void onAddClick()
    {
    }

    @Override
    public void onShowClick(TemplateEntry<T> entry)
    {
    }

    @Override
    public void onEditClick(TemplateEntry<T> entry)
    {
    }

    @Override
    public void onDeleteClick(TemplateEntry<T> entry)
    {
        /* Ask to delete entry */
        YesNoDialog dialog = new YesNoDialog(this);
        dialog.setText(R.string.dialog_entry_delete, entry.getName());

        dialog.setOnYesListener((d) ->
        {
            /* Delete and refresh */
            T value = entry.getValue();
            value.delete();

            this.refresh();
        });

        dialog.show();
    }

    public void refresh()
    {
    }

    protected TemplateEntry<T> createEntry(T value)
    {
        TemplateEntry<T> entry = new TemplateEntry<>(this, value);
        return entry;
    }

    public List<T> getValues()
    {
        return this.values;
    }

    public void setValues(List<T> values)
    {
        this.values = values;

        this.views = new ArrayList<>();
        this.container.removeAllViews();

        /* Add entries */
        for (T value : values)
        {
            TemplateEntry<T> entry = this.createEntry(value);
            entry.setOnActionListener(this);

            this.views.add(entry);
            this.container.addView(entry.getView());
        }
    }

    public void setAddVisible(boolean visible)
    {
        int visibility = visible ? View.VISIBLE : View.GONE;
        this.add.setVisibility(visibility);
    }
}
