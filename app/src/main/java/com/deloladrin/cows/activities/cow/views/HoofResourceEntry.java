package com.deloladrin.cows.activities.cow.views;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceType;

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

        if (this.resource.getType() == ResourceType.COPY)
        {
            /* Copy resources are  */
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

            this.setColorFilter(filter);
        }
        else
        {
            /* Reset  */
            this.setImageAlpha(255);
            this.setColorFilter(null);
        }
    }
}
