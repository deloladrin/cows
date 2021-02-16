package com.deloladrin.cows.activities.export;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.export.views.ResourceTemplateEntry;
import com.deloladrin.cows.activities.export.views.TreatmentTypeEntry;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.views.ToggleImageView;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class ExportSettings extends ChildActivity<ExportActivity>
{
    private FlowLayout typeContainer;
    private FlowLayout resourceContainer;

    public ExportSettings(ExportActivity parent, int layout)
    {
        super(parent, layout);

        /* Load all children */
        this.typeContainer = this.findViewById(R.id.settings_type_container);
        this.resourceContainer = this.findViewById(R.id.settings_resource_container);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Database database = this.getDatabase();

        /* Load all types */
        for (TreatmentType type : TreatmentType.values())
        {
            if (type != TreatmentType.NONE)
            {
                this.add(type);
            }
        }

        /* Load all resource templates */
        for (ResourceTemplate resourceTemplate : ResourceTemplate.selectAll(database))
        {
            this.add(resourceTemplate);
        }
    }

    public void add(TreatmentType type)
    {
        Context context = this.getContext();

        TreatmentTypeEntry entry = new TreatmentTypeEntry(context);
        entry.setGravity(Gravity.CENTER);
        entry.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.text_big));
        entry.setTypeface(entry.getTypeface(), Typeface.BOLD);
        entry.setTextColor(context.getColor(R.color.text_tint));
        entry.setBackgroundResource(R.color.button_light);
        entry.setToggledColorResource(R.color.text_tint);
        entry.setStrokeWidth(4.0f);

        /* Set type */
        entry.setType(type);
        entry.setToggled(true);

        int padding = context.getResources().getDimensionPixelOffset(R.dimen.padding_image_button);
        entry.setPadding(padding, padding, padding, padding);

        int size = context.getResources().getDimensionPixelSize(R.dimen.size_cow_finger_entry);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(size, size);

        int margin = context.getResources().getDimensionPixelOffset(R.dimen.margin_container);
        params.setMargins(margin, margin, margin, margin);

        entry.setLayoutParams(params);

        this.typeContainer.addView(entry);
    }

    public void add(ResourceTemplate resourceTemplate)
    {
        Context context = this.getContext();

        ResourceTemplateEntry entry = new ResourceTemplateEntry(context);
        entry.setBackgroundResource(R.color.button_light);
        entry.setToggledColorResource(R.color.text_tint);
        entry.setStrokeWidth(4.0f);

        /* Set template */
        entry.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        entry.setTemplate(resourceTemplate);
        entry.setToggled(true);

        int padding = context.getResources().getDimensionPixelOffset(R.dimen.padding_image_button);
        entry.setPadding(padding, padding, padding, padding);

        int size = context.getResources().getDimensionPixelSize(R.dimen.size_cow_finger_entry);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(size, size);

        int margin = context.getResources().getDimensionPixelOffset(R.dimen.margin_container);
        params.setMargins(margin, margin, margin, margin);

        entry.setLayoutParams(params);

        this.resourceContainer.addView(entry);
    }

    public List<TreatmentType> getTreatmentTypes()
    {
        List<TreatmentType> types = new ArrayList<>();

        /* Get all toggled buttons */
        for (int i = 0; i < this.typeContainer.getChildCount(); i++)
        {
            TreatmentTypeEntry entry = (TreatmentTypeEntry)this.typeContainer.getChildAt(i);

            if (entry.isToggled())
            {
                types.add(entry.getType());
            }
        }

        return types;
    }

    public List<ResourceTemplate> getResourceTemplates()
    {
        List<ResourceTemplate> templates = new ArrayList<>();

        /* Get all toggled buttons */
        for (int i = 0; i < this.resourceContainer.getChildCount(); i++)
        {
            ResourceTemplateEntry entry = (ResourceTemplateEntry)this.resourceContainer.getChildAt(i);

            if (entry.isToggled())
            {
                templates.add(entry.getTemplate());
            }
        }

        return templates;
    }
}
