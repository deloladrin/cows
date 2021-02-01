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

    public static List<Treatment> getAll(Database database)
    {
        return database.getTreatmentTable().selectAll();
    }

    public List<Diagnosis> getDiagnoses()
    {
        ValueParams params = new ValueParams();
        params.put(Diagnosis.Table.COLUMN_TREATMENT, this.getID());

        return this.database.getDiagnosesTable().selectAll(params);
    }

    public List<Resource> getResources()
    {
        ValueParams params = new ValueParams();
        params.put(Resource.Table.COLUMN_TREATMENT, this.getID());

        return this.database.getResourceTable().selectAll(params);
    }

    public List<Status> getStatuses()
    {
        ValueParams params = new ValueParams();
        params.put(Status.Table.COLUMN_TREATMENT, this.getID());

        return this.database.getStatusTable().selectAll(params);
    }

    public boolean isHealed()
    {
        boolean healed = true;

        for (Diagnosis diagnosis : this.getDiagnoses())
        {
            if (diagnosis.getState() != DiagnosisState.HEALED)
            {
                healed = false;
            }
        }

        return healed;
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

    public void refresh()
    {
        Treatment refreshed = this.database.getTreatmentTable().select(this.id);

        this.cow = refreshed.cow;
        this.type = refreshed.type;
        this.date = refreshed.date;
        this.comment = refreshed.comment;
        this.user = refreshed.user;
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

    public static class Table extends TableBase<Treatment>
    {
        public static final String TABLE_NAME = "treatments";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_COW = new TableColumn(1, "cow", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_TYPE = new TableColumn(2, "type", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_DATE = new TableColumn(3, "date", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COMMENT = new TableColumn(4, "comment", ValueType.TEXT, true);
        public static final TableColumn COLUMN_USER = new TableColumn(5, "user", ValueType.TEXT, false);

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
        protected ValueParams getParams(Treatment object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_COW, object.cow);
            params.put(COLUMN_TYPE, object.type);
            params.put(COLUMN_DATE, object.date);
            params.put(COLUMN_COMMENT, object.comment);
            params.put(COLUMN_USER, object.user);

            return params;
        }

        @Override
        protected Treatment getObject(Cursor cursor)
        {
            long id = cursor.getLong(COLUMN_ID.getID());
            int cow = cursor.getInt(COLUMN_COW.getID());
            int type = cursor.getInt(COLUMN_TYPE.getID());
            long date = cursor.getLong(COLUMN_DATE.getID());
            String comment = cursor.getString(COLUMN_COMMENT.getID());
            String user = cursor.getString(COLUMN_USER.getID());

            return new Treatment(this.database, id, cow, type, date, comment, user);
        }
    }
}
