package com.deloladrin.cows.activities.company.cow;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.cow.views.StatusTemplateContainer;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Status;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.SelectOrder;
import com.deloladrin.cows.database.SelectValues;
import com.deloladrin.cows.dialogs.YesNoDialog;

import java.time.format.DateTimeFormatter;

public class CompanyCowEntry implements View.OnClickListener, StatusTemplateContainer.OnToggleListener
{
    private CompanyCowActivity parent;
    private Cow cow;

    private View view;

    private TextView collar;
    private TextView number;
    private TextView treatment;
    private StatusTemplateContainer statuses;

    private ImageButton delete;

    public CompanyCowEntry(CompanyCowActivity parent, Cow cow, LayoutInflater inflater)
    {
        this.parent = parent;
        this.cow = cow;

        this.view = inflater.inflate(R.layout.entry_company_cow, null);

        /* Load all children */
        this.collar = this.view.findViewById(R.id.entry_collar);
        this.number = this.view.findViewById(R.id.entry_number);
        this.treatment = this.view.findViewById(R.id.entry_treatment);
        this.statuses = this.view.findViewById(R.id.entry_statuses);

        this.delete = this.view.findViewById(R.id.entry_delete);

        /* Add events */
        this.view.setOnClickListener(this);
        this.delete.setOnClickListener(this);
        this.statuses.setOnToggleListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Database database = this.parent.getDatabase();

        /* Cow parameters */
        int collar = this.cow.getCollar();

        if (collar != 0)
        {
            this.collar.setText(Integer.toString(collar));
        }

        int number = this.cow.getID();
        this.number.setText(Integer.toString(number));

        SelectValues values = new SelectValues()
                .where(Treatment.Table.COLUMN_COW, number)
                .orderBy(Treatment.Table.COLUMN_DATE, SelectOrder.DESCENDING);

        Treatment treatment = database.getTreatmentTable().select(values);

        if (treatment != null)
        {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            this.treatment.setText(treatment.getDate().format(dateFormatter));

            /* Show statuses */
            this.statuses.refresh(database);

            for (Status status : treatment.getStatuses())
            {
                this.statuses.setEnabled(status.getTemplate(), true);
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.delete))
        {
            /* Request to delete cow */
            YesNoDialog dialog = new YesNoDialog(this.parent);
            dialog.setText(R.string.dialog_cow_delete, this.cow.getID());

            dialog.setOnYesListener((YesNoDialog d) ->
            {
                /* Delete and refresh */
                this.cow.delete();
                this.parent.refresh();
            });

            dialog.show();
            return;
        }

        if (view.equals(this.view))
        {
            this.parent.onShow(this.cow);
        }
    }

    @Override
    public void onToggle(StatusTemplate template)
    {
        this.parent.onShow(this.cow);
    }

    public CompanyCowActivity getParent()
    {
        return this.parent;
    }

    public Cow getCow()
    {
        return this.cow;
    }

    public View getView()
    {
        return this.view;
    }
}
