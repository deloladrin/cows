package com.deloladrin.cows.data;

import android.content.ContentValues;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.database.TableEntry;
import com.deloladrin.cows.database.SelectOrder;
import com.deloladrin.cows.database.SelectValues;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class Company implements TableEntry
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

    public static Company select(Database database, int id)
    {
        SelectValues values = new SelectValues()
                .where(Table.COLUMN_ID, id);

        return database.getCompanyTable().select(values);
    }

    public static List<Company> selectAll(Database database)
    {
        SelectValues values = new SelectValues();
        return database.getCompanyTable().selectAll(values);
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

    public DatabaseBitmap getImage()
    {
        return DatabaseBitmap.bytesToBitmap(this.image);
    }

    public void setImage(DatabaseBitmap image)
    {
        this.image = DatabaseBitmap.bitmapToBytes(image);
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

    public List<Cow> getCows(int collar)
    {
        SelectValues values = new SelectValues()
                .where(Cow.Table.COLUMN_COMPANY, this.id)
                .where(Cow.Table.COLUMN_COLLAR, collar)
                .orderBy(Cow.Table.COLUMN_ID, SelectOrder.DESCENDING);

        return this.database.getCowTable().selectAll(values);
    }

    public List<Cow> getCows()
    {
        SelectValues values = new SelectValues()
                .where(Cow.Table.COLUMN_COMPANY, this.id);

        return this.database.getCowTable().selectAll(values);
    }

    public static class Table extends TableBase<Company>
    {
        public static final String TABLE_NAME = "companies";

        public static final TableColumn COLUMN_ID = new TableColumn("id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn("name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_GROUP = new TableColumn("grp", ValueType.TEXT, true);
        public static final TableColumn COLUMN_IMAGE = new TableColumn("image", ValueType.BLOB, true);
        public static final TableColumn COLUMN_LAST = new TableColumn("last", ValueType.TEXT, true);

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
        protected ContentValues getValues(Company object)
        {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID.getName(), object.id);
            values.put(COLUMN_NAME.getName(), object.name);
            values.put(COLUMN_GROUP.getName(), object.group);
            values.put(COLUMN_IMAGE.getName(), object.image);
            values.put(COLUMN_LAST.getName(), object.last);

            return values;
        }

        @Override
        protected Company getObject(ContentValues values)
        {
            int id = values.getAsInteger(COLUMN_ID.getName());
            String name = values.getAsString(COLUMN_NAME.getName());
            String group = values.getAsString(COLUMN_GROUP.getName());
            byte[] image = values.getAsByteArray(COLUMN_IMAGE.getName());
            String last = values.getAsString(COLUMN_LAST.getName());

            return new Company(this.database, id, name, group, image, last);
        }
    }
}
