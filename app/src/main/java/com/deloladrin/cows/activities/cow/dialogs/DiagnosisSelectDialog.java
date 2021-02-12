package com.deloladrin.cows.activities.cow.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.deloladrin.cows.dialogs.SelectDialog;

public class DiagnosisSelectDialog extends ChildDialog<CowActivity> implements View.OnClickListener
{
    private Treatment treatment;
    private Diagnosis diagnosis;
    private FingerMask mask;

    private TextView text;
    private LinearLayout container;

    private EditText comment;

    private Button cancel;
    private Button submit;

    private int selected;

    private OnSelectListener onSelectListener;

    public DiagnosisSelectDialog(ChildActivity<CowActivity> parent, Treatment treatment, Diagnosis diagnosis, FingerMask mask)
    {
        super(parent);
        super.setContentView(R.layout.dialog_cow_diagnosis_select);

        this.treatment = treatment;
        this.diagnosis = diagnosis;
        this.mask = mask;

        /* Load all children */
        this.text = this.findViewById(R.id.dialog_text);
        this.container = this.findViewById(R.id.dialog_container);

        this.comment = this.findViewById(R.id.dialog_comment);

        this.cancel = this.findViewById(R.id.dialog_cancel);
        this.submit = this.findViewById(R.id.dialog_submit);

        this.setSelected(-1);

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
        this.setText(R.string.dialog_finger_select, fingerName);

        /* Load all templates */
        DiagnosisTemplate current = null;

        if (this.diagnosis != null)
        {
            current = this.diagnosis.getTemplate();

            /* Set comment */
            this.comment.setText(this.diagnosis.getComment());
        }

        for (DiagnosisTemplate template : DiagnosisTemplate.getAll(this.getDatabase()))
        {
            boolean select = false;

            if (current != null && template.equals(current))
            {
                select = true;
            }

            this.add(template, select);
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.submit))
        {
            if (this.selected != -1)
            {
                DiagnosisTemplateEntry child = (DiagnosisTemplateEntry) this.container.getChildAt(this.selected);
                this.select(child);

                this.dismiss();
            }

            return;
        }

        if (view.equals(this.cancel))
        {
            this.dismiss();
            return;
        }

        /* Select new item */
        int index = this.container.indexOfChild(view);

        if (index != this.selected)
        {
            this.setSelected(index);
        }
        else
        {
            /* Submit on double click */
            this.select((DiagnosisTemplateEntry)view);
            this.dismiss();
        }
    }

    private void select(DiagnosisTemplateEntry entry)
    {
        DiagnosisTemplate template = entry.getValue();
        boolean create;

        if (this.diagnosis == null)
        {
            /* Create new diagnosis */
            this.diagnosis = new Diagnosis(this.getDatabase());
            this.diagnosis.setTreatment(this.treatment);
            this.diagnosis.setState(DiagnosisState.NEW);

            /* Use hoof target for hoof diagnosis */
            if (template.getType() == DiagnosisType.HOOF)
            {
                this.diagnosis.setTarget(this.mask.getHoof());
            }
            else
            {
                this.diagnosis.setTarget(this.mask);
            }

            create = true;
        }
        else
        {
            /* Update current diagnosis */
            create = false;
        }

        /* Set template and comment */
        this.diagnosis.setTemplate(template);

        String comment = this.comment.getText().toString();

        if (comment.isEmpty())
        {
            this.diagnosis.setComment(null);
        }
        else
        {
            this.diagnosis.setComment(comment);
        }

        /* Show resource editor */
        ResourceEditDialog dialog = new ResourceEditDialog(this.parent, this.treatment, this.mask);

        dialog.setOnSubmitListener((ResourceEditDialog d) ->
        {
            /* Create or update */
            if (create)
            {
                this.diagnosis.insert();
            }
            else
            {
                this.diagnosis.update();
            }

            this.onSelectListener.onSelect(entry);
            this.dismiss();
        });

        dialog.show();
    }

    public void add(DiagnosisTemplate template, boolean select)
    {
        /* Make entry clickable and add */
        DiagnosisTemplateEntry entry = new DiagnosisTemplateEntry(this.getContext(), template);
        entry.setClickable(true);
        entry.setOnClickListener(this);

        this.container.addView(entry);

        /* Select if requested */
        if (select)
        {
            int index = this.container.getChildCount() - 1;
            this.setSelected(index);
        }
    }

    public void clear()
    {
        /* Delete all children */
        this.container.removeAllViews();
        this.selected = -1;
    }

    public Treatment getTreatment()
    {
        return this.treatment;
    }

    public Diagnosis getDiagnosis()
    {
        return this.diagnosis;
    }

    public FingerMask getMask()
    {
        return this.mask;
    }

    public String getText()
    {
        return this.text.getText().toString();
    }

    public void setText(String text)
    {
        this.text.setText(text);
    }

    public void setText(int text)
    {
        this.setText(this.getContext().getResources().getString(text));
    }

    public void setText(int text, Object... args)
    {
        String format = this.getContext().getResources().getString(text);
        this.setText(String.format(format, args));
    }

    public LinearLayout getContainer()
    {
        return this.container;
    }

    public int getSelected()
    {
        return this.selected;
    }

    public void setSelected(int selected)
    {
        this.selected = selected;

        for (int i = 0; i < this.container.getChildCount(); i++)
        {
            View child = this.container.getChildAt(i);
            int colorID = SelectDialog.DEFAULT_BACKGROUND;

            if (selected == i)
            {
                colorID = SelectDialog.SELECTED_BACKGROUND;
            }

            child.setBackgroundResource(colorID);
        }
    }

    public Button getCancelButton()
    {
        return this.cancel;
    }

    public String getCancelText()
    {
        return this.cancel.getText().toString();
    }

    public void setCancelText(String text)
    {
        this.cancel.setText(text);
    }

    public void setCancelText(int text)
    {
        this.setCancelText(this.getContext().getResources().getString(text));
    }

    public OnSelectListener getOnSelectListener()
    {
        return this.onSelectListener;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener)
    {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener
    {
        void onSelect(DiagnosisTemplateEntry view);
    }
}
