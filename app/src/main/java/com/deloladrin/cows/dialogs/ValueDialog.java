package com.deloladrin.cows.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.deloladrin.cows.R;

public class ValueDialog extends Dialog implements View.OnClickListener
{
    private ValueDialogType type;
    private TextView text;

    private EditText input;
    private ImageButton copy;

    private Button cancel;
    private Button submit;

    private OnSubmitListener onSubmitListener;
    private OnCopyListener onCopyListener;
    private boolean dismissCanceled;

    private KeyListener defaultKeyListener;
    private KeyListener numberKeyListener;

    public ValueDialog(Context context, ValueDialogType type)
    {
        super(context);
        this.setContentView(R.layout.dialog_value);

        /* Load all children */
        this.text = this.findViewById(R.id.dialog_text);

        this.input = this.findViewById(R.id.dialog_input);
        this.copy = this.findViewById(R.id.dialog_copy);

        this.cancel = this.findViewById(R.id.dialog_cancel);
        this.submit = this.findViewById(R.id.dialog_submit);

        this.defaultKeyListener = this.input.getKeyListener();
        this.numberKeyListener = DigitsKeyListener.getInstance(context.getString(R.string.digits_numbers));

        /* Add events */
        this.copy.setOnClickListener(this);

        this.cancel.setOnClickListener(this);
        this.submit.setOnClickListener(this);

        /* Initialize type */
        this.setType(type);
        this.focusInput();
    }

    @Override
    public void onClick(View view)
    {
        this.dismissCanceled = false;

        if (view.equals(this.submit))
        {
            this.onSubmitListener.onSubmit(this, this.getInput());
        }

        if (view.equals(this.copy))
        {
            this.onCopyListener.onCopy(this);

            this.focusInput();
            return;
        }

        if (!this.dismissCanceled)
        {
            this.dismiss();
        }
    }

    public void focusInput()
    {
        new Handler().postDelayed(() ->
        {
            this.input.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0 ,0));
            this.input.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0 ,0));
            this.input.selectAll();
        }, 100);
    }

    public void cancelDismiss()
    {
        this.dismissCanceled = true;
    }

    public ValueDialogType getType()
    {
        return this.type;
    }

    public void setType(ValueDialogType type)
    {
        this.type = type;

        /* Set correct input parameters */
        switch (type)
        {
            case TEXT:

                this.input.setInputType(InputType.TYPE_CLASS_TEXT);
                this.input.setKeyListener(this.defaultKeyListener);

                break;

            case NUMBER:

                this.input.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.input.setKeyListener(this.numberKeyListener);

                break;
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

    public void setText(int text)
    {
        this.setText(this.getContext().getResources().getString(text));
    }

    public void setText(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setText(String.format(format, args));
    }

    public boolean isCopyEnabled()
    {
        return this.copy.getVisibility() == View.VISIBLE;
    }

    public void setCopyEnabled(boolean enabled)
    {
        int visibility = View.VISIBLE;

        if (!enabled)
        {
            visibility = View.GONE;
        }

        this.copy.setVisibility(visibility);
    }

    public String getInput()
    {
        return this.input.getText().toString();
    }

    public void setInput(String input)
    {
        this.input.setText(input);
    }

    public ImageButton getCopyButton()
    {
        return this.copy;
    }

    public Drawable getCopyDrawable()
    {
        return this.copy.getDrawable();
    }

    public void setCopyDrawable(Drawable drawable)
    {
        this.copy.setImageDrawable(drawable);
    }

    public void setCopyDrawable(int drawable)
    {
        this.setCopyDrawable(ContextCompat.getDrawable(this.getContext(), drawable));
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

    public void setCancelText(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setCancelText(String.format(format, args));
    }

    public Button getSubmitButton()
    {
        return this.submit;
    }

    public String getSubmitText()
    {
        return this.submit.getText().toString();
    }

    public void setSubmitText(String text)
    {
        this.submit.setText(text);
    }

    public void setSubmitText(int text)
    {
        this.setSubmitText(this.getContext().getResources().getString(text));
    }

    public void setSubmitText(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setSubmitText(String.format(format, args));
    }

    public OnSubmitListener getOnSubmitListener()
    {
        return this.onSubmitListener;
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener)
    {
        this.onSubmitListener = onSubmitListener;
    }

    public OnCopyListener getOnCopyListener()
    {
        return this.onCopyListener;
    }

    public void setOnCopyListener(OnCopyListener onCopyListener)
    {
        this.onCopyListener = onCopyListener;
    }

    public interface OnSubmitListener
    {
        void onSubmit(ValueDialog dialog, String value);
    }

    public interface OnCopyListener
    {
        void onCopy(ValueDialog dialog);
    }
}
