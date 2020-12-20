package com.deloladrin.cows.data;

import android.database.Cursor;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

public class Resource
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
        return this.database.getTreatmentTable().select(this.treatment);
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
        return this.database.getResourceTemplateTable().select(this.template);
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

    public static class Table extends TableBase<Resource>
    {
        public static final String TABLE_NAME = "resources";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_TREATMENT = new TableColumn(1, "treatment", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TEMPLATE = new TableColumn(2, "template", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TARGET = new TableColumn(3, "target", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COPY = new TableColumn(4, "copy", ValueType.INTEGER, false);

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
        protected ValueParams getParams(Resource object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_TREATMENT, object.treatment);
            params.put(COLUMN_TEMPLATE, object.template);
            params.put(COLUMN_TARGET, object.target);
            params.put(COLUMN_COPY, object.copy);

            return params;
        }

        @Override
        protected Resource getObject(Cursor cursor)
        {
            long id = cursor.getLong(COLUMN_ID.getID());
            long treatment = cursor.getLong(COLUMN_TREATMENT.getID());
            int template = cursor.getInt(COLUMN_TEMPLATE.getID());
            int target = cursor.getInt(COLUMN_TARGET.getID());
            int copy = cursor.getInt(COLUMN_COPY.getID());

            return new Resource(this.database, id, treatment, template, target, copy);
        }
    }
}
