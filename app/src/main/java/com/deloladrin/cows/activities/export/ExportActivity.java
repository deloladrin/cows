package com.deloladrin.cows.activities.export;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.database.DatabaseActivity;
import com.deloladrin.cows.database.SelectValues;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExportActivity extends DatabaseActivity implements View.OnClickListener
{
    private Company company;

    private ExportDate date;
    private ExportSettings settings;

    private Button export;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_export);

        this.company = this.preferences.getActiveCompany();

        /* Load all children */
        this.date = new ExportDate(this, R.id.export_date);
        this.settings = new ExportSettings(this, R.id.export_settings);

        this.export = this.findViewById(R.id.export_export);

        /* Add events */
        this.export.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.export))
        {
            /* Export xlsx file */
            TreatmentWorkbook workbook = new TreatmentWorkbook(this, this.settings);
            workbook.setCompany(this.company);

            /* Get dates */
            LocalDate start = this.date.getStart();
            LocalDate end = this.date.getEnd();
            LocalDate repeat = this.date.getRepeat();

            if (!start.equals(end))
            {
                workbook.setDate(start, end);
            }
            else
            {
                workbook.setDate(start);
            }

            workbook.setRepeatDate(repeat);

            /* Get all cows */
            SelectValues values = new SelectValues()
                    .where(Cow.Table.COLUMN_COMPANY, this.company.getID());

            List<Cow> cows = this.database.getCowTable().selectAll(values);
            Cow.sort(cows);

            for (Cow cow : cows)
            {
                /* Select treatments in range */
                for (Treatment treatment : cow.getTreatments())
                {
                    LocalDateTime date = treatment.getDate();

                    if (date.isAfter(start.atStartOfDay()) &&
                        date.isBefore(end.atTime(23, 59, 59)))
                    {
                        workbook.add(treatment);
                    }
                }
            }

            String fileName = this.getFileName();
            workbook.save(fileName);
        }
    }

    private String getFileName()
    {
        String output = this.company.getName() + " ";

        /* Add dates */
        String start = this.date.getStartString();
        String end = this.date.getEndString();

        if (!start.equals(end))
        {
            output += start + " - " + end;
        }
        else
        {
            output += start;
        }

        return output;
    }
}
