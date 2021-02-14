package com.deloladrin.cows.activities.cow;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.database.DatabaseActivity;

import java.util.List;

public class CowActivity extends DatabaseActivity
{
    public static final String EXTRA_COW_ID = "com.deloladrin.cows.activities.cow.CowActivity.EXTRA_COW_ID";
    public static final String EXTRA_USER_NAME = "com.deloladrin.cows.activities.cow.CowActivity.EXTRA_USER_NAME";

    private Cow cow;

    private CowHeader header;
    private TreatmentEditor editor;
    private TreatmentHistory history;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cow);

        /* Load all children */
        this.header = new CowHeader(this, R.id.cow_header);
        this.editor = new TreatmentEditor(this, R.id.cow_treatment_editor);
        this.history = new TreatmentHistory(this, R.id.cow_treatment_history);

        /* Load requested cow */
        Intent intent = this.getIntent();

        int cowID = intent.getIntExtra(EXTRA_COW_ID, 0);
        String user = intent.getStringExtra(EXTRA_USER_NAME);

        this.setCow(Cow.select(this.database, cowID));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        /* Allow EditText saving after clicking outside */
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            View view = this.getCurrentFocus();

            if (view instanceof EditText)
            {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);

                if (!rect.contains((int)event.getRawX(), (int)event.getRawY()))
                {
                    view.clearFocus();

                    /* Close the keyboard */
                    InputMethodManager manager = (InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    public void refreshFull()
    {
        /* Reload the same cow */
        this.setCow(this.cow);
    }

    public void refresh()
    {
        /* Reload the same cow with the same treatment */
        Treatment current = this.editor.getTreatment();
        this.setCow(this.cow, current);
    }

    public Cow getCow()
    {
        return this.cow;
    }

    public void setCow(Cow cow)
    {
        this.cow = cow;
        this.header.setCow(cow);

        if (cow != null)
        {
            List<Treatment> treatments = cow.getTreatments();
            this.history.setTreatments(treatments);

            if (treatments.size() > 0)
            {
                Treatment treatment = treatments.get(treatments.size() - 1);
                this.editor.setTreatment(treatment);
            }
            else
            {
                this.editor.setTreatment(null);
            }
        }
        else
        {
            this.history.setTreatments(null);
            this.editor.setTreatment(null);
        }
    }

    public void setCow(Cow cow, Treatment treatment)
    {
        this.cow = cow;
        this.header.setCow(cow);

        if (cow != null)
        {
            List<Treatment> treatments = cow.getTreatments();
            this.history.setTreatments(treatments);
            this.editor.setTreatment(treatment);
        }
        else
        {
            this.history.setTreatments(null);
            this.editor.setTreatment(null);
        }
    }

    public CowHeader getHeader()
    {
        return this.header;
    }

    public TreatmentEditor getEditor()
    {
        return this.editor;
    }

    public TreatmentHistory getHistory()
    {
        return this.history;
    }
}
