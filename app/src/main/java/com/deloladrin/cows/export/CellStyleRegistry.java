package com.deloladrin.cows.export;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class CellStyleRegistry
{
    private XSSFWorkbook workbook;
    private List<XSSFCellStyle> styles;

    private XSSFFont bold;
    private XSSFFont italic;
    private XSSFFont boldItalic;

    public CellStyleRegistry(XSSFWorkbook workbook)
    {
        this.workbook = workbook;
        this.styles = new ArrayList<>();

        this.createFonts();
    }

    public XSSFWorkbook getWorkbook()
    {
        return this.workbook;
    }

    public XSSFCellStyle getStyleFor(Cell cell)
    {
        /* Check if we already have that style */
        for (XSSFCellStyle style : this.styles)
        {
            if (this.isValidStyle(cell, style))
            {
                return style;
            }
        }

        /* Create new style */
        XSSFCellStyle style = this.workbook.createCellStyle();

        if (cell.isBold() && cell.isItalic())
        {
            style.setFont(this.boldItalic);
        }
        else if (cell.isBold())
        {
            style.setFont(this.bold);
        }
        else if (cell.isItalic())
        {
            style.setFont(this.italic);
        }

        style.setAlignment(cell.getHorizontalAlignment());
        style.setVerticalAlignment(cell.getVerticalAlignment());

        style.setBorderTop(cell.getTopBorder());
        style.setBorderRight(cell.getRightBorder());
        style.setBorderBottom(cell.getBottomBorder());
        style.setBorderLeft(cell.getLeftBorder());

        this.styles.add(style);
        return style;
    }

    private void createFonts()
    {
        this.bold = this.workbook.createFont();
        this.bold.setBold(true);

        this.italic = this.workbook.createFont();
        this.italic.setItalic(true);

        this.boldItalic = this.workbook.createFont();
        this.boldItalic.setBold(true);
        this.boldItalic.setItalic(true);
    }

    private boolean isValidStyle(Cell cell, XSSFCellStyle style)
    {
        return cell.isBold() == style.getFont().getBold() &&
               cell.isItalic() == style.getFont().getItalic() &&

               cell.getHorizontalAlignment() == style.getAlignmentEnum() &&
               cell.getVerticalAlignment() == style.getVerticalAlignmentEnum() &&

               cell.getTopBorder() == style.getBorderTopEnum() &&
               cell.getRightBorder() == style.getBorderRightEnum() &&
               cell.getBottomBorder() == style.getBorderBottomEnum() &&
               cell.getLeftBorder() == style.getBorderLeftEnum();
    }
}
