package com.deloladrin.cows.data;

import android.content.ContentValues;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.SelectValues;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.TableEntry;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class Status implements TableEntry
{
    private Database database;

    private long id;
    private long treatment;
    private int template;

    public Status(Database database)
    {
        this.database = database;
    }

    public Status(Database database, long id, long treatment, int template)
    {
        this.database = database;

        this.id = id;
        this.treatment = treatment;
        this.template = template;
    }

    public Status(Database database, long id, Treatment treatment, StatusTemplate template)
    {
        this.database = database;

        this.setID(id);
        this.setTreatment(treatment);
        this.setTemplate(template);
    }

    public static Status select(Database database, int id)
    {
        SelectValues values = new SelectValues()
                .where(Table.COLUMN_ID, id);

        return database.getStatusTable().select(values);
    }

    public static List<Status> selectAll(Database database)
    {
        SelectValues values = new SelectValues();
        return database.getStatusTable().selectAll(values);
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
        return Long.hashCode(this.id);
    }

    public long getID()
    {
        return this.id;
    }

    public void setID(long id)
    {
        this.id = id;
    }

    public Treatment getTreatment()
    {
        return Treatment.select(this.database, this.treatment);
    }

    public void setTreatment(Treatment treatment)
    {
        this.treatment = treatment.getID();
    }

    public void setTreatment(long treatment)
    {
        this.treatment = treatment;
    }

    public StatusTemplate getTemplate()
    {
        return StatusTemplate.select(this.database, this.template);
    }

    public void setTemplate(StatusTemplate template)
    {
        this.template = template.getID();
    }

    public void setTemplate(int template)
    {
        this.template = template;
    }

    public String getName()
    {
        return this.getTemplate().getName();
    }

    public static class Table extends TableBase<Status>
    {
        public static final String TABLE_NAME = "statuses";

        public static final TableColumn COLUMN_ID = new TableColumn("id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_TREATMENT = new TableColumn("treatment", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TEMPLATE = new TableColumn("template", ValueType.INTEGER, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_TREATMENT);
            this.columns.add(COLUMN_TEMPLATE);
        }

        @Override
        protected ContentValues getValues(Status object)
        {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID.getName(), object.id);
            values.put(COLUMN_TREATMENT.getName(), object.treatment);
            values.put(COLUMN_TEMPLATE.getName(), object.template);

            return values;
        }

        @Override
        protected Status getObject(ContentValues values)
        {
            long id = values.getAsLong(COLUMN_ID.getName());
            long treatment = values.getAsLong(COLUMN_TREATMENT.getName());
            int template = values.getAsInteger(COLUMN_TEMPLATE.getName());

            return new Status(this.database, id, treatment, template);
        }
    }
}
