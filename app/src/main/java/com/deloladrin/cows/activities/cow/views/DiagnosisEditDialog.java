package com.deloladrin.cows.activities.cow.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.FingerMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.data.TargetMask;

import java.util.ArrayList;
import java.util.List;

public class DiagnosisEditDialog extends ChildDialog<CowActivity> implements View.OnClickListener
{
    private Diagnosis diagnosis;
    private TargetMask mask;

    private TextView finger;
    private TextView name;

    private Button cancel;
    private Button healedType;
    private Button treatedType;
    private Button newType;

    private OnSubmitListener onSubmitListener;

    public DiagnosisEditDialog(ChildActivity<CowActivity> parent, Diagnosis diagnosis)
    {
        super(parent);
        this.setContentView(R.layout.dialog_cow_diagnosis_edit);

        this.diagnosis = diagnosis;
        this.mask = diagnosis.getTarget();

        /* Load all children */
        this.finger = this.findViewById(R.id.dialog_finger);
        this.name = this.findViewById(R.id.dialog_name);

        this.cancel = this.findViewById(R.id.dialog_cancel);
        this.healedType = this.findViewById(R.id.dialog_healed);
        this.treatedType = this.findViewById(R.id.dialog_treated);
        this.newType = this.findViewById(R.id.dialog_new);

        /* Add events */
        this.cancel.setOnClickListener(this);
        this.healedType.setOnClickListener(this);
        this.treatedType.setOnClickListener(this);
        this.newType.setOnClickListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Context context = this.getContext();

        /* Set finger and diagnosis name */
        String fingerName = this.mask.getName(context);
        this.setFingerName(R.string.dialog_finger_name, fingerName);

        String name = this.diagnosis.getName();
        int color = this.diagnosis.getState().getColor(context);

        this.name.setText(name);
        this.name.setTextColor(color);
    }

    @Override
    public void onClick(View view)
    {
        Context context = this.getContext();

        if (view.equals(this.healedType))
        {
            /* Change to healed and submit */
            this.diagnosis.setState(DiagnosisState.HEALED);

            /* Remove all non-copy resources */
            List<Resource> oldResources = this.diagnosis.getResources();
            List<Resource> newResources = new ArrayList<>();

            for (Resource resource : oldResources)
            {
                if (resource.getType() == ResourceType.COPY)
                {
                    newResources.add(resource);
                }
            }

            this.diagnosis.setResources(newResources);
            this.diagnosis.update();

            this.onSubmitListener.onSubmit(this);
        }

        if (view.equals(this.treatedType))
        {
            /* Show resources edit dialog */
            ResourcesEditDialog dialog = new ResourcesEditDialog(this.parent, this.diagnosis);

            dialog.setOnSubmitListener((ResourcesEditDialog d) ->
            {
                /* Change to treated and submit */
                this.diagnosis.setState(DiagnosisState.TREATED);
                this.diagnosis.update();

                this.onSubmitListener.onSubmit(this);
                this.dismiss();
            });

            dialog.show();
            return;
        }

        if (view.equals(this.newType))
        {
            /* Show resources edit dialog */
            ResourcesEditDialog dialog = new ResourcesEditDialog(this.parent, this.diagnosis);

            dialog.setOnSubmitListener((ResourcesEditDialog d) ->
            {
                /* Change to new and submit */
                this.diagnosis.setState(DiagnosisState.NEW);
                this.diagnosis.update();

                this.onSubmitListener.onSubmit(this);
                this.dismiss();
            });

            dialog.show();
            return;
        }

        this.dismiss();
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
        void onSubmit(DiagnosisEditDialog dialog);
    }
}
