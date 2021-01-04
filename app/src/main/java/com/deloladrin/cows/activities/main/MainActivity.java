package com.deloladrin.cows.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.company.CompanyActivity;
import com.deloladrin.cows.activities.cow.CowActivity;
import com.deloladrin.cows.activities.main.dialogs.CowDialog;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.DiagnosisTemplate;
import com.deloladrin.cows.data.DiagnosisType;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.database.DatabaseActivity;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.views.ImageTextButton;

public class MainActivity extends DatabaseActivity implements View.OnClickListener
{
    private Company company;

    private TextView companyName;
    private TextView companyGroup;

    private ImageTextButton companies;
    private ImageTextButton cow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        /* Load all children */
        this.companyName = this.findViewById(R.id.main_company_name);
        this.companyGroup = this.findViewById(R.id.main_company_group);

        this.companies = this.findViewById(R.id.main_companies);
        this.cow = this.findViewById(R.id.main_cow);

        /* Add events */
        this.companies.setOnClickListener(this);
        this.cow.setOnClickListener(this);

        /* TODO: Temporary code - will be removed in the future */
        this.database.onUpgrade(this.database.getWritableDatabase(), 0, 0);

        DiagnosisTemplate diagnosis_dermatitis_digitalis_ii = new DiagnosisTemplate(this.database, 0, "Dermatitis digitalis II.st.", "Rozléčený dermatitis digitalis", "Dermatitis digitalis - doléčení", "DD", DiagnosisType.HOOF); diagnosis_dermatitis_digitalis_ii.insert();
        DiagnosisTemplate diagnosis_dermatitits_interdigitalis_ii = new DiagnosisTemplate(this.database, 0, "Dermatitis interdigitalis II.st.", "Rozléčený dermatitis interdigitalis", "Dermatitis interdigitalis - doléčení", "DID", DiagnosisType.HOOF); diagnosis_dermatitits_interdigitalis_ii.insert();
        DiagnosisTemplate diagnosis_nekrobacilosa_ii = new DiagnosisTemplate(this.database, 0, "Nekrobacilosa II.st.", "Rozléčená nekrobacilosa - opak. ošetření", "Vyhojená nekrobacilosa - doléčení", "NB2", DiagnosisType.HOOF); diagnosis_nekrobacilosa_ii.insert();
        DiagnosisTemplate diagnosis_nekrobacilosa_iii = new DiagnosisTemplate(this.database, 0, "Nekrobacilosa III.st.", "Rozléčená nekrobacilosa - opak. ošetření", "Vyhojená nekrobacilosa - doléčení", "NB3", DiagnosisType.HOOF); diagnosis_nekrobacilosa_iii.insert();
        DiagnosisTemplate diagnosis_rusterholzuv_vred_ii = new DiagnosisTemplate(this.database, 0, "Rusterholzův vřed II.st.", "Rozléčený R.V.- opakované ošetření", "Vyhojený R.V.- doléčení", "RV2", DiagnosisType.FINGER); diagnosis_rusterholzuv_vred_ii.insert();
        DiagnosisTemplate diagnosis_rusterholzuv_vred_iii = new DiagnosisTemplate(this.database, 0, "Rusterholzův vřed III.st.", "Rozléčený R.V.- opakované ošetření", "Vyhojený R.V.- doléčení", "RV3", DiagnosisType.FINGER); diagnosis_rusterholzuv_vred_iii.insert();
        DiagnosisTemplate diagnosis_rusterholzuv_vred_v_patce = new DiagnosisTemplate(this.database, 0, "Rusterholzův vřed v patce", "Rozléčený R.V.- opakované ošetření", "Vyhojený R.V.- doléčení", "RVP", DiagnosisType.FINGER); diagnosis_rusterholzuv_vred_v_patce.insert();
        DiagnosisTemplate diagnosis_hnisavy_zanet_patky = new DiagnosisTemplate(this.database, 0, "Hnisavý zánět patky", "Rozléčená po H.z. patky", "Vyhojená po H.z.patky", "HZP", DiagnosisType.FINGER); diagnosis_hnisavy_zanet_patky.insert();
        DiagnosisTemplate diagnosis_nekroza_patky = new DiagnosisTemplate(this.database, 0, "Nekroza patky", "Rozléčená patka - opakované ošetření", "Vyhojená patka - doléčení", "NP", DiagnosisType.FINGER); diagnosis_nekroza_patky.insert();
        DiagnosisTemplate diagnosis_porucha_bile_cary_ii = new DiagnosisTemplate(this.database, 0, "Porucha bílé čáry II.st.", "Rozléčená porucha bílé čáry- opak. oš.", "Vyhojená porucha bílé čáry - doléčení", "PBČ", DiagnosisType.FINGER); diagnosis_porucha_bile_cary_ii.insert();
        DiagnosisTemplate diagnosis_absces_spicky = new DiagnosisTemplate(this.database, 0, "Absces špičky", "Rozléčená špička - opakované ošetření", "Vyhojená špička - doléčení", "AŠ", DiagnosisType.FINGER); diagnosis_absces_spicky.insert();
        DiagnosisTemplate diagnosis_nekroza_spicky = new DiagnosisTemplate(this.database, 0, "Nekroza špičky", "Rozléčená špička - opakované ošetření", "Vyhojená špička - doléčení", "NŠ", DiagnosisType.FINGER); diagnosis_nekroza_spicky.insert();
        DiagnosisTemplate diagnosis_vred_spicky = new DiagnosisTemplate(this.database, 0, "Vřed špičky", "Rozléčená špička - opakované ošetření", "Vyhojená špička - doléčení", "VŠ", DiagnosisType.FINGER); diagnosis_vred_spicky.insert();
        DiagnosisTemplate diagnosis_dvojita_chodidlova_stena = new DiagnosisTemplate(this.database, 0, "Dvojitá chodidlová stěna", "Rozléčená dvojitá chod. stěna", "Vyhojená dvojitá chod. stěna", "DCHS", DiagnosisType.FINGER); diagnosis_dvojita_chodidlova_stena.insert();
        DiagnosisTemplate diagnosis_mekka_chodidlova_stena = new DiagnosisTemplate(this.database, 0, "Měkká chodidlová stěna", "Rozléčená měkká chodidlová stěna", "Vyhojená měkká chodidlová stěna", "MCHS", DiagnosisType.FINGER); diagnosis_mekka_chodidlova_stena.insert();
        DiagnosisTemplate diagnosis_rozstep_rohoveho_pouzdra = new DiagnosisTemplate(this.database, 0, "Rozštěp rohového pouzdra", "Rozléčený roštěp roh. pouzdra", "Vyhojený roštěp roh. pouzdra", "RRP", DiagnosisType.FINGER); diagnosis_rozstep_rohoveho_pouzdra.insert();
        DiagnosisTemplate diagnosis_hnisavy_zanet_chodidla = new DiagnosisTemplate(this.database, 0, "Hnisavý zánět chodidla", "Rozléčená po H.z.chodidla - opak.ošetření", "Vyhojená po H.z.chodidla - doléčení", "HZCH", DiagnosisType.FINGER); diagnosis_hnisavy_zanet_chodidla.insert();
        DiagnosisTemplate diagnosis_hnisave_volna_stena = new DiagnosisTemplate(this.database, 0, "Hnisavě volná stěna", "Rozléčená H.v.stěna - opak.ošetření", "Vyhojená H.v.stěna - doléčení", "HVS", DiagnosisType.FINGER); diagnosis_hnisave_volna_stena.insert();
        DiagnosisTemplate diagnosis_poraneni_pri_osetreni = new DiagnosisTemplate(this.database, 0, "Poranění při ošetření", "Poranění při ošetření", "Poranění při ošetření", "PO", DiagnosisType.FINGER); diagnosis_poraneni_pri_osetreni.insert();
        DiagnosisTemplate diagnosis_poranena_spicka_pri_osetreni = new DiagnosisTemplate(this.database, 0, "Poraněná špička při ošetření", "Poraněná špička při ošetření", "Poraněná špička při ošetření", "PŠO", DiagnosisType.FINGER); diagnosis_poranena_spicka_pri_osetreni.insert();
        DiagnosisTemplate diagnosis_kulha_z_jineho_duvodu = new DiagnosisTemplate(this.database, 0, "Kulhá z jiného důvodu", "Kulhá z jiného důvodu", "Kulhá z jiného důvodu", "KZJ", DiagnosisType.HOOF); diagnosis_kulha_z_jineho_duvodu.insert();

