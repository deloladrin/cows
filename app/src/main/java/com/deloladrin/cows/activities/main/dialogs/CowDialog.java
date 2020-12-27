package com.deloladrin.cows.activities.main.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.main.MainActivity;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.dialogs.YesNoDialog;

public class CowDialog extends ChildDialog<MainActivity> implements View.OnClickListener, TextWatcher
{
    private CowDialogStage stage;
    private Company company;
    private Cow cow;

    private LinearLayout collarStage;
    private TextView collarText;
    private EditText collarInput;

    private LinearLayout numberStage;
    private TextView numberText;
    private TextView numberError;
    private EditText numberInput;

    private LinearLayout groupStage;
    private TextView groupText;
    private EditText groupInput;
    private ImageButton groupCopy;

    private Button cancel;
    private Button next;

    private OnSubmitListener onSubmitListener;

    public CowDialog(MainActivity parent, Company company)
    {
        super(parent);
        this.setContentView(R.layout.dialog_main_cow);

        this.stage = CowDialogStage.COLLAR;
        this.company = company;

        this.cow = new Cow(this.getDatabase());
        this.cow.setCompany(company);

        /* Load all children */
        this.collarStage = this.findViewById(R.id.dialog_stage_collar);
        this.collarText = this.findViewById(R.id.dialog_collar_text);
        this.collarInput = this.findViewById(R.id.dialog_collar_input);

        this.numberStage = this.findViewById(R.id.dialog_stage_number);
        this.numberText = this.findViewById(R.id.dialog_number_text);
        this.numberError = this.findViewById(R.id.dialog_number_error);
        this.numberInput = this.findViewById(R.id.dialog_number_input);

        this.groupStage = this.findViewById(R.id.dialog_stage_group);
        this.groupText = this.findViewById(R.id.dialog_group_text);
        this.groupInput = this.findViewById(R.id.dialog_group_input);
        this.groupCopy = this.findViewById(R.id.dialog_group_copy);

        this.cancel = this.findViewById(R.id.dialog_cancel);
        this.next = this.findViewById(R.id.dialog_continue);

        /* Add events */
        this.groupCopy.setOnClickListener(this);

        this.cancel.setOnClickListener(this);
        this.next.setOnClickListener(this);

        this.collarInput.addTextChangedListener(this);
        this.numberInput.addTextChangedListener(this);
        this.groupInput.addTextChangedListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Context context = this.getContext();
        Database database = this.getDatabase();

        this.setCollarText(R.string.dialog_cow_collar);
        this.setNumberText(R.string.dialog_cow_number);
        this.setGroupText(R.string.dialog_cow_group);

        switch (this.stage)
        {
            case COLLAR:

                /* Set visibilities */
                this.collarStage.setVisibility(View.VISIBLE);
                this.numberStage.setVisibility(View.GONE);
                this.groupStage.setVisibility(View.GONE);

                this.focusInput(this.collarInput);
                break;

            case NUMBER:

                /* Set visibilities */
                this.collarStage.setVisibility(View.GONE);
                this.numberStage.setVisibility(View.VISIBLE);
                this.groupStage.setVisibility(View.GONE);

                int collar = this.cow.getCollar();
                String number = "";

                if (collar != 0)
                {
                    Cow cow = Cow.getByCollar(database, this.cow.getCompany(), collar);

                    if (cow != null)
                    {
                        number = Integer.toString(cow.getNumber());
                    }
                }

                this.numberInput.setText(number);
                this.focusInput(this.numberInput);

                break;

            case GROUP:

                /* Set visibilities */
                this.collarStage.setVisibility(View.GONE);
                this.numberStage.setVisibility(View.GONE);
                this.groupStage.setVisibility(View.VISIBLE);

                Cow cow = Cow.getByNumber(database, this.cow.getNumber());
                String group = "";

                if (cow != null)
                {
                    group = cow.getGroup();
                }

                this.groupInput.setText(group);
                this.focusInput(this.groupInput);

                break;
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.next))
        {
            String input;

            switch (this.stage)
            {
                case COLLAR:

                    input = this.collarInput.getText().toString();

                    /* Store collar if any */
                    int collar = 0;

                    if (!input.isEmpty())
                    {
                        collar = Integer.parseInt(input);
                    }

                    this.cow.setCollar(collar);

                    this.setStage(CowDialogStage.NUMBER);
                    break;

                case NUMBER:

                    input = this.numberInput.getText().toString();

                    /* Cow number restrictions */
                    if (input.isEmpty())
                    {
                        this.setNumberError(R.string.error_cow_number_empty);
                        break;
                    }

                    if (input.length() != 6)
                    {
                        this.setNumberError(R.string.error_cow_number_length);
                        break;
                    }

                    /* Store number */
                    int number = Integer.parseInt(input);
                    this.cow.setNumber(number);

                    this.setStage(CowDialogStage.GROUP);
                    break;

                case GROUP:

                    input = this.groupInput.getText().toString();

                    /* Store group if any */
                    String group = null;

                    if (!input.isEmpty())
                    {
                        group = input;
                    }

                    this.cow.setGroup(group);

                    /* Update existing / Create new */
                    Cow previous = Cow.getByNumber(this.getDatabase(), this.cow.getNumber());

                    if (previous != null)
                    {
                        this.cow.setID(previous.getID());
                        this.cow.update();

                        this.onSubmitListener.onSubmit(this.cow);
                        this.dismiss();
                    }
                    else
                    {
                        /* Ask to create new one */
                        YesNoDialog dialog = new YesNoDialog(this.getContext());
                        dialog.setText(R.string.dialog_cow_add);

                        dialog.setOnYesListener((YesNoDialog d) ->
                        {
                            this.cow.insert();

                            this.onSubmitListener.onSubmit(this.cow);
                            this.dismiss();
                        });

                        dialog.show();
                    }

                    break;
            }

            return;
        }

