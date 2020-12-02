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
    private ResourceType type;
    private DatabaseBitmap image;

    public Resource(Database database, int id, String name, ResourceType type, DatabaseBitmap image)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.type = type;
        this.image = image;
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
        int resourceMask = 0;

        for (Resource resource : resources)
        {
            int maskID = (1 << resource.getID());
            resourceMask |= maskID;
        }

        return resourceMask;
    }

    public void insert()
    {
        this.database.getResourceTable().insert(this);
    }

    public void update()
    {
        this.database.getResourceTable().update(this);
    }

    public void delete()
    {
        this.database.getResourceTable().delete(this);
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
        return this.type;
    }

    public void setType(ResourceType type)
    {
        this.type = type;
    }

    public DatabaseBitmap getImage()
    {
        return this.image;
    }

    public void setImage(DatabaseBitmap image)
    {
        this.image = image;
    }

    public static class Table extends TableBase<Resource>
    {
        public static final String TABLE_NAME = "resources";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn(1, "name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_TYPE = new TableColumn(2, "type", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_IMAGE = new TableColumn(3, "image", ValueType.BLOB, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME);
            this.columns.add(COLUMN_TYPE);
            this.columns.add(COLUMN_IMAGE);
        }

        @Override
        public void create(SQLiteDatabase db)
        {
            super.create(db);

            Context context = this.database.getContext();
            List<Resource> defaults = new ArrayList<>();

            /* Default resources */
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_bandage), ResourceType.HOOF, new DatabaseBitmap(context, R.drawable.resource_bandage)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_wood), ResourceType.FINGER_INVERTED, new DatabaseBitmap(context, R.drawable.resource_block_wood)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_wood_xxl), ResourceType.FINGER_INVERTED, new DatabaseBitmap(context, R.drawable.resource_block_wood_xxl)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_tp), ResourceType.FINGER_INVERTED, new DatabaseBitmap(context, R.drawable.resource_block_tp)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_tp_xxl), ResourceType.FINGER_INVERTED, new DatabaseBitmap(context, R.drawable.resource_block_tp_xxl)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_iron_half), ResourceType.FINGER_INVERTED, new DatabaseBitmap(context, R.drawable.resource_block_iron_half)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_iron), ResourceType.HOOF, new DatabaseBitmap(context, R.drawable.resource_block_iron)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_synulox), ResourceType.HOOF, new DatabaseBitmap(context, R.drawable.resource_bandage)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_synulox_2x), ResourceType.HOOF, new DatabaseBitmap(context, R.drawable.resource_bandage)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_antibiotics), ResourceType.COW, new DatabaseBitmap(context, R.drawable.resource_antibiotics)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_checkup), ResourceType.COW, new DatabaseBitmap(context, R.drawable.resource_checkup)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_no_bathing), ResourceType.COW, new DatabaseBitmap(context, R.drawable.resource_no_bathing)));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_take_out), ResourceType.COW, new DatabaseBitmap(context, R.drawable.resource_take_out)));

            this.insertAll(db, defaults);
        }

        @Override
        protected ValueParams getParams(Resource object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.getID());
            params.put(COLUMN_NAME, object.getName());
            params.put(COLUMN_TYPE, object.getType().getID());
            params.put(COLUMN_IMAGE, object.getImage().getHexBytes());

            return params;
        }

        @Override
        protected Resource getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            String name = cursor.getString(COLUMN_NAME.getID());
            ResourceType type = ResourceType.parse(cursor.getInt(COLUMN_TYPE.getID()));
            DatabaseBitmap image = new DatabaseBitmap(cursor.getBlob(COLUMN_IMAGE.getID()));

            return new Resource(this.database, id, name, type, image);
        }
    }
}
