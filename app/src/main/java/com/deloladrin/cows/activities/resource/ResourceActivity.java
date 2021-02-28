package com.deloladrin.cows.activities.resource;

import android.os.Bundle;
import android.view.View;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.template.TemplateActivity;

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
    }
}
