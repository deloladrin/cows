package com.deloladrin.cows.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deloladrin.cows.R;

public class YesNoDialog extends Dialog implements View.OnClickListener
{
    private TextView text;

    private Button no;
    private Button yes;

    private OnYesListener onYesListener;
    private boolean dismissCanceled;

    public YesNoDialog(Context context)
    {
        super(context);
        this.setContentView(R.layout.dialog_yes_no);

        /* Load all children */
        this.text = this.findViewById(R.id.dialog_text);

        this.no = this.findViewById(R.id.dialog_no);
        this.yes = this.findViewById(R.id.dialog_yes);

        /* Add events */
        this.no.setOnClickListener(this);
        this.yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        this.dismissCanceled = false;

        /* On yes click */
        if (view.equals(this.yes))
        {
            this.onYesListener.onYesClick(this);
        }

        if (!this.dismissCanceled)
        {
            this.dismiss();
        }
    }

    public void cancelDismiss()
    {
        this.dismissCanceled = true;
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

    public Button getNoButton()
    {
        return this.no;
    }

    public String getNoText()
    {
        return this.no.getText().toString();
    }

    public void setNoText(String text)
    {
        this.no.setText(text);
    }

    public void setNoText(int text)
    {
        this.setNoText(this.getContext().getResources().getString(text));
    }

    public Button getYesButton()
    {
        return this.yes;
    }

    public String getYesText()
    {
        return this.yes.getText().toString();
    }

    public void setYesText(String text)
    {
        this.yes.setText(text);
    }

    public void setYesText(int text)
    {
        this.setYesText(this.getContext().getResources().getString(text));
    }

    public OnYesListener getOnYesListener()
    {
        return this.onYesListener;
    }

    public void setOnYesListener(OnYesListener listener)
    {
        this.onYesListener = listener;
    }

    public interface OnYesListener
    {
        void onYesClick(YesNoDialog dialog);
    }
}
