package com.deloladrin.cows.activities.cow;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.database.DatabaseActivity;
import com.deloladrin.cows.database.DatabaseBitmap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CowActivity extends DatabaseActivity
{
    public static final String EXTRA_COW_ID = "com.deloladrin.cows.activities.cow.CowActivity.EXTRA_COW_ID";
    public static final String EXTRA_USER_NAME = "com.deloladrin.cows.activities.cow.CowActivity.EXTRA_USER_NAME";

    private Cow cow;
    private String user;

    private CowHeader header;
    private CowTreatmentEditor editor;
    private CowTreatmentHistory history;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cow);

        /* Load all children */
        this.header = new CowHeader(this, R.id.cow_header);
        this.editor = new CowTreatmentEditor(this, R.id.cow_treatment_editor);
        this.history = new CowTreatmentHistory(this, R.id.cow_treatment_history);

        this.database.onUpgrade(this.database.getWritableDatabase(), 0, 0);
        Company company = new Company(this.database, 1, "AGRAS BOHDALOV", null);
        company.insert();

        Cow cow = new Cow(this.database, 582344, 86, company, null);
        cow.insert();

        List<Resource> cowResources = new ArrayList<>();
        cowResources.add(this.database.getResourceTable().select(10));
        cowResources.add(this.database.getResourceTable().select(11));
        Treatment treatment = new Treatment(this.database, 1, cow, TreatmentType.WHOLE, LocalDateTime.of(2020, 12, 2, 8, 45), null, "Bohous", cowResources);
        treatment.insert();

        List<Resource> resources = new ArrayList<>();
        resources.add(this.database.getResourceTable().select(4));

        new Diagnosis(this.database, -1, treatment, "Hnisavě volná stěna", "Rozléčená H.v. stěna - opak. ošetření", "Vyhojená H.v. stěna - doléčení", "HVS", DiagnosisState.NEW, 0b00001000, resources).insert();
        new Diagnosis(this.database, -1, treatment, "Rusterholzův vřed III. st.", "Rozléčený R.V. - opak. ošetření", "Vyhoený R.V. - doléčení", "RV3", DiagnosisState.NEW, 0b00000010, resources).insert();

        /* Load requested cow */
        Intent intent = this.getIntent();

        int cowID = intent.getIntExtra(EXTRA_COW_ID, 582344 /* TODO */);
        String user = "Bohous";// intent.getStringExtra(EXTRA_USER_NAME);

        this.user = user;
        this.setCow(this.database.getCowTable().select(cowID));
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

    public String getUser()
    {
        return this.user;
    }

    public CowHeader getHeader()
    {
        return this.header;
    }

    public CowTreatmentEditor getEditor()
    {
        return this.editor;
    }

    public CowTreatmentHistory getHistory()
    {
        return this.history;
    }
}
