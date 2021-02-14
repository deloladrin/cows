package com.deloladrin.cows.database;

public class TableColumn
{
    private String name;
    private ValueType type;
    private boolean nullable;
    private boolean primary;
    private boolean autoIncrement;

    public TableColumn(String name, ValueType type, boolean nullable)
    {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
    }

    public TableColumn(String name, ValueType type, boolean nullable, boolean primary, boolean autoIncrement)
    {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.primary = primary;
        this.autoIncrement = autoIncrement;
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
