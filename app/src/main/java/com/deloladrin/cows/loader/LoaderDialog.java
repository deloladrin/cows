package com.deloladrin.cows.loader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.deloladrin.cows.Preferences;
import com.deloladrin.cows.storage.Database;

public abstract class LoaderDialog extends Dialog implements ParentView
{
    private static final String TAG = LoaderDialog.class.getSimpleName();

    public LoaderDialog(Context context)
    {
        super(context);
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

    @Override
    protected void onStop()
    {
        Context context = this.getContext();
        LoaderUtils.update(context);

        super.onStop();
    }

    public Database getDatabase()
    {
        return LoaderUtils.getDatabase(this);
    }

    public Preferences getPreferences()
    {
        return LoaderUtils.getPreferences(this);
    }
}
