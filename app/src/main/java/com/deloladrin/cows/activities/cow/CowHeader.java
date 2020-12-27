package com.deloladrin.cows.activities.cow;

import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.data.Cow;

public class CowHeader extends ChildActivity<CowActivity>
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
            int number = cow.getNumber();
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
