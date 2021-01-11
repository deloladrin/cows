package com.deloladrin.cows.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.DatabaseEntry;
import com.deloladrin.cows.database.SelectOrder;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.util.ArrayList;
import java.util.List;

public class Cow implements DatabaseEntry
{
    private Database database;

    private int id;
    private int number;
    private int collar;
    private int company;
    private String group;

    public Cow(Database database)
    {
        this.database = database;
    }

    public Cow(Database database, int id, int number, int collar, int company, String group)
    {
        this.database = database;

        this.id = id;
        this.number = number;
        this.collar = collar;
        this.company = company;
        this.group = group;
    }

    public Cow(Database database, int id, int number, int collar, Company company, String group)
    {
        this.database = database;

        this.setID(id);
        this.setNumber(number);
        this.setCollar(collar);
        this.setCompany(company);
        this.setGroup(group);
    }

    public static Cow get(Database database, int id)
    {
        return database.getCowTable().select(id);
    }

    public static Cow getByNumber(Database database, int number)
    {
        ValueParams params = new ValueParams();
        params.put(Table.COLUMN_NUMBER, number);

        return database.getCowTable().select(params, SelectOrder.DESCENDING);
    }

    public static List<Cow> getByCollar(Database database, Company company, int collar)
    {
        ValueParams params = new ValueParams();
        params.put(Table.COLUMN_COMPANY, company.getID());
        params.put(Table.COLUMN_COLLAR, collar);

        return database.getCowTable().selectAll(params, SelectOrder.DESCENDING);
    }

    public static List<Cow> getAll(Database database)
    {
        return database.getCowTable().selectAll();
    }

    public List<Treatment> getTreatments()
    {
        ValueParams params = new ValueParams();
        params.put(Treatment.Table.COLUMN_COW, this.getID());

        return this.database.getTreatmentTable().selectAll(params);
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

    public void refresh()
    {
        Cow refreshed = this.database.getCowTable().select(this.id);

        this.number = refreshed.number;
        this.collar = refreshed.collar;
        this.company = refreshed.company;
        this.group = refreshed.group;
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

    public int getNumber()
    {
        return this.number;
    }

    public void setNumber(int number)
    {
        this.number = number;
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
        return this.database.getCompanyTable().select(this.company);
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

    public static class Table extends TableBase<Cow>
    {
        public static final String TABLE_NAME = "cows";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NUMBER = new TableColumn(1, "number", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COLLAR = new TableColumn(2, "collar", ValueType.INTEGER, true);
        public static final TableColumn COLUMN_COMPANY = new TableColumn(3, "company", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_GROUP = new TableColumn(4, "grp", ValueType.TEXT, true);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NUMBER);
            this.columns.add(COLUMN_COLLAR);
            this.columns.add(COLUMN_COMPANY);
            this.columns.add(COLUMN_GROUP);
        }

        @Override
        protected ValueParams getParams(Cow object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_NUMBER, object.number);
            params.put(COLUMN_COLLAR, object.collar);
            params.put(COLUMN_COMPANY, object.company);
            params.put(COLUMN_GROUP, object.group);

            return params;
        }

        @Override
        protected Cow getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            int number = cursor.getInt(COLUMN_NUMBER.getID());
            int collar = cursor.getInt(COLUMN_COLLAR.getID());
            int company = cursor.getInt(COLUMN_COMPANY.getID());
            String group = cursor.getString(COLUMN_GROUP.getID());

            return new Cow(this.database, id, number, collar, company, group);
        }
    }
}
