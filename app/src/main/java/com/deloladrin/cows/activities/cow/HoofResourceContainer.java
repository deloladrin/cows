package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceType;

public class HoofResourceContainer extends FrameLayout
{
    public HoofResourceContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void add(Resource resource, int target)
    {
        /* Create ImageView */
        ImageView view = new ImageView(this.getContext());

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

                if (target == CowTreatmentHoof.MASK_RIGHT)
                {
                    view.setScaleX(-1);
                    params.gravity = Gravity.RIGHT;
                }

                break;

            case FINGER_INVERTED:

                if (target == CowTreatmentHoof.MASK_LEFT)
                {
                    view.setScaleX(-1);
                    params.gravity = Gravity.RIGHT;
                }

                break;
        }

        view.setLayoutParams(params);
        this.addView(view);
    }

    public void clear()
    {
        /* Remove all children */
        this.removeAllViews();
    }
}
