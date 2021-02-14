package com.deloladrin.cows.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisTemplate;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.Status;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.data.Treatment;

public class Database extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "Cows.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;

    private Company.Table companyTable;
    private Cow.Table cowTable;
    private Treatment.Table treatmentTable;
    private DiagnosisTemplate.Table diagnosisTemplateTable;
    private Diagnosis.Table diagnosisTable;
    private ResourceTemplate.Table resourceTemplateTable;
    private Resource.Table resourceTable;
    private StatusTemplate.Table statusTemplateTable;
    private Status.Table statusTable;

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        this.companyTable = new Company.Table(this);
        this.cowTable = new Cow.Table(this);
        this.treatmentTable = new Treatment.Table(this);
        this.diagnosisTemplateTable = new DiagnosisTemplate.Table(this);
        this.diagnosisTable = new Diagnosis.Table(this);
        this.resourceTemplateTable = new ResourceTemplate.Table(this);
        this.resourceTable = new Resource.Table(this);
        this.statusTemplateTable = new StatusTemplate.Table(this);
        this.statusTable = new Status.Table(this);

        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        this.companyTable.create(db);
        this.cowTable.create(db);
        this.treatmentTable.create(db);
        this.diagnosisTemplateTable.create(db);
        this.diagnosisTable.create(db);
        this.resourceTemplateTable.create(db);
        this.resourceTable.create(db);
        this.statusTemplateTable.create(db);
        this.statusTable.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        this.companyTable.drop(db);
        this.cowTable.drop(db);
        this.treatmentTable.drop(db);
        this.diagnosisTemplateTable.drop(db);
        this.diagnosisTable.drop(db);
        this.resourceTemplateTable.drop(db);
        this.resourceTable.drop(db);
        this.statusTemplateTable.drop(db);
        this.statusTable.drop(db);

        this.onCreate(db);
    }

    public Context getContext()
    {
        return this.context;
    }

    public Company.Table getCompanyTable()
    {
        return this.companyTable;
    }

    public Cow.Table getCowTable()
    {
        return this.cowTable;
    }

    public Treatment.Table getTreatmentTable()
    {
        return this.treatmentTable;
    }

    public DiagnosisTemplate.Table getDiagnosisTemplateTable()
    {
        return this.diagnosisTemplateTable;
    }

    public Diagnosis.Table getDiagnosisTable()
    {
        return this.diagnosisTable;
    }

    public ResourceTemplate.Table getResourceTemplateTable()
    {
        return this.resourceTemplateTable;
    }

    public Resource.Table getResourceTable()
    {
        return this.resourceTable;
    }

    public StatusTemplate.Table getStatusTemplateTable()
    {
        return this.statusTemplateTable;
    }

    public Status.Table getStatusTable()
    {
        return this.statusTable;
    }
}
