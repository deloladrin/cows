package com.deloladrin.cows.activities.cow;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.dialogs.ValueDialog;
import com.deloladrin.cows.dialogs.ValueDialogType;

public class CowHeader extends ChildActivity<CowActivity> implements View.OnClickListener
{
    private Cow cow;

    private TextView number;
    private TextView collar;
    private TextView company;
    private TextView group;

    public CowHeader(CowActivity parent, int layout)
    {
        super(parent, layout);

        /* Load all children */
        this.number = this.findViewById(R.id.header_number);
        this.collar = this.findViewById(R.id.header_collar);
        this.company = this.findViewById(R.id.header_company);
        this.group = this.findViewById(R.id.header_group);

        /* Add events */
        this.collar.setOnClickListener(this);
        this.group.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        Context context = this.getContext();

        if (this.cow != null)
        {
            if (view.equals(this.collar))
            {
                /* Show collar change dialog */
                ValueDialog dialog = new ValueDialog(context, ValueDialogType.NUMBER);
                dialog.setText(R.string.dialog_cow_collar);
                dialog.setCopyEnabled(false);

                int collar = this.cow.getCollar();

                if (collar != 0)
                {
                    dialog.setInput(Integer.toString(collar));
                }

                dialog.setOnSubmitListener((d, value) ->
                {
                    /* Update and refresh */
                    int newCollar = 0;

                    if (!value.isEmpty())
                    {
                        newCollar = Integer.parseInt(value);
                    }

                    this.cow.setCollar(newCollar);
                    this.cow.update();

                    this.activity.refresh();
                });

                dialog.show();
            }

            if (view.equals(this.group))
            {
                /* Show group change dialog */
                ValueDialog dialog = new ValueDialog(context, ValueDialogType.TEXT);
                dialog.setText(R.string.dialog_cow_group);
                dialog.setCopyEnabled(true);

                dialog.setInput(this.cow.getGroup());

                dialog.setOnSubmitListener((d, value) ->
                {
                    /* Update and refresh */
                    String newGroup = null;

                    if (!value.isEmpty())
                    {
                        newGroup = value;
                    }

                    this.cow.setGroup(newGroup);
                    this.cow.update();

                    this.activity.refresh();
                });

                dialog.setOnCopyListener((d) ->
                {
                    /* Copy last group */
                    String lastGroup = this.cow.getCompany().getLastGroup();
                    d.setInput(lastGroup);
                });

                dialog.show();
            }
        }
    }

    public Cow getCow()
    {
        return this.cow;
    }

    public void setCow(Cow cow)
    {
        this.cow = cow;

        if (cow != null)
        {
            int number = cow.getID();
            int collar = cow.getCollar();
            String company = cow.getCompany().getName();
            String group = cow.getGroup();

            /* Convert to required format */
            this.number.setText(Integer.toString(number));
            this.collar.setText(collar != 0 ? Integer.toString(collar) : "—");
            this.company.setText(company);
            this.group.setText(group != null ? group : "—");
        }
        else
        {
            this.number.setText("0");
            this.collar.setText("—");
            this.company.setText("—");
            this.group.setText("—");
        }
    }
}
