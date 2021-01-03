package com.deloladrin.cows.activities.company;

import android.os.Bundle;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.template.TemplateActivity;
import com.deloladrin.cows.activities.template.TemplateEntry;
import com.deloladrin.cows.data.Company;

import java.util.List;

public class CompanyActivity extends TemplateActivity<Company>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.image.setImageResource(R.drawable.icon_companies);
        this.name.setText(R.string.activity_companies);
    }

    @Override
    protected TemplateEntry<Company> createEntry(Company value)
    {
        TemplateEntry<Company> entry = super.createEntry(value);

        /* Set visibilities */
        entry.setShortNameVisible(false);
        entry.setImageVisible(false);

        /* Load values */
        entry.setName(value.getName());

        return entry;
    }

    @Override
    public void refresh()
    {
        super.refresh();

        /* Reload all companies */
        List<Company> companies = Company.getAll(this.database);

        /* Sort companies by name */
        companies.sort((a, b) -> a.getName().compareTo(b.getName()));
        this.setValues(companies);
    }
}
