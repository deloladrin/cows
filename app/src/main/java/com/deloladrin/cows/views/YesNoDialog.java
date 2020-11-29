package com.deloladrin.cows.views;

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

    public YesNoDialog(Context context)
    {
        super(context);
        this.setContentView(R.layout.dialog_yes_no);

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
        /* On yes click */
        if (view == this.yes)
        {
            this.onYesListener.onYesClick(this);
        }

        this.dismiss();
    }

    public String getText()
    {
        return this.text.getText().toString();
    }

    public void setText(String text)
    {
        this.text.setText(text);
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
