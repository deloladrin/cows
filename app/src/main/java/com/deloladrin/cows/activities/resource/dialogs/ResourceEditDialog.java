package com.deloladrin.cows.activities.resource.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.resource.ResourceActivity;
import com.deloladrin.cows.data.DiagnosisType;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.views.ImageSelect;
import com.deloladrin.cows.views.ToggleTextView;

public class ResourceEditDialog extends ChildDialog<ResourceActivity> implements View.OnClickListener
{
    private ResourceTemplate template;

    private EditText name;
    private Spinner type;
    private EditText layer;
    private ToggleTextView copying;
    private ImageSelect image;
    private ImageSelect imageSmall;

    private Button cancel;
    private Button submit;

    private OnSubmitListener onSubmitListener;

    public ResourceEditDialog(ResourceActivity parent, ResourceTemplate template)
    {
        super(parent);
        this.setContentView(R.layout.dialog_resource_edit);

        this.template = template;

        /* Load all children */
        this.name = this.findViewById(R.id.dialog_name);
        this.type = this.findViewById(R.id.dialog_type);
        this.layer = this.findViewById(R.id.dialog_layer);
        this.copying = this.findViewById(R.id.dialog_copying);
        this.image = new ImageSelect(parent, this.findViewById(R.id.dialog_image), 1024, 1024);
        this.imageSmall = new ImageSelect(parent, this.findViewById(R.id.dialog_image_small), 256, 256);

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

        /* Setup type adapter */
        ResourceTypeAdapter adapter = new ResourceTypeAdapter(context);
        this.type.setAdapter(adapter);

        /* Template data */
        String name = this.template.getName();
        this.name.setText(name);

        int layer = this.template.getLayer();
        this.layer.setText(Integer.toString(layer));

        boolean copying = this.template.isCopying();
        this.copying.setToggled(copying);

        DatabaseBitmap image = this.template.getImage();
        this.image.setDatabaseBitmap(image);

        DatabaseBitmap imageSmall = this.template.getSmallImage();
        this.imageSmall.setDatabaseBitmap(this.template.getSmallImage());

        ResourceType type = this.template.getType();
        int selected = 0;

        for (int i = 0; i < adapter.getCount(); i++)
        {
            if (type.equals(adapter.getItem(i)))
            {
                selected = i;
            }
        }

        this.type.setSelection(selected);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.submit))
        {
            /* Update and submit */
            DatabaseBitmap image = this.image.getDatabaseBitmap();
            DatabaseBitmap imageSmall = this.imageSmall.getDatabaseBitmap();

            /* All images must be set! */
            if (image != null && imageSmall != null)
            {
                String name = this.name.getText().toString();
                this.template.setName(name);

                ResourceType type = (ResourceType) this.type.getSelectedItem();
                this.template.setType(type);

                try
                {
                    int layer = Integer.parseInt(this.layer.getText().toString());
                    this.template.setLayer(layer);
                }
                catch (Exception e)
                {
                    this.template.setLayer(0);
                }

                boolean copying = this.copying.isToggled();
                this.template.setCopying(copying);

                this.template.setImage(image);
                this.template.setSmallImage(imageSmall);

                this.onSubmitListener.onSubmit(this.template);
            }

            else
            {
                return;
            }
        }

        this.dismiss();
    }

    public ResourceTemplate getTemplate()
    {
        return this.template;
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
        void onSubmit(ResourceTemplate template);
    }
}
