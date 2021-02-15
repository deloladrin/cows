package com.deloladrin.cows.activities.company.cow;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.deloladrin.cows.R;

public class CompanyCowGroupEntry implements View.OnClickListener
{
    private CompanyCowActivity parent;
    private String group;

    private View view;
    private TextView name;

    public CompanyCowGroupEntry(CompanyCowActivity parent, String group, LayoutInflater inflater)
    {
        this.parent = parent;
        this.group = group;

        this.view = inflater.inflate(R.layout.entry_company_cow_group, null);

        /* Load all children */
        this.name = this.view.findViewById(R.id.entry_name);
        String name = this.parent.getString(R.string.dialog_company_cow_group);
        this.name.setText(String.format(name, group));
    }

    @Override
    public void onClick(View view)
    {
    }

    public CompanyCowActivity getParent()
    {
        return this.parent;
    }

    public String getGroup()
    {
        return this.group;
    }

    public View getView()
    {
        return this.view;
    }
}
