package com.deloladrin.cows.data;

import android.content.ContentValues;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableEntry;
import com.deloladrin.cows.database.SelectValues;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class Cow implements TableEntry
{
    private Database database;

    private int id;
    private int collar;
    private int company;
    private String group;

    public Cow(Database database)
    {
        this.database = database;
    }

    public Cow(Database database, int id, int collar, int company, String group)
    {
        this.database = database;

        this.id = id;
        this.collar = collar;
        this.company = company;
        this.group = group;
    }

    public Cow(Database database, int id, int collar, Company company, String group)
    {
        this.database = database;

        this.setID(id);
        this.setCollar(collar);
        this.setCompany(company);
        this.setGroup(group);
    }

    public static Cow select(Database database, int id)
    {
        SelectValues values = new SelectValues()
                .where(Table.COLUMN_ID, id);

        return database.getCowTable().select(values);
    }

    public static List<Cow> selectAll(Database database)
    {
        SelectValues values = new SelectValues();
        return database.getCowTable().selectAll(values);
    }

    public void insert()
    {
        this.id = this.database.getCowTable().insert(this);
        this.updateLastGroup();
    }

    public void update()
    {
        this.database.getCowTable().update(this);
        this.updateLastGroup();
    }

    private void updateLastGroup()
    {
        Company company = this.getCompany();
        company.setLastGroup(this.group);

        company.update();
    }

    public void delete()
    {
        this.database.getCowTable().delete(this);
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof Cow))
            return false;

        return ((Cow)obj).id == this.id;
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

    public int getCollar()
    {
        return this.collar;
    }

    public void setCollar(int collar)
    {
        this.collar = collar;
    }

    public Company getCompany()
    {
        return Company.select(this.database, this.company);
    }

    public void setCompany(Company company)
    {
        this.company = company.getID();
    }

    public void setCompany(int company)
    {
        this.company = company;
    }

    public String getGroup()
    {
        return this.group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public List<Treatment> getTreatments()
    {
        SelectValues values = new SelectValues()
                .where(Treatment.Table.COLUMN_COW, this.id);

        return this.database.getTreatmentTable().selectAll(values);
    }

    public static class Table extends TableBase<Cow>
    {
        public static final String TABLE_NAME = "cows";

        public static final TableColumn COLUMN_ID = new TableColumn("id", ValueType.INTEGER, false, true, false);
        public static final TableColumn COLUMN_COLLAR = new TableColumn("collar", ValueType.INTEGER, true);
        public static final TableColumn COLUMN_COMPANY = new TableColumn("company", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_GROUP = new TableColumn("grp", ValueType.TEXT, true);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_COLLAR);
            this.columns.add(COLUMN_COMPANY);
            this.columns.add(COLUMN_GROUP);
        }

        @Override
        protected ContentValues getValues(Cow object)
        {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID.getName(), object.id);
            values.put(COLUMN_COLLAR.getName(), object.collar);
            values.put(COLUMN_COMPANY.getName(), object.company);
            values.put(COLUMN_GROUP.getName(), object.group);

            return values;
        }

        @Override
        protected Cow getObject(ContentValues values)
        {
            int id = values.getAsInteger(COLUMN_ID.getName());
            int collar = values.getAsInteger(COLUMN_COLLAR.getName());
            int company = values.getAsInteger(COLUMN_COMPANY.getName());
            String group = values.getAsString(COLUMN_GROUP.getName());

            return new Cow(this.database, id, collar, company, group);
        }
    }
}
