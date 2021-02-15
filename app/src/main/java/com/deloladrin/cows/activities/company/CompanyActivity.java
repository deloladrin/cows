package com.deloladrin.cows.activities.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.company.cow.CompanyCowActivity;
import com.deloladrin.cows.activities.company.dialogs.CompanyEditDialog;
import com.deloladrin.cows.activities.template.TemplateActivity;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.database.SelectOrder;
import com.deloladrin.cows.database.SelectValues;

public class CompanyActivity extends TemplateActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Update header */
        this.image.setImageResource(R.drawable.icon_companies);
        this.name.setText(R.string.activity_company);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.add))
        {
            /* Add new company */
            Company company = new Company(this.database);
            CompanyEditDialog dialog = new CompanyEditDialog(this, company);

            dialog.setOnSubmitListener((Company edited) ->
            {
                edited.insert();
                this.refresh();
            });

            dialog.show();
        }
    }

    public void onShow(Company company)
    {
        /* Open company cow activity */
        Intent intent = new Intent(this, CompanyCowActivity.class);
        intent.putExtra(CompanyCowActivity.EXTRA_COMPANY_ID, company.getID());

        this.startActivity(intent);
    }

    @Override
    public void refresh()
    {
        super.refresh();

        /* Refresh companies */
        LayoutInflater inflater = this.getLayoutInflater();

        SelectValues values = new SelectValues()
                .orderBy(Company.Table.COLUMN_NAME, SelectOrder.ASCENDING);

        for (Company company : this.database.getCompanyTable().selectAll(values))
        {
            CompanyEntry entry = new CompanyEntry(this, company, inflater);
            this.add(entry.getView());
        }
    }
}
