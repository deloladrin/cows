package com.deloladrin.cows.activities.cow.views;

import android.content.Context;

import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.views.ToggleImageView;

public class ResourceEntry extends ToggleImageView
{
    private Resource resource;

    public ResourceEntry(Context context)
    {
        super(context);
    }

    public Resource getResource()
    {
        return this.resource;
    }

    public void setResource(Resource resource)
    {
        this.resource = resource;
    }
}
