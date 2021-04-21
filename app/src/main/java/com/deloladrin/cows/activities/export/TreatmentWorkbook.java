package com.deloladrin.cows.activities.export;

import android.content.Context;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.FingerMask;
import com.deloladrin.cows.data.HoofMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.Status;
import com.deloladrin.cows.data.TargetMask;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.export.Cell;
import com.deloladrin.cows.export.Picture;
import com.deloladrin.cows.export.Sheet;
import com.deloladrin.cows.export.Table;
import com.deloladrin.cows.export.TableColumn;
import com.deloladrin.cows.export.Workbook;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TreatmentWorkbook extends Workbook
{
    private ExportSettings settings;

    private Sheet sheet;
    private LocalDate repeatDate;

    private Cell title;
    private Cell date;
    private Cell company;
    private Cell companyGroup;

    private Table table;
    private TableColumn index;
    private TableColumn number;
    private TableColumn collar;
    private TableColumn group;
    private TableColumn diagnosis;
    private TableColumn target;
    private TableColumn repeat;
    private TableColumn extra;

    private Picture picture;

    private int current;

    public TreatmentWorkbook(Context context, ExportSettings settings)
    {
        super(context);
        this.settings = settings;

        String name = context.getString(R.string.export_treatment);
        this.sheet = new Sheet(name);

        this.current = 1;

        /* Load header cells */
        this.title = new Cell(4, 1);
        this.title.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.title.setBold(true);
        this.title.setValue(context, R.string.export_treatment_title);
        this.sheet.add(this.title);

        this.date = new Cell(5, 2);
        this.date.setXSpan(3);
        this.date.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.sheet.add(this.date);

        this.company = new Cell(4, 4);
        this.company.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.company.setItalic(true);
        this.sheet.add(this.company);

        this.companyGroup = new Cell(4, 3);
        this.companyGroup.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.sheet.add(this.companyGroup);

        /* Load table */
        this.table = new Table(this.sheet, 0, 6);

        this.index = new TableColumn(this.context, R.string.export_treatment_index);
        this.index.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.index.setWidth(6.0f);
        this.table.addColumn(this.index);

        this.number = new TableColumn(this.context, R.string.export_treatment_number);
        this.number.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.number.setWidth(8.0f);
        this.table.addColumn(this.number);

        this.collar = new TableColumn(this.context, R.string.export_treatment_collar);
        this.collar.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.collar.setBold(true);
        this.collar.setWidth(6.0f);
        this.table.addColumn(this.collar);

        this.group = new TableColumn(this.context, R.string.export_treatment_group);
        this.group.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.group.setWidth(6.0f);
        this.table.addColumn(this.group);

        this.diagnosis = new TableColumn(this.context, R.string.export_treatment_diagnosis);
        this.diagnosis.setWidth(38.0f);
        this.table.addColumn(this.diagnosis);

        this.target = new TableColumn(this.context, R.string.export_treatment_target);
        this.target.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.target.setWidth(7.0f);
        this.table.addColumn(this.target);

        this.repeat = new TableColumn(this.context, R.string.export_treatment_repeat);
        this.repeat.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.repeat.setWidth(7.0f);
        this.table.addColumn(this.repeat);

        this.extra = new TableColumn(this.context, R.string.export_treatment_extra);
        this.extra.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.extra.setWidth(15.0f);
        this.table.addColumn(this.extra);

        /* Add picture */
        DatabaseBitmap bitmap = new DatabaseBitmap(this.context, R.drawable.icon_salas);
        this.picture = new Picture(bitmap, 1, 1, 2, 4);
        this.picture.setXOffset(-0.25f);
        this.sheet.add(this.picture);

        this.add(this.sheet);
    }

    @Override
    public void save(String name)
    {
        this.table.updateBorders();

        super.save(name);
    }

    public void setCompany(Company company)
    {
        String group = company.getGroup();
        int y = 3;

        this.company.setValue(company.getName());

        if (group != null)
        {
            this.companyGroup.setValue(group);
            y = 4;
        }

        this.company.setY(y);
    }

    public void setDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String string = date.format(formatter);

        this.date.setValue(string);
    }

    public void setDate(LocalDate start, LocalDate end)
    {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String string = start.format(formatter1) + " - " + end.format(formatter2);

        this.date.setValue(string);
    }

    public LocalDate getRepeatDate()
    {
        return this.repeatDate;
    }

    public void setRepeatDate(LocalDate repeatDate)
    {
        this.repeatDate = repeatDate;
    }

    public void add(Treatment treatment)
    {
        List<TreatmentType> allowedTypes = this.settings.getTreatmentTypes();

        /* Check if treatment is allowed */
        if (allowedTypes.contains(treatment.getType()))
        {
            Map<TargetMask, List<Diagnosis>> diagnoses = new LinkedHashMap<>();
            Map<TargetMask, List<Resource>> resources = new LinkedHashMap<>();
            this.getTargetMaps(treatment, diagnoses, resources);

            Map<TableColumn, Cell> first = null;
            Map<TableColumn, Cell> repeat = null;

            /* Mark whole treatments if exporting multiple types */
            if (allowedTypes.size() > 1 && treatment.getType() == TreatmentType.WHOLE)
            {
                first = this.table.addRow();
                first.get(this.diagnosis).setValue(this.context, R.string.export_treatment_whole);
            }

            /* Add all diagnoses and resources */
            for (TargetMask target : diagnoses.keySet())
            {
                List<Diagnosis> currentDiagnoses = diagnoses.get(target);
                List<Resource> currentResources = resources.get(target);
                int length = Math.max(currentDiagnoses.size(), currentResources.size());

                for (int i = 0; i < length; i++)
                {
                    Map<TableColumn, Cell> entry = this.table.addRow();

                    if (first == null)
                        first = entry;

                    if (repeat == null)
                        repeat = entry;

                    /* Fill values */
                    if (i < currentDiagnoses.size())
                    {
                        Diagnosis diagnosis = currentDiagnoses.get(i);
                        String name = diagnosis.getName();
                        String comment = diagnosis.getComment();

                        if (comment != null)
                        {
                            name = name + " — " + comment;
                        }

                        entry.get(this.diagnosis).setValue(name);
                        entry.get(this.target).setValue(target.getName(this.context));
                    }

                    if (i < currentResources.size())
                    {
                        Resource resource = currentResources.get(i);
                        String name = resource.getName() + "!";

                        entry.get(this.extra).setValue(name);
                    }
                }
            }

            /* Add statuses and comment */
            List<Status> statuses = treatment.getStatuses();

            String comment = treatment.getComment();
            int length = Math.max(comment != null ? 1 : 0, statuses.size());

            for (int i = 0; i < length; i++)
            {
                Map<TableColumn, Cell> entry = this.table.addRow();

                if (first == null)
                    first = entry;

                /* Fill values */
                if (i == 0 && comment != null)
                {
                    entry.get(this.diagnosis).setValue(comment);
                }

                if (i < statuses.size())
                {
                    Status status = statuses.get(i);
                    String name = status.getName() + "!";

                    Cell cell = entry.get(this.extra);
                    cell.setBold(true);
                    cell.setValue(name);
                }
            }

            /* Add cow information */
            Cow cow = treatment.getCow();
            int number = cow.getID();
            int collar = cow.getCollar();
            String group = cow.getGroup();

            if (first == null)
                first = this.table.addRow();

            first.get(this.index).setValue(this.current);
            first.get(this.number).setValue(number);
            first.get(this.collar).setValue(collar != 0 ? collar : "—");
            first.get(this.group).setValue(group != null ? group : "—");

            /* Add repeat date */
            if (!treatment.isHealed() && repeat != null)
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.");
                String repeatDate = formatter.format(this.repeatDate);

                repeat.get(this.repeat).setValue(repeatDate);
            }

            this.current++;
            this.table.endBlock();
        }
    }

    private void getTargetMaps(Treatment treatment, Map<TargetMask, List<Diagnosis>> diagnoses, Map<TargetMask, List<Resource>> resources)
    {
        /* Initialize empty maps */
        for (HoofMask mask : HoofMask.values())
        {
            diagnoses.put(mask.getLeftFinger(), new ArrayList<>());
            diagnoses.put(mask.getRightFinger(), new ArrayList<>());
            diagnoses.put(mask, new ArrayList<>());

            resources.put(mask.getLeftFinger(), new ArrayList<>());
            resources.put(mask.getRightFinger(), new ArrayList<>());
            resources.put(mask, new ArrayList<>());
        }

        /* Fill diagnoses */
        for (Diagnosis diagnosis : treatment.getDiagnoses())
        {
            if (diagnosis.getTemplate() != null)
            {
                TargetMask target = diagnosis.getTarget();
                diagnoses.get(target).add(diagnosis);
            }
        }

        /* Fill resources */
        List<ResourceTemplate> allowedResources = this.settings.getResourceTemplates();

        for (Resource resource : treatment.getResources())
        {
            if (resource.getTemplate() != null)
            {
                /* Check if resource is allowed */
                if (allowedResources.contains(resource.getTemplate()))
                {
                    if (!resource.isCopy())
                    {
                        TargetMask target = resource.getTarget();

                        if (target instanceof FingerMask)
                        {
                            FingerMask finger = (FingerMask) target;

                            /* Attempt correct finger -> hoof */
                            if (!this.attemptAdd(resource, finger, diagnoses, resources))
                            {
                                this.attemptAdd(resource, finger.getHoof(), diagnoses, resources);
                            }
                        }
                        else
                        {
                            HoofMask hoof = (HoofMask) target;

                            /* Attempt left finger -> right finger -> hoof */
                            if (!this.attemptAdd(resource, hoof.getLeftFinger(), diagnoses, resources))
                            {
                                if (!this.attemptAdd(resource, hoof.getRightFinger(), diagnoses, resources))
                                {
                                    this.attemptAdd(resource, hoof, diagnoses, resources);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean attemptAdd(Resource resource, TargetMask mask, Map<TargetMask, List<Diagnosis>> diagnoses, Map<TargetMask, List<Resource>> resources)
    {
        if (diagnoses.get(mask).size() > 0)
        {
            resources.get(mask).add(resource);
            return true;
        }

        return false;
    }
}
