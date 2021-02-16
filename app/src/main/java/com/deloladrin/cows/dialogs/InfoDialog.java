package com.deloladrin.cows.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deloladrin.cows.R;

public class InfoDialog extends Dialog implements View.OnClickListener
{
    private TextView text;
    private Button ok;

    public InfoDialog(Context context)
    {
        super(context);
        this.setContentView(R.layout.dialog_info);

        /* Load all children */
        this.text = this.findViewById(R.id.dialog_text);
        this.ok = this.findViewById(R.id.dialog_ok);

        /* Add events */
        this.ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
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

    public void setText(int text)
    {
        this.setText(this.getContext().getResources().getString(text));
    }

    public void setText(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setText(String.format(format, args));
    }

    public Button getOKButton()
    {
        return this.ok;
    }
}
