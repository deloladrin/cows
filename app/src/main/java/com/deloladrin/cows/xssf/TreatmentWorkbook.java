package com.deloladrin.cows.xssf;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.HoofMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.Status;
import com.deloladrin.cows.data.TargetMask;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.database.DatabaseActivity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreatmentWorkbook extends Workbook
{
    private Sheet sheet;
    private LocalDate repeatDate;

    private Cell title;
    private Cell date;
    private Cell company;
    private Cell companyGroup;

    private Cell index;
    private Cell cow;
    private Cell collar;
    private Cell group;
    private Cell diagnosis;
    private Cell target;
    private Cell repeat;
    private Cell extra;

    private int currentIndex;
    private int currentY;

    public TreatmentWorkbook(DatabaseActivity parent)
    {
        super(parent);

        /* Load all children */
        this.sheet = this.getSheet(R.string.xssf_treatment);

        this.title = this.getCell(this.sheet, 4, 1);
        this.date = this.getCell(this.sheet, 0, 2);
        this.company = this.getCell(this.sheet, 4, 3);
        this.companyGroup = this.getCell(this.sheet, 4, 4);

        this.currentIndex = 1;
        this.currentY = 6;

        this.index = this.getCell(this.sheet, 0, this.currentY);
        this.cow = this.getCell(this.sheet, 1, this.currentY);
        this.collar = this.getCell(this.sheet, 2, this.currentY);
        this.group = this.getCell(this.sheet, 3, this.currentY);
        this.diagnosis = this.getCell(this.sheet, 4, this.currentY);
        this.target = this.getCell(this.sheet, 5, this.currentY);
        this.repeat = this.getCell(this.sheet, 6, this.currentY);
        this.extra = this.getCell(this.sheet, 7, this.currentY);
        this.currentY++;

        /* Apply styles */
        this.title.setCellStyle(this.getCenteredBoldStyle());
        this.date.setCellStyle(this.getCenteredStyle());
        this.company.setCellStyle(this.getCenteredStyle());
        this.companyGroup.setCellStyle(this.getCenteredStyle());

        this.index.setCellStyle(this.getHeaderStartStyle());
        this.setColumnWidth(this.index, 6.0f);
        this.cow.setCellStyle(this.getHeaderMiddleStyle());
        this.setColumnWidth(this.cow, 7.0f);
        this.collar.setCellStyle(this.getHeaderMiddleStyle());
        this.setColumnWidth(this.collar, 5.75f);
        this.group.setCellStyle(this.getHeaderMiddleStyle());
        this.setColumnWidth(this.group, 5.75f);
        this.diagnosis.setCellStyle(this.getHeaderMiddleStyle());
        this.setColumnWidth(this.diagnosis, 37.5f);
        this.target.setCellStyle(this.getHeaderMiddleStyle());
        this.setColumnWidth(this.target, 7.0f);
        this.repeat.setCellStyle(this.getHeaderMiddleStyle());
        this.setColumnWidth(this.repeat, 7.0f);
        this.extra.setCellStyle(this.getHeaderEndStyle());
        this.setColumnWidth(this.extra, 15.0f);

        CellRangeAddress dateMerge = new CellRangeAddress(2, 2, 0, 2);
        this.sheet.addMergedRegion(dateMerge);

        /* Load default values */
        this.title.setCellValue(this.getString(R.string.xssf_treatment_title));

        this.index.setCellValue(this.getString(R.string.xssf_treatment_index));
        this.cow.setCellValue(this.getString(R.string.xssf_treatment_cow));
        this.collar.setCellValue(this.getString(R.string.xssf_treatment_collar));
        this.group.setCellValue(this.getString(R.string.xssf_treatment_group));
        this.diagnosis.setCellValue(this.getString(R.string.xssf_treatment_diagnosis));
        this.target.setCellValue(this.getString(R.string.xssf_treatment_target));
        this.repeat.setCellValue(this.getString(R.string.xssf_treatment_repeat));
        this.extra.setCellValue(this.getString(R.string.xssf_treatment_extra));
    }

    public LocalDate getRepeatDate()
    {
        return this.repeatDate;
    }

    public void setRepeatDate(LocalDate repeatDate)
    {
        this.repeatDate = repeatDate;
    }

    public void setDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String string = date.format(formatter);

        this.date.setCellValue(string);
    }

    public void setDate(LocalDate start, LocalDate end)
    {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String string = start.format(formatter1) + " - " + end.format(formatter2);

        this.date.setCellValue(string);
    }

    public void setCompany(Company company)
    {
        String name = company.getName();
        String group = company.getGroup();

        this.company.setCellValue(name);

        if (group != null)
        {
            this.companyGroup.setCellValue(group);
        }
    }

    public void add(Treatment treatment)
    {
        Cow cow = treatment.getCow();

        /* Add cow info */
        this.addIndex();
        this.addCow(cow);
        this.addCollar(cow);
        this.addGroup(cow);

        /* Add diagnosis info */
        int height = this.addTreatment(treatment);

        this.currentIndex++;
        this.currentY += Math.max(1, height);
    }

    private void addIndex()
    {
        Cell cell = this.getCell(this.sheet, this.index.getColumnIndex(), this.currentY);
        cell.setCellStyle(this.getCenteredStyle());
        cell.setCellValue(this.currentIndex);
    }

    private void addCow(Cow cow)
    {
        Cell cell = this.getCell(this.sheet, this.cow.getColumnIndex(), this.currentY);
        cell.setCellStyle(this.getCenteredStyle());
        cell.setCellValue(cow.getNumber());
    }

    private void addCollar(Cow cow)
    {
        Cell cell = this.getCell(this.sheet, this.collar.getColumnIndex(), this.currentY);
        cell.setCellStyle(this.getCenteredBoldStyle());

        int collar = cow.getCollar();

        if (collar != 0)
        {
            cell.setCellValue(collar);
        }
        else
        {
            cell.setCellValue("—");
        }
    }

    private void addGroup(Cow cow)
    {
        Cell cell = this.getCell(this.sheet, this.group.getColumnIndex(), this.currentY);
        cell.setCellStyle(this.getCenteredStyle());

        String group = cow.getGroup();

        if (group == null)
        {
            group = "—";
        }

        cell.setCellValue(group);
    }

    private int addTreatment(Treatment treatment)
    {
        int height = 0;

        /* Add whole marker */
        if (treatment.getType() == TreatmentType.WHOLE)
        {
            String wholeString = this.getString(R.string.xssf_treatment_whole);

            Cell wholeCell = this.getCell(this.sheet, this.diagnosis.getColumnIndex(), this.currentY);
            wholeCell.setCellValue(wholeString);

            height++;
        }

        /* Add repeat */
        if (this.shouldRepeat(treatment))
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.");
            String repeatDate = this.repeatDate.format(formatter);

            Cell repeatCell = this.getCell(this.sheet, this.repeat.getColumnIndex(), this.currentY + height);
            repeatCell.setCellStyle(this.getCenteredStyle());
            repeatCell.setCellValue(repeatDate);
        }

        /* Add diagnoses + resources */
        Map<TargetMask, List<Diagnosis>> diagnoses = this.getDiagnosisMap(treatment);
        Map<TargetMask, List<Resource>> resources = this.getResourceMap(treatment, diagnoses);

        for (HoofMask mask : HoofMask.values())
        {
            height += this.addEntries(height, mask.getLeftFinger(), diagnoses, resources);
            height += this.addEntries(height, mask.getRightFinger(), diagnoses, resources);
            height += this.addEntries(height, mask, diagnoses, resources);
        }

        /* Add statuses and comment */
        height += this.addOptionals(height, treatment);

        return height;
    }

    private boolean shouldRepeat(Treatment treatment)
    {
        for (Diagnosis diagnosis : treatment.getDiagnoses())
        {
            if (diagnosis.getState() != DiagnosisState.HEALED)
            {
                return true;
            }
        }

        return false;
    }

    private int addEntries(int offset, TargetMask mask, Map<TargetMask, List<Diagnosis>> diagnoses, Map<TargetMask, List<Resource>> resources)
    {
        /* Add all diagnoses */
        int diagnosisCount = 0;

        for (Diagnosis diagnosis : diagnoses.get(mask))
        {
            String name = diagnosis.getName();
            String comment = diagnosis.getComment();

            if (comment != null)
            {
                name = name + " — " + comment;
            }

            Cell nameCell = this.getCell(this.sheet, this.diagnosis.getColumnIndex(), this.currentY + offset + diagnosisCount);
            nameCell.setCellValue(name);

            Cell targetCell = this.getCell(this.sheet, this.target.getColumnIndex(), this.currentY + offset + diagnosisCount);
            targetCell.setCellStyle(this.getCenteredStyle());
            targetCell.setCellValue(mask.getName(this.parent));

            diagnosisCount++;
        }

        /* Add all resources */
        int resourceCount = 0;

        for (Resource resource : resources.get(mask))
        {
            Cell extraCell = this.getCell(this.sheet, this.extra.getColumnIndex(), this.currentY + offset + resourceCount);
            extraCell.setCellStyle(this.getCenteredStyle());
            extraCell.setCellValue(resource.getName() + "!");

            resourceCount++;
        }

        return Math.max(diagnosisCount, resourceCount);
    }

    private int addOptionals(int offset, Treatment treatment)
    {
        /* Add comment if any */
        String comment = treatment.getComment();
        int commentCount = 0;

        if (comment != null)
        {
            Cell commentCell = this.getCell(this.sheet, this.diagnosis.getColumnIndex(), this.currentY + offset);
            commentCell.setCellValue(comment);

            commentCount = 1;
        }

        /* Add statuses */
        int statusCount = 0;

        for (Status status : treatment.getStatuses())
        {
            Cell statusCell = this.getCell(this.sheet, this.extra.getColumnIndex(), this.currentY + offset + statusCount);
            statusCell.setCellStyle(this.getCenteredBoldStyle());
            statusCell.setCellValue(status.getName() + "!");

            statusCount++;
        }

        return Math.max(commentCount, statusCount);
    }

    private Map<TargetMask, List<Diagnosis>> getDiagnosisMap(Treatment treatment)
    {
        Map<TargetMask, List<Diagnosis>> map = new HashMap<>();

        /* Setup empty map */
        for (HoofMask mask : HoofMask.values())
        {
            map.put(mask.getLeftFinger(), new ArrayList<>());
            map.put(mask.getRightFinger(), new ArrayList<>());
            map.put(mask, new ArrayList<>());
        }

        /* Load diagnoses */
        for (Diagnosis diagnosis : treatment.getDiagnoses())
        {
            map.get(diagnosis.getTarget()).add(diagnosis);
        }

        return map;
    }

    private Map<TargetMask, List<Resource>> getResourceMap(Treatment treatment, Map<TargetMask, List<Diagnosis>> diagnosisMap)
    {
        Map<TargetMask, List<Resource>> map = new HashMap<>();

        /* Setup empty map */
        for (HoofMask mask : HoofMask.values())
        {
            map.put(mask.getLeftFinger(), new ArrayList<>());
            map.put(mask.getRightFinger(), new ArrayList<>());
            map.put(mask, new ArrayList<>());
        }

        /* Load resources */
        List<Resource> resources = treatment.getResources();
        resources.sort((a, b) -> b.getTemplate().getLayer() - a.getTemplate().getLayer());

        for (Resource resource : resources)
        {
            if (!resource.isCopy())
            {
                TargetMask target = resource.getTarget();

                switch (resource.getTemplate().getType())
                {
                    case FINGER:
                    case FINGER_INVERTED:

                        this.tryAddResource(resource, target, map, diagnosisMap);

                        break;

                    case HOOF:

                        HoofMask hoofTarget = (HoofMask) target;

                        if (!tryAddResource(resource, hoofTarget.getLeftFinger(), map, diagnosisMap))
                        {
                            if (!tryAddResource(resource, hoofTarget.getRightFinger(), map, diagnosisMap))
                            {
                                tryAddResource(resource, hoofTarget, map, diagnosisMap);
                            }
                        }

                        break;
                }
            }
        }

        return map;
    }

    private boolean tryAddResource(Resource resource, TargetMask mask, Map<TargetMask, List<Resource>> resources, Map<TargetMask, List<Diagnosis>> diagnoses)
    {
        if (diagnoses.get(mask).size() > 0)
        {
            resources.get(mask).add(resource);
            return true;
        }

        return false;
    }
}
