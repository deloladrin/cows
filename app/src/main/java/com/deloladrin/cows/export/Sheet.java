package com.deloladrin.cows.export;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFAnchor;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
import org.openxmlformats.schemas.drawingml.x2006.main.impl.CTPositiveSize2DImpl;

import java.util.ArrayList;
import java.util.List;

public class Sheet
{
    private String name;
    private List<Cell> cells;
    private List<Picture> pictures;

    public Sheet(String name)
    {
        this.name = name;
        this.cells = new ArrayList<>();
        this.pictures = new ArrayList<>();
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void add(Cell cell)
    {
        this.cells.add(cell);
    }

    public void add(List<Cell> cells)
    {
        this.cells.addAll(cells);
    }

    public void add(Picture picture)
    {
        this.pictures.add(picture);
    }

    public void export(XSSFWorkbook workbook, CellStyleRegistry styles)
    {
        /* Create sheet and export all cells */
        XSSFSheet sheet = workbook.createSheet(this.name);

        for (Cell cell : this.cells)
        {
            cell.export(sheet, styles);
        }

        /* Draw pictures */
        XSSFCreationHelper helper = workbook.getCreationHelper();
        XSSFDrawing drawing = sheet.createDrawingPatriarch();

        for (Picture picture : this.pictures)
        {
            int picID = workbook.addPicture(picture.getBytes(), Workbook.PICTURE_TYPE_PNG);

            XSSFClientAnchor anchor = helper.createClientAnchor();
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
            anchor.setCol1(picture.getX());
            anchor.setCol2(picture.getX() + picture.getWidth());
            anchor.setRow1(picture.getY());
            anchor.setRow2(picture.getY() + picture.getHeight());
            anchor.setDx2((int)(picture.getXOffset() * 1000000));
            anchor.setDy2((int)(picture.getYOffset() * 1000000));

            drawing.createPicture(anchor, picID);
        }

        /* Add watermark (for now hardcoded) */
        sheet.getHeader().setCenter("Salaš - zemědělské služby s.r.o. (https://www.zdravepaznehty.cz)");
        sheet.getFooter().setCenter("&C&P");
    }
}
