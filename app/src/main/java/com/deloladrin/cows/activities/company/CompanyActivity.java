package com.deloladrin.cows.activities.company;

import android.content.Intent;
import android.os.Bundle;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.company.dialogs.CompanyEditDialog;
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
        this.name.setText(R.string.activity_company);

        /* Load default values */
        this.refresh();
    }

    @Override
    protected TemplateEntry<Company> createEntry(Company value)
    {
        TemplateEntry<Company> entry = super.createEntry(value);

        /* Set visibilities */
        entry.setShowVisible(true);
        entry.setEditVisible(true);
        entry.setDeleteVisible(true);

        /* Load values */
        entry.setName(value.getName());

        return entry;
    }

    @Override
    public void onAddClick()
    {
        /* Show edit dialog for new company */
        Company company = new Company(this.database);
        CompanyEditDialog dialog = new CompanyEditDialog(this, company);

        dialog.setOnSubmitListener((Company edited) ->
        {
            edited.insert();
            this.refresh();
        });

        dialog.show();
    }

    @Override
    public void onShowClick(TemplateEntry<Company> entry)
    {
        /* Open Company's Cow activity */
        Intent intent = new Intent(this, CompanyCowActivity.class);
        intent.putExtra(CompanyCowActivity.EXTRA_COMPANY, entry.getValue().getID());

        this.startActivity(intent);
    }

    @Override
    public void onEditClick(TemplateEntry<Company> entry)
    {
        /* Show edit dialog */
        CompanyEditDialog dialog = new CompanyEditDialog(this, entry.getValue());

        dialog.setOnSubmitListener((company) ->
        {
            /* Update and refresh */
            company.update();
            this.refresh();
        });

        dialog.show();
    }

    @Override
    public void refresh()
    {
        /* Reload all companies */
        this.setValues(Company.getAll(this.database));
    }
}
