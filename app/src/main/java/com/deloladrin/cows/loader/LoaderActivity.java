package com.deloladrin.cows.loader;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.deloladrin.cows.BuildConfig;
import com.deloladrin.cows.Preferences;
import com.deloladrin.cows.storage.Database;
import com.deloladrin.cows.storage.DatabaseApplication;

public abstract class LoaderActivity extends Activity implements ParentView
{
    private static final String TAG = LoaderActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);

        Log.d(TAG, "Loading all children ...");
        LoaderUtils.reload(this);
    }

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);

        Log.d(TAG, "Loading all children ...");
        LoaderUtils.reload(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params)
    {
        super.setContentView(view, params);

        Log.d(TAG, "Loading all children ...");
        LoaderUtils.reload(this);
    }

    public void onUpdate()
    {
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.onUpdate();
    }

    @Override
    protected void onStop()
    {
        Activity parent = this.getParent();
        LoaderUtils.update(parent);

        super.onStop();
    }

    @Override
    public Context getContext()
    {
        return this;
    }

    public Database getDatabase()
    {
        return LoaderUtils.getDatabase((Context) this);
    }

    public Preferences getPreferences()
    {
        return LoaderUtils.getPreferences((Context) this);
    }
}
