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
    private Cow cow;
    private TreatmentType type;
    private LocalDateTime date;
    private String comment;
    private String user;
    private List<Resource> resources;

    public Treatment(Database database)
    {
        this.database = database;
    }

    public Treatment(Database database, int id, Cow cow, TreatmentType type, LocalDateTime date, String comment, String user, List<Resource> resources)
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
        return this.cow;
    }

    public void setCow(Cow cow)
    {
        this.cow = cow;
    }

    public TreatmentType getType()
    {
        return this.type;
    }

    public void setType(TreatmentType type)
    {
        this.type = type;
    }

    public LocalDateTime getDate()
    {
        return this.date;
    }

    public void setDate(LocalDateTime date)
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
        return this.resources;
    }

    public void setResources(List<Resource> resources)
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

            params.put(COLUMN_ID, object.getID());
            params.put(COLUMN_COW, object.getCow().getID());
            params.put(COLUMN_TYPE, object.getType().getID());
            params.put(COLUMN_DATE, object.getDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000);
            params.put(COLUMN_COMMENT, object.getComment());
            params.put(COLUMN_USER, object.getUser());
            params.put(COLUMN_RESOURCES, Resource.valueOf(object.getResources()));

            return params;
        }

        @Override
        protected Treatment getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            Cow cow = this.database.getCowTable().select(cursor.getInt(COLUMN_COW.getID()));
            TreatmentType type = TreatmentType.parse(cursor.getInt(COLUMN_TYPE.getID()));
            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(cursor.getLong(COLUMN_DATE.getID())), ZoneId.systemDefault());
            String comment = cursor.getString(COLUMN_COMMENT.getID());
            String user = cursor.getString(COLUMN_USER.getID());
            List<Resource> resources = Resource.parse(this.database, cursor.getInt(COLUMN_RESOURCES.getID()));

            return new Treatment(this.database, id, cow, type, date, comment, user, resources);
        }
    }
}
