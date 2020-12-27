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
    private String last;
    private int active;

    public Company(Database database)
    {
        this.database = database;
    }

    public Company(Database database, int id, String name, String group, String last, int active)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.group = group;
        this.last = last;
        this.active = active;
    }

    public Company(Database database, int id, String name, String group, String last, boolean active)
    {
        this.database = database;

        this.setID(id);
        this.setName(name);
        this.setGroup(group);
        this.setLastGroup(last);
        this.setActive(active);
    }

    public static Company get(Database database, int id)
    {
        return database.getCompanyTable().select(id);
    }

    public static List<Company> getAll(Database database)
    {
        return database.getCompanyTable().selectAll();
    }

    public static Company getActive(Database database)
    {
        ValueParams params = new ValueParams();
        params.put(Table.COLUMN_ACTIVE, 1);

        return database.getCompanyTable().select(params);
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

    public void refresh()
    {
        Company refreshed = this.database.getCompanyTable().select(this.id);

        this.name = refreshed.name;
        this.group = refreshed.group;
        this.last = refreshed.last;
        this.active = refreshed.active;
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

    public String getLastGroup()
    {
        return this.last;
    }

    public void setLastGroup(String last)
    {
        this.last = last;
    }

    public boolean isActive()
    {
        return this.active > 0;
    }

    public void setActive(boolean active)
    {
        this.active = active ? 1 : 0;
    }

    public void setActive(int active)
    {
        this.active = active;
    }

    public static class Table extends TableBase<Company>
    {
        public static final String TABLE_NAME = "companies";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn(1, "name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_GROUP = new TableColumn(2, "grp", ValueType.TEXT, true);
        public static final TableColumn COLUMN_LAST = new TableColumn(3, "last", ValueType.TEXT, true);
        public static final TableColumn COLUMN_ACTIVE = new TableColumn(4, "active", ValueType.INTEGER, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME);
            this.columns.add(COLUMN_GROUP);
            this.columns.add(COLUMN_LAST);
            this.columns.add(COLUMN_ACTIVE);
        }

        @Override
        protected ValueParams getParams(Company object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_NAME, object.name);
            params.put(COLUMN_GROUP, object.group);
            params.put(COLUMN_LAST, object.last);
            params.put(COLUMN_ACTIVE, object.active);

            return params;
        }

        @Override
        protected Company getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            String name = cursor.getString(COLUMN_NAME.getID());
            String group = cursor.getString(COLUMN_GROUP.getID());
            String last = cursor.getString(COLUMN_LAST.getID());
            int active = cursor.getInt(COLUMN_ACTIVE.getID());

            return new Company(this.database, id, name, group, last, active);
        }
    }
}
