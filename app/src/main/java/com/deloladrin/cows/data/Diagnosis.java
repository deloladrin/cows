package com.deloladrin.cows.data;

import android.database.Cursor;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.io.StringReader;
import java.util.List;

public class Diagnosis
{
    private Database database;

    private int id;
    private Treatment treatment;
    private String nameNew;
    private String nameHealed;
    private String nameTreated;
    private String nameShort;
    private DiagnosisState state;
    private int target;
    private List<Resource> resources;

    public Diagnosis(Database database, int id, Treatment treatment, String nameNew, String nameHealed, String nameTreated, String nameShort, DiagnosisState state, int target, List<Resource> resources)
    {
        this.database = database;

        this.id = id;
        this.treatment = treatment;
        this.nameNew = nameNew;
        this.nameHealed = nameHealed;
        this.nameTreated = nameTreated;
        this.nameShort = nameShort;
        this.state = state;
        this.target = target;
        this.resources = resources;
    }

    public void insert()
    {
        this.database.getDiagnosesTable().insert(this);
    }

    public void update()
    {
        this.database.getDiagnosesTable().update(this);
    }

    public void delete()
    {
        this.database.getDiagnosesTable().delete(this);
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
        return this.treatment;
    }

    public void setTreatment(Treatment treatment)
    {
        this.treatment = treatment;
    }

    public String getNewName()
    {
        return this.nameNew;
    }

    public void setNewName(String name)
    {
        this.nameNew = name;
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
        return this.state;
    }

    public void setState(DiagnosisState state)
    {
        this.state = state;
    }

    public int getTarget()
    {
        return this.target;
    }

    public void setTarget(int target)
    {
        this.target = target;
    }

    public List<Resource> getResources()
    {
        return this.resources;
    }

    public void setResources(List<Resource> resources)
    {
        this.resources = resources;
    }

    public static class Table extends TableBase<Diagnosis>
    {
        public static final String TABLE_NAME = "diagnosis"; /* TODO */

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

            params.put(COLUMN_ID, object.getID());
            params.put(COLUMN_TREATMENT, object.getTreatment().getID());
            params.put(COLUMN_NAME_NEW, object.getNewName());
            params.put(COLUMN_NAME_HEALED, object.getHealedName());
            params.put(COLUMN_NAME_TREATED, object.getTreatedName());
            params.put(COLUMN_NAME_SHORT, object.getShortName());
            params.put(COLUMN_STATE, object.getState().getID());
            params.put(COLUMN_TARGET, object.getTarget());
            params.put(COLUMN_RESOURCES, Resource.valueOf(object.getResources()));

            return params;
        }

        @Override
        protected Diagnosis getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            Treatment treatment = this.database.getTreatmentTable().select(cursor.getInt(COLUMN_TREATMENT.getID()));
            String nameNew = cursor.getString(COLUMN_NAME_NEW.getID());
            String nameHealed = cursor.getString(COLUMN_NAME_HEALED.getID());
            String nameTreated = cursor.getString(COLUMN_NAME_TREATED.getID());
            String nameShort = cursor.getString(COLUMN_NAME_SHORT.getID());
            DiagnosisState state = DiagnosisState.parse(cursor.getInt(COLUMN_STATE.getID()));
            int target = cursor.getInt(COLUMN_TARGET.getID());
            List<Resource> resources = Resource.parse(this.database, cursor.getInt(COLUMN_RESOURCES.getID()));

            return new Diagnosis(this.database, id, treatment, nameNew, nameHealed, nameTreated, nameShort, state, target, resources);
        }
    }
}
