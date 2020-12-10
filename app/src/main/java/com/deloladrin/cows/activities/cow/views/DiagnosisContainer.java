package com.deloladrin.cows.activities.cow.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;

public class DiagnosisContainer extends LinearLayout
{
    private int textSize;

    public DiagnosisContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        /* Get attributes */
        TypedArray attrArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DiagnosisContainer, 0, 0);

        try
        {
            this.textSize = attrArray.getDimensionPixelSize(R.styleable.DiagnosisContainer_textSize, 20);
        }
        finally
        {
            attrArray.recycle();
        }

        /* Create empty diagnosis in horizontal views */
        if (this.getOrientation() == HORIZONTAL)
        {
            Diagnosis empty = new Diagnosis(null, -1, null, "", "", "", "", DiagnosisState.NONE, 0, null);
            this.add(empty);
        }
    }

    public void add(Diagnosis diagnosis)
    {
        /* Create with params */
        TextView view = new TextView(this.getContext());
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSize);
        view.setTextColor(diagnosis.getState().getColor(this.getContext()));
        view.setText(diagnosis.getShortName());

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        /* Add margin to horizontal views */
        if (this.getOrientation() == HORIZONTAL)
        {
            int marginPx = this.getResources().getDimensionPixelSize(R.dimen.margin_container);

            params.setMargins(marginPx, 0, marginPx, 0);
            view.setLayoutParams(params);
        }

        this.addView(view);
    }

    public void clear()
    {
        int startIndex = 0;
        int count = this.getChildCount();

        /* Skip first view in horizontal views */
        if (this.getOrientation() == HORIZONTAL)
        {
            startIndex = 1;
        }

        for (int i = count - 1; i >= startIndex; i--)
        {
            this.removeViewAt(i);
        }
    }

    public int getTextSize()
    {
        return this.textSize;
    }

    public void setTextSize(int textSize)
    {
        this.textSize = textSize;
        this.invalidate();
    }
}
