package com.deloladrin.cows.activities.main.dialogs;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.main.MainActivity;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.dialogs.YesNoDialog;

public class CowDialog extends ChildDialog<MainActivity> implements View.OnClickListener
{
    private static final String NUMBER_DIGITS = "0123456789";

    private Company company;
    private CowDialogState state;
    private Cow cow;

    private TextView text;
    private EditText input;

    private Button cancel;
    private Button next;

    private OnSubmitListener onSubmitListener;

    private KeyListener defaultListener;
    private Animation animation;

    public CowDialog(MainActivity parent, Company company)
    {
        super(parent);
        this.setContentView(R.layout.dialog_main_cow);

        this.company = company;
        this.state = CowDialogState.COLLAR;

        this.cow = new Cow(this.getDatabase());
        this.cow.setCompany(company);

        /* Load all children */
        this.text = this.findViewById(R.id.dialog_text);
        this.input = this.findViewById(R.id.dialog_input);

        this.cancel = this.findViewById(R.id.dialog_cancel);
        this.next = this.findViewById(R.id.dialog_continue);

        this.defaultListener = this.input.getKeyListener();
        this.animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_in_right);

        /* Add events */
        this.cancel.setOnClickListener(this);
        this.next.setOnClickListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Context context = this.getContext();
        Database database = this.getDatabase();

        Cow cow;

        switch (this.state)
        {
            case COLLAR:

                /* Display collar message */
                this.setText(R.string.dialog_cow_collar);

                this.input.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.input.setKeyListener(DigitsKeyListener.getInstance(NUMBER_DIGITS));

                break;

            case ID:

                /* Find first cow with correct collar and company */
                cow = Cow.get(database, this.cow.getCompany(), this.cow.getCollar());
                String id = "";

                if (cow != null)
                {
                    id = Integer.toString(cow.getID());
                }

                /* Display id message */
                this.setText(R.string.dialog_cow_id);
                this.input.setText(id);
                this.input.selectAll();

                this.input.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.input.setKeyListener(DigitsKeyListener.getInstance(NUMBER_DIGITS));

                this.text.startAnimation(this.animation);
                this.input.startAnimation(this.animation);

                break;

            case GROUP:

                /* Find cow with correct id */
                cow = Cow.get(database, this.cow.getID());
                String group = "";

                if (cow != null)
                {
                    group = cow.getGroup();
                }

                /* Display group message */
                this.setText(R.string.dialog_cow_group);
                this.input.setText(group);
                this.input.selectAll();

                this.input.setInputType(InputType.TYPE_CLASS_TEXT);
                this.input.setKeyListener(this.defaultListener);

                this.text.startAnimation(this.animation);
                this.input.startAnimation(this.animation);

                break;
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.next))
        {
            String input = this.input.getText().toString();

            /* Change current state */
            switch (this.state)
            {
                case COLLAR:

                    /* Store collar if any */
                    int collar = 0;

                    if (!input.isEmpty())
                    {
                        collar = Integer.parseInt(input);
                    }

                    this.cow.setCollar(collar);

                    /* Goto ID */
                    this.state = CowDialogState.ID;
                    this.initialize();

                    break;

                case ID:

                    /* Cow id cannot be empty! */
                    if (!input.isEmpty())
                    {
                        int id = Integer.parseInt(input);
                        this.cow.setID(id);

                        /* Goto GROUP */
                        this.state = CowDialogState.GROUP;
                        this.initialize();
                    }

                    break;

                case GROUP:

                    /* Store group if any */
                    String group = null;

                    if (!input.isEmpty())
                    {
                        group = input;
                    }

                    this.cow.setGroup(group);

                    /* Does the cow exist? */
                    if (Cow.get(this.getDatabase(), this.cow.getID()) != null)
                    {
                        /* Update group and submit */
                        this.cow.update();

                        this.onSubmitListener.onSubmit(this.cow);
                        this.dismiss();
                    }
                    else
                    {
                        /* Request to create new cow */
                        YesNoDialog dialog = new YesNoDialog(this.getContext());
                        dialog.setText(R.string.dialog_cow_add);

                        dialog.setOnYesListener((YesNoDialog d) ->
                        {
                            /* Insert cow and submit */
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

        this.dismiss();
    }

    public Company getCompany()
    {
        return this.company;
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
