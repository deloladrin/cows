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

public class ResourceTemplate implements TableEntry
{
    private Database database;

    private int id;
    private String name;
    private int type;
    private int layer;
    private int copying;
    private byte[] image;
    private byte[] imageSmall;

    public ResourceTemplate(Database database)
    {
        this.database = database;
    }

    public ResourceTemplate(Database database, int id, String name, int type, int layer, int copying, byte[] image, byte[] imageSmall)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.type = type;
        this.layer = layer;
        this.copying = copying;
        this.image = image;
        this.imageSmall = imageSmall;
    }

    public ResourceTemplate(Database database, int id, String name, ResourceType type, int layer, boolean copying, DatabaseBitmap image, DatabaseBitmap imageSmall)
    {
        this.database = database;

        this.setID(id);
        this.setName(name);
        this.setType(type);
        this.setLayer(layer);
        this.setCopying(copying);
        this.setImage(image);
        this.setSmallImage(imageSmall);
    }

    public static ResourceTemplate select(Database database, int id)
    {
        SelectValues values = new SelectValues()
                .where(Table.COLUMN_ID, id);

        return database.getResourceTemplateTable().select(values);
    }

    public static List<ResourceTemplate> selectAll(Database database)
    {
        SelectValues values = new SelectValues();
        return database.getResourceTemplateTable().selectAll(values);
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

    public DatabaseBitmap getSmallImage()
    {
        DatabaseBitmap small = DatabaseBitmap.bytesToBitmap(this.imageSmall);

        if (small != null)
        {
            return small;
        }

        return this.getImage();
    }

    public void setSmallImage(DatabaseBitmap imageSmall)
    {
        this.imageSmall = DatabaseBitmap.bitmapToBytes(imageSmall);
    }

    public void setSmallImage(byte[] imageSmall)
    {
        this.imageSmall = imageSmall;
    }

    public static class Table extends TableBase<ResourceTemplate>
    {
        public static final String TABLE_NAME = "resource_templates";

        public static final TableColumn COLUMN_ID = new TableColumn("id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn("name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_TYPE = new TableColumn("type", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_LAYER = new TableColumn("layer", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COPYING = new TableColumn("copying", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_IMAGE = new TableColumn("image", ValueType.BLOB, false);
        public static final TableColumn COLUMN_IMAGE_SMALL = new TableColumn("image_small", ValueType.BLOB, true);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME);
            this.columns.add(COLUMN_TYPE);
            this.columns.add(COLUMN_LAYER);
            this.columns.add(COLUMN_COPYING);
            this.columns.add(COLUMN_IMAGE);
            this.columns.add(COLUMN_IMAGE_SMALL);
        }

        @Override
        protected ContentValues getValues(ResourceTemplate object)
        {
            ContentValues values = new ContentValues();

            values.put(COLUMN_ID.getName(), object.id);
            values.put(COLUMN_NAME.getName(), object.name);
            values.put(COLUMN_TYPE.getName(), object.type);
            values.put(COLUMN_LAYER.getName(), object.layer);
            values.put(COLUMN_COPYING.getName(), object.copying);
            values.put(COLUMN_IMAGE.getName(), object.image);
            values.put(COLUMN_IMAGE_SMALL.getName(), object.imageSmall);

            return values;
        }

        @Override
        protected ResourceTemplate getObject(ContentValues values)
        {
            int id = values.getAsInteger(COLUMN_ID.getName());
            String name = values.getAsString(COLUMN_NAME.getName());
            int type = values.getAsInteger(COLUMN_TYPE.getName());
            int layer = values.getAsInteger(COLUMN_TYPE.getName());
            int copying = values.getAsInteger(COLUMN_COPYING.getName());
            byte[] image = values.getAsByteArray(COLUMN_IMAGE.getName());
            byte[] imageSmall = values.getAsByteArray(COLUMN_IMAGE_SMALL.getName());

            return new ResourceTemplate(this.database, id, name, type, layer, copying, image, imageSmall);
        }
    }
}
