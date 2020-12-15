package com.deloladrin.cows.data;

import android.database.Cursor;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.io.StringReader;
import java.lang.annotation.Target;
import java.util.List;

public class Diagnosis
{
    private Database database;

    private int id;
    private int treatment;
    private String nameHealed;
    private String nameTreated;
    private String nameNew;
    private String nameShort;
    private int state;
    private int target;
    private int resources;

    public Diagnosis(Database database)
    {
        this.database = database;
    }

    public Diagnosis(Database database, int id, int treatment, String nameHealed, String nameTreated, String nameNew, String nameShort, int state, int target, int resources)
    {
        this.database = database;

        this.id = id;
        this.treatment = treatment;
        this.nameHealed = nameHealed;
        this.nameTreated = nameTreated;
        this.nameNew = nameNew;
        this.nameShort = nameShort;
        this.state = state;
        this.target = target;
        this.resources = resources;
    }

    public Diagnosis(Database database, int id, Treatment treatment, String nameHealed, String nameTreated, String nameNew, String nameShort, DiagnosisState state, int target, List<Resource> resources)
    {
        this.database = database;

        this.id = id;
        this.treatment = treatment.getID();
        this.nameHealed = nameHealed;
        this.nameTreated = nameTreated;
        this.nameNew = nameNew;
        this.nameShort = nameShort;
        this.state = state.getID();
        this.target = target;
        this.resources = Resource.valueOf(resources);
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

    public String getName()
    {
        switch (this.getState())
        {
            case HEALED: return this.nameHealed;
            case TREATED: return this.nameTreated;
            case NEW: return this.nameNew;
        }

        return null;
    }

    public String getHealedName()
    {
        return this.nameHealed;
    }

    public void setHealedName(String name)
    {
        this.nameHealed = name;
    }

    public String getTreatedName()
    {
        return this.nameTreated;
    }

    public void setTreatedName(String name)
    {
        this.nameTreated = name;
    }

    public String getNewName()
    {
        return this.nameNew;
    }

    public void setNewName(String name)
    {
        this.nameNew = name;
    }

    public String getShortName()
    {
        return this.nameShort;
    }

    public void setShortName(String name)
    {
        this.nameShort = name;
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

    public List<Resource> getResources()
    {
        return Resource.parse(this.database, this.resources);
    }

    public void setResources(List<Resource> resources)
    {
        this.resources = Resource.valueOf(resources);
    }

    public void setResources(int resources)
    {
        this.resources = resources;
    }

    public static class Table extends TableBase<Diagnosis>
    {
        public static final String TABLE_NAME = "diagnoses";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_TREATMENT = new TableColumn(1, "treatment", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_NAME_NEW = new TableColumn(2, "name_new", ValueType.TEXT, false);
        public static final TableColumn COLUMN_NAME_HEALED = new TableColumn(3, "name_healed", ValueType.TEXT, false);
        public static final TableColumn COLUMN_NAME_TREATED = new TableColumn(4, "name_treated", ValueType.TEXT, false);
        public static final TableColumn COLUMN_NAME_SHORT = new TableColumn(5, "name_short", ValueType.TEXT, false);
        public static final TableColumn COLUMN_STATE = new TableColumn(6, "state", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TARGET = new TableColumn(7, "target", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_RESOURCES = new TableColumn(8, "resources", ValueType.INTEGER, true);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_TREATMENT);
            this.columns.add(COLUMN_NAME_NEW);
            this.columns.add(COLUMN_NAME_HEALED);
            this.columns.add(COLUMN_NAME_TREATED);
            this.columns.add(COLUMN_NAME_SHORT);
            this.columns.add(COLUMN_STATE);
            this.columns.add(COLUMN_TARGET);
            this.columns.add(COLUMN_RESOURCES);
        }

        @Override
        protected ValueParams getParams(Diagnosis object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_TREATMENT, object.treatment);
            params.put(COLUMN_NAME_NEW, object.nameNew);
            params.put(COLUMN_NAME_HEALED, object.nameHealed);
            params.put(COLUMN_NAME_TREATED, object.nameTreated);
            params.put(COLUMN_NAME_SHORT, object.nameShort);
            params.put(COLUMN_STATE, object.state);
            params.put(COLUMN_TARGET, object.target);
            params.put(COLUMN_RESOURCES, object.resources);

            return params;
        }

        @Override
        protected Diagnosis getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            int treatment = cursor.getInt(COLUMN_TREATMENT.getID());
            String nameNew = cursor.getString(COLUMN_NAME_NEW.getID());
            String nameHealed = cursor.getString(COLUMN_NAME_HEALED.getID());
            String nameTreated = cursor.getString(COLUMN_NAME_TREATED.getID());
            String nameShort = cursor.getString(COLUMN_NAME_SHORT.getID());
            int state = cursor.getInt(COLUMN_STATE.getID());
            int target = cursor.getInt(COLUMN_TARGET.getID());
            int resources = cursor.getInt(COLUMN_RESOURCES.getID());

            return new Diagnosis(this.database, id, treatment, nameNew, nameHealed, nameTreated, nameShort, state, target, resources);
        }
    }
}
