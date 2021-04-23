package com.deloladrin.cows.main.dialogs;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.Ui;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.main.views.ActiveCompanyEntry;
import com.deloladrin.cows.storage.Selection;
import com.deloladrin.cows.views.SelectDialog;

public class ActiveCompanyDialog extends SelectDialog<Company, ActiveCompanyEntry>
{
    private static final String TAG = SelectDialog.class.getSimpleName();

    public ActiveCompanyDialog(Context context)
    {
        super(context);
        this.title.setText(R.string.main_company_select);

        Log.i(TAG, "Loading Company adapter ...");
        Selection<Company> companies = this.getDatabase().getTable(Company.class).select();

        Adapter adapter = new Adapter(companies);
        this.setAdapter(adapter);
    }

    @Override
    public void onClick(View view)
    {
        super.onClick(view);

        if (view instanceof ActiveCompanyEntry)
        {
            ActiveCompanyEntry entry = (ActiveCompanyEntry) view;

            if (this.onEntrySelectedListener != null)
            {
                Company company = entry.getCompany();
                this.onEntrySelectedListener.onEntrySelected(company);
            }

            this.dismiss();
            return;
        }
    }

    public class Adapter extends SelectDialog.SelectionAdapter<Company, ActiveCompanyEntry>
    {
        public Adapter(Selection<Company> companies)
        {
            super(companies);
        }

        public Holder<ActiveCompanyEntry> onCreateViewHolder(ViewGroup parent, int viewType)
        {
            Context context = parent.getContext();
            ActiveCompanyEntry entry = new ActiveCompanyEntry(context);
            entry.setBackgroundResource(R.drawable.global_ripple_light);
            entry.setOnClickListener(ActiveCompanyDialog.this);
            entry.setClickable(true);

            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            entry.setLayoutParams(params);

            return new Holder<>(entry);
        }

        @Override
        public void onBindViewHolder(Holder<ActiveCompanyEntry> holder, int position)
        {
            Company company = this.items.get(position);
            holder.getItemView().setCompany(company);
        }
    }
}
