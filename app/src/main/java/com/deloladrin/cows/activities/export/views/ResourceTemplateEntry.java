package com.deloladrin.cows.activities.export.views;

import android.content.Context;

import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.views.ToggleImageView;

public class ResourceTemplateEntry extends ToggleImageView
{
    private ResourceTemplate template;

    public ResourceTemplateEntry(Context context)
    {
        super(context);
    }

    public ResourceTemplate getTemplate()
    {
        return this.template;
    }

    public void setTemplate(ResourceTemplate template)
    {
        this.template = template;

        /* Update image */
        if (template != null)
        {
            this.setImageBitmap(template.getSmallImage().getBitmap());
        }
        else
        {
            this.setImageBitmap(null);
        }
    }
}
