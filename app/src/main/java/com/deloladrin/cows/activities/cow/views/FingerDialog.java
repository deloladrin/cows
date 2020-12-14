package com.deloladrin.cows.activities.cow.views;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.FingerMask;
import com.deloladrin.cows.data.Treatment;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FingerDialog extends ChildDialog<CowActivity> implements View.OnClickListener
{
    private Treatment treatment;
    private FingerMask mask;

    private TextView name;
    private FlowLayout container;

    private Button cancel;
    private Button add;

    private OnSubmitListener onSubmitListener;

    public FingerDialog(ChildActivity<CowActivity> parent, Treatment treatment, FingerMask mask)
    {
        super(parent);
        this.setContentView(R.layout.dialog_cow_finger);

        this.treatment = treatment;
        this.mask = mask;

        /* Load all children */
        this.name = this.findViewById(R.id.dialog_name);
        this.container = this.findViewById(R.id.dialog_container);

        this.cancel = this.findViewById(R.id.dialog_cancel);
        this.add = this.findViewById(R.id.dialog_add);

        /* Add events */
        this.cancel.setOnClickListener(this);
        this.add.setOnClickListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Context context = this.getContext();

        /* Set name */
        String name = this.mask.getName(context);
        this.setName(R.string.dialog_finger_name, name);

        for (Diagnosis diagnosis : this.treatment.getDiagnoses())
        {
            /* Load only valid diagnoses */
            int target = diagnosis.getTarget();

            if (target == this.mask.getMask() ||
                target == this.mask.getHoof().getMask())
            {
                this.add(diagnosis);
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        Context context = this.getContext();

        /* Get selected diagnosis */
        if (view instanceof FingerEntry)
        {
            FingerEntry entry = (FingerEntry) view;
            Diagnosis diagnosis = entry.getDiagnosis();

            /* Show edit dialog */
            DiagnosisEditDialog dialog = new DiagnosisEditDialog(this.parent, diagnosis);

            dialog.setOnSubmitListener((DiagnosisEditDialog d) ->
            {
                /* Close on submit */
                this.onSubmitListener.onSubmit(FingerDialog.this);
                this.dismiss();
            });

            dialog.show();

            return;
        }

        /* Add new diagnosis */
        if (view.equals(this.add))
        {


            return;
        }

        this.dismiss();
    }

    public void add(Diagnosis diagnosis)
    {
        /* Create diagnosis button */
        Context context = this.getContext();

        FingerEntry button = new FingerEntry(context);
        button.setBackgroundResource(R.color.button_dark);
        button.setTextColor(diagnosis.getState().getColor(context));

        /* Set text */
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.text_medium));
        button.setText(diagnosis.getShortName());

        int size = context.getResources().getDimensionPixelSize(R.dimen.size_cow_finger_entry);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(size, size);

        int margin = context.getResources().getDimensionPixelOffset(R.dimen.margin_container);
        params.setMargins(margin, margin, margin, margin);

        button.setLayoutParams(params);
        button.setOnClickListener(this);

        button.setDiagnosis(diagnosis);

        this.container.addView(button);
    }

    public String getName()
    {
        return this.name.getText().toString();
    }

    public void setName(String text)
    {
        this.name.setText(text);
    }

    public void setName(int text)
    {
        this.setName(this.getContext().getResources().getString(text));
    }

    public void setName(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setName(String.format(format, args));
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
        void onSubmit(FingerDialog dialog);
    }
}
