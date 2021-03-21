package com.deloladrin.cows.activities.record;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.activities.cow.views.StatusTemplateContainer;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Status;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.views.CircleTextView;

public class RecordEntry implements View.OnClickListener, StatusTemplateContainer.OnToggleListener
{
    private RecordActivity parent;
    private Treatment treatment;

    private View view;

    private TextView collar;
    private TextView number;
    private TextView group;
    private CircleTextView type;
    private StatusTemplateContainer statuses;

    public RecordEntry(RecordActivity parent, Treatment treatment, LayoutInflater inflater)
    {
        this.parent = parent;
        this.treatment = treatment;

        this.view = inflater.inflate(R.layout.entry_record, null);

        /* Load all children */
        this.collar = this.view.findViewById(R.id.entry_collar);
        this.number = this.view.findViewById(R.id.entry_number);
        this.group = this.view.findViewById(R.id.entry_group);
        this.type = this.view.findViewById(R.id.entry_type);
        this.statuses = this.view.findViewById(R.id.entry_statuses);

        /* Add events */
        this.view.setOnClickListener(this);
        this.statuses.setOnToggleListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Context context = this.view.getContext();
        Database database = this.parent.getDatabase();

        Cow cow = this.treatment.getCow();
        this.statuses.refresh(database);

        /* Cow parameters */
        int collar = cow.getCollar();

        if (collar != 0)
        {
            this.collar.setText(Integer.toString(collar));
        }

        int number = cow.getID();
        this.number.setText(Integer.toString(number));

        String group = cow.getGroup();

        if (group != null)
        {
            this.group.setText(group);
        }
        else
        {
            this.group.setText("—");
        }

        String type = this.treatment.getType().getShortName(context);
        this.type.setText(type);

        for (Status status : this.treatment.getStatuses())
        {
            this.statuses.setEnabled(status.getTemplate(), true);
        }
    }

    @Override
    public void onClick(View view)
    {
        /* Start cow activity */
        Intent intent = new Intent(this.view.getContext(), CowActivity.class);
        intent.putExtra(CowActivity.EXTRA_COW_ID, this.treatment.getCow().getID());
        intent.putExtra(CowActivity.EXTRA_TREATMENT_ID, this.treatment.getID());

        this.parent.startActivity(intent);
    }

    @Override
    public void onToggle(StatusTemplate template)
    {
        this.onClick(null);
    }

    public RecordActivity getParent()
    {
        return this.parent;
    }

    public Treatment getTreatment()
    {
        return this.treatment;
    }

    public View getView()
    {
        return this.view;
    }
}
