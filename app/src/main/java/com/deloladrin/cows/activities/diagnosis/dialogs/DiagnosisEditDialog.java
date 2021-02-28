package com.deloladrin.cows.activities.diagnosis.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.diagnosis.DiagnosisActivity;
import com.deloladrin.cows.data.DiagnosisTemplate;
import com.deloladrin.cows.data.DiagnosisType;
import com.deloladrin.cows.views.ImageSelect;

public class DiagnosisEditDialog extends ChildDialog<DiagnosisActivity> implements View.OnClickListener
{
    private DiagnosisTemplate template;

    private EditText nameNew;
    private EditText nameTreated;
    private EditText nameHealed;
    private EditText nameShort;
    private Spinner type;

    private Button cancel;
    private Button submit;

    private OnSubmitListener onSubmitListener;

    public DiagnosisEditDialog(DiagnosisActivity parent, DiagnosisTemplate template)
    {
        super(parent);
        this.setContentView(R.layout.dialog_diagnosis_edit);

        this.template = template;

        /* Load all children */
        this.nameNew = this.findViewById(R.id.dialog_name_new);
        this.nameTreated = this.findViewById(R.id.dialog_name_treated);
        this.nameHealed = this.findViewById(R.id.dialog_name_healed);
        this.nameShort = this.findViewById(R.id.dialog_name_short);
        this.type = this.findViewById(R.id.dialog_type);

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
        DiagnosisTypeAdapter adapter = new DiagnosisTypeAdapter(context);
        this.type.setAdapter(adapter);

        /* Load current template data */
        this.nameNew.setText(this.template.getNewName());
        this.nameTreated.setText(this.template.getTreatedName());
        this.nameHealed.setText(this.template.getHealedName());
        this.nameShort.setText(this.template.getShortName());

        DiagnosisType type = this.template.getType();
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
            String nameNew = this.nameNew.getText().toString();
            this.template.setNewName(nameNew);

            String nameTreated = this.nameTreated.getText().toString();
            this.template.setTreatedName(nameTreated);

            String nameHealed = this.nameHealed.getText().toString();
            this.template.setHealedName(nameHealed);

            String nameShort = this.nameShort.getText().toString();
            this.template.setShortName(nameShort);

            DiagnosisType type = (DiagnosisType) this.type.getSelectedItem();
            this.template.setType(type);

            this.onSubmitListener.onSubmit(this.template);
        }

        this.dismiss();
    }

    public DiagnosisTemplate getTemplate()
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
        void onSubmit(DiagnosisTemplate template);
    }
}
