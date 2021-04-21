package com.deloladrin.cows.data;

import android.content.ContentValues;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.SelectValues;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.TableEntry;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class Diagnosis implements TableEntry
{
    private Database database;

    private long id;
    private long treatment;
    private int template;
    private int target;
    private int state;
    private String comment;

    public Diagnosis(Database database)
    {
        this.database = database;
    }

    public Diagnosis(Database database, long id, long treatment, int template, int target, int state, String comment)
    {
        this.database = database;

        this.id = id;
        this.treatment = treatment;
        this.template = template;
        this.target = target;
        this.state = state;
        this.comment = comment;
    }

    public Diagnosis(Database database, long id, Treatment treatment, DiagnosisTemplate template, TargetMask target, DiagnosisState state, String comment)
    {
        this.database = database;

        this.setID(id);
        this.setTreatment(treatment);
        this.setTemplate(template);
        this.setTarget(target);
        this.setState(state);
        this.setComment(comment);
    }

    public static Diagnosis select(Database database, long id)
    {
        SelectValues values = new SelectValues()
                .where(Table.COLUMN_ID, id);

        return database.getDiagnosisTable().select(values);
    }

    public static List<Diagnosis> selectAll(Database database)
    {
        SelectValues values = new SelectValues();
        return database.getDiagnosisTable().selectAll(values);
    }

    public void insert()
    {
        this.id = this.database.getDiagnosisTable().insert(this);
    }

    public void update()
    {
        this.database.getDiagnosisTable().update(this);
    }

    public void delete()
    {
        this.database.getDiagnosisTable().delete(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof Diagnosis))
            return false;

        return ((Diagnosis)obj).id == this.id;
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

    public DiagnosisTemplate getTemplate()
    {
        return DiagnosisTemplate.select(this.database, this.template);
    }

    public void setTemplate(DiagnosisTemplate template)
    {
        this.template = template.getID();
    }

    public void setTemplate(int template)
    {
        this.template = template;
    }

    public TargetMask getTarget()
    {
        return TargetMask.parse(target);
    }

    public void setTarget(TargetMask target)
    {
        this.target = target.getMask();
    }

    public void setTarget(int target)
    {
        this.target = target;
    }

    public DiagnosisState getState()
    {
        return DiagnosisState.parse(this.state);
    }

    public void setState(DiagnosisState state)
    {
        this.state = state.getID();
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public String getComment()
    {
        return this.comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getName()
    {
        DiagnosisTemplate template = this.getTemplate();

        if (template != null)
        {
            switch (this.getState())
            {
                case NEW:
                    return template.getNewName();
                case TREATED:
                    return template.getTreatedName();
                case HEALED:
                    return template.getHealedName();
            }
        }

        return null;
    }

    public String getShortName()
    {
        DiagnosisTemplate template = this.getTemplate();

        if (template != null)
        {
            return template.getShortName();
        }

        return null;
    }

    public static class Table extends TableBase<Diagnosis>
    {
        public static final String TABLE_NAME = "diagnoses";

        public static final TableColumn COLUMN_ID = new TableColumn("id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_TREATMENT = new TableColumn("treatment", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TEMPLATE = new TableColumn("template", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TARGET = new TableColumn("target", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_STATE = new TableColumn("state", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COMMENT = new TableColumn("comment", ValueType.TEXT, true);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_TREATMENT);
            this.columns.add(COLUMN_TEMPLATE);
            this.columns.add(COLUMN_TARGET);
            this.columns.add(COLUMN_STATE);
            this.columns.add(COLUMN_COMMENT);
        }

        @Override
        protected ContentValues getValues(Diagnosis object)
        {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID.getName(), object.id);
            values.put(COLUMN_TREATMENT.getName(), object.treatment);
            values.put(COLUMN_TEMPLATE.getName(), object.template);
            values.put(COLUMN_TARGET.getName(), object.target);
            values.put(COLUMN_STATE.getName(), object.state);
            values.put(COLUMN_COMMENT.getName(), object.comment);

            return values;
        }

        @Override
        protected Diagnosis getObject(ContentValues values)
        {
            long id = values.getAsLong(COLUMN_ID.getName());
            long treatment = values.getAsLong(COLUMN_TREATMENT.getName());
            int template = values.getAsInteger(COLUMN_TEMPLATE.getName());
            int target = values.getAsInteger(COLUMN_TARGET.getName());
            int state = values.getAsInteger(COLUMN_STATE.getName());
            String comment = values.getAsString(COLUMN_COMMENT.getName());

            return new Diagnosis(this.database, id, treatment, template, target, state, comment);
        }
    }
}
