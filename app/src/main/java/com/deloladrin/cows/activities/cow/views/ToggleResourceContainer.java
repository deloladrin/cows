package com.deloladrin.cows.activities.cow.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.views.CircleImageView;

import java.util.HashMap;
import java.util.Map;

public class ToggleResourceContainer extends LinearLayout implements View.OnClickListener
{
    private HashMap<Resource, CircleImageView> views;

    private float strokeWidth;
    private int strokeColor;
    private int disabledStrokeColor;

    private OnToggleListener onToggleListener;

    public ToggleResourceContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        /* Get attributes */
        TypedArray attrArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ToggleResourceContainer, 0, 0);

        try
        {
            this.strokeWidth = attrArray.getFloat(R.styleable.ToggleResourceContainer_strokeWidth, 1.0f);
            this.strokeColor = attrArray.getColor(R.styleable.ToggleResourceContainer_strokeColor, 0xFFFFFFFF);
            this.disabledStrokeColor = attrArray.getColor(R.styleable.ToggleResourceContainer_disabledStrokeColor, 0xFFFFFFFF);
        }
        finally
        {
            attrArray.recycle();
        }
    }

    public void refresh(Database database)
    {
        this.views = new HashMap<>();
        this.removeAllViews();

        /* Load all resources */
        ValueParams resParams = new ValueParams();
        resParams.put(Resource.Table.COLUMN_TYPE, ResourceType.COW.getID());

        for (Resource resource : database.getResourceTable().selectAll(resParams))
        {
            CircleImageView view = new CircleImageView(this.getContext());
            view.setStrokeWidth(this.strokeWidth);

            view.setClickable(true);
            view.setOnClickListener(this);

            view.setImageBitmap(resource.getImage().getBitmap());
            view.setAdjustViewBounds(true);

            int size = this.getLayoutParams().height;
            LayoutParams params = new LayoutParams(size, size);

            int marginPx = this.getResources().getDimensionPixelSize(R.dimen.margin_container);
            params.setMargins(marginPx, 0, 0, 0);

            view.setLayoutParams(params);

            this.views.put(resource, view);
            this.addView(view);
        }

        /* Turn all off */
        this.reset();
    }

    public void setEnabled(Resource resource, boolean enabled)
    {
        CircleImageView view = this.views.get(resource);

        if (view != null)
        {
            this.setEnabled(view, enabled);
        }
    }

    private void setEnabled(CircleImageView view, boolean enabled)
    {
        if (enabled)
        {
            view.setStrokeColor(this.strokeColor);
            view.setColorFilter(null);
        }
        else
        {
            view.setStrokeColor(this.disabledStrokeColor);
            view.setColorFilter(this.disabledStrokeColor);
        }
    }

    public void reset()
    {
        for (CircleImageView view : this.views.values())
        {
            this.setEnabled(view, false);
        }
    }

    @Override
    public void onClick(View view)
    {
        /* Check, which is clicked */
        for (Map.Entry<Resource, CircleImageView> entry : this.views.entrySet())
        {
            if (view.equals(entry.getValue()))
            {
                this.onToggleListener.onToggle(entry.getKey());
            }
        }
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

    public int getStrokeColor()
    {
        return this.strokeColor;
    }

    public void setStrokeColor(int strokeColor)
    {
        this.strokeColor = strokeColor;
        this.invalidate();
    }

    public int getDisabledStrokeColor()
    {
        return this.disabledStrokeColor;
    }

    public void setDisabledStrokeColor(int disabledStrokeColor)
    {
        this.disabledStrokeColor = disabledStrokeColor;
        this.invalidate();
    }

    public OnToggleListener getOnToggleListener()
    {
        return this.onToggleListener;
    }

    public void setOnToggleListener(OnToggleListener onToggleListener)
    {
        this.onToggleListener = onToggleListener;
    }

    public interface OnToggleListener
    {
        void onToggle(Resource resource);
    }
}
