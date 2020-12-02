package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deloladrin.cows.data.Resource;

public class HoofResourceContainer extends LinearLayout
{
    public HoofResourceContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void add(Resource resource)
    {
        /* Create ImageView */
        ImageView view = new ImageView(this.getContext());

        Bitmap bitmap = resource.getImage().getBitmap();
        view.setImageBitmap(bitmap);
        view.setAdjustViewBounds(true);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);

        this.addView(view);
    }

    public void clear()
    {
        /* Remove all children */
        this.removeAllViews();
    }
}
