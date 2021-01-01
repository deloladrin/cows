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
    private Diagnosis.Table diagnosesTable;
    private DiagnosisTemplate.Table diagnosesTemplateTable;
    private Resource.Table resourceTable;
    private ResourceTemplate.Table resourceTemplateTable;
    private Status.Table statusTable;
    private StatusTemplate.Table statusTemplateTable;

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;

        this.companyTable = new Company.Table(this);
        this.cowTable = new Cow.Table(this);
        this.treatmentTable = new Treatment.Table(this);
        this.diagnosesTable = new Diagnosis.Table(this);
        this.diagnosesTemplateTable = new DiagnosisTemplate.Table(this);
        this.resourceTable = new Resource.Table(this);
        this.resourceTemplateTable = new ResourceTemplate.Table(this);
        this.statusTable = new Status.Table(this);
        this.statusTemplateTable = new StatusTemplate.Table(this);

        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        this.companyTable.create(db);
        this.cowTable.create(db);
        this.treatmentTable.create(db);
        this.diagnosesTable.create(db);
        this.diagnosesTemplateTable.create(db);
        this.resourceTable.create(db);
        this.resourceTemplateTable.create(db);
        this.statusTable.create(db);
        this.statusTemplateTable.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        this.companyTable.drop(db);
        this.cowTable.drop(db);
        this.treatmentTable.drop(db);
        this.diagnosesTable.drop(db);
        this.diagnosesTemplateTable.drop(db);
        this.resourceTable.drop(db);
        this.resourceTemplateTable.drop(db);
        this.statusTable.drop(db);
        this.statusTemplateTable.drop(db);

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

    public Diagnosis.Table getDiagnosesTable()
    {
        return this.diagnosesTable;
    }

    public DiagnosisTemplate.Table getDiagnosesTemplateTable()
    {
        return this.diagnosesTemplateTable;
    }

    public Resource.Table getResourceTable()
    {
        return this.resourceTable;
    }

    public ResourceTemplate.Table getResourceTemplateTable()
    {
        return this.resourceTemplateTable;
    }

    public Status.Table getStatusTable()
    {
        return this.statusTable;
    }

    public StatusTemplate.Table getStatusTemplateTable()
    {
        return this.statusTemplateTable;
    }
}
