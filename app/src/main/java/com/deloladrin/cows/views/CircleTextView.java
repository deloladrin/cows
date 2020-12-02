package com.deloladrin.cows.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.deloladrin.cows.R;

public class CircleTextView extends AppCompatTextView
{
    private Paint paint;
    private int strokeColor;
    private float strokeWidth;

    public CircleTextView(Context context)
    {
        super(context);

        this.strokeWidth = 1.0f;
        this.paint = new Paint();
    }

    public CircleTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        /* Get attributes */
        TypedArray attrArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleTextView, 0, 0);

        try
        {
            this.strokeWidth = attrArray.getFloat(R.styleable.CircleTextView_strokeWidth, 1.0f);
            this.strokeColor = attrArray.getColor(R.styleable.CircleTextView_strokeColor, 0xFFFFFFFF);
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

        int x = this.getWidth() / 2;
        int y = this.getHeight() / 2;
        int size = (int)(Math.min(this.getWidth(), this.getHeight()) / 2.0f - this.strokeWidth);

        /* Setup paint */
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(this.strokeWidth);
        paint.setColor(this.strokeColor);

        canvas.drawCircle(x, y, size, paint);
    }

    public int getStrokeColor()
    {
        return this.strokeColor;
    }

    public void setStrokeColor(int strokeColor)
    {
        this.strokeColor = strokeColor;
        this.invalidate();
    }

    public float getStrokeWidth()
    {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth)
    {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }
}
