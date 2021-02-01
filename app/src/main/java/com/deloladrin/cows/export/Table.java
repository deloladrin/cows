package com.deloladrin.cows.export;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table
{
    private Sheet sheet;
    private int x;
    private int y;

    private Map<TableColumn, Cell> headers;
    private Map<TableColumn, List<Cell>> columns;
    private List<Integer> blockEnds;

    private int current;

    public Table(Sheet sheet, int x, int y)
    {
        this.sheet = sheet;
        this.x = x;
        this.y = y;

        this.headers = new LinkedHashMap<>();
        this.columns = new LinkedHashMap<>();
        this.blockEnds = new ArrayList<>();

        this.current = 0;
    }

    public void addColumn(TableColumn column)
    {
        /* Create header cell */
        int index = this.columns.size();

        Cell cell = new Cell(this.x + index, this.y);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setWidth(column.getWidth());
        cell.setValue(column.getName());

        this.headers.put(column, cell);
        this.sheet.add(cell);

        /* Create row collection */
        List<Cell> cells = new ArrayList<>();
        this.columns.put(column, cells);
    }

    public Map<TableColumn, Cell> addRow()
    {
        Map<TableColumn, Cell> row = new HashMap<>();
        int index = 0;

        for (TableColumn column : this.columns.keySet())
        {
            Cell cell = new Cell(this.x + index, this.y + this.current + 1);

            /* Inherit properties */
            cell.setBold(column.isBold());
            cell.setItalic(column.isItalic());
            cell.setHorizontalAlignment(column.getHorizontalAlignment());
            cell.setVerticalAlignment(column.getVerticalAlignment());

            row.put(column, cell);

            this.columns.get(column).add(cell);
            this.sheet.add(cell);

            index++;
        }

        this.current++;
        return row;
    }

    public void endBlock()
    {
        this.blockEnds.add(this.current - 1);
    }

    public void updateBorders()
    {
        /* Update headers */
        int columnIndex = 0;

        for (Cell cell : this.headers.values())
        {
            cell.setTopBorder(BorderStyle.MEDIUM);
            cell.setBottomBorder(BorderStyle.MEDIUM);

            if (columnIndex == 0)
            {
                cell.setLeftBorder(BorderStyle.MEDIUM);
                cell.setRightBorder(BorderStyle.THIN);
            }
            else if (columnIndex == this.headers.size() - 1)
            {
                cell.setLeftBorder(BorderStyle.THIN);
                cell.setRightBorder(BorderStyle.MEDIUM);
            }
            else
            {
                cell.setLeftBorder(BorderStyle.THIN);
                cell.setRightBorder(BorderStyle.THIN);
            }

            columnIndex++;
        }

        /* Update columns */
        columnIndex = 0;

        for (List<Cell> cells : this.columns.values())
        {
            int rowIndex = 0;

            for (Cell cell : cells)
            {
                if (columnIndex == 0)
                {
                    cell.setLeftBorder(BorderStyle.MEDIUM);
                    cell.setRightBorder(BorderStyle.THIN);
                }
                else if (columnIndex == this.headers.size() - 1)
                {
                    cell.setLeftBorder(BorderStyle.THIN);
                    cell.setRightBorder(BorderStyle.MEDIUM);
                }
                else
                {
                    cell.setLeftBorder(BorderStyle.THIN);
                    cell.setRightBorder(BorderStyle.THIN);
                }

                if (this.blockEnds.contains(rowIndex))
                {
                    cell.setBottomBorder(BorderStyle.THIN);
                }

                if (rowIndex == cells.size() - 1)
                {
                    cell.setBottomBorder(BorderStyle.MEDIUM);
                }

                rowIndex++;
            }

            columnIndex++;
        }
    }

    public Sheet getSheet()
    {
        return this.sheet;
    }

    public int getX()
    {
        return this.x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return this.y;
    }

    public void setY(int y)
    {
        this.y = y;
    }
}
