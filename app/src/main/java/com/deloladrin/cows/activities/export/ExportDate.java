package com.deloladrin.cows.activities.export;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExportDate extends ChildActivity<ExportActivity> implements View.OnClickListener
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private EditText start;
    private EditText end;

    public ExportDate(ExportActivity parent, int layout)
    {
        super(parent, layout);

        /* Load all children */
        this.start = this.findViewById(R.id.date_start);
        this.end = this.findViewById(R.id.date_end);

        /* Add events */
        this.start.setOnClickListener(this);
        this.end.setOnClickListener(this);

        /* Load default values */
        LocalDate today = LocalDate.now();

        this.setStart(today);
        this.setEnd(today);
    }

    @Override
    public void onClick(View view)
    {
        /* Show date selection */
        DatePickerDialog dialog = new DatePickerDialog(this.activity);

        dialog.setOnDateSetListener((DatePicker picker, int year, int month, int dayOfMonth) ->
        {
            LocalDate date = LocalDate.of(year, month + 1, dayOfMonth);

            if (view.equals(this.start))
            {
                /* Assign to both */
                this.setStart(date);
                this.setEnd(date);
            }
            else
            {
                /* Assign only to end */
                this.setEnd(date);
            }
        });

        dialog.show();
    }

    public String getStartString()
    {
        return this.start.getText().toString();
    }

    public LocalDate getStart()
    {
        return LocalDate.parse(this.getStartString(), FORMATTER);
    }

    public void setStart(LocalDate start)
    {
        this.start.setText(FORMATTER.format(start));
    }

    public String getEndString()
    {
        return this.end.getText().toString();
    }

    public LocalDate getEnd()
    {
        return LocalDate.parse(this.getEndString(), FORMATTER);
    }

    public void setEnd(LocalDate end)
    {
        this.end.setText(FORMATTER.format(end));
    }
}
