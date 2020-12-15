package com.deloladrin.cows.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.deloladrin.cows.R;
import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.util.ArrayList;
import java.util.List;

public class Resource
{
    private Database database;

    private int id;
    private String name;
    private int type;
    private int layer;
    private int copy;
    private byte[] image;

    public Resource(Database database)
    {
        this.database = database;
    }

    public Resource(Database database, int id, String name, int type, int layer, int copy, byte[] image)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.type = type;
        this.layer = layer;
        this.copy = copy;
        this.image = image;
    }

    public Resource(Database database, int id, String name, ResourceType type, int layer, Resource copy, DatabaseBitmap image)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.type = type.getID();
        this.layer = layer;
        this.copy = copy.getID();
        this.image = image.getBytes();
    }

    public Resource(Database database, int id, String name, ResourceType type, int layer, boolean copy, DatabaseBitmap image)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.type = type.getID();
        this.layer = layer;
        this.copy = copy ? -1 : 0;
        this.image = image.getBytes();
    }

    public static List<Resource> parse(Database database, int resourceMask)
    {
        List<Resource> resources = new ArrayList<>();

        for (Resource resource : database.getResourceTable().selectAll())
        {
            int maskID = (1 << resource.getID());

            if ((resourceMask & maskID) == maskID)
            {
                resources.add(resource);
            }
        }

        return resources;
    }

    public static int valueOf(List<Resource> resources)
    {
        if (resources != null)
        {
            int resourceMask = 0;

            for (Resource resource : resources)
            {
                int maskID = (1 << resource.getID());
                resourceMask |= maskID;
            }

            return resourceMask;
        }

        return 0;
    }

    public void insert()
    {
        Table table = this.database.getResourceTable();

        if (this.copy != 0)
        {
            Resource copy = new Resource(this.database);
            this.copyValues(copy);

            int id = table.insert(copy);
            this.copy = id;
        }

        this.id = table.insert(this);
    }

    public void update()
    {
        Table table = this.database.getResourceTable();

        if (this.copy != 0)
        {
            Resource copy = this.getCopy();
            this.copyValues(copy);

            table.update(copy);
        }

        table.update(this);
    }

    public void delete()
    {
        Table table = this.database.getResourceTable();

        if (this.copy != 0)
        {
            Resource copy = this.getCopy();
            table.delete(copy);
        }

        table.delete(this);
    }

    private void copyValues(Resource copy)
    {
        copy.setName(this.name);
        copy.setType(ResourceType.COPY);
        copy.setLayer(this.layer);
        copy.setImage(this.image);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof Resource))
            return false;

        return ((Resource)obj).id == this.id;
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

    public ResourceType getType()
    {
        return ResourceType.parse(this.type);
    }

    public void setType(ResourceType type)
    {
        this.type = type.getID();
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getLayer()
    {
        return this.layer;
    }

    public void setLayer(int layer)
    {
        this.layer = layer;
    }

    public Resource getCopy()
    {
        return this.database.getResourceTable().select(this.copy);
    }

    public void setCopy(Resource copy)
    {
        this.copy = copy.getID();
    }

    public void setCopy(int copy)
    {
        this.copy = copy;
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

    public static class Table extends TableBase<Resource>
    {
        public static final String TABLE_NAME = "resources";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn(1, "name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_TYPE = new TableColumn(2, "type", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_LAYER = new TableColumn(3, "layer", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COPY = new TableColumn(4, "copy", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_IMAGE = new TableColumn(5, "image", ValueType.BLOB, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME);
            this.columns.add(COLUMN_TYPE);
            this.columns.add(COLUMN_LAYER);
            this.columns.add(COLUMN_COPY);
            this.columns.add(COLUMN_IMAGE);
        }

        @Override
        protected ValueParams getParams(Resource object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_NAME, object.name);
            params.put(COLUMN_TYPE, object.type);
            params.put(COLUMN_LAYER, object.layer);
            params.put(COLUMN_COPY, object.copy);
            params.put(COLUMN_IMAGE, object.getImageHexBytes());

            return params;
        }

        @Override
        protected Resource getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            String name = cursor.getString(COLUMN_NAME.getID());
            int type = cursor.getInt(COLUMN_TYPE.getID());
            int layer = cursor.getInt(COLUMN_LAYER.getID());
            int copy = cursor.getInt(COLUMN_COPY.getID());
            byte[] image = cursor.getBlob(COLUMN_IMAGE.getID());

            return new Resource(this.database, id, name, type, layer, copy, image);
        }
    }
}
