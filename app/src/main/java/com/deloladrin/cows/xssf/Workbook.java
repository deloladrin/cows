package com.deloladrin.cows.xssf;

import android.content.Context;

import com.deloladrin.cows.database.DatabaseActivity;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class Workbook extends XSSFWorkbook
{
    protected DatabaseActivity parent;

    private Font bold;
    private CellStyle centered;
    private CellStyle centeredBold;

    private CellStyle headerStart;
    private CellStyle headerMiddle;
    private CellStyle headerEnd;

    public Workbook(DatabaseActivity parent)
    {
        super();
        this.parent = parent;

        /* Create styles */
        this.bold = this.createFont();
        this.bold.setBold(true);

        this.centered = this.createCellStyle();
        this.centered.setAlignment(HorizontalAlignment.CENTER);

        this.centeredBold = this.createCellStyle();
        this.centeredBold.setAlignment(HorizontalAlignment.CENTER);
        this.centeredBold.setFont(this.bold);

        this.headerStart = this.createCellStyle();
        this.headerStart.setAlignment(HorizontalAlignment.CENTER);
        this.headerStart.setBorderTop(BorderStyle.MEDIUM);
        this.headerStart.setBorderRight(BorderStyle.THIN);
        this.headerStart.setBorderBottom(BorderStyle.MEDIUM);
        this.headerStart.setBorderLeft(BorderStyle.MEDIUM);

        this.headerMiddle = this.createCellStyle();
        this.headerMiddle.setAlignment(HorizontalAlignment.CENTER);
        this.headerMiddle.setBorderTop(BorderStyle.MEDIUM);
        this.headerMiddle.setBorderRight(BorderStyle.THIN);
        this.headerMiddle.setBorderBottom(BorderStyle.MEDIUM);
        this.headerMiddle.setBorderLeft(BorderStyle.THIN);

        this.headerEnd = this.createCellStyle();
        this.headerEnd.setAlignment(HorizontalAlignment.CENTER);
        this.headerEnd.setBorderTop(BorderStyle.MEDIUM);
        this.headerEnd.setBorderRight(BorderStyle.MEDIUM);
        this.headerEnd.setBorderBottom(BorderStyle.MEDIUM);
        this.headerEnd.setBorderLeft(BorderStyle.THIN);
    }

    public boolean save(String path)
    {
        try
        {
            FileOutputStream stream = new FileOutputStream(path);
            this.write(stream);
            stream.close();

            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public String getString(int resource)
    {
        return this.parent.getString(resource);
    }

    public CellStyle getCenteredStyle()
    {
        return this.centered;
    }

    public CellStyle getCenteredBoldStyle()
    {
        return this.centeredBold;
    }

    public CellStyle getHeaderStartStyle()
    {
        return this.headerStart;
    }

    public CellStyle getHeaderMiddleStyle()
    {
        return this.headerMiddle;
    }

    public CellStyle getHeaderEndStyle()
    {
        return this.headerEnd;
    }

    @Override
    public XSSFSheet getSheet(String name)
    {
        XSSFSheet sheet = super.getSheet(name);

        if (sheet != null)
        {
            return sheet;
        }

        return this.createSheet(name);
    }

    public XSSFSheet getSheet(int nameID)
    {
        String name = this.getString(nameID);
        return this.getSheet(name);
    }

    public Cell getCell(Sheet sheet, int x, int y)
    {
        Row row = sheet.getRow(y);

        /* Create new row if required */
        if (row == null)
        {
            row = sheet.createRow(y);
        }

        Cell cell = row.getCell(x);

        /* Create new cell if required */
        if (cell == null)
        {
            return row.createCell(x);
        }

        return cell;
    }

    public void setColumnWidth(Cell cell, float width)
    {
        int finalWidth = Math.round(width * 256);
        cell.getSheet().setColumnWidth(cell.getColumnIndex(), finalWidth);
    }
}
