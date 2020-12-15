package com.deloladrin.cows.activities.cow.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deloladrin.cows.data.HoofMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.data.TargetMask;

import java.util.Arrays;

public class HoofResourceContainer extends FrameLayout
{
    private HoofMask mask;

    public HoofResourceContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void add(Resource resource, TargetMask target)
    {
        /* Create ImageView */
        HoofResourceEntry view = new HoofResourceEntry(this.getContext());
        view.setResource(resource);

        Bitmap bitmap = resource.getImage().getBitmap();
        view.setImageBitmap(bitmap);
        view.setAdjustViewBounds(true);

        /* Set correct location and scale */
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        switch (resource.getType())
        {
            case HOOF:
                break;

            case FINGER:

                if (target == this.mask.getRightFinger())
                {
                    view.setScaleX(-1);
                    params.gravity = Gravity.RIGHT;
                }

                break;

            case FINGER_INVERTED:

                if (target == this.mask.getLeftFinger())
                {
                    view.setScaleX(-1);
                    params.gravity = Gravity.RIGHT;
                }

                break;
        }

        view.setLayoutParams(params);
        this.addView(view);
    }

    public void sort()
    {
        /* Get all children */
        int childCount = this.getChildCount();
        HoofResourceEntry[] children = new HoofResourceEntry[childCount];

        for (int i = 0; i < childCount; i++)
        {
            children[i] = (HoofResourceEntry)this.getChildAt(i);
        }

        /* Sort children */
        Arrays.sort(children, (HoofResourceEntry a, HoofResourceEntry b) ->
        {
            return a.getResource().getLayer() - b.getResource().getLayer();
        });

        /* Put children back */
        this.clear();

        for (HoofResourceEntry view : children)
        {
            this.addView(view);
        }
    }

    public void clear()
    {
        /* Remove all children */
        this.removeAllViews();
    }

    public HoofMask getMask()
    {
        return this.mask;
    }

    public void setMask(HoofMask mask)
    {
        this.mask = mask;
    }
}
