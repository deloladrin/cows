package com.deloladrin.cows.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.deloladrin.cows.R;

public class LimitedScrollView extends ScrollView
{
    private float maxHeight;

    public LimitedScrollView(Context context)
    {
        super(context);

        this.maxHeight = -1;
    }

    public LimitedScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        /* Get attributes */
        TypedArray attrArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LimitedScrollView, 0, 0);

        try
        {
            this.maxHeight = attrArray.getDimension(R.styleable.LimitedScrollView_maxHeight, -1);
        }
        finally
        {
            attrArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        /* Fit height into limit */
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (this.maxHeight != -1 && height > this.maxHeight)
        {
            height = (int)this.maxHeight;
        }

        /* Convert height spec back */
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        this.getLayoutParams().height = height;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public float getMaxHeight()
    {
        return this.maxHeight;
    }

    public void setMaxHeight(float maxHeight)
    {
        this.maxHeight = maxHeight;
    }
}
