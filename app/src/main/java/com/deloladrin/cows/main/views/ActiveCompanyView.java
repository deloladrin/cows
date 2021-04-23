package com.deloladrin.cows.main.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.deloladrin.cows.Preferences;
import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.loader.AttributeRegistry;
import com.deloladrin.cows.loader.LoaderLayout;
import com.deloladrin.cows.main.dialogs.ActiveCompanyDialog;

public class ActiveCompanyView extends LoaderLayout
{
    private static final String TAG = ActiveCompanyView.class.getSimpleName();

    private Company company;

    private ImageView icon;
    private TextView name;

    public ActiveCompanyView(Context context)
    {
        this(context, null);
    }

    public ActiveCompanyView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ActiveCompanyView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.setContentView(R.layout.main_company_active);

        Company active = this.getPreferences().getActiveCompany();
        this.setCompany(active);
    }

    @Override
    public boolean performClick()
    {
        Log.d(TAG, "Showing Company dialog ...");
        ActiveCompanyDialog dialog = new ActiveCompanyDialog(this.getContext());

        dialog.setOnEntrySelectedListener((Company company) ->
        {
            this.getPreferences().setActiveCompany(company);
            this.setCompany(company);
        });

        dialog.show();
        return super.performClick();
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
            this.icon.setVisibility(VISIBLE);
            this.icon.setImageBitmap(this.company.getIcon());
            this.name.setText(this.company.getName());

            Log.d(TAG, "Active company: '" + this.company.getName() + "'!");
        }
        else
        {
            this.icon.setVisibility(GONE);
            this.icon.setImageDrawable(null);
            this.name.setText(R.string.global_null);

            Log.d(TAG, "Active company: NULL!");
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