        if (view.equals(this.groupCopy))
        {
            /* Copy companies last group if any */
            String last = this.company.getLastGroup();

            this.groupInput.setText(last);
            this.focusInput(this.groupInput);

            return;
        }

        this.dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        this.resetErrors();
    }

    private void setNumberError(int errorID)
    {
        Resources resources = this.getContext().getResources();
        String error = resources.getString(errorID);

        this.numberError.setText(error);
        this.numberError.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.text_small));
    }

    private void resetErrors()
    {
        this.numberError.setText("");
        this.numberError.setTextSize(0);
    }

    public void focusInput(EditText input)
    {
        new Handler().postDelayed(() ->
        {
            input.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0 ,0));
            input.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0 ,0));
            input.selectAll();
        }, 100);
    }

    public String getCollarText()
    {
        return this.collarText.getText().toString();
    }

    public void setCollarText(String text)
    {
        this.collarText.setText(text);
    }

    public void setCollarText(int text)
    {
        this.setCollarText(this.getContext().getResources().getString(text));
    }

    public void setCollarText(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setCollarText(String.format(format, args));
    }

    public String getNumberText()
    {
        return this.numberText.getText().toString();
    }

    public void setNumberText(String text)
    {
        this.numberText.setText(text);
    }

    public void setNumberText(int text)
    {
        this.setNumberText(this.getContext().getResources().getString(text));
    }

    public void setNumberText(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setNumberText(String.format(format, args));
    }

    public String getGroupText()
    {
        return this.groupText.getText().toString();
    }

    public void setGroupText(String text)
    {
        this.groupText.setText(text);
    }

    public void setGroupText(int text)
    {
        this.setGroupText(this.getContext().getResources().getString(text));
    }

    public void setGroupText(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setGroupText(String.format(format, args));
    }

    public CowDialogStage getStage()
    {
        return this.stage;
    }

    public void setStage(CowDialogStage stage)
    {
        this.stage = stage;
        this.initialize();
    }

    public Company getCompany()
    {
        return this.company;
    }

    public Cow getCow()
    {
        return this.cow;
    }

    public OnSubmitListener getOnSubmitListener()
    {
        return this.onSubmitListener;
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener)
    {
        this.onSubmitListener = onSubmitListener;
    }

    public interface OnSubmitListener
    {
        void onSubmit(Cow cow);
    }
}
