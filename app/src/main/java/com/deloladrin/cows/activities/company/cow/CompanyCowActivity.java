package com.deloladrin.cows.activities.company.cow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.activities.template.TemplateActivity;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.database.SelectOrder;
import com.deloladrin.cows.database.SelectValues;

public class CompanyCowActivity extends TemplateActivity
{
    public static final String EXTRA_COMPANY_ID = "com.deloladrin.cows.activities.company.cow.CompanyCowActivity.EXTRA_COMPANY_ID";

    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Get requested company */
        Intent intent = this.getIntent();

        int companyID = intent.getIntExtra(EXTRA_COMPANY_ID, 0);
        this.company = Company.select(this.database, companyID);

        /* Update header */
        DatabaseBitmap image = this.company.getImage();

        if (image != null)
        {
            this.image.setImageBitmap(image.getBitmap());
        }
        else
        {
            this.image.setImageResource(R.drawable.icon_cow);
        }

        String name = this.company.getName();
        this.name.setText(name);

        /* Don't allow adding cows here */
        this.add.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view)
    {
    }

    public void onShow(Cow cow)
    {
        /* Open cow activity */
        Intent intent = new Intent(this, CowActivity.class);
        intent.putExtra(CowActivity.EXTRA_COW_ID, cow.getID());

        this.startActivity(intent);
    }

    @Override
    public void refresh()
    {
        super.refresh();

        /* Load all cows */
        LayoutInflater inflater = this.getLayoutInflater();
        String group = null;

        SelectValues values = new SelectValues()
                .where(Cow.Table.COLUMN_COMPANY, this.company.getID())
                .orderBy(Cow.Table.COLUMN_GROUP, SelectOrder.ASCENDING)
                .orderBy(Cow.Table.COLUMN_COLLAR, SelectOrder.ASCENDING)
                .orderBy(Cow.Table.COLUMN_ID, SelectOrder.ASCENDING);

        for (Cow cow : this.database.getCowTable().selectAll(values))
        {
            String cowGroup = cow.getGroup();

            if (cowGroup != null)
            {
                if (!cowGroup.equals(group))
                {
                    /* Company has changed */
                    CompanyCowGroupEntry groupEntry = new CompanyCowGroupEntry(this, cowGroup, inflater);
                    group = cowGroup;

                    this.add(groupEntry.getView());
                }
            }

            /* Add cow */
            CompanyCowEntry entry = new CompanyCowEntry(this, cow, inflater);
            this.add(entry.getView());
        }
    }
}
