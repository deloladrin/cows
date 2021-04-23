package com.deloladrin.cows.loader;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.deloladrin.cows.Preferences;
import com.deloladrin.cows.storage.Database;

public abstract class LoaderLayout extends LinearLayout implements ParentView
{
    private static final String TAG = LoaderLayout.class.getSimpleName();

    public LoaderLayout(Context context)
    {
        this(context, null);
    }

    public LoaderLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public LoaderLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setContentView(int layoutResID)
    {
        Context context = this.getContext();
        View.inflate(context, layoutResID, this);

        Log.d(TAG, "Loading all children ...");
        LoaderUtils.reload(this);
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
