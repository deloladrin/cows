package com.deloladrin.cows.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.company.CompanyActivity;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.activities.diagnosis.DiagnosisActivity;
import com.deloladrin.cows.activities.export.ExportActivity;
import com.deloladrin.cows.activities.export.TreatmentWorkbook;
import com.deloladrin.cows.activities.main.dialogs.CowDialog;
import com.deloladrin.cows.activities.main.views.CompanyEntry;
import com.deloladrin.cows.activities.record.RecordActivity;
import com.deloladrin.cows.activities.resource.ResourceActivity;
import com.deloladrin.cows.activities.status.StatusActivity;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.DiagnosisTemplate;
import com.deloladrin.cows.data.DiagnosisType;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.database.DatabaseActivity;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.dialogs.SelectDialog;
import com.deloladrin.cows.views.ImageTextButton;

import java.time.LocalDate;

public class MainActivity extends DatabaseActivity implements View.OnClickListener
{
    private Company company;

    private LinearLayout currentCompany;
    private ImageView companyImage;
    private TextView companyName;

    private ImageTextButton companies;
    private ImageTextButton cow;
    private ImageTextButton records;
    private ImageTextButton diagnoses;
    private ImageTextButton resources;
    private ImageTextButton statuses;
    private ImageTextButton export;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        /* Fix poi-on-android xml parser */
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        /* Load all children */
        this.currentCompany = this.findViewById(R.id.main_company);
        this.companyImage = this.findViewById(R.id.main_company_image);
        this.companyName = this.findViewById(R.id.main_company_name);

        this.companies = this.findViewById(R.id.main_companies);
        this.cow = this.findViewById(R.id.main_cow);
        this.records = this.findViewById(R.id.main_records);
        this.diagnoses = this.findViewById(R.id.main_diagnoses);
        this.resources = this.findViewById(R.id.main_resources);
        this.statuses = this.findViewById(R.id.main_statuses);
        this.export = this.findViewById(R.id.main_export);

        /* Add events */
        this.currentCompany.setOnClickListener(this);

        this.companies.setOnClickListener(this);
        this.cow.setOnClickListener(this);
        this.records.setOnClickListener(this);
        this.diagnoses.setOnClickListener(this);
        this.resources.setOnClickListener(this);
        this.statuses.setOnClickListener(this);
        this.export.setOnClickListener(this);

        /* Load active company */
        this.refresh();
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.currentCompany))
        {
            /* Show company select dialog */
            SelectDialog<CompanyEntry> dialog = new SelectDialog<>(this);
            dialog.setText(R.string.dialog_company);

            Company active = this.preferences.getActiveCompany();

            for (Company company : Company.selectAll(this.database))
            {
                CompanyEntry entry = new CompanyEntry(this, company);
                dialog.add(entry, company.equals(active));
            }

            dialog.setOnSelectListener((CompanyEntry entry) ->
            {
                /* Make company current and refresh */
                this.preferences.setActiveCompany(entry.getValue());
                this.refresh();
            });

            dialog.show();
            return;
        }

        if (view.equals(this.companies))
        {
            /* Open company activity */
            Intent intent = new Intent(this, CompanyActivity.class);
            this.startActivity(intent);

            return;
        }

        if (view.equals(this.cow))
        {
            /* Show cow select dialog */
            CowDialog dialog = new CowDialog(this, this.company);

            dialog.setOnSubmitListener((Cow cow) ->
            {
                /* Open cow activity */
                Intent intent = new Intent(this, CowActivity.class);
                intent.putExtra(CowActivity.EXTRA_COW_ID, cow.getID());

                this.startActivity(intent);
            });

            dialog.show();
            return;
        }

        if (view.equals(this.records))
        {
            /* Open record activity */
            Intent intent = new Intent(this, RecordActivity.class);
            this.startActivity(intent);

            return;
        }

        if (view.equals(this.diagnoses))
        {
            /* Open diagonsis activity */
            Intent intent = new Intent(this, DiagnosisActivity.class);
            this.startActivity(intent);

            return;
        }

        if (view.equals(this.resources))
        {
            /* Open resource activity */
            Intent intent = new Intent(this, ResourceActivity.class);
            this.startActivity(intent);

            return;
        }

        if (view.equals(this.statuses))
        {
            /* Open status activity */
            Intent intent = new Intent(this, StatusActivity.class);
            this.startActivity(intent);

            return;
        }

        if (view.equals(this.export))
        {
            /* Open export activity */
            Intent intent = new Intent(this, ExportActivity.class);
            this.startActivity(intent);

            return;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        /* Refresh company data */
        this.refresh();
    }

    public void refresh()
    {
        /* Update current active company */
        Company active = this.preferences.getActiveCompany();
        this.setCompany(active);
    }

    public Company getCompany()
    {
        return this.company;
    }

    public void setCompany(Company company)
    {
        this.company = company;

        if (company != null)
        {
            this.companyName.setText(company.getName());

            /* Company image */
            DatabaseBitmap image = company.getImage();

            if (image != null)
            {
                this.companyImage.setVisibility(View.VISIBLE);
                this.companyImage.setImageBitmap(image.getBitmap());
            }
            else
            {
                this.companyImage.setVisibility(View.GONE);
            }

            /* Enable company based buttons */
            this.cow.setEnabled(true);
            this.records.setEnabled(true);
            this.export.setEnabled(true);
        }
        else
        {
            this.companyImage.setVisibility(View.GONE);
            this.companyName.setText("—");

            /* Disable company based buttons */
            this.cow.setEnabled(false);
            this.records.setEnabled(false);
            this.export.setEnabled(false);
        }
    }
}
