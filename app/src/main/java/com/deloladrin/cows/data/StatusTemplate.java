package com.deloladrin.cows.data;

import android.database.Cursor;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.database.DatabaseEntry;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class StatusTemplate implements DatabaseEntry
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

    public static StatusTemplate get(Database database, long id)
    {
        return database.getStatusTemplateTable().select(id);
    }

    public static List<StatusTemplate> getAll(Database database)
    {
        return database.getStatusTemplateTable().selectAll();
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

    public void refresh()
    {
        StatusTemplate refreshed = this.database.getStatusTemplateTable().select(this.id);

        this.name = refreshed.name;
        this.image = refreshed.image;
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
        return new DatabaseBitmap(this.image);
    }

    public String getImageHexBytes()
    {
        return this.getImage().getHexBytes();
    }

    public void setImage(DatabaseBitmap image)
    {
        this.image = image.getBytes();
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public static class Table extends TableBase<StatusTemplate>
    {
        public static final String TABLE_NAME = "status_templates";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn(1, "name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_IMAGE = new TableColumn(2, "image", ValueType.BLOB, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME);
            this.columns.add(COLUMN_IMAGE);
        }

        @Override
        protected ValueParams getParams(StatusTemplate object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_NAME, object.name);
            params.put(COLUMN_IMAGE, object.getImageHexBytes());

            return params;
        }

        @Override
        protected StatusTemplate getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            String name = cursor.getString(COLUMN_NAME.getID());
            byte[] image = cursor.getBlob(COLUMN_IMAGE.getID());

            return new StatusTemplate(this.database, id, name, image);
        }
    }
}
