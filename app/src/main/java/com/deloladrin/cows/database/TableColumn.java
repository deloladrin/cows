package com.deloladrin.cows.database;

public class TableColumn
{
    private int id;
    private String name;
    private ValueType type;
    private boolean nullable;
    private boolean primary;
    private boolean autoIncrement;

    public TableColumn(int id, String name, ValueType type, boolean nullable)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.primary = false;
        this.autoIncrement = false;
    }

    public TableColumn(int id, String name, ValueType type, boolean nullable, boolean primary, boolean autoIncrement)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.primary = primary;
        this.autoIncrement = autoIncrement;
    }

    public int getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public ValueType getType()
    {
        return this.type;
    }

    public boolean isNullable()
    {
        return this.nullable;
    }

    public boolean isPrimary()
    {
        return this.primary;
    }

    public boolean isAutoIncrement()
    {
        return this.autoIncrement;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name + " " + this.type);

        if (!this.nullable)
        {
            sb.append(" NOT NULL");
        }

        if (this.primary)
        {
            sb.append(" PRIMARY KEY");
        }

        if (this.autoIncrement)
        {
            sb.append(" AUTOINCREMENT");
        }

        return sb.toString();
    }
}
