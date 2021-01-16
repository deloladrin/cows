package com.deloladrin.cows.activities.company;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.company.dialogs.CompanyEditDialog;
import com.deloladrin.cows.activities.template.TemplateActivity;
import com.deloladrin.cows.activities.template.TemplateEntry;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.database.DatabaseBitmap;

import java.util.List;

public class CompanyActivity extends TemplateActivity<Company>
{
    private CompanyEditDialog editDialog;

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
        entry.setImageVisible(true);
        entry.setShortNameVisible(true);
        entry.setEditVisible(true);
        entry.setDeleteVisible(true);

        /* Load values */
        entry.setName(value.getName());

        int cows = value.getCows().size();
        entry.setShortName(Integer.toString(cows));

        DatabaseBitmap image = value.getImage();
        entry.getImageView().setScaleType(ImageView.ScaleType.FIT_CENTER);
        entry.getImageView().setPadding(0, 0, 0, 0);

        if (image != null)
        {
            entry.setImage(image.getBitmap());
        }
        else
        {
            entry.setImage(null);
        }

        return entry;
    }

    @Override
    public void onAddClick()
    {
        /* Show edit dialog for new company */
        Company company = new Company(this.database);
        this.editDialog = new CompanyEditDialog(this, company);

        this.editDialog.setOnSubmitListener((Company edited) ->
        {
            edited.insert();
            this.refresh();
        });

        this.editDialog.show();
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
        this.editDialog = new CompanyEditDialog(this, entry.getValue());

        this.editDialog.setOnSubmitListener((company) ->
        {
            /* Update and refresh */
            company.update();
            this.refresh();
        });

        this.editDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (this.editDialog != null)
        {
            this.editDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void refresh()
    {
        /* Reload all companies */
        this.setValues(Company.getAll(this.database));
    }
}
