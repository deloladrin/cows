package com.deloladrin.cows.data;

import android.content.ContentValues;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.database.SelectValues;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.TableEntry;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class StatusTemplate implements TableEntry
{
    private Database database;

    private int id;
    private String name;
    private byte[] image;

    public StatusTemplate(Database database)
    {
        this.database = database;
    }

    public StatusTemplate(Database database, int id, String name, byte[] image)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.image = image;
    }

    public StatusTemplate(Database database, int id, String name, DatabaseBitmap image)
    {
        this.database = database;

        this.setID(id);
        this.setName(name);
        this.setImage(image);
    }

    public static StatusTemplate select(Database database, int id)
    {
        SelectValues values = new SelectValues()
                .where(Table.COLUMN_ID, id);

        return database.getStatusTemplateTable().select(values);
    }

    public static List<StatusTemplate> selectAll(Database database)
    {
        SelectValues values = new SelectValues();
        return database.getStatusTemplateTable().selectAll(values);
    }

    public void insert()
    {
        this.id = this.database.getStatusTemplateTable().insert(this);
    }

    public void update()
    {
        this.database.getStatusTemplateTable().update(this);
    }

    public void delete()
    {
        this.database.getStatusTemplateTable().delete(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof StatusTemplate))
            return false;

        return ((StatusTemplate)obj).id == this.id;
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

    public static class Table extends TableBase<StatusTemplate>
    {
        public static final String TABLE_NAME = "status_templates";

        public static final TableColumn COLUMN_ID = new TableColumn("id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn("name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_IMAGE = new TableColumn("image", ValueType.BLOB, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME);
            this.columns.add(COLUMN_IMAGE);
        }

        @Override
        protected ContentValues getValues(StatusTemplate object)
        {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID.getName(), object.id);
            values.put(COLUMN_NAME.getName(), object.name);
            values.put(COLUMN_IMAGE.getName(), object.image);

            return values;
        }

        @Override
        protected StatusTemplate getObject(ContentValues values)
        {
            int id = values.getAsInteger(COLUMN_ID.getName());
            String name = values.getAsString(COLUMN_NAME.getName());
            byte[] image = values.getAsByteArray(COLUMN_IMAGE.getName());

            return new StatusTemplate(this.database, id, name, image);
        }
    }
}
