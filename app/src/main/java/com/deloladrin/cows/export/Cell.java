package com.deloladrin.cows.export;

import android.content.Context;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class Cell
{
    private int x;
    private int y;
    private int xspan;
    private int yspan;

    private float width;
    private float height;

    private boolean bold;
    private boolean italic;

    private HorizontalAlignment horizontal;
    private VerticalAlignment vertical;

    private BorderStyle top;
    private BorderStyle right;
    private BorderStyle bottom;
    private BorderStyle left;

    private Object value;

    public Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.xspan = 1;
        this.yspan = 1;

        this.width = Float.NaN;
        this.height = Float.NaN;

        this.horizontal = HorizontalAlignment.LEFT;
        this.vertical = VerticalAlignment.TOP;

        this.top = BorderStyle.NONE;
        this.right = BorderStyle.NONE;
        this.bottom = BorderStyle.NONE;
        this.left = BorderStyle.NONE;
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

    public int getXSpan()
    {
        return this.xspan;
    }

    public void setXSpan(int xspan)
    {
        this.xspan = xspan;
    }

    public int getYSpan()
    {
        return this.yspan;
    }

    public void setYSpan(int yspan)
    {
        this.yspan = yspan;
    }

    public float getWidth()
    {
        return this.width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return this.height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public boolean isBold()
    {
        return this.bold;
    }

    public void setBold(boolean bold)
    {
        this.bold = bold;
    }

    public boolean isItalic()
    {
        return this.italic;
    }

    public void setItalic(boolean italic)
    {
        this.italic = italic;
    }

    public HorizontalAlignment getHorizontalAlignment()
    {
        return this.horizontal;
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontal)
    {
        this.horizontal = horizontal;
    }

    public VerticalAlignment getVerticalAlignment()
    {
        return this.vertical;
    }

    public void setVerticalAlignment(VerticalAlignment vertical)
    {
        this.vertical = vertical;
    }

    public BorderStyle getTopBorder()
    {
        return this.top;
    }

    public void setTopBorder(BorderStyle top)
    {
        this.top = top;
    }

    public BorderStyle getRightBorder()
    {
        return this.right;
    }

    public void setRightBorder(BorderStyle right)
    {
        this.right = right;
    }

    public BorderStyle getBottomBorder()
    {
        return this.bottom;
    }

    public void setBottomBorder(BorderStyle bottom)
    {
        this.bottom = bottom;
    }

    public BorderStyle getLeftBorder()
    {
        return this.left;
    }

    public void setLeftBorder(BorderStyle left)
    {
        this.left = left;
    }

    public Object getValue()
    {
        return this.value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public void setValue(Context context, int resource)
    {
        this.value = context.getString(resource);
    }

    public void export(XSSFSheet sheet, CellStyleRegistry styles)
    {
        /* Attempt to get row */
        XSSFRow row = sheet.getRow(this.y);

        if (row == null)
        {
            row = sheet.createRow(this.y);
        }

        /* Attempt to get cell */
        XSSFCell cell = row.getCell(this.x);

        if (cell == null)
        {
            cell = row.createCell(this.x);
        }

        /* Change column and row dimensions */
        if (!Float.isNaN(this.width))
        {
            sheet.setColumnWidth(this.x, (int)(this.width * 256));
        }

        if (!Float.isNaN(this.height))
        {
            row.setHeight((short)(this.height * 256));
        }

        /* Apply spans and style */
        if (this.xspan > 1 || this.yspan > 1)
        {
            CellRangeAddress address = new CellRangeAddress(this.y, this.y + this.yspan - 1, this.x, this.x + this.xspan - 1);
            sheet.addMergedRegion(address);
        }

        XSSFCellStyle style = styles.getStyleFor(this);
        cell.setCellStyle(style);

        /* Update value */
        if (this.value != null)
        {
            if (this.value instanceof Integer)
            {
                cell.setCellValue((int)this.value);
            }
            else
            {
                cell.setCellValue(this.value.toString());
            }
        }
    }
}