        ResourceTemplate resource_bandage = new ResourceTemplate(this.database, 0, "Obvaz", ResourceType.HOOF, 0, false, new DatabaseBitmap(this, R.drawable.resource_bandage)); resource_bandage.insert();
        ResourceTemplate resource_block_wood = new ResourceTemplate(this.database, 0, "Wooden Block", ResourceType.FINGER_INVERTED, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_wood)); resource_block_wood.insert();
        ResourceTemplate resource_block_wood_xxl = new ResourceTemplate(this.database, 0, "Wooden Block", ResourceType.FINGER_INVERTED, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_wood_xxl)); resource_block_wood_xxl.insert();
        ResourceTemplate resource_block_tp = new ResourceTemplate(this.database, 0, "TP Block", ResourceType.FINGER_INVERTED, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_tp)); resource_block_tp.insert();
        ResourceTemplate resource_block_tp_xxl = new ResourceTemplate(this.database, 0, "TP Block XXL", ResourceType.FINGER_INVERTED, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_tp_xxl)); resource_block_tp_xxl.insert();
        ResourceTemplate resource_block_iron_half = new ResourceTemplate(this.database, 0, "½ Iron Block", ResourceType.FINGER_INVERTED, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_iron_half)); resource_block_iron_half.insert();
        ResourceTemplate resource_block_iron = new ResourceTemplate(this.database, 0, "Iron Block", ResourceType.HOOF, 1, true, new DatabaseBitmap(this, R.drawable.resource_block_iron)); resource_block_iron.insert();
        ResourceTemplate resource_synulox = new ResourceTemplate(this.database, 0, "Synulox", ResourceType.HOOF, 2, false, new DatabaseBitmap(this, R.drawable.resource_synulox)); resource_synulox.insert();
        ResourceTemplate resource_synulox_2x = new ResourceTemplate(this.database, 0, "2x Synulox", ResourceType.HOOF, 2, false, new DatabaseBitmap(this, R.drawable.resource_synulox_2x)); resource_synulox_2x.insert();

        StatusTemplate status_antibiotics = new StatusTemplate(this.database, 0, "Antibiotika", new DatabaseBitmap(this, R.drawable.resource_antibiotics)); status_antibiotics.insert();
        StatusTemplate status_checkup = new StatusTemplate(this.database, 0, "Kontrola", new DatabaseBitmap(this, R.drawable.resource_checkup)); status_checkup.insert();
        StatusTemplate status_no_bathing = new StatusTemplate(this.database, 0, "Nekoupat", new DatabaseBitmap(this, R.drawable.resource_no_bathing)); status_no_bathing.insert();
        StatusTemplate status_take_out = new StatusTemplate(this.database, 0, "Vyřadit z chovu", new DatabaseBitmap(this, R.drawable.resource_take_out)); status_take_out.insert();

        Company company_agras_bohdalov = new Company(this.database, 0, "AGRAS BOHDALOV", null, null); company_agras_bohdalov.insert();
        Company company_bonagro_as = new Company(this.database, 0, "BONAGRO a.s.", null, null); company_bonagro_as.insert();
        Company company_spolecnost_bohunov = new Company(this.database, 0, "SPOLEČNOST BOHUŇOV", null, null); company_spolecnost_bohunov.insert();
        Company company_vkk_cernov = new Company(this.database, 0, "VKK ČERNOV", null, null); company_vkk_cernov.insert();
        Company company_dalecin = new Company(this.database, 0, "DALEČÍN", null, null); company_dalecin.insert();
        Company company_dvorek_karel_pometlo = new Company(this.database, 0, "DVOREK – KAREL POMETLO", null, null); company_dvorek_karel_pometlo.insert();
        Company company_havlickova_nizkov = new Company(this.database, 0, "f. HAVLÍČKOVÁ - NÍŽKOV", null, null); company_havlickova_nizkov.insert();
        Company company_zdv_hodiskov = new Company(this.database, 0, "ZDV HODIŠKOV", null, null); company_zdv_hodiskov.insert();
        Company company_chroust_jan = new Company(this.database, 0, "CHROUST Jan", null, null); company_chroust_jan.insert();
        Company company_hos_jablonov = new Company(this.database, 0, "HOS JABLOŇOV", null, null); company_hos_jablonov.insert();
        Company company_vkk_krucemburk = new Company(this.database, 0, "VKK KRUCEMBURK", null, null); company_vkk_krucemburk.insert();
        Company company_zas_lipa = new Company(this.database, 0, "ZAS LÍPA", null, null); company_zas_lipa.insert();
        Company company_vkk_nove_veseli = new Company(this.database, 0, "VKK NOVÉ VESELÍ", null, null); company_vkk_nove_veseli.insert();
        Company company_as_netin = new Company(this.database, 0, "a.s. NETÍN", null, null); company_as_netin.insert();
        Company company_zd_nizkov = new Company(this.database, 0, "ZD NÍŽKOV", null, null); company_zd_nizkov.insert();
        Company company_vkk_slavkovice = new Company(this.database, 0, "VKK SLAVKOVICE", null, null); company_vkk_slavkovice.insert();
        Company company_vkk_pavlov = new Company(this.database, 0, "VKK PAVLOV", null, null); company_vkk_pavlov.insert();
        Company company_slama_podesin = new Company(this.database, 0, "SLÁMA - PODĚŠÍN", null, null); company_slama_podesin.insert();
        Company company_zd_nove_mesto_na_morave = new Company(this.database, 0, "ZD Nové Město na Moravě", null, null); company_zd_nove_mesto_na_morave.insert();
        Company company_vkk_radostin = new Company(this.database, 0, "VKK RADOSTÍN", null, null); company_vkk_radostin.insert();
        Company company_zd_sazava = new Company(this.database, 0, "ZD SÁZAVA", null, null); company_zd_sazava.insert();
        Company company_seneco_polna = new Company(this.database, 0, "SENECO POLNÁ", null, null); company_seneco_polna.insert();
        Company company_svetnov_tepla_marie = new Company(this.database, 0, "SVĚTNOV - TEPLÁ MARIE", null, null); company_svetnov_tepla_marie.insert();
        Company company_zd_velka_losenice = new Company(this.database, 0, "ZD VELKÁ LOSENICE", null, null); company_zd_velka_losenice.insert();
        Company company_vkk_zabcice = new Company(this.database, 0, "VKK ŽABČICE", null, null); company_vkk_zabcice.insert();
        Company company_zd_snezne = new Company(this.database, 0, "ZD SNĚŽNÉ", null, null); company_zd_snezne.insert();

        this.preferences.setActiveCompany(company_agras_bohdalov);

        /* Load active company */
        this.refresh();
    }

    @Override
    public void onClick(View view)
    {
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
                intent.putExtra(CowActivity.EXTRA_USER_NAME, "Bohous");

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

            /* Company group is optional */
            String group = company.getGroup();

            if (group != null)
            {
                this.companyGroup.setText(group);
            }
            else
            {
                this.companyGroup.setText("");
            }
        }
        else
        {
            this.companyName.setText("—");
            this.companyGroup.setText("");
        }
    }
}
