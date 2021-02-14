package com.deloladrin.cows.database;

import android.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectValues
{
    private Map<TableColumn, String> wheres;
    private Map<TableColumn, SelectOrder> orders;
    private int limit;

    public SelectValues()
    {
        this.wheres = new HashMap<>();
        this.orders = new HashMap<>();
        this.limit = -1;
    }

    public SelectValues where(TableColumn column, String value)
    {
        this.wheres.put(column, DatabaseUtils.sqlEscapeString(value));
        return this;
    }

    public SelectValues where(TableColumn column, int value)
    {
        this.wheres.put(column, Integer.toString(value));
        return this;
    }

    public SelectValues where(TableColumn column, long value)
    {
        this.wheres.put(column, Long.toString(value));
        return this;
    }

    public SelectValues orderBy(TableColumn column, SelectOrder order)
    {
        this.orders.put(column, order);
        return this;
    }

    public SelectValues limit(int limit)
    {
        this.limit = limit;
        return this;
    }

    public String getQuery(TableBase table)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM " + table.getName());

        /* Where clauses */
        if (this.wheres.size() > 0)
        {
            List<String> whereStrings = new ArrayList<>();
            builder.append(" WHERE ");

            for (Map.Entry<TableColumn, String> where : this.wheres.entrySet())
            {
                String column = where.getKey().getName();
                String value = where.getValue();

                whereStrings.add(column + " = " + value);
            }

            builder.append(String.join(" AND ", whereStrings));
        }

        /* Order by clauses */
        if (this.orders.size() > 0)
        {
            List<String> orderStrings = new ArrayList<>();
            builder.append(" ORDER BY ");

            for (Map.Entry<TableColumn, SelectOrder> order : this.orders.entrySet())
            {
                String column = order.getKey().getName();
                String value = "ASC";

                if (order.getValue() == SelectOrder.DESCENDING)
                {
                    value = "DESC";
                }

                orderStrings.add(column + " " + value);
            }

            builder.append(String.join(", ", orderStrings));
        }

        /* Limit clause */
        if (this.limit != -1)
        {
            builder.append(" LIMIT " + this.limit);
        }

        return builder.toString();
    }
}
