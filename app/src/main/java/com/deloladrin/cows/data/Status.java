package com.deloladrin.cows.data;

import android.database.Cursor;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

public class Status
{
    private Database database;

    private int id;
    private int treatment;
    private int template;

    public Status(Database database)
    {
        this.database = database;
    }

    public Status(Database database, int id, int treatment, int template)
    {
        this.database = database;

        this.id = id;
        this.treatment = treatment;
        this.template = template;
    }

    public Status(Database database, int id, Treatment treatment, StatusTemplate template)
    {
        this.database = database;

        this.setID(id);
        this.setTreatment(treatment);
        this.setTemplate(template);
    }

    public void insert()
    {
        this.id = this.database.getStatusTable().insert(this);
    }

    public void update()
    {
        this.database.getStatusTable().update(this);
    }

    public void delete()
    {
        this.database.getStatusTable().delete(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof Status))
            return false;

        return ((Status)obj).id == this.id;
    }

    @Override
    public int hashCode()
    {
        return this.id;
    }

    public int getID()
    {
        return this.id;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public Treatment getTreatment()
    {
        return this.database.getTreatmentTable().select(this.treatment);
    }

    public void setTreatment(Treatment treatment)
    {
        this.treatment = treatment.getID();
    }

    public void setTreatment(int treatment)
    {
        this.treatment = treatment;
    }

    public StatusTemplate getTemplate()
    {
        return this.database.getStatusTemplateTable().select(this.template);
    }

    public void setTemplate(StatusTemplate template)
    {
        this.template = template.getID();
    }

    public void setTemplate(int template)
    {
        this.template = template;
    }

    public static class Table extends TableBase<Status>
    {
        public static final String TABLE_NAME = "statuses";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_TREATMENT = new TableColumn(1, "treatment", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TEMPLATE = new TableColumn(2, "template", ValueType.INTEGER, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_TREATMENT);
            this.columns.add(COLUMN_TEMPLATE);
        }

        @Override
        protected ValueParams getParams(Status object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_TREATMENT, object.treatment);
            params.put(COLUMN_TEMPLATE, object.template);

            return params;
        }

        @Override
        protected Status getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            int treatment = cursor.getInt(COLUMN_TREATMENT.getID());
            int template = cursor.getInt(COLUMN_TEMPLATE.getID());

            return new Status(this.database, id, treatment, template);
        }
    }
}