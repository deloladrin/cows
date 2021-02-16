package com.deloladrin.cows.export;

import android.content.Context;
import android.os.Environment;

import com.deloladrin.cows.R;
import com.deloladrin.cows.dialogs.InfoDialog;
import com.deloladrin.cows.dialogs.YesNoDialog;

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

    public void save(String name)
    {
        /* Get correct location */
        String appName = this.context.getString(R.string.app_name);
        Path outDir = Paths.get(Environment.getExternalStorageDirectory().getAbsolutePath(), appName);
        Path outFile = outDir.resolve(name + ".xlsx");

        if (Files.exists(outFile))
        {
            /* Ask to overwrite file */
            YesNoDialog dialog = new YesNoDialog(this.context);
            dialog.setText(R.string.dialog_export_exists, name);

            dialog.setOnYesListener((YesNoDialog d) ->
            {
                this.saveToFile(name, outDir, outFile);
            });

            dialog.show();
        }
        else
        {
            this.saveToFile(name, outDir, outFile);
        }
    }

    private void saveToFile(String name, Path outDir, Path outFile)
    {
        /* Export everything */
        XSSFWorkbook workbook = new XSSFWorkbook();
        CellStyleRegistry styles = new CellStyleRegistry(workbook);

        for (Sheet sheet : this.sheets)
        {
            sheet.export(workbook, styles);
        }

        try
        {
            /* Write out to a file */
            Files.createDirectories(outDir);

            FileOutputStream stream = new FileOutputStream(outFile.toFile());
            workbook.write(stream);
            stream.close();

            /* Show success dialog */
            InfoDialog dialog = new InfoDialog(this.context);
            dialog.setText(R.string.dialog_export_success, name);
            dialog.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();

            /* Show error dialog */
            InfoDialog dialog = new InfoDialog(this.context);
            dialog.setText(R.string.dialog_export_error, name);
            dialog.show();
        }
    }
}
