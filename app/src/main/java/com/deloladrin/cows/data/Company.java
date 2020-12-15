package com.deloladrin.cows.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class Company
{
    private Database database;

    private int id;
    private String name;
    private String group;

    public Company(Database database)
    {
        this.database = database;
    }

    public Company(Database database, int id, String name, String group)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.group = group;
    }

    public List<Cow> getCows()
    {
        ValueParams params = new ValueParams();
        params.put(Cow.Table.COLUMN_COMPANY, this.getID());

        return this.database.getCowTable().selectAll(params);
    }

    public void insert()
    {
        this.id = this.database.getCompanyTable().insert(this);
    }

    public void update()
    {
        this.database.getCompanyTable().update(this);
    }

    public void delete()
    {
        this.database.getCompanyTable().delete(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof Company))
            return false;

        return ((Company)obj).id == this.id;
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

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGroup()
    {
        return this.group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public static class Table extends TableBase<Company>
    {
        public static final String TABLE_NAME = "companies";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn(1, "name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_GROUP = new TableColumn(2, "grp", ValueType.TEXT, true);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME);
            this.columns.add(COLUMN_GROUP);
        }

        @Override
        protected ValueParams getParams(Company object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_NAME, object.name);
            params.put(COLUMN_GROUP, object.group);

            return params;
        }

        @Override
        protected Company getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            String name = cursor.getString(COLUMN_NAME.getID());
            String group = cursor.getString(COLUMN_GROUP.getID());

            return new Company(this.database, id, name, group);
        }
    }
}
