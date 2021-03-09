package com.deloladrin.cows.activities.status.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.status.StatusActivity;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.views.ImageSelect;

public class StatusEditDialog extends ChildDialog<StatusActivity> implements View.OnClickListener
{
    private StatusTemplate template;

    private EditText name;
    private ImageSelect image;

    private Button cancel;
    private Button submit;

    private OnSubmitListener onSubmitListener;

    public StatusEditDialog(StatusActivity parent, StatusTemplate template)
    {
        super(parent);
        this.setContentView(R.layout.dialog_status_edit);

        this.template = template;

        /* Load all children */
        this.name = this.findViewById(R.id.dialog_name);
        this.image = new ImageSelect(parent, this.findViewById(R.id.dialog_image), 256, 256);

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

        /* Template data */
        String name = this.template.getName();
        this.name.setText(name);

        DatabaseBitmap image = this.template.getImage();
        this.image.setDatabaseBitmap(image);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.submit))
        {
            DatabaseBitmap image = this.image.getDatabaseBitmap();

            /* Image must be set! */
            if (image != null)
            {
                String name = this.name.getText().toString();
                this.template.setName(name);

                this.template.setImage(image);

                this.onSubmitListener.onSubmit(this.template);
            }

            else
            {
                return;
            }
        }

        this.dismiss();
    }

    public StatusTemplate getTemplate()
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
        void onSubmit(StatusTemplate template);
    }
}
