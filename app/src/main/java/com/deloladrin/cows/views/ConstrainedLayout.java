package com.deloladrin.cows.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.deloladrin.cows.R;
import com.deloladrin.cows.loader.AttributeRegistry;

public class ConstrainedLayout extends LinearLayout
{
    private int maxWidth;
    private int maxHeight;

    public ConstrainedLayout(Context context)
    {
        this(context, null);
    }

    public ConstrainedLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ConstrainedLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        try (AttributeRegistry registry = new AttributeRegistry(context, attrs, R.styleable.ConstrainedLayout))
        {
            this.setMaxWidth(registry.getDimensionPixelSize(R.styleable.ConstrainedLayout_maxWidth));
            this.setMaxHeight(registry.getDimensionPixelSize(R.styleable.ConstrainedLayout_maxHeight));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (this.maxWidth != 0)
        {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(this.maxWidth, MeasureSpec.AT_MOST);
        }

        if (this.maxHeight != 0)
        {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.maxHeight, MeasureSpec.AT_MOST);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getMaxWidth()
    {
        return this.maxWidth;
    }

    public void setMaxWidth(int maxWidth)
    {
        this.maxWidth = maxWidth;
        this.invalidate();
    }

    public int getMaxHeight()
    {
        return this.maxHeight;
    }

    public void setMaxHeight(int maxHeight)
    {
        this.maxHeight = maxHeight;
        this.invalidate();
    }
}
