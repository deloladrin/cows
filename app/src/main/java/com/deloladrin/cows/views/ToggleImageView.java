package com.deloladrin.cows.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.deloladrin.cows.R;

public class ToggleImageView extends AppCompatImageView
{
    private Paint paint;
    private int toggledColor;
    private float strokeWidth;

    private boolean toggled;

    public ToggleImageView(Context context)
    {
        super(context);
        this.setClickable(true);

        this.paint = new Paint();
        this.toggledColor = 0xFFFFFFFF;
        this.strokeWidth = 1.0f;
    }

    public ToggleImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.setClickable(true);

        /* Get attributes */
        TypedArray attrArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ToggleImageView, 0, 0);

        try
        {
            this.toggledColor = attrArray.getColor(R.styleable.ToggleImageView_toggledColor, 0xFFFFFFFF);
            this.strokeWidth = attrArray.getFloat(R.styleable.ToggleImageView_strokeWidth, 1.0f);
        }
        finally
        {
            attrArray.recycle();
        }

        this.paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (this.toggled)
        {
            /* Create border rect */
            float offset = this.strokeWidth / 2.0f;

            RectF rect = new RectF
            (
                    offset,
                    offset,
                    this.getWidth() - offset,
                    this.getHeight() - offset
            );

            /* Setup paint */
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(this.strokeWidth);
            paint.setColor(this.toggledColor);

            canvas.drawRect(rect, this.paint);
        }
    }

    @Override
    public boolean performClick()
    {
        /* Toggle */
        this.toggled = !this.toggled;
        this.invalidate();

        return super.performClick();
    }

    public int getToggledColor()
    {
        return this.toggledColor;
    }

    public void setToggledColor(int toggledColor)
    {
        this.toggledColor = toggledColor;
        this.invalidate();
    }

    public void setToggledColorResource(int resource)
    {
        this.setToggledColor(this.getContext().getColor(resource));
    }

    public float getStrokeWidth()
    {
        return this.strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth)
    {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }

    public boolean isToggled()
    {
        return this.toggled;
    }

    public void setToggled(boolean toggled)
    {
        this.toggled = toggled;
        this.invalidate();
    }
}
