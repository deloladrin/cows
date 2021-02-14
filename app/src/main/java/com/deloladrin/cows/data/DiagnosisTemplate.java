package com.deloladrin.cows.data;

import android.content.ContentValues;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.SelectValues;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.TableEntry;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class DiagnosisTemplate implements TableEntry
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

    public static DiagnosisTemplate select(Database database, int id)
    {
        SelectValues values = new SelectValues()
                .where(Table.COLUMN_ID, id);

        return database.getDiagnosisTemplateTable().select(values);
    }

    public static List<DiagnosisTemplate> selectAll(Database database)
    {
        SelectValues values = new SelectValues();
        return database.getDiagnosisTemplateTable().selectAll(values);
    }

    public void insert()
    {
        this.id = this.database.getDiagnosisTemplateTable().insert(this);
    }

    public void update()
    {
        this.database.getDiagnosisTemplateTable().update(this);
    }

    public void delete()
    {
        this.database.getDiagnosisTemplateTable().delete(this);
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

        public static final TableColumn COLUMN_ID = new TableColumn("id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME_NEW = new TableColumn("name_new", ValueType.TEXT, false);
        public static final TableColumn COLUMN_NAME_TREATED = new TableColumn("name_treated", ValueType.TEXT, false);
        public static final TableColumn COLUMN_NAME_HEALED = new TableColumn("name_healed", ValueType.TEXT, false);
        public static final TableColumn COLUMN_NAME_SHORT = new TableColumn("name_short", ValueType.TEXT, false);
        public static final TableColumn COLUMN_TYPE = new TableColumn("type", ValueType.INTEGER, false);

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
        protected ContentValues getValues(DiagnosisTemplate object)
        {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID.getName(), object.id);
            values.put(COLUMN_NAME_NEW.getName(), object.nameNew);
            values.put(COLUMN_NAME_TREATED.getName(), object.nameTreated);
            values.put(COLUMN_NAME_HEALED.getName(), object.nameHealed);
            values.put(COLUMN_NAME_SHORT.getName(), object.nameShort);
            values.put(COLUMN_TYPE.getName(), object.type);

            return values;
        }

        @Override
        protected DiagnosisTemplate getObject(ContentValues values)
        {
            int id = values.getAsInteger(COLUMN_ID.getName());
            String nameNew = values.getAsString(COLUMN_NAME_NEW.getName());
            String nameTreated = values.getAsString(COLUMN_NAME_TREATED.getName());
            String nameHealed = values.getAsString(COLUMN_NAME_HEALED.getName());
            String nameShort = values.getAsString(COLUMN_NAME_SHORT.getName());
            int type = values.getAsInteger(COLUMN_TYPE.getName());

            return new DiagnosisTemplate(this.database, id, nameNew, nameTreated, nameHealed, nameShort, type);
        }
    }
}
