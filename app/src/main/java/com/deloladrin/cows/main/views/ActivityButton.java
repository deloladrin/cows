package com.deloladrin.cows.main.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.loader.AttributeRegistry;
import com.deloladrin.cows.loader.LoaderLayout;
import com.deloladrin.cows.views.ViewUtils;

public class ActivityButton extends LoaderLayout
{
    private ImageView image;
    private TextView text;

    public ActivityButton(Context context)
    {
        this(context, null);
    }

    public ActivityButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ActivityButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.setContentView(R.layout.main_button_activity);

        try (AttributeRegistry registry = new AttributeRegistry(context, attrs, R.styleable.ActivityButton))
        {
            this.setEnabled(registry.getBoolean(R.styleable.ActivityButton_enabled, true));

            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, registry.getDimension(R.styleable.ActivityButton_textSize));
            this.setTextColor(registry.getColor(R.styleable.ActivityButton_textColor));
            this.setText(registry.getString(R.styleable.ActivityButton_text));

            this.setImageDrawable(registry.getDrawable(R.styleable.ActivityButton_image));
        }

        this.text.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    public ImageView getImageView()
    {
        return this.image;
    }

    public TextView getTextView()
    {
        return this.text;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);

        if (enabled)
        {
            this.image.setColorFilter(null);
        }
        else
        {
            this.image.setColorFilter(ViewUtils.FILTER_GRAYSCALE);
        }
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

    public int getTextColor()
    {
        return this.text.getCurrentTextColor();
    }

    public void setTextColor(int color)
    {
        this.text.setTextColor(color);
    }

    public CharSequence getText()
    {
        return this.text.getText();
    }

    public void setText(int stringID)
    {
        this.text.setText(stringID);
    }

    public void setText(int formatID, Object... args)
    {
        String format = this.getContext().getString(formatID);
        String text = String.format(format, args);
        this.text.setText(text);
    }

    public void setText(String text)
    {
        this.text.setText(text);
    }

    public void setText(String format, Object... args)
    {
        String text = String.format(format, args);
        this.text.setText(text);
    }

    public Drawable getDrawable()
    {
        return this.image.getDrawable();
    }

    public void setImageDrawable(Drawable drawable)
    {
        this.image.setImageDrawable(drawable);
    }

    public void setImageBitmap(Bitmap bitmap)
    {
        this.image.setImageBitmap(bitmap);
    }

    public void setImageResource(int resourceID)
    {
        this.image.setImageResource(resourceID);
    }

    public void setImageURI(Uri uri)
    {
        this.image.setImageURI(uri);
    }
}
