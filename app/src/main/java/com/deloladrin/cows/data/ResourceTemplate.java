package com.deloladrin.cows.data;

import android.database.Cursor;

import com.deloladrin.cows.database.Database;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.database.TableBase;
import com.deloladrin.cows.database.TableColumn;
import com.deloladrin.cows.database.ValueParams;
import com.deloladrin.cows.database.ValueType;

import java.util.List;

public class ResourceTemplate
{
    private Database database;

    private int id;
    private String name;
    private int type;
    private int layer;
    private int copying;
    private byte[] image;

    public ResourceTemplate(Database database)
    {
        this.database = database;
    }

    public ResourceTemplate(Database database, int id, String name, int type, int layer, int copying, byte[] image)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.type = type;
        this.layer = layer;
        this.copying = copying;
        this.image = image;
    }

    public ResourceTemplate(Database database, int id, String name, ResourceType type, int layer, boolean copying, DatabaseBitmap image)
    {
        this.database = database;

        this.setID(id);
        this.setName(name);
        this.setType(type);
        this.setLayer(layer);
        this.setCopying(copying);
        this.setImage(image);
    }

    public static ResourceTemplate get(Database database, long id)
    {
        return database.getResourceTemplateTable().select(id);
    }

    public static List<ResourceTemplate> getAll(Database database)
    {
        return database.getResourceTemplateTable().selectAll();
    }

    public void insert()
    {
        this.id = this.database.getResourceTemplateTable().insert(this);
    }

    public void update()
    {
        this.database.getResourceTemplateTable().update(this);
    }

    public void delete()
    {
        this.database.getResourceTemplateTable().delete(this);
    }

    public void refresh()
    {
        ResourceTemplate refreshed = this.database.getResourceTemplateTable().select(this.id);

        this.name = refreshed.name;
        this.type = refreshed.type;
        this.layer = refreshed.layer;
        this.copying = refreshed.copying;
        this.image = refreshed.image;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof ResourceTemplate))
            return false;

        return ((ResourceTemplate)obj).id == this.id;
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

    public boolean isCopying()
    {
        return this.copying > 0;
    }

    public void setCopying(boolean copying)
    {
        this.copying = copying ? 1 : 0;
    }

    public void setCopying(int copying)
    {
        this.copying = copying;
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

    public static class Table extends TableBase<ResourceTemplate>
    {
        public static final String TABLE_NAME = "resource_templates";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn(1, "name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_TYPE = new TableColumn(2, "type", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_LAYER = new TableColumn(3, "layer", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COPYING = new TableColumn(4, "copying", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_IMAGE = new TableColumn(5, "image", ValueType.BLOB, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME);
            this.columns.add(COLUMN_TYPE);
            this.columns.add(COLUMN_LAYER);
            this.columns.add(COLUMN_COPYING);
            this.columns.add(COLUMN_IMAGE);
        }

        @Override
        protected ValueParams getParams(ResourceTemplate object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.id);
            params.put(COLUMN_NAME, object.name);
            params.put(COLUMN_TYPE, object.type);
            params.put(COLUMN_LAYER, object.layer);
            params.put(COLUMN_COPYING, object.copying);
            params.put(COLUMN_IMAGE, object.getImageHexBytes());

            return params;
        }

        @Override
        protected ResourceTemplate getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            String name = cursor.getString(COLUMN_NAME.getID());
            int type = cursor.getInt(COLUMN_TYPE.getID());
            int layer = cursor.getInt(COLUMN_LAYER.getID());
            int copying = cursor.getInt(COLUMN_COPYING.getID());
            byte[] image = cursor.getBlob(COLUMN_IMAGE.getID());

            return new ResourceTemplate(this.database, id, name, type, layer, copying, image);
        }
    }
}
