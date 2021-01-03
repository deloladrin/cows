package com.deloladrin.cows.activities;

import android.app.Dialog;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.DatabaseActivity;

public abstract class ChildDialog<T extends DatabaseActivity> extends Dialog
{
    protected T activity;
    protected ChildActivity<T> parent;

    public ChildDialog(T activity)
    {
        super(activity);

        this.activity = activity;
        this.parent = null;
    }

    public ChildDialog(ChildActivity<T> parent)
    {
        super(parent.getContext());

        this.activity = parent.getActivity();
        this.parent = parent;
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
}
