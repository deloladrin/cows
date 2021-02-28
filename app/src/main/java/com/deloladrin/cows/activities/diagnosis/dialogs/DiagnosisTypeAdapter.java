package com.deloladrin.cows.activities.diagnosis.dialogs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.deloladrin.cows.data.DiagnosisType;

import java.util.ArrayList;

public class DiagnosisTypeAdapter extends ArrayAdapter<DiagnosisType>
{
    public DiagnosisTypeAdapter(Context context)
    {
        super(context, android.R.layout.simple_spinner_dropdown_item, DiagnosisType.values());
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);

        DiagnosisType type = this.getItem(position);
        view.setText(type.getName(this.getContext()));

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView view = (TextView) super.getView(position, convertView, parent);

        DiagnosisType type = this.getItem(position);
        view.setText(type.getName(this.getContext()));

        return view;
    }
}
