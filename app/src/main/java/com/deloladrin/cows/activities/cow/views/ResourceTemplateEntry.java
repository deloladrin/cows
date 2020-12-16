package com.deloladrin.cows.activities.cow.views;

import android.content.Context;
import android.graphics.Canvas;

import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.views.ToggleImageView;

public class ResourceTemplateEntry extends ToggleImageView
{
    private ResourceTemplate template;
    private ResourceTemplateState state;

    private int toggledColor;
    private int copyColor;

    public ResourceTemplateEntry(Context context)
    {
        super(context);

        this.state = ResourceTemplateState.OFF;
        this.copyColor = 0xFFFFFFFF;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        /* Override toggled color */
        switch (this.state)
        {
            case OFF:
            case ON:
                super.setToggledColor(this.toggledColor);
                break;

            case COPY:
                super.setToggledColor(this.copyColor);
                break;
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean performClick()
    {
        boolean result = super.performClick();

        /* Override behaviour */
        switch (this.state)
        {
            case OFF:
                this.setState(ResourceTemplateState.ON);
                break;

            case ON:

                /* Switch to copy */
                if (this.template.isCopying())
                {
                    this.setState(ResourceTemplateState.COPY);
                }
                else
                {
                    this.setState(ResourceTemplateState.OFF);
                }

                break;

            case COPY:
                this.setState(ResourceTemplateState.OFF);
                break;
        }

        this.invalidate();
        return result;
    }

    public ResourceTemplate getTemplate()
    {
        return this.template;
    }

    public void setTemplate(ResourceTemplate template)
    {
        this.template = template;

        if (template != null)
        {
            this.setImageBitmap(template.getImage().getBitmap());
        }
        else
        {
            this.setImageBitmap(null);
        }
    }

    public ResourceTemplateState getState()
    {
        return this.state;
    }

    public void setState(ResourceTemplateState state)
    {
        this.state = state;

        switch (state)
        {
            case OFF:
                super.setToggled(false);
                break;

            case COPY:
            case ON:
                super.setToggled(true);
                break;
        }
    }

    @Override
    public int getToggledColor()
    {
        return this.toggledColor;
    }

    @Override
    public void setToggledColor(int toggledColor)
    {
        this.toggledColor = toggledColor;
    }

    @Override
    public void setToggledColorResource(int resource)
    {
        this.setToggledColor(this.getContext().getColor(resource));
    }

    public int getCopyColor()
    {
        return this.copyColor;
    }

    public void setCopyColor(int copyColor)
    {
        this.copyColor = copyColor;
        this.invalidate();
    }

    public void setCopyColorResource(int resource)
    {
        this.setCopyColor(this.getContext().getColor(resource));
    }
}
