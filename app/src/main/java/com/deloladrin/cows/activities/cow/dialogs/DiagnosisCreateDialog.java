package com.deloladrin.cows.activities.cow.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildActivity;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.activities.cow.views.DiagnosisTemplateEntry;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.DiagnosisTemplate;
import com.deloladrin.cows.data.DiagnosisType;
import com.deloladrin.cows.data.FingerMask;
import com.deloladrin.cows.data.Treatment;

public class DiagnosisCreateDialog extends ChildDialog<CowActivity> implements View.OnClickListener
{
    private static final int DIAGNOSIS_ID_TEMPORARY = -1;

    private Treatment treatment;
    private FingerMask mask;

    private TextView finger;
    private LinearLayout container;

    private Button cancel;

    private OnSubmitListener onSubmitListener;

    public DiagnosisCreateDialog(ChildActivity<CowActivity> parent, Treatment treatment, FingerMask mask)
    {
        super(parent);
        super.setContentView(R.layout.dialog_select);

        this.treatment = treatment;
        this.mask = mask;

        /* Load all children */
        this.finger = this.findViewById(R.id.dialog_text);
        this.container = this.findViewById(R.id.dialog_container);

        this.cancel = this.findViewById(R.id.dialog_cancel);

        /* Add events */
        this.cancel.setOnClickListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Context context = this.getContext();

        /* Set finger and diagnosis name */
        String fingerName = this.mask.getName(context);
        this.setFingerName(R.string.dialog_finger_name, fingerName);

        /* Load all templates */
        for (DiagnosisTemplate template : DiagnosisTemplate.getAll(this.getDatabase()))
        {
            this.add(template);
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view instanceof DiagnosisTemplateEntry)
        {
            DiagnosisTemplateEntry entry = (DiagnosisTemplateEntry)view;
            DiagnosisTemplate template = entry.getValue();

            /* Create diagnosis */
            Diagnosis diagnosis = new Diagnosis(this.getDatabase());
            diagnosis.setID(DIAGNOSIS_ID_TEMPORARY);
            diagnosis.setTreatment(this.treatment);
            diagnosis.setTemplate(template);
            diagnosis.setState(DiagnosisState.NEW);

            /* Use hoof target for hoof diagnosis */
            if (template.getType() == DiagnosisType.HOOF)
            {
                diagnosis.setTarget(this.mask.getHoof());
            }
            else
            {
                diagnosis.setTarget(this.mask);
            }

            /* Show resource editor */
            ResourceEditDialog dialog = new ResourceEditDialog(this.parent, this.treatment, this.mask);

            dialog.setOnSubmitListener((ResourceEditDialog d) ->
            {
                /* Insert and submit */
                diagnosis.insert();

                this.onSubmitListener.onSubmit(this);
                this.dismiss();
            });

            dialog.show();
            return;
        }

        this.dismiss();
    }

    public void add(DiagnosisTemplate template)
    {
        /* Create new entry */
        DiagnosisTemplateEntry entry = new DiagnosisTemplateEntry(this.getContext(), template);
        entry.setClickable(true);
        entry.setOnClickListener(this);

        this.container.addView(entry);
    }

    public void clear()
    {
        /* Remove all children */
        this.container.removeAllViews();
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
        void onSubmit(DiagnosisCreateDialog dialog);
    }
}
