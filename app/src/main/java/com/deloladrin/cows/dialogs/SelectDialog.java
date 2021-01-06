package com.deloladrin.cows.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;

import java.util.List;

public class SelectDialog<T extends View> extends Dialog implements View.OnClickListener
{
    private TextView text;
    private LinearLayout container;

    private Button cancel;

    private OnSelectListener<T> onSelectListener;

    public SelectDialog(Context context)
    {
        super(context);
        this.setContentView(R.layout.dialog_select);

        /* Load all children */
        this.text = this.findViewById(R.id.dialog_text);
        this.container = this.findViewById(R.id.dialog_container);

        this.cancel = this.findViewById(R.id.dialog_cancel);

        /* Add events */
        this.cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        /* Get selected item */
        for (int i = 0; i < this.container.getChildCount(); i++)
        {
            if (view.equals(this.container.getChildAt(i)))
            {
                this.onSelectListener.onSelect((T)view);
            }
        }

        this.dismiss();
    }

    public void add(T entry)
    {
        /* Make entry clickable and add */
        entry.setClickable(true);
        entry.setOnClickListener(this);

        this.container.addView(entry);
    }

    public void clear()
    {
        /* Delete all children */
        this.container.removeAllViews();
    }

    public String getText()
    {
        return this.text.getText().toString();
    }

    public void setText(String text)
    {
        this.text.setText(text);
    }

    public void setText(int text)
    {
        this.setText(this.getContext().getResources().getString(text));
    }

    public void setText(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setText(String.format(format, args));
    }

    public LinearLayout getContainer()
    {
        return this.container;
    }

    public Button getCancelButton()
    {
        return this.cancel;
    }

    public String getCancelText()
    {
        return this.cancel.getText().toString();
    }

    public void setCancelText(String text)
    {
        this.cancel.setText(text);
    }

    public void setCancelText(int text)
    {
        this.setCancelText(this.getContext().getResources().getString(text));
    }

    public OnSelectListener<T> getOnSelectListener()
    {
        return this.onSelectListener;
    }

    public void setOnSelectListener(OnSelectListener<T> onSelectListener)
    {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener<T extends View>
    {
        void onSelect(T view);
    }
}
