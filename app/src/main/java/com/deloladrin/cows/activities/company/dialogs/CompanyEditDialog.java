package com.deloladrin.cows.activities.company.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.company.CompanyActivity;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.views.ImageSelect;

public class CompanyEditDialog extends ChildDialog<CompanyActivity> implements View.OnClickListener
{
    private Company company;

    private EditText name;
    private EditText group;
    private ImageSelect image;

    private Button cancel;
    private Button submit;

    private OnSubmitListener onSubmitListener;

    public CompanyEditDialog(CompanyActivity parent, Company company)
    {
        super(parent);
        this.setContentView(R.layout.dialog_template_edit_company);

        this.company = company;

        /* Load all children */
        this.name = this.findViewById(R.id.dialog_name);
        this.group = this.findViewById(R.id.dialog_group);
        this.image = new ImageSelect(this.activity, this.findViewById(R.id.dialog_image), 256, 256);

        this.cancel = this.findViewById(R.id.dialog_cancel);
        this.submit = this.findViewById(R.id.dialog_submit);

        /* Add events */
        this.cancel.setOnClickListener(this);
        this.submit.setOnClickListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Context context = this.getContext();

        /* Load current company data */
        this.name.setText(this.company.getName());
        this.group.setText(this.company.getGroup());
        this.image.setDatabaseBitmap(this.company.getImage());
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.submit))
        {
            /* Update and submit */
            String name = this.name.getText().toString();
            this.company.setName(name);

            String group = this.group.getText().toString();

            if (!group.isEmpty())
            {
                this.company.setGroup(group);
            }
            else
            {
                this.company.setGroup(null);
            }

            DatabaseBitmap image = this.image.getDatabaseBitmap();
            this.company.setImage(image);

            this.onSubmitListener.onSubmit(this.company);
        }

        this.dismiss();
    }

    public Company getCompany()
    {
        return this.company;
    }

    public OnSubmitListener getOnSubmitListener()
    {
        return this.onSubmitListener;
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener)
    {
        this.onSubmitListener = onSubmitListener;
    }

    public interface OnSubmitListener
    {
        void onSubmit(Company company);
    }
}
