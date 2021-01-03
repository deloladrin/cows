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

import java.util.List;

public abstract class TemplateActivity<T extends DatabaseEntry> extends DatabaseActivity implements View.OnClickListener
{
    protected List<T> values;

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
    }

    public List<T> getValues()
    {
        return this.values;
    }

    public void setValues(List<T> values)
    {
        this.values = values;
    }
}
