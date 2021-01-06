package com.deloladrin.cows.activities.template;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.database.DatabaseEntry;

public class TemplateEntry<T extends DatabaseEntry> implements View.OnClickListener
{
    private TemplateActivity<T> parent;
    private T value;

    private View view;

    private TextView shortName;
    private ImageView image;

    private TextView name;

    private ImageButton show;
    private ImageButton edit;
    private ImageButton delete;

    private OnActionListener<T> onActionListener;

    public TemplateEntry(TemplateActivity<T> parent, T value)
    {
        this.view = parent.getLayoutInflater().inflate(R.layout.activity_template_entry, null);

        this.parent = parent;
        this.value = value;

        /* Load all children */
        this.shortName = this.view.findViewById(R.id.entry_short_name);
        this.image = this.view.findViewById(R.id.entry_image);

        this.name = this.view.findViewById(R.id.entry_name);

        this.show = this.view.findViewById(R.id.entry_show);
        this.edit = this.view.findViewById(R.id.entry_edit);
        this.delete = this.view.findViewById(R.id.entry_delete);

        /* Add events */
        this.show.setOnClickListener(this);
        this.edit.setOnClickListener(this);
        this.delete.setOnClickListener(this);

        /* Setup layout params */
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.view.setLayoutParams(params);
    }

    @Override
    public void onClick(View view)
    {
        /* Handle click listeners */
        if (view.equals(this.show))
        {
            this.onActionListener.onShowClick(this);
        }

        if (view.equals(this.edit))
        {
            this.onActionListener.onEditClick(this);
        }

        if (view.equals(this.delete))
        {
            this.onActionListener.onDeleteClick(this);
        }
    }

    public TemplateActivity<T> getParent()
    {
        return this.parent;
    }

    public T getValue()
    {
        return this.value;
    }

    public View getView()
    {
        return this.view;
    }

    public TextView getShortNameView()
    {
        return this.shortName;
    }

    public String getShortName()
    {
        return this.shortName.getText().toString();
    }

    public void setShortName(String shortName)
    {
        this.shortName.setText(shortName);
    }

    public void setShortNameVisible(boolean visible)
    {
        int visibility = visible ? View.VISIBLE : View.GONE;
        this.shortName.setVisibility(visibility);
    }

    public ImageView getImageView()
    {
        return this.image;
    }

    public void setImage(Bitmap bitmap)
    {
        this.image.setImageBitmap(bitmap);
    }

    public void setImageVisible(boolean visible)
    {
        int visibility = visible ? View.VISIBLE : View.GONE;
        this.image.setVisibility(visibility);
    }

    public TextView getNameView()
    {
        return this.name;
    }

    public String getName()
    {
        return this.name.getText().toString();
    }

    public void setName(String name)
    {
        this.name.setText(name);
    }

    public ImageButton getShowButton()
    {
        return this.show;
    }

    public void setShowVisible(boolean visible)
    {
        int visibility = visible ? View.VISIBLE : View.GONE;
        this.show.setVisibility(visibility);
    }

    public ImageButton getEditButton()
    {
        return this.edit;
    }

    public ImageButton getDeleteButton()
    {
        return this.delete;
    }

    public OnActionListener<T> getOnActionListener()
    {
        return this.onActionListener;
    }

    public void setOnActionListener(OnActionListener<T> onActionListener)
    {
        this.onActionListener = onActionListener;
    }

    public interface OnActionListener<T extends DatabaseEntry>
    {
        void onShowClick(TemplateEntry<T> entry);
        void onEditClick(TemplateEntry<T> entry);
        void onDeleteClick(TemplateEntry<T> entry);
    }
}
