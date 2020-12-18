package com.deloladrin.cows.activities.cow.views;

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
import com.deloladrin.cows.data.TargetMask;

public class DiagnosisEditDialog extends ChildDialog<CowActivity> implements View.OnClickListener
{
    private Diagnosis diagnosis;
    private TargetMask mask;

    private TextView finger;
    private TextView name;

    private Button cancel;
    private Button healedState;
    private Button treatedState;
    private Button newState;

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
        this.healedState = this.findViewById(R.id.dialog_healed);
        this.treatedState = this.findViewById(R.id.dialog_treated);
        this.newState = this.findViewById(R.id.dialog_new);

        /* Add events */
        this.cancel.setOnClickListener(this);
        this.healedState.setOnClickListener(this);
        this.treatedState.setOnClickListener(this);
        this.newState.setOnClickListener(this);

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

        if (view.equals(this.healedState))
        {
            this.changeDiagnosisState(DiagnosisState.HEALED);
            return;
        }

        if (view.equals(this.treatedState))
        {
            this.changeDiagnosisState(DiagnosisState.TREATED);
            return;
        }

        if (view.equals(this.newState))
        {
            this.changeDiagnosisState(DiagnosisState.NEW);
            return;
        }

        this.dismiss();
    }

    private void changeDiagnosisState(DiagnosisState state)
    {
        /* Show resources editor */
        ResourceEditDialog dialog = new ResourceEditDialog(this.parent, this.diagnosis);

        dialog.setOnSubmitListener((ResourceEditDialog d) ->
        {
            /* Update and submit */
            this.diagnosis.setState(state);
            this.diagnosis.update();

            this.onSubmitListener.onSubmit(this);
            this.dismiss();
        });

        dialog.show();
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
