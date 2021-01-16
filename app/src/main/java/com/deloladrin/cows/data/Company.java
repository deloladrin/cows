package com.deloladrin.cows.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.database.DatabaseEntry;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class Company implements DatabaseEntry
{
    private Database database;

    private int id;
    private String name;
    private String group;
    private byte[] image;
    private String last;

    public Company(Database database)
    {
        this.database = database;
    }

    public Company(Database database, int id, String name, String group, byte[] image, String last)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.group = group;
        this.image = image;
        this.last = last;
    }

    public Company(Database database, int id, String name, String group, DatabaseBitmap image, String last)
    {
        this.database = database;

        this.setID(id);
        this.setName(name);
        this.setGroup(group);
        this.setImage(image);
        this.setLastGroup(last);
    }

    public static Company get(Database database, int id)
    {
        return database.getCompanyTable().select(id);
    }

    public static List<Company> getAll(Database database)
    {
        List<Company> companies = database.getCompanyTable().selectAll();
        companies.sort((a, b) -> a.getName().compareTo(b.getName()));

        return companies;
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
        this.image = refreshed.image;
        this.last = refreshed.last;
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

    public DatabaseBitmap getImage()
    {
        if (this.image != null)
        {
            return new DatabaseBitmap(this.image);
        }
        else
        {
            return null;
        }
    }

    public String getImageHexBytes()
    {
        if (image != null)
        {
            return this.getImage().getHexBytes();
        }
        else
        {
            return null;
        }
    }

    public void setImage(DatabaseBitmap image)
    {
        if (image != null)
        {
            this.image = image.getBytes();
        }
        else
        {
            this.image = null;
        }
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public String getLastGroup()
    {
        return this.last;
    }

    public void setLastGroup(String last)
    {
        this.last = last;
    }

    public static class Table extends TableBase<Company>
    {
        public static final String TABLE_NAME = "companies";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn(1, "name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_GROUP = new TableColumn(2, "grp", ValueType.TEXT, true);
        public static final TableColumn COLUMN_IMAGE = new TableColumn(3, "image", ValueType.BLOB, true);
        public static final TableColumn COLUMN_LAST = new TableColumn(4, "last", ValueType.TEXT, true);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME);
            this.columns.add(COLUMN_GROUP);
            this.columns.add(COLUMN_IMAGE);
            this.columns.add(COLUMN_LAST);
        }

        @Override
        protected ValueParams getParams(Company object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_NAME, object.name);
            params.put(COLUMN_GROUP, object.group);
            params.put(COLUMN_IMAGE, object.getImageHexBytes());
            params.put(COLUMN_LAST, object.last);

            return params;
        }

        @Override
        protected Company getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            String name = cursor.getString(COLUMN_NAME.getID());
            String group = cursor.getString(COLUMN_GROUP.getID());
            byte[] image = cursor.getBlob(COLUMN_IMAGE.getID());
            String last = cursor.getString(COLUMN_LAST.getID());

            return new Company(this.database, id, name, group, image, last);
        }
    }
}
