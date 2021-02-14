package com.deloladrin.cows.data;

import android.content.ContentValues;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.SelectValues;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.TableEntry;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class Resource implements TableEntry
{
    private Database database;

    private long id;
    private long treatment;
    private int template;
    private int target;
    private int copy;

    public Resource(Database database)
    {
        this.database = database;
    }

    public Resource(Database database, long id, long treatment, int template, int target, int copy)
    {
        this.database = database;

        this.id = id;
        this.treatment = treatment;
        this.template = template;
        this.target = target;
        this.copy = copy;
    }

    public Resource(Database database, long id, Treatment treatment, ResourceTemplate template, TargetMask target, boolean copy)
    {
        this.database = database;

        this.setID(id);
        this.setTreatment(treatment);
        this.setTemplate(template);
        this.setTarget(target);
        this.setCopy(copy);
    }

    public static Resource select(Database database, int id)
    {
        SelectValues values = new SelectValues()
                .where(Table.COLUMN_ID, id);

        return database.getResourceTable().select(values);
    }

    public static List<Resource> selectAll(Database database)
    {
        SelectValues values = new SelectValues();
        return database.getResourceTable().selectAll(values);
    }

    public void insert()
    {
        this.id = this.database.getResourceTable().insert(this);
    }

    public void update()
    {
        this.database.getResourceTable().update(this);
    }

    public void delete()
    {
        this.database.getResourceTable().delete(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof Resource))
            return false;

        return ((Resource)obj).id == this.id;
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

    public ResourceTemplate getTemplate()
    {
        return ResourceTemplate.select(this.database, this.template);
    }

    public void setTemplate(ResourceTemplate template)
    {
        this.template = template.getID();
    }

    public void setTemplate(int template)
    {
        this.template = template;
    }

    public TargetMask getTarget()
    {
        return TargetMask.parse(this.target);
    }

    public void setTarget(TargetMask target)
    {
        this.target = target.getMask();
    }

    public void setTarget(int target)
    {
        this.target = target;
    }

    public boolean isCopy()
    {
        return this.copy > 0;
    }

    public void setCopy(boolean copy)
    {
        this.copy = copy ? 1 : 0;
    }

    public void setCopy(int copy)
    {
        this.copy = copy;
    }

    public String getName()
    {
        return this.getTemplate().getName();
    }

    public static class Table extends TableBase<Resource>
    {
        public static final String TABLE_NAME = "resources";

        public static final TableColumn COLUMN_ID = new TableColumn("id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_TREATMENT = new TableColumn("treatment", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TEMPLATE = new TableColumn("template", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TARGET = new TableColumn("target", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COPY = new TableColumn("copy", ValueType.INTEGER, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_TREATMENT);
            this.columns.add(COLUMN_TEMPLATE);
            this.columns.add(COLUMN_TARGET);
            this.columns.add(COLUMN_COPY);
        }

        @Override
        protected ContentValues getValues(Resource object)
        {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID.getName(), object.id);
            values.put(COLUMN_TREATMENT.getName(), object.treatment);
            values.put(COLUMN_TEMPLATE.getName(), object.template);
            values.put(COLUMN_TARGET.getName(), object.target);
            values.put(COLUMN_COPY.getName(), object.copy);

            return values;
        }

        @Override
        protected Resource getObject(ContentValues values)
        {
            long id = values.getAsLong(COLUMN_ID.getName());
            long treatment = values.getAsLong(COLUMN_TREATMENT.getName());
            int template = values.getAsInteger(COLUMN_TEMPLATE.getName());
            int target = values.getAsInteger(COLUMN_TARGET.getName());
            int copy = values.getAsInteger(COLUMN_COPY.getName());

            return new Resource(this.database, id, treatment, template, target, copy);
        }
    }
}
