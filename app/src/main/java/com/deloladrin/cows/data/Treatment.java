package com.deloladrin.cows.data;

import android.content.ContentValues;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableEntry;
import com.deloladrin.cows.database.SelectValues;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class Treatment implements TableEntry
{
    private Database database;

    private long id;
    private int cow;
    private int type;
    private long date;
    private String comment;
    private String user;

    public Treatment(Database database)
    {
        this.database = database;
    }

    public Treatment(Database database, long id, int cow, int type, long date, String comment, String user)
    {
        this.database = database;

        this.id = id;
        this.cow = cow;
        this.type = type;
        this.date = date;
        this.comment = comment;
        this.user = user;
    }

    public Treatment(Database database, long id, Cow cow, TreatmentType type, LocalDateTime date, String comment, String user)
    {
        this.database = database;

        this.setID(id);
        this.setCow(cow);
        this.setType(type);
        this.setDate(date);
        this.setComment(comment);
        this.setUser(user);
    }

    public static Treatment select(Database database, long id)
    {
        SelectValues values = new SelectValues()
                .where(Table.COLUMN_ID, id);

        return database.getTreatmentTable().select(values);
    }

    public static List<Treatment> selectAll(Database database)
    {
        SelectValues values = new SelectValues();
        return database.getTreatmentTable().selectAll(values);
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

        /* Delete all resources */
        for (Resource resource : this.getResources())
        {
            resource.delete();
        }

        /* Delete all statuses */
        for (Status status : this.getStatuses())
        {
            status.delete();
        }

        this.database.getTreatmentTable().delete(this);
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

    public Cow getCow()
    {
        return Cow.select(this.database, this.cow);
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

    public LocalDateTime getDate()
    {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(this.date), ZoneId.systemDefault());
    }

    public void setDate(LocalDateTime date)
    {
        this.date = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
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

    public List<Diagnosis> getDiagnoses()
    {
        SelectValues values = new SelectValues()
                .where(Diagnosis.Table.COLUMN_TREATMENT, this.id);

        return this.database.getDiagnosisTable().selectAll(values);
    }

    public List<Resource> getResources()
    {
        SelectValues values = new SelectValues()
                .where(Resource.Table.COLUMN_TREATMENT, this.id);

        return this.database.getResourceTable().selectAll(values);
    }

    public List<Status> getStatuses()
    {
        SelectValues values = new SelectValues()
                .where(Status.Table.COLUMN_TREATMENT, this.id);

        return this.database.getStatusTable().selectAll(values);
    }

    public boolean isHealed()
    {
        for (Diagnosis diagnosis : this.getDiagnoses())
        {
            if (diagnosis.getState() != DiagnosisState.HEALED)
            {
                return false;
            }
        }

        return true;
    }

    public static class Table extends TableBase<Treatment>
    {
        public static final String TABLE_NAME = "treatments";

        public static final TableColumn COLUMN_ID = new TableColumn("id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_COW = new TableColumn("cow", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TYPE = new TableColumn("type", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_DATE = new TableColumn("date", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COMMENT = new TableColumn("comment", ValueType.TEXT, true);
        public static final TableColumn COLUMN_USER = new TableColumn("user", ValueType.TEXT, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_COW);
            this.columns.add(COLUMN_TYPE);
            this.columns.add(COLUMN_DATE);
            this.columns.add(COLUMN_COMMENT);
            this.columns.add(COLUMN_USER);
        }

        @Override
        protected ContentValues getValues(Treatment object)
        {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID.getName(), object.id);
            values.put(COLUMN_COW.getName(), object.cow);
            values.put(COLUMN_TYPE.getName(), object.type);
            values.put(COLUMN_DATE.getName(), object.date);
            values.put(COLUMN_COMMENT.getName(), object.comment);
            values.put(COLUMN_USER.getName(), object.user);

            return values;
        }

        @Override
        protected Treatment getObject(ContentValues values)
        {
            long id = values.getAsLong(COLUMN_ID.getName());
            int cow = values.getAsInteger(COLUMN_COW.getName());
            int type = values.getAsInteger(COLUMN_TYPE.getName());
            long date = values.getAsLong(COLUMN_DATE.getName());
            String comment = values.getAsString(COLUMN_COMMENT.getName());
            String user = values.getAsString(COLUMN_USER.getName());

            return new Treatment(this.database, id, cow, type, date, comment, user);
        }
    }
}
