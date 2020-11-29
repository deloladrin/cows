package com.deloladrin.cows.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.util.ArrayList;
import java.util.List;

public class Cow
{
    private Database database;

    private int id;
    private int collar;
    private Company company;
    private String group;

    public Cow(Database database, int id, int collar, Company company, String group)
    {
        this.database = database;

        this.id = id;
        this.collar = collar;
        this.company = company;
        this.group = group;
    }

    public List<Treatment> getTreatments()
    {
        ValueParams params = new ValueParams();
        params.put(Treatment.Table.COLUMN_COW, this.getID());

        return this.database.getTreatmentTable().selectAll(params);
    }

    public Treatment getLastTreatment()
    {
        List<Treatment> treatments = this.getTreatments();

        if (treatments != null && treatments.size() != 0)
        {
            int lastIndex = treatments.size() - 1;
            return treatments.get(lastIndex);
        }

        return null;
    }

    public void insert()
    {
        this.database.getCowTable().insert(this);
    }

    public void update()
    {
        this.database.getCowTable().update(this);
    }

    public void delete()
    {
        this.database.getCowTable().delete(this);
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
        return this.company;
    }

    public void setCompany(Company company)
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

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, false);
        public static final TableColumn COLUMN_COLLAR = new TableColumn(1, "collar", ValueType.INTEGER, true);
        public static final TableColumn COLUMN_COMPANY = new TableColumn(2, "company", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_GROUP = new TableColumn(3, "grp", ValueType.TEXT, true);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_COLLAR);
            this.columns.add(COLUMN_COMPANY);
            this.columns.add(COLUMN_GROUP);
        }

        @Override
        protected ValueParams getParams(Cow object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.getID());
            params.put(COLUMN_COLLAR, object.getCollar());
            params.put(COLUMN_COMPANY, object.getCompany().getID());
            params.put(COLUMN_GROUP, object.getGroup());

            return params;
        }

        @Override
        protected Cow getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            int collar = cursor.getInt(COLUMN_COLLAR.getID());
            Company company = this.database.getCompanyTable().select(cursor.getInt(COLUMN_COMPANY.getID()));
            String group = cursor.getString(COLUMN_GROUP.getID());

            return new Cow(this.database, id, collar, company, group);
        }
    }
}
