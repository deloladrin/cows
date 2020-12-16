package com.deloladrin.cows.activities.cow.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.deloladrin.cows.data.HoofMask;
import com.deloladrin.cows.data.Resource;

import java.util.Arrays;

public class ResourceContainer extends FrameLayout
{
    private HoofMask mask;

    public ResourceContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void add(Resource resource)
    {
        /* Create ImageView */
        ResourceEntry view = new ResourceEntry(this.getContext());
        view.setResource(resource);

        Bitmap bitmap = resource.getTemplate().getImage().getBitmap();
        view.setImageBitmap(bitmap);
        view.setAdjustViewBounds(true);

        /* Set correct location and scale */
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        switch (resource.getTemplate().getType())
        {
            case HOOF:
                break;

            case FINGER:

                if (resource.getTarget() == this.mask.getRightFinger())
                {
                    view.setScaleX(-1);
                    params.gravity = Gravity.RIGHT;
                }

                break;

            case FINGER_INVERTED:

                if (resource.getTarget() == this.mask.getLeftFinger())
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
        ResourceEntry[] children = new ResourceEntry[childCount];

        for (int i = 0; i < childCount; i++)
        {
            children[i] = (ResourceEntry)this.getChildAt(i);
        }

        /* Sort children */
        Arrays.sort(children, (ResourceEntry a, ResourceEntry b) ->
        {
            return a.getResource().getTemplate().getLayer() - b.getResource().getTemplate().getLayer();
        });

        /* Put children back */
        this.clear();

        for (ResourceEntry view : children)
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
