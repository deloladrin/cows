package com.deloladrin.cows.activities.cow.views;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.FingerMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.data.TargetMask;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class ResourcesEditDialog extends ChildDialog<CowActivity> implements View.OnClickListener
{
    private Diagnosis diagnosis;
    private TargetMask mask;

    private TextView finger;
    private FlowLayout container;

    private List<ResourceEntry> entries;

    private Button cancel;
    private Button submit;

    private OnSubmitListener onSubmitListener;

    public ResourcesEditDialog(ChildActivity<CowActivity> parent, Diagnosis diagnosis)
    {
        super(parent);
        this.setContentView(R.layout.dialog_cow_resources_edit);

        this.diagnosis = diagnosis;
        this.mask = FingerMask.parseUnknown(diagnosis.getTarget());

        /* Load all children */
        this.finger = this.findViewById(R.id.dialog_finger);
        this.container = this.findViewById(R.id.dialog_container);

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
        this.entries = new ArrayList<>();

        /* Set finger and diagnosis name */
        String fingerName = this.mask.getName(context);
        this.setFingerName(R.string.dialog_finger_name, fingerName);

        /* Load all hoof resources */
        List<Resource> current = this.diagnosis.getResources();

        for (Resource resource : this.getDatabase().getResourceTable().selectAll())
        {
            if (resource.getType() != ResourceType.COW)
            {
                boolean toggled = current.contains(resource);
                this.add(resource, toggled);
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.submit))
        {
            /* Update resources and submit */
            List<Resource> enabled = new ArrayList<>();

            for (ResourceEntry entry : this.entries)
            {
                if (entry.isToggled())
                {
                    enabled.add(entry.getResource());
                }
            }

            this.diagnosis.setResources(enabled);
            this.diagnosis.update();

            this.onSubmitListener.onSubmit(this);
        }

        this.dismiss();
    }

    public void add(Resource resource)
    {
        this.add(resource, false);
    }

    public void add(Resource resource, boolean toggled)
    {
        Context context = this.getContext();

        ResourceEntry button = new ResourceEntry(context);
        button.setBackgroundResource(R.color.button_dark);
        button.setToggledColorResource(R.color.text_tint);
        button.setStrokeWidth(4.0f);

        /* Set resource */
        button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        button.setImageBitmap(resource.getImage().getBitmap());

        int padding = context.getResources().getDimensionPixelOffset(R.dimen.padding_image_button);
        button.setPadding(padding, padding, padding, padding);

        int size = context.getResources().getDimensionPixelSize(R.dimen.size_cow_finger_entry);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(size, size);

        int margin = context.getResources().getDimensionPixelOffset(R.dimen.margin_container);
        params.setMargins(margin, margin, margin, margin);

        button.setLayoutParams(params);

        button.setResource(resource);
        button.setToggled(toggled);

        this.entries.add(button);
        this.container.addView(button);
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
        void onSubmit(ResourcesEditDialog dialog);
    }
}
