package com.deloladrin.cows.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;

public class ImageTextButton extends LinearLayout
{
    private ImageView image;
    private TextView text;

    public ImageTextButton(Context context)
    {
        this(context, null);
    }

    public ImageTextButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        inflate(context, R.layout.view_image_text_button, this);

        /* Load all children */
        this.image = this.findViewById(R.id.view_image);
        this.text = this.findViewById(R.id.view_text);

        /* Get attributes */
        TypedArray attrArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTextButton, 0, 0);

        try
        {
            this.setImage(attrArray.getDrawable(R.styleable.ImageTextButton_android_src));
            this.setText(attrArray.getString(R.styleable.ImageTextButton_android_text));
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, attrArray.getDimension(R.styleable.ImageTextButton_android_textSize, 20.0f));
            this.setTextColor(attrArray.getColor(R.styleable.ImageTextButton_android_textColor, 0xFFFFFFFF));
        }
        finally
        {
            attrArray.recycle();
        }
    }

    public String getText()
    {
        return this.text.getText().toString();
    }

    public void setText(String text)
    {
        this.text.setText(text);
    }

    public void setTextColor(int color)
    {
        this.text.setTextColor(color);
    }

    public float getTextSize()
    {
        return this.text.getTextSize();
    }

    public void setTextSize(float size)
    {
        this.text.setTextSize(size);
    }

    public void setTextSize(int unit, float size)
    {
        this.text.setTextSize(unit, size);
    }

    public Drawable getImage()
    {
        return this.image.getDrawable();
    }

    public void setImage(Drawable image)
    {
        this.image.setImageDrawable(image);
    }
}
