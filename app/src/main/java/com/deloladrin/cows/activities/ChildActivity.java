package com.deloladrin.cows.activities;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.DatabaseActivity;

public abstract class ChildActivity<T extends DatabaseActivity>
{
    protected T activity;
    protected ChildActivity<T> parent;
    protected LinearLayout layout;

    public ChildActivity(T activity, int layout)
    {
        this.activity = activity;
        this.parent = null;
        this.layout = activity.findViewById(layout);
    }

    public ChildActivity(ChildActivity<T> parent, int layout)
    {
        this.activity = parent.getActivity();
        this.parent = parent;
        this.layout = parent.findViewById(layout);
    }
    
    public <T extends View> T findViewById(int id)
    {
        return this.layout.findViewById(id);
    }

    public Context getContext()
    {
        return this.layout.getContext();
    }

    public T getActivity()
    {
        return this.activity;
    }

    public Database getDatabase()
    {
        return this.activity.getDatabase();
    }

    public ChildActivity<T> getParent()
    {
        return this.parent;
    }

    public LinearLayout getLayout()
    {
        return this.layout;
    }
}
