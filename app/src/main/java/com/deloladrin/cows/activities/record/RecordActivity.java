package com.deloladrin.cows.activities.record;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.database.DatabaseActivity;
import com.deloladrin.cows.database.SelectValues;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends DatabaseActivity implements View.OnClickListener
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private Company company;

    private EditText date;
    private TextView count;

    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_record);

        this.company = this.preferences.getActiveCompany();

        /* Load all children */
        this.date = this.findViewById(R.id.record_date);
        this.count = this.findViewById(R.id.record_count);

        this.container = this.findViewById(R.id.record_container);

        /* Add events */
        this.date.setOnClickListener(this);
        this.setDate(LocalDate.now());
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.date))
        {
            /* Show date selection */
            DatePickerDialog dialog = new DatePickerDialog(this);

            dialog.setOnDateSetListener((DatePicker picker, int year, int month, int dayOfMonth) ->
            {
                LocalDate date = LocalDate.of(year, month + 1, dayOfMonth);

                this.setDate(date);
                this.refresh();
            });

            dialog.show();
            return;
        }
    }

    public void refresh()
    {
        this.clear();

        /* Get all treatments today */
        SelectValues values = new SelectValues()
                .where(Cow.Table.COLUMN_COMPANY, this.company.getID());

        List<Cow> cows = this.database.getCowTable().selectAll(values);
        List<Treatment> treatments = new ArrayList<>();

        int count = 0;

        for (Cow cow : cows)
        {
            /* Select treatments in range */
            for (Treatment treatment : cow.getTreatments())
            {
                if (treatment.getDate().toLocalDate().equals(this.getDate()))
                {
                    treatments.add(treatment);
                    count++;
                }
            }
        }

        /* Update count */
        String format = this.getResources().getString(R.string.entry_record_count);
        this.count.setText(String.format(format, count));

        /* Add treatments */
        LayoutInflater inflater = this.getLayoutInflater();
        Treatment.sort(treatments);

        for (Treatment treatment : treatments)
        {
            RecordEntry entry = new RecordEntry(this, treatment, inflater);
            this.add(entry.getView());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        /* Refresh data */
        this.refresh();
    }

    public void add(View view)
    {
        this.container.addView(view);
    }

    public void clear()
    {
        this.container.removeAllViews();
    }

    public String getDateString()
    {
        return this.date.getText().toString();
    }

    public LocalDate getDate()
    {
        return LocalDate.parse(this.getDateString(), FORMATTER);
    }

    public void setDate(LocalDate start)
    {
        this.date.setText(FORMATTER.format(start));
    }
}
