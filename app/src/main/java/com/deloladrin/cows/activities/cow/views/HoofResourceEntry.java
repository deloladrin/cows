package com.deloladrin.cows.activities.cow.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.deloladrin.cows.data.Resource;

public class HoofResourceEntry extends AppCompatImageView
{
    private Resource resource;

    public HoofResourceEntry(Context context)
    {
        super(context);
    }

    public HoofResourceEntry(Context context, AttributeSet attrs)
    {
        super(context, attrs);
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
