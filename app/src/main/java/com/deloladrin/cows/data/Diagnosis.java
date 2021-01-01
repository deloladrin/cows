package com.deloladrin.cows.data;

import android.database.Cursor;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class Diagnosis
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

    public static Diagnosis get(Database database, long id)
    {
        return database.getDiagnosesTable().select(id);
    }

    public static List<Diagnosis> getAll(Database database)
    {
        return database.getDiagnosesTable().selectAll();
    }

    public String getName()
    {
        DiagnosisTemplate template = this.getTemplate();

        switch (this.getState())
        {
            case HEALED: return template.getHealedName();
            case TREATED: return template.getTreatedName();
            case NEW: return template.getNewName();
        }

        return null;
    }

    public String getShortName()
    {
        return this.getTemplate().getShortName();
    }

    public void insert()
    {
        this.id = this.database.getDiagnosesTable().insert(this);
    }

    public void update()
    {
        this.database.getDiagnosesTable().update(this);
    }

    public void delete()
    {
        this.database.getDiagnosesTable().delete(this);
    }

    public void refresh()
    {
        Diagnosis refreshed = this.database.getDiagnosesTable().select(this.id);

        this.treatment = refreshed.treatment;
        this.template = refreshed.template;
        this.target = refreshed.target;
        this.state = refreshed.state;
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

    public DiagnosisTemplate getTemplate()
    {
        return this.database.getDiagnosesTemplateTable().select(this.template);
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

    public static class Table extends TableBase<Diagnosis>
    {
        public static final String TABLE_NAME = "diagnoses";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_TREATMENT = new TableColumn(1, "treatment", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TEMPLATE = new TableColumn(2, "template", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TARGET = new TableColumn(3, "target", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_STATE = new TableColumn(4, "state", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COMMENT = new TableColumn(5, "comment", ValueType.TEXT, true);

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
        protected ValueParams getParams(Diagnosis object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_TREATMENT, object.treatment);
            params.put(COLUMN_TEMPLATE, object.template);
            params.put(COLUMN_TARGET, object.target);
            params.put(COLUMN_STATE, object.state);
            params.put(COLUMN_COMMENT, object.comment);

            return params;
        }

        @Override
        protected Diagnosis getObject(Cursor cursor)
        {
            long id = cursor.getLong(COLUMN_ID.getID());
            long treatment = cursor.getLong(COLUMN_TREATMENT.getID());
            int template = cursor.getInt(COLUMN_TEMPLATE.getID());
            int target = cursor.getInt(COLUMN_TARGET.getID());
            int state = cursor.getInt(COLUMN_STATE.getID());
            String comment = cursor.getString(COLUMN_COMMENT.getID());

            return new Diagnosis(this.database, id, treatment, template, target, state, comment);
        }
    }
}
