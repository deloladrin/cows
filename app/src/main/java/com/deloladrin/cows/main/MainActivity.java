package com.deloladrin.cows.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.deloladrin.cows.R;
import com.deloladrin.cows.loader.LoaderActivity;
import com.deloladrin.cows.main.views.ActiveCompanyView;
import com.deloladrin.cows.main.views.ActivityButton;

public class MainActivity extends LoaderActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActiveCompanyView active;

    private ActivityButton company;
    private ActivityButton cow;
    private ActivityButton record;
    private ActivityButton diagnosis;
    private ActivityButton resource;
    private ActivityButton status;
    private ActivityButton export;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_activity);
    }

    public void onCompanyClick(View view)
    {
        Log.i(TAG, "Requesting Company activity ...");
    }

    public void onCowClick(View view)
    {
        Log.i(TAG, "Requesting Cow activity ...");
    }

    public void onRecordClick(View view)
    {
        Log.i(TAG, "Requesting Record activity ...");
    }

    public void onDiagnosisClick(View view)
    {
        Log.i(TAG, "Requesting Diagnosis activity ...");
    }

    public void onResourceClick(View view)
    {
        Log.i(TAG, "Requesting Resource activity ...");
    }

    public void onStatusClick(View view)
    {
        Log.i(TAG, "Requesting Status activity ...");
    }

    public void onExportClick(View view)
    {
        Log.i(TAG, "Requesting Export activity ...");
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.getPreferences().getActiveCompany() != null)
        {
            Log.i(TAG, "Company selected! Unlocking buttons ...");


            this.cow.setEnabled(true);
            this.record.setEnabled(true);
            this.export.setEnabled(true);
        }
        else
        {
            Log.i(TAG, "No company is selected! Locking buttons ...");

            this.cow.setEnabled(false);
            this.record.setEnabled(false);
            this.export.setEnabled(false);
        }
    }

    public ActiveCompanyView getActiveCompanyView()
    {
        return this.active;
    }

    public ActivityButton getCompanyButton()
    {
        return this.company;
    }

    public ActivityButton getCowButton()
    {
        return this.cow;
    }

    public ActivityButton getRecordButton()
    {
        return this.record;
    }

    public ActivityButton getDiagnosisButton()
    {
        return this.diagnosis;
    }

    public ActivityButton getResourceButton()
    {
        return this.resource;
    }

    public ActivityButton getStatusButton()
    {
        return this.status;
    }

    public ActivityButton getExportButton()
    {
        return this.export;
    }
}
