package com.deloladrin.cows.activities.main.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Company;

public class CompanyEntry extends LinearLayout
{
    private Company value;

    private TextView name;

    public CompanyEntry(Context context, Company value)
    {
        super(context);
        inflate(context, R.layout.entry_main_company, this);

        /* Load all children */
        this.name = this.findViewById(R.id.entry_name);

        this.setValue(value);
    }

    public Company getValue()
    {
        return this.value;
    }

    public void setValue(Company value)
    {
        this.value = value;
        this.name.setText(value.getName());
    }
}
