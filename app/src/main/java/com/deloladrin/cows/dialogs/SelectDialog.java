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
    private static final int DEFAULT_BACKGROUND = R.color.button_light;
    private static final int SELECTED_BACKGROUND = R.color.button_tint;

    private TextView text;
    private LinearLayout container;

    private Button cancel;
    private Button submit;

    private int selected;

    private OnSelectListener<T> onSelectListener;

    public SelectDialog(Context context)
    {
        super(context);
        this.setContentView(R.layout.dialog_select);

        /* Load all children */
        this.text = this.findViewById(R.id.dialog_text);
        this.container = this.findViewById(R.id.dialog_container);

        this.cancel = this.findViewById(R.id.dialog_cancel);
        this.submit = this.findViewById(R.id.dialog_submit);

        this.setSelected(-1);

        /* Add events */
        this.cancel.setOnClickListener(this);
        this.submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.submit))
        {
            /* Don't allow selecting noting */
            if (this.selected != -1)
            {
                T child = (T) this.container.getChildAt(this.selected);
                this.onSelectListener.onSelect(child);

                this.dismiss();
            }

            return;
        }

        if (view.equals(this.cancel))
        {
            this.dismiss();
            return;
        }

        /* Select new item */
        int index = this.container.indexOfChild(view);

        if (index != this.selected)
        {
            this.setSelected(index);
        }
        else
        {
            /* Submit on double click */
            this.onSelectListener.onSelect((T)view);
            this.dismiss();
        }
    }

    public void add(T entry, boolean select)
    {
        /* Make entry clickable and add */
        entry.setClickable(true);
        entry.setOnClickListener(this);

        this.container.addView(entry);

        /* Select if requested */
        if (select)
        {
            int index = this.container.getChildCount() - 1;
            this.setSelected(index);
        }
    }

    public void clear()
    {
        /* Delete all children */
        this.container.removeAllViews();
        this.selected = -1;
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

    public int getSelected()
    {
        return this.selected;
    }

    public void setSelected(int selected)
    {
        this.selected = selected;

        for (int i = 0; i < this.container.getChildCount(); i++)
        {
            View child = this.container.getChildAt(i);
            int colorID = DEFAULT_BACKGROUND;

            if (selected == i)
            {
                colorID = SELECTED_BACKGROUND;
            }

            child.setBackgroundResource(colorID);
        }
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
