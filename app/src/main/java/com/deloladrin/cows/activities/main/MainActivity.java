package com.deloladrin.cows.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.company.CompanyActivity;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.activities.export.TreatmentWorkbook;
import com.deloladrin.cows.activities.main.dialogs.CowDialog;
import com.deloladrin.cows.activities.main.views.CompanyEntry;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.DiagnosisTemplate;
import com.deloladrin.cows.data.DiagnosisType;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.database.DatabaseActivity;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.dialogs.SelectDialog;
import com.deloladrin.cows.export.Cell;
import com.deloladrin.cows.export.Sheet;
import com.deloladrin.cows.export.Workbook;
import com.deloladrin.cows.views.ImageTextButton;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainActivity extends DatabaseActivity implements View.OnClickListener
{
    private Company company;

    private LinearLayout currentCompany;
    private ImageView companyImage;
    private TextView companyName;

    private ImageTextButton companies;
    private ImageTextButton cow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        /* Fix poi-on-android xml parser */
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        /* Load all children */
        this.currentCompany = this.findViewById(R.id.main_company);
        this.companyImage = this.findViewById(R.id.main_company_image);
        this.companyName = this.findViewById(R.id.main_company_name);

        this.companies = this.findViewById(R.id.main_companies);
        this.cow = this.findViewById(R.id.main_cow);

        /* Add events */
        this.currentCompany.setOnClickListener(this);

        this.companies.setOnClickListener(this);
        this.cow.setOnClickListener(this);

        /* TODO: Temporary code - will be removed in the future */
//        this.database.onUpgrade(this.database.getWritableDatabase(), 0, 0);
//
//        DiagnosisTemplate diagnosis_dermatitis_digitalis_ii = new DiagnosisTemplate(this.database, 0, "Dermatitis digitalis II.st.", "Rozléčený dermatitis digitalis", "Dermatitis digitalis - doléčení", "DD", DiagnosisType.HOOF); diagnosis_dermatitis_digitalis_ii.insert();
//        DiagnosisTemplate diagnosis_dermatitits_interdigitalis_ii = new DiagnosisTemplate(this.database, 0, "Dermatitis interdigitalis II.st.", "Rozléčený dermatitis interdigitalis", "Dermatitis interdigitalis - doléčení", "DID", DiagnosisType.HOOF); diagnosis_dermatitits_interdigitalis_ii.insert();
//        DiagnosisTemplate diagnosis_nekrobacilosa_ii = new DiagnosisTemplate(this.database, 0, "Nekrobacilosa II.st.", "Rozléčená nekrobacilosa - opak. ošetření", "Vyhojená nekrobacilosa - doléčení", "NB2", DiagnosisType.HOOF); diagnosis_nekrobacilosa_ii.insert();
//        DiagnosisTemplate diagnosis_nekrobacilosa_iii = new DiagnosisTemplate(this.database, 0, "Nekrobacilosa III.st.", "Rozléčená nekrobacilosa - opak. ošetření", "Vyhojená nekrobacilosa - doléčení", "NB3", DiagnosisType.HOOF); diagnosis_nekrobacilosa_iii.insert();
//        DiagnosisTemplate diagnosis_rusterholzuv_vred_ii = new DiagnosisTemplate(this.database, 0, "Rusterholzův vřed II.st.", "Rozléčený R.V.- opakované ošetření", "Vyhojený R.V.- doléčení", "RV2", DiagnosisType.FINGER); diagnosis_rusterholzuv_vred_ii.insert();
//        DiagnosisTemplate diagnosis_rusterholzuv_vred_iii = new DiagnosisTemplate(this.database, 0, "Rusterholzův vřed III.st.", "Rozléčený R.V.- opakované ošetření", "Vyhojený R.V.- doléčení", "RV3", DiagnosisType.FINGER); diagnosis_rusterholzuv_vred_iii.insert();
//        DiagnosisTemplate diagnosis_rusterholzuv_vred_v_patce = new DiagnosisTemplate(this.database, 0, "Rusterholzův vřed v patce", "Rozléčený R.V.- opakované ošetření", "Vyhojený R.V.- doléčení", "RVP", DiagnosisType.FINGER); diagnosis_rusterholzuv_vred_v_patce.insert();
//        DiagnosisTemplate diagnosis_hnisavy_zanet_patky = new DiagnosisTemplate(this.database, 0, "Hnisavý zánět patky", "Rozléčená po H.z. patky", "Vyhojená po H.z.patky", "HZP", DiagnosisType.FINGER); diagnosis_hnisavy_zanet_patky.insert();
//        DiagnosisTemplate diagnosis_nekroza_patky = new DiagnosisTemplate(this.database, 0, "Nekroza patky", "Rozléčená patka - opakované ošetření", "Vyhojená patka - doléčení", "NP", DiagnosisType.FINGER); diagnosis_nekroza_patky.insert();
//        DiagnosisTemplate diagnosis_porucha_bile_cary_ii = new DiagnosisTemplate(this.database, 0, "Porucha bílé čáry II.st.", "Rozléčená porucha bílé čáry- opak. oš.", "Vyhojená porucha bílé čáry - doléčení", "PBČ", DiagnosisType.FINGER); diagnosis_porucha_bile_cary_ii.insert();
//        DiagnosisTemplate diagnosis_absces_spicky = new DiagnosisTemplate(this.database, 0, "Absces špičky", "Rozléčená špička - opakované ošetření", "Vyhojená špička - doléčení", "AŠ", DiagnosisType.FINGER); diagnosis_absces_spicky.insert();
//        DiagnosisTemplate diagnosis_nekroza_spicky = new DiagnosisTemplate(this.database, 0, "Nekroza špičky", "Rozléčená špička - opakované ošetření", "Vyhojená špička - doléčení", "NŠ", DiagnosisType.FINGER); diagnosis_nekroza_spicky.insert();
//        DiagnosisTemplate diagnosis_vred_spicky = new DiagnosisTemplate(this.database, 0, "Vřed špičky", "Rozléčená špička - opakované ošetření", "Vyhojená špička - doléčení", "VŠ", DiagnosisType.FINGER); diagnosis_vred_spicky.insert();
//        DiagnosisTemplate diagnosis_dvojita_chodidlova_stena = new DiagnosisTemplate(this.database, 0, "Dvojitá chodidlová stěna", "Rozléčená dvojitá chod. stěna", "Vyhojená dvojitá chod. stěna", "DCHS", DiagnosisType.FINGER); diagnosis_dvojita_chodidlova_stena.insert();
//        DiagnosisTemplate diagnosis_mekka_chodidlova_stena = new DiagnosisTemplate(this.database, 0, "Měkká chodidlová stěna", "Rozléčená měkká chodidlová stěna", "Vyhojená měkká chodidlová stěna", "MCHS", DiagnosisType.FINGER); diagnosis_mekka_chodidlova_stena.insert();
//        DiagnosisTemplate diagnosis_rozstep_rohoveho_pouzdra = new DiagnosisTemplate(this.database, 0, "Rozštěp rohového pouzdra", "Rozléčený roštěp roh. pouzdra", "Vyhojený roštěp roh. pouzdra", "RRP", DiagnosisType.FINGER); diagnosis_rozstep_rohoveho_pouzdra.insert();
//        DiagnosisTemplate diagnosis_hnisavy_zanet_chodidla = new DiagnosisTemplate(this.database, 0, "Hnisavý zánět chodidla", "Rozléčená po H.z.chodidla - opak.ošetření", "Vyhojená po H.z.chodidla - doléčení", "HZCH", DiagnosisType.FINGER); diagnosis_hnisavy_zanet_chodidla.insert();
//        DiagnosisTemplate diagnosis_hnisave_volna_stena = new DiagnosisTemplate(this.database, 0, "Hnisavě volná stěna", "Rozléčená H.v.stěna - opak.ošetření", "Vyhojená H.v.stěna - doléčení", "HVS", DiagnosisType.FINGER); diagnosis_hnisave_volna_stena.insert();
//        DiagnosisTemplate diagnosis_poraneni_pri_osetreni = new DiagnosisTemplate(this.database, 0, "Poranění při ošetření", "Poranění při ošetření", "Poranění při ošetření", "PO", DiagnosisType.FINGER); diagnosis_poraneni_pri_osetreni.insert();
//        DiagnosisTemplate diagnosis_poranena_spicka_pri_osetreni = new DiagnosisTemplate(this.database, 0, "Poraněná špička při ošetření", "Poraněná špička při ošetření", "Poraněná špička při ošetření", "PŠO", DiagnosisType.FINGER); diagnosis_poranena_spicka_pri_osetreni.insert();
//        DiagnosisTemplate diagnosis_kulha_z_jineho_duvodu = new DiagnosisTemplate(this.database, 0, "Kulhá z jiného důvodu", "Kulhá z jiného důvodu", "Kulhá z jiného důvodu", "KZJ", DiagnosisType.HOOF); diagnosis_kulha_z_jineho_duvodu.insert();
//
//        ResourceTemplate resource_bandage = new ResourceTemplate(this.database, 0, "Obvaz", ResourceType.HOOF, 0, false, new DatabaseBitmap(this, R.drawable.resource_bandage), new DatabaseBitmap(this, R.drawable.resource_bandage_small)); resource_bandage.insert();
//        ResourceTemplate resource_block_wood = new ResourceTemplate(this.database, 0, "Wooden Block", ResourceType.FINGER_INVERTED, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_wood), null); resource_block_wood.insert();
//        ResourceTemplate resource_block_wood_xxl = new ResourceTemplate(this.database, 0, "Wooden Block", ResourceType.FINGER_INVERTED, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_wood_xxl), null); resource_block_wood_xxl.insert();
//        ResourceTemplate resource_block_tp = new ResourceTemplate(this.database, 0, "TP Block", ResourceType.FINGER_INVERTED, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_tp), null); resource_block_tp.insert();
//        ResourceTemplate resource_block_tp_xxl = new ResourceTemplate(this.database, 0, "TP Block XXL", ResourceType.FINGER_INVERTED, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_tp_xxl), null); resource_block_tp_xxl.insert();
//        ResourceTemplate resource_block_iron_half = new ResourceTemplate(this.database, 0, "½ Iron Block", ResourceType.FINGER_INVERTED, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_iron_half), null); resource_block_iron_half.insert();
//        ResourceTemplate resource_block_iron = new ResourceTemplate(this.database, 0, "Iron Block", ResourceType.HOOF, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_iron), null); resource_block_iron.insert();
//        ResourceTemplate resource_synulox = new ResourceTemplate(this.database, 0, "Synulox", ResourceType.HOOF, 2, false, new DatabaseBitmap(this, R.drawable.resource_synulox), null); resource_synulox.insert();
//        ResourceTemplate resource_synulox_2x = new ResourceTemplate(this.database, 0, "2x Synulox", ResourceType.HOOF, 2, false, new DatabaseBitmap(this, R.drawable.resource_synulox_2x), null); resource_synulox_2x.insert();
//
//        StatusTemplate status_antibiotics = new StatusTemplate(this.database, 0, "Antibiotika", new DatabaseBitmap(this, R.drawable.resource_antibiotics)); status_antibiotics.insert();
//        StatusTemplate status_checkup = new StatusTemplate(this.database, 0, "Kontrola", new DatabaseBitmap(this, R.drawable.resource_checkup)); status_checkup.insert();
//        StatusTemplate status_no_bathing = new StatusTemplate(this.database, 0, "Nekoupat", new DatabaseBitmap(this, R.drawable.resource_no_bathing)); status_no_bathing.insert();
//        StatusTemplate status_take_out = new StatusTemplate(this.database, 0, "Vyřadit z chovu", new DatabaseBitmap(this, R.drawable.resource_take_out)); status_take_out.insert();
//
//        Company company_agras_bohdalov = new Company(this.database, 0, "AGRAS Bohdalov, a.s.", null, new DatabaseBitmap(this, R.drawable.company_agras_bohdalov), null); company_agras_bohdalov.insert();
//
//        this.preferences.setActiveCompany(company_agras_bohdalov);
//        this.preferences.setActiveUser("Bohous");

        TreatmentWorkbook workbook = new TreatmentWorkbook(this);
        workbook.setCompany(Company.get(this.database, 1));
        workbook.setDate(LocalDate.of(2021, 1, 30));

        for (Treatment treatment : Treatment.getAll(this.database))
        {
            if (LocalDate.of(2021, 1, 30).equals(treatment.getDate().toLocalDate()))
                workbook.add(treatment);
        }

        workbook.save("VKK Šlapanice 30.1.2021");

        /* Load active company */
        this.refresh();
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.currentCompany))
        {
            /* Show company select dialog */
            SelectDialog<CompanyEntry> dialog = new SelectDialog<>(this);
            dialog.setText(R.string.dialog_company);

            Company active = this.preferences.getActiveCompany();

            for (Company company : Company.getAll(this.database))
            {
                CompanyEntry entry = new CompanyEntry(this, company);
                dialog.add(entry, company.equals(active));
            }

            dialog.setOnSelectListener((CompanyEntry entry) ->
            {
                /* Make company current and refresh */
                this.preferences.setActiveCompany(entry.getValue());
                this.refresh();
            });

            dialog.show();
        }

        if (view.equals(this.companies))
        {
            /* Open company activity */
            Intent intent = new Intent(this, CompanyActivity.class);
            this.startActivity(intent);

            return;
        }

        if (view.equals(this.cow))
        {
            /* Show cow select dialog */
            CowDialog dialog = new CowDialog(this, this.company);

            dialog.setOnSubmitListener((Cow cow) ->
            {
                /* Open cow activity */
                Intent intent = new Intent(this, CowActivity.class);
                intent.putExtra(CowActivity.EXTRA_COW_ID, cow.getID());

                this.startActivity(intent);
            });

            dialog.show();
            return;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        /* Refresh company data */
        this.refresh();
    }

    public void refresh()
    {
        /* Update current active company */
        Company active = this.preferences.getActiveCompany();
        this.setCompany(active);
    }

    public Company getCompany()
    {
        return this.company;
    }

    public void setCompany(Company company)
    {
        this.company = company;

        if (company != null)
        {
            this.companyName.setText(company.getName());

            /* Company image */
            DatabaseBitmap image = company.getImage();

            if (image != null)
            {
                this.companyImage.setVisibility(View.VISIBLE);
                this.companyImage.setImageBitmap(image.getBitmap());
            }
            else
            {
                this.companyImage.setVisibility(View.GONE);
            }

            /* Enable company based buttons */
            this.cow.setEnabled(true);
        }
        else
        {
            this.companyImage.setVisibility(View.GONE);
            this.companyName.setText("—");

            /* Disable company based buttons */
            this.cow.setEnabled(false);
        }
    }
}
