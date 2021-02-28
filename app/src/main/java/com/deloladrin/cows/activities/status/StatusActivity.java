package com.deloladrin.cows.activities.status;

import android.os.Bundle;
import android.view.View;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.template.TemplateActivity;

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
    }
}
