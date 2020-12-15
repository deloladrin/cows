package com.deloladrin.cows.data;

import android.database.Cursor;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class Treatment
{
    private Database database;

    private int id;
    private int cow;
    private int type;
    private long date;
    private String comment;
    private String user;
    private int resources;

    public Treatment(Database database)
    {
        this.database = database;
    }

    public Treatment(Database database, int id, int cow, int type, long date, String comment, String user, int resources)
    {
        this.database = database;

        this.id = id;
        this.cow = cow;
        this.type = type;
        this.date = date;
        this.comment = comment;
        this.user = user;
        this.resources = resources;
    }

    public Treatment(Database database, int id, Cow cow, TreatmentType type, LocalDateTime date, String comment, String user, List<Resource> resources)
    {
        this.database = database;

        this.id = id;
        this.cow = cow.getID();
        this.type = type.getID();
        this.date = this.dateToTimestamp(date);
        this.comment = comment;
        this.user = user;
        this.resources = Resource.valueOf(resources);
    }

    public List<Diagnosis> getDiagnoses()
    {
        ValueParams params = new ValueParams();
        params.put(Diagnosis.Table.COLUMN_TREATMENT, this.getID());

        return this.database.getDiagnosesTable().selectAll(params);
    }

    public void insert()
    {
        this.id = this.database.getTreatmentTable().insert(this);
    }

    public void update()
    {
        this.database.getTreatmentTable().update(this);
    }

    public void delete()
    {
        /* Delete all diagnoses */
        for (Diagnosis diagnosis : this.getDiagnoses())
        {
            diagnosis.delete();
        }

        this.database.getTreatmentTable().delete(this);
    }

    private long dateToTimestamp(LocalDateTime date)
    {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
    }

    private LocalDateTime timestampToDate(long timestamp)
    {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof Treatment))
            return false;

        return ((Treatment)obj).id == this.id;
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

    public Cow getCow()
    {
        return this.database.getCowTable().select(this.cow);
    }

    public void setCow(Cow cow)
    {
        this.cow = cow.getID();
    }

    public void setCow(int cow)
    {
        this.cow = cow;
    }

    public TreatmentType getType()
    {
        return TreatmentType.parse(this.type);
    }

    public void setType(TreatmentType type)
    {
        this.type = type.getID();
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public LocalDateTime timestampToDate()
    {
        return this.timestampToDate(this.date);
    }

    public void setDate(LocalDateTime date)
    {
        this.date = this.dateToTimestamp(date);
    }

    public void setDate(long date)
    {
        this.date = date;
    }

    public String getComment()
    {
        return this.comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getUser()
    {
        return this.user;
    }

    public void setUser(String user)
    {
        this.user = user;
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

    public static class Table extends TableBase<Treatment>
    {
        public static final String TABLE_NAME = "treatments";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_COW = new TableColumn(1, "cow", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TYPE = new TableColumn(2, "type", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_DATE = new TableColumn(3, "date", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COMMENT = new TableColumn(4, "comment", ValueType.TEXT, true);
        public static final TableColumn COLUMN_USER = new TableColumn(5, "user", ValueType.TEXT, false);
        public static final TableColumn COLUMN_RESOURCES = new TableColumn(6, "resources", ValueType.INTEGER, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_COW);
            this.columns.add(COLUMN_TYPE);
            this.columns.add(COLUMN_DATE);
            this.columns.add(COLUMN_COMMENT);
            this.columns.add(COLUMN_USER);
            this.columns.add(COLUMN_RESOURCES);
        }

        @Override
        protected ValueParams getParams(Treatment object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_COW, object.cow);
            params.put(COLUMN_TYPE, object.type);
            params.put(COLUMN_DATE, object.date);
            params.put(COLUMN_COMMENT, object.comment);
            params.put(COLUMN_USER, object.user);
            params.put(COLUMN_RESOURCES, object.resources);

            return params;
        }

        @Override
        protected Treatment getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            int cow = cursor.getInt(COLUMN_COW.getID());
            int type = cursor.getInt(COLUMN_TYPE.getID());
            long date = cursor.getLong(COLUMN_DATE.getID());
            String comment = cursor.getString(COLUMN_COMMENT.getID());
            String user = cursor.getString(COLUMN_USER.getID());
            int resources = cursor.getInt(COLUMN_RESOURCES.getID());

            return new Treatment(this.database, id, cow, type, date, comment, user, resources);
        }
    }
}
