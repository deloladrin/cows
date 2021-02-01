package com.deloladrin.cows.export;

import android.content.Context;
import android.os.Environment;

import com.deloladrin.cows.R;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Workbook
{
    protected Context context;
    protected List<Sheet> sheets;

    public Workbook(Context context)
    {
        this.context = context;
        this.sheets = new ArrayList<>();
    }

    public void add(Sheet sheet)
    {
        this.sheets.add(sheet);
    }

    public boolean save(String name)
    {
        /* Get correct location */
        String appName = this.context.getString(R.string.app_name);
        Path outDir = Paths.get(Environment.getExternalStorageDirectory().getAbsolutePath(), appName);
        Path outFile = outDir.resolve(name + ".xlsx");

        try
        {
            /* Export everything */
            XSSFWorkbook workbook = new XSSFWorkbook();
            CellStyleRegistry styles = new CellStyleRegistry(workbook);

            for (Sheet sheet : this.sheets)
            {
                sheet.export(workbook, styles);
            }

            /* Write out to a file */
            Files.createDirectories(outDir);
            
            FileOutputStream stream = new FileOutputStream(outFile.toFile());
            workbook.write(stream);
            stream.close();

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
