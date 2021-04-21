package com.deloladrin.cows.activities.cow.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.activities.cow.views.ResourceTemplateEntry;
import com.deloladrin.cows.activities.cow.views.ResourceTemplateState;
import com.deloladrin.cows.data.FingerMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.data.Treatment;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class ResourceEditDialog extends ChildDialog<CowActivity> implements View.OnClickListener
{
    private Treatment treatment;
    private FingerMask mask;

    private TextView finger;
    private FlowLayout container;

    private List<Resource> resources;
    private List<ResourceTemplateEntry> templates;

    private Button cancel;
    private Button submit;

    private OnSubmitListener onSubmitListener;

    public ResourceEditDialog(ChildActivity<CowActivity> parent, Treatment treatment, FingerMask mask)
    {
        super(parent);
        this.setContentView(R.layout.dialog_cow_resource_edit);

        this.treatment = treatment;
        this.mask = mask;

        /* Load all children */
        this.finger = this.findViewById(R.id.dialog_finger);
        this.container = this.findViewById(R.id.dialog_container);
        this.templates = new ArrayList<>();

        this.cancel = this.findViewById(R.id.dialog_cancel);
        this.submit = this.findViewById(R.id.dialog_submit);

        /* Add events */
        this.cancel.setOnClickListener(this);
        this.submit.setOnClickListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Context context = this.getContext();

        /* Set finger name */
        String fingerName = this.mask.getName(context);
        this.setFingerName(R.string.dialog_resources_select, fingerName);

        /* Get current resources */
        this.resources = new ArrayList<>();

        for (Resource resource : this.treatment.getResources())
        {
            if (this.mask.contains(resource.getTarget()))
            {
                this.resources.add(resource);
            }
        }

        /* Add all resource templates */
        for (ResourceTemplate template : ResourceTemplate.selectAll(this.getDatabase()))
        {
            this.add(template);
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.submit))
        {
            /* Update resources and submit */
            for (ResourceTemplateEntry entry : this.templates)
            {
                ResourceTemplate template = entry.getTemplate();
                ResourceTemplateState state = entry.getState();

                switch (state)
                {
                    case OFF:
                        /* Remove all occurrences of the type */
                        for (Resource resource : this.resources)
                        {
                            ResourceTemplate current = resource.getTemplate();

                            if (current != null && current.equals(template))
                            {
                                resource.delete();
                            }
                        }

                        break;

                    case ON:
                    case COPY:

                        /* Try to find occurrence */
                        Resource occurrence = null;

                        for (Resource resource : this.resources)
                        {
                            ResourceTemplate current = resource.getTemplate();

                            if (current != null && current.equals(template))
                            {
                                occurrence = resource;
                            }
                        }

                        if (occurrence == null)
                        {
                            /* Create new resource */
                            occurrence = new Resource(this.getDatabase());
                            occurrence.setTreatment(this.treatment);
                            occurrence.setTemplate(template);

                            /* Hoof vs finger */
                            if (template.getType() == ResourceType.HOOF)
                            {
                                occurrence.setTarget(this.mask.getHoof());
                            }
                            else
                            {
                                occurrence.setTarget(this.mask);
                            }

                            occurrence.insert();
                        }

                        /* Make sure copy parameter is correct */
                        if (state == ResourceTemplateState.COPY)
                        {
                            occurrence.setCopy(true);
                        }
                        else
                        {
                            occurrence.setCopy(false);
                        }

                        occurrence.update();

                        break;
                }
            }

            this.onSubmitListener.onSubmit(this);
        }

        this.dismiss();
    }

    public void add(ResourceTemplate template)
    {
        Context context = this.getContext();

        ResourceTemplateEntry entry = new ResourceTemplateEntry(context);
        entry.setBackgroundResource(R.color.button_dark);
        entry.setToggledColorResource(R.color.text_tint);
        entry.setCopyColorResource(R.color.text_light);
        entry.setStrokeWidth(4.0f);

        /* Set template */
        entry.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        entry.setTemplate(template);
        entry.setState(this.getCurrentState(template));

        int padding = context.getResources().getDimensionPixelOffset(R.dimen.padding_image_button);
        entry.setPadding(padding, padding, padding, padding);

        int size = context.getResources().getDimensionPixelSize(R.dimen.size_cow_finger_entry);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(size, size);

        int margin = context.getResources().getDimensionPixelOffset(R.dimen.margin_container);
        params.setMargins(margin, margin, margin, margin);

        entry.setLayoutParams(params);

        this.templates.add(entry);
        this.container.addView(entry);
    }

    private ResourceTemplateState getCurrentState(ResourceTemplate template)
    {
        for (Resource resource : this.resources)
        {
            ResourceTemplate current = resource.getTemplate();

            if (current != null && current.equals(template))
            {
                if (resource.isCopy())
                {
                    return ResourceTemplateState.COPY;
                }
                else
                {
                    return ResourceTemplateState.ON;
                }
            }
        }

        return ResourceTemplateState.OFF;
    }

    public void clear()
    {
        /* Remove all templates */
        this.container.removeAllViews();
        this.templates.clear();
    }

    public Treatment getTreatment()
    {
        return this.treatment;
    }

    public FingerMask getMask()
    {
        return this.mask;
    }

    public String getFingerName()
    {
        return this.finger.getText().toString();
    }

    public void setFingerName(String text)
    {
        this.finger.setText(text);
    }

    public void setFingerName(int text)
    {
        this.setFingerName(this.getContext().getResources().getString(text));
    }

    public void setFingerName(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setFingerName(String.format(format, args));
    }

    public OnSubmitListener getOnSubmitListener()
    {
        return this.onSubmitListener;
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener)
    {
        this.onSubmitListener = onSubmitListener;
    }

    public interface OnSubmitListener
    {
        void onSubmit(ResourceEditDialog dialog);
    }
}
