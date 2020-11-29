package com.deloladrin.cows.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.deloladrin.cows.R;
import com.deloladrin.cows.database.Database;
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
    private int image;
    private int color;

    public Resource(Database database, int id, String name, ResourceType type, int image, int color)
    {
        this.database = database;

        this.id = id;
        this.name = name;
        this.type = type;
        this.image = image;
        this.color = color;
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

    public int getImage()
    {
        return this.image;
    }

    public void setImage(int image)
    {
        this.image = image;
    }

    public Drawable getDrawable(Context context)
    {
        if (this.image != 0)
        {
            return context.getDrawable(this.image);
        }

        return null;
    }

    public int getColor()
    {
        return this.color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public static class Table extends TableBase<Resource>
    {
        public static final String TABLE_NAME = "resources";

        public static final TableColumn COLUMN_ID = new TableColumn(0, "id", ValueType.INTEGER, false, true, true);
        public static final TableColumn COLUMN_NAME = new TableColumn(1, "name", ValueType.TEXT, false);
        public static final TableColumn COLUMN_TYPE = new TableColumn(2, "type", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_IMAGE = new TableColumn(3, "image", ValueType.INTEGER, false);
        public static final TableColumn COLUMN_COLOR = new TableColumn(4, "color", ValueType.INTEGER, false);

        public Table(Database database)
        {
            super(database, TABLE_NAME);

            this.columns.add(COLUMN_ID);
            this.columns.add(COLUMN_NAME);
            this.columns.add(COLUMN_TYPE);
            this.columns.add(COLUMN_IMAGE);
            this.columns.add(COLUMN_COLOR);
        }

        @Override
        public void create(SQLiteDatabase db)
        {
            super.create(db);

            Context context = this.database.getContext();
            List<Resource> defaults = new ArrayList<>();

            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_bandage), ResourceType.HOOF, R.drawable.resource_bandage, 0));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_wood), ResourceType.FINGER, R.drawable.resource_block_wood, 0));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_wood_xxl), ResourceType.FINGER, R.drawable.resource_block_wood_xxl, 0));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_tp), ResourceType.FINGER, R.drawable.resource_block_tp, 0));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_tp_xxl), ResourceType.FINGER, R.drawable.resource_block_tp_xxl, 0));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_iron_half), ResourceType.FINGER, 0, 0));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_block_iron), ResourceType.FINGER, 0, 0));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_synulox), ResourceType.COW, 0, 0));
            defaults.add(new Resource(this.database, -1, context.getString(R.string.resource_synulox_2x), ResourceType.COW, 0, 0));

            this.insertAll(db, defaults);
        }

        @Override
        protected ValueParams getParams(Resource object)
        {
            ValueParams params = new ValueParams();

            params.put(COLUMN_ID, object.getID());
            params.put(COLUMN_NAME, object.getName());
            params.put(COLUMN_TYPE, object.getType().getID());
            params.put(COLUMN_IMAGE, object.getImage());
            params.put(COLUMN_COLOR, object.getColor());

            return params;
        }

        @Override
        protected Resource getObject(Cursor cursor)
        {
            int id = cursor.getInt(COLUMN_ID.getID());
            String name = cursor.getString(COLUMN_NAME.getID());
            ResourceType type = ResourceType.parse(cursor.getInt(COLUMN_TYPE.getID()));
            int image = cursor.getInt(COLUMN_IMAGE.getID());
            int color = cursor.getInt(COLUMN_COLOR.getID());

            return new Resource(this.database, id, name, type, image, color);
        }
    }
}
