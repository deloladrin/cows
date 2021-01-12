package com.deloladrin.cows.activities.company;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.activities.template.TemplateActivity;
import com.deloladrin.cows.activities.template.TemplateEntry;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompanyCowActivity extends TemplateActivity<Cow>
{
    public static final String EXTRA_COMPANY = "com.deloladrin.cows.activities.company.CompanyCowActivity.EXTRA_COMPANY";

    private static final String GROUP_NULL = "\0";

    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.image.setImageResource(R.drawable.icon_cow);
        this.name.setText(R.string.activity_company_cow);

        this.setAddVisible(false);

        /* Load requested company */
        int companyID = getIntent().getIntExtra(EXTRA_COMPANY, 0);
        this.company = Company.get(this.database, companyID);

        /* Load default values */
        this.refresh();
    }

    @Override
    protected TemplateEntry<Cow> createEntry(Cow value)
    {
        TemplateEntry<Cow> entry = super.createEntry(value);

        if (value.getID() == 0)
        {
            /* 0 id means separator */
            TextView view = entry.getNameView();
            view.setTextColor(ContextCompat.getColor(this, R.color.text_tint));
            view.setTypeface(view.getTypeface(), Typeface.ITALIC);

            entry.setName(R.string.dialog_company_cow_group, value.getGroup());
        }

        else
        {
            /* Set visibilities */
            entry.setShowVisible(true);
            entry.setDeleteVisible(true);

            /* Add cow as normal */
            String collar = Integer.toString(value.getCollar());
            entry.setShortName(collar);

            String number = Integer.toString(value.getNumber());
            entry.setName(number);
        }

        return entry;
    }

    @Override
    public void onShowClick(TemplateEntry<Cow> entry)
    {
        /* Open cow activity */
        Intent intent = new Intent(this, CowActivity.class);
        intent.putExtra(CowActivity.EXTRA_COW_ID, entry.getValue().getID());

        this.startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        /* Refresh company data */
        this.refresh();
    }

    @Override
    public void refresh()
    {
        List<Cow> values = new ArrayList<>();

        /* Reload all cows */
        List<Cow> cows = this.company.getCows();

        /* Put all cows into groups */
        Map<String, List<Cow>> groups = new HashMap<>();

        for (Cow cow : cows)
        {
            String group = cow.getGroup();

            /* Don't crash on null */
            if (group == null)
            {
                group = GROUP_NULL;
            }

            List<Cow> collection = groups.get(group);

            if (collection == null)
            {
                collection = new ArrayList<>();
                groups.put(group, collection);
            }

            collection.add(cow);
        }

        /* Sort cows into groups */
        List<String> sortedGroups = new ArrayList<>(groups.keySet());
        Collections.sort(sortedGroups, (a, b) ->
        {
            try
            {
                Integer ai = Integer.parseInt(a);
                Integer bi = Integer.parseInt(b);

                return ai.compareTo(bi);
            }
            catch (NumberFormatException e)
            {
                return a.compareTo(b);
            }
        });

        for (String group : sortedGroups)
        {
            /* Sort cows by their collar/number */
            List<Cow> collection = groups.get(group);
            Collections.sort(collection, (a, b) ->
            {
                Integer anum = a.getNumber();
                Integer acol = a.getCollar();

                Integer bnum = b.getNumber();
                Integer bcol = b.getCollar();

                if ((acol != 0) && (bcol != 0))
                {
                    return acol.compareTo(bcol);
                }

                return anum.compareTo(bnum);
            });

            /* Add separator and cows */
            if (!group.equals(GROUP_NULL))
            {
                Cow separator = new Cow(null);
                separator.setGroup(group);

                values.add(separator);
            }

            values.addAll(collection);
        }

        this.setValues(values);
    }

    public Company getCompany()
    {
        return this.company;
    }
}
