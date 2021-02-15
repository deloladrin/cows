package com.deloladrin.cows.activities.company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.company.dialogs.CompanyEditDialog;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.dialogs.YesNoDialog;

import java.util.List;

public class CompanyEntry implements View.OnClickListener
{
    private CompanyActivity parent;
    private Company company;

    private View view;

    private ImageView image;
    private TextView name;
    private TextView count;

    private ImageButton edit;
    private ImageButton delete;

    public CompanyEntry(CompanyActivity parent, Company company, LayoutInflater inflater)
    {
        this.parent = parent;
        this.company = company;

        this.view = inflater.inflate(R.layout.entry_company, null);

        /* Load all children */
        this.image = this.view.findViewById(R.id.entry_image);
        this.name = this.view.findViewById(R.id.entry_name);
        this.count = this.view.findViewById(R.id.entry_count);

        this.edit = this.view.findViewById(R.id.entry_edit);
        this.delete = this.view.findViewById(R.id.entry_delete);

        /* Add events */
        this.view.setOnClickListener(this);
        this.edit.setOnClickListener(this);
        this.delete.setOnClickListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        /* Company image */
        DatabaseBitmap image = this.company.getImage();

        if (image != null)
        {
            this.image.setImageBitmap(image.getBitmap());
        }

        /* Other parameters */
        String name = this.company.getName();
        this.name.setText(name);

        List<Cow> cows = this.company.getCows();
        String count = this.parent.getString(R.string.entry_company_count);
        this.count.setText(String.format(count, cows.size()));
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.edit))
        {
            /* Show edit dialog */
            CompanyEditDialog dialog = new CompanyEditDialog(this.parent, this.company);

            dialog.setOnSubmitListener((Company c) ->
            {
                /* Update and refresh */
                this.company.update();
                this.parent.refresh();
            });

            dialog.show();
            return;
        }

        if (view.equals(this.delete))
        {
            /* Request to delete company */
            YesNoDialog dialog = new YesNoDialog(this.parent);
            dialog.setText(R.string.dialog_company_delete, this.company.getName());

            dialog.setOnYesListener((YesNoDialog d) ->
            {
                /* Delete and refresh */
                this.company.delete();
                this.parent.refresh();
            });

            dialog.show();
            return;
        }

        if (view.equals(this.view))
        {
            this.parent.onShow(this.company);
        }
    }

    public CompanyActivity getParent()
    {
        return this.parent;
    }

    public Company getCompany()
    {
        return this.company;
    }

    public View getView()
    {
        return this.view;
    }
}
