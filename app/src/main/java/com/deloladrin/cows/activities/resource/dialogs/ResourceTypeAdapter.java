package com.deloladrin.cows.activities.resource.dialogs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.deloladrin.cows.data.DiagnosisType;
import com.deloladrin.cows.data.ResourceType;

public class ResourceTypeAdapter extends ArrayAdapter<ResourceType>
{
    public ResourceTypeAdapter(Context context)
    {
        super(context, android.R.layout.simple_spinner_dropdown_item, ResourceType.values());
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);

        ResourceType type = this.getItem(position);
        view.setText(type.getName(this.getContext()));

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView view = (TextView) super.getView(position, convertView, parent);

        ResourceType type = this.getItem(position);
        view.setText(type.getName(this.getContext()));

        return view;
    }
}
