package com.deloladrin.cows.activities;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public abstract class ChildActivity<T extends Container> implements Container
{
    protected T parent;
    protected LinearLayout layout;

    public ChildActivity(T parent, int layoutID)
    {
        this.parent = parent;
        this.layout = parent.findViewById(layoutID);
    }

    public <T extends View> T findViewById(int id)
    {
        return this.layout.findViewById(id);
    }

    public T getParent()
    {
        return this.parent;
    }

    public LinearLayout getLayout()
    {
        return this.layout;
    }

    public Context getContext()
    {
        return this.layout.getContext();
    }
}
