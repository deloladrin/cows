package com.deloladrin.cows.activities.template;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.database.DatabaseActivity;

public abstract class TemplateActivity extends DatabaseActivity implements View.OnClickListener
{
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

    public void refresh()
    {
        this.clear();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        /* Refresh data */
        this.refresh();
    }

    public void add(View view)
    {
        this.container.addView(view);
    }

    public void clear()
    {
        this.container.removeAllViews();
    }
}
