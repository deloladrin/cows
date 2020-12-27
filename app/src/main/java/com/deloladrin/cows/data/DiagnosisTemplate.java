package com.deloladrin.cows.data;

import android.database.Cursor;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class DiagnosisTemplate
{
    private Database database;

    private int id;
    private String nameNew;
    private String nameTreated;
    private String nameHealed;
    private String nameShort;
    private int type;

    public DiagnosisTemplate(Database database)
    {
        this.database = database;
    }

    public DiagnosisTemplate(Database database, int id, String nameNew, String nameTreated, String nameHealed, String nameShort, int type)
    {
        this.database = database;

        this.id = id;
        this.nameNew = nameNew;
        this.nameTreated = nameTreated;
        this.nameHealed = nameHealed;
        this.nameShort = nameShort;
        this.type = type;
    }

    public DiagnosisTemplate(Database database, int id, String nameNew, String nameTreated, String nameHealed, String nameShort, DiagnosisType type)
    {
        this.database = database;

        this.setID(id);
        this.setNewName(nameNew);
        this.setTreatedName(nameTreated);
        this.setHealedName(nameHealed);
        this.setShortName(nameShort);
        this.setType(type);
    }

    public static DiagnosisTemplate get(Database database, long id)
    {
        return database.getDiagnosesTemplateTable().select(id);
    }

    public static List<DiagnosisTemplate> getAll(Database database)
    {
        return database.getDiagnosesTemplateTable().selectAll();
    }

    public void insert()
    {
        this.id = this.database.getDiagnosesTemplateTable().insert(this);
    }

    public void update()
    {
        this.database.getDiagnosesTemplateTable().update(this);
    }

    public void delete()
    {
        this.database.getDiagnosesTemplateTable().delete(this);
    }

    public void refresh()
    {
        DiagnosisTemplate refreshed = this.database.getDiagnosesTemplateTable().select(this.id);;

        this.nameNew = refreshed.nameNew;
        this.nameTreated = refreshed.nameTreated;
        this.nameHealed = refreshed.nameHealed;
        this.nameShort = refreshed.nameShort;
        this.type = refreshed.type;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof DiagnosisTemplate))
            return false;

        return ((DiagnosisTemplate)obj).id == this.id;
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

    public String getNewName()
    {
        return this.nameNew;
    }

    public void setNewName(String nameNew)
    {
        this.nameNew = nameNew;
    }

    public String getTreatedName()
    {
        return this.nameTreated;
    }

    public void setTreatedName(String nameTreated)
    {
        this.nameTreated = nameTreated;
    }

    public String getHealedName()
    {
        return this.nameHealed;
    }

    public void setHealedName(String nameHealed)
    {
        this.nameHealed = nameHealed;
    }

    public String getShortName()
    {
        return this.nameShort;
    }

    public void setShortName(String nameShort)
    {
        this.nameShort = nameShort;
    }

    public DiagnosisType getType()
    {
        return DiagnosisType.parse(this.type);
    }

    public void setType(DiagnosisType type)
    {
        this.type = type.getID();
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public static class Table extends TableBase<DiagnosisTemplate>
    {
        public static final String TABLE_NAME = "diagonsis_templates";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME_NEW = new TableColumn(1, "name_new", ValueType.TEXT, false);
        public static final TableColumn COLUMN_NAME_TREATED = new TableColumn(2, "name_treated", ValueType.TEXT, false);
        public static final TableColumn COLUMN_NAME_HEALED = new TableColumn(3, "name_healed", ValueType.TEXT, false);
        public static final TableColumn COLUMN_NAME_SHORT = new TableColumn(4, "name_short", ValueType.TEXT, false);
        public static final TableColumn COLUMN_TYPE = new TableColumn(5, "type", ValueType.INTEGER, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME_NEW);
            this.columns.add(COLUMN_NAME_TREATED);
            this.columns.add(COLUMN_NAME_HEALED);
            this.columns.add(COLUMN_NAME_SHORT);
            this.columns.add(COLUMN_TYPE);
        }

        @Override
        protected ValueParams getParams(DiagnosisTemplate object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_NAME_NEW, object.nameNew);
            params.put(COLUMN_NAME_TREATED, object.nameTreated);
            params.put(COLUMN_NAME_HEALED, object.nameHealed);
            params.put(COLUMN_NAME_SHORT, object.nameShort);
            params.put(COLUMN_TYPE, object.type);

            return params;
        }

        @Override
        protected DiagnosisTemplate getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            String nameNew = cursor.getString(COLUMN_NAME_NEW.getID());
            String nameTreated = cursor.getString(COLUMN_NAME_TREATED.getID());
            String nameHealed = cursor.getString(COLUMN_NAME_HEALED.getID());
            String nameShort = cursor.getString(COLUMN_NAME_SHORT.getID());
            int type = cursor.getInt(COLUMN_TYPE.getID());

            return new DiagnosisTemplate(this.database, id, nameNew, nameTreated, nameHealed, nameShort, type);
        }
    }
}
