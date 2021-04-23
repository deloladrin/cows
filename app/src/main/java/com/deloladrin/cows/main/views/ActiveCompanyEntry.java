package com.deloladrin.cows.main.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.loader.LoaderLayout;

public class ActiveCompanyEntry extends LoaderLayout
{
    private Company company;

    private ImageView icon;
    private TextView name;

    public ActiveCompanyEntry(Context context)
    {
        this(context, null);
    }

    public ActiveCompanyEntry(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ActiveCompanyEntry(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.setContentView(R.layout.main_company_entry);
    }

    public Company getCompany()
    {
        return this.company;
    }

    public void setCompany(Company company)
    {
        this.company = company;

        if (this.company != null)
        {
            this.icon.setImageBitmap(this.company.getIcon());
            this.name.setText(this.company.getName());
        }
        else
        {
            this.icon.setImageDrawable(null);
            this.name.setText(R.string.global_null);
        }
    }

    public ImageView getIconView()
    {
        return this.icon;
    }

    public TextView getNameView()
    {
        return this.name;
    }
}
