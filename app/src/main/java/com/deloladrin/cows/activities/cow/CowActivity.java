package com.deloladrin.cows.activities.cow;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.deloladrin.cows.R;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.data.Cow;
import com.deloladrin.cows.data.Diagnosis;
import com.deloladrin.cows.data.DiagnosisState;
import com.deloladrin.cows.data.DiagnosisTemplate;
import com.deloladrin.cows.data.DiagnosisType;
import com.deloladrin.cows.data.FingerMask;
import com.deloladrin.cows.data.HoofMask;
import com.deloladrin.cows.data.Resource;
import com.deloladrin.cows.data.ResourceTemplate;
import com.deloladrin.cows.data.ResourceType;
import com.deloladrin.cows.data.Status;
import com.deloladrin.cows.data.StatusTemplate;
import com.deloladrin.cows.data.Treatment;
import com.deloladrin.cows.data.TreatmentType;
import com.deloladrin.cows.database.DatabaseActivity;
import com.deloladrin.cows.database.DatabaseBitmap;

import java.time.LocalDateTime;
import java.util.List;

public class CowActivity extends DatabaseActivity
{
    public static final String EXTRA_COW_ID = "com.deloladrin.cows.activities.cow.CowActivity.EXTRA_COW_ID";
    public static final String EXTRA_USER_NAME = "com.deloladrin.cows.activities.cow.CowActivity.EXTRA_USER_NAME";

    private Cow cow;
    private String user;

    private CowHeader header;
    private TreatmentEditor editor;
    private TreatmentHistory history;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cow);

        /* Load all children */
        this.header = new CowHeader(this, R.id.cow_header);
        this.editor = new TreatmentEditor(this, R.id.cow_treatment_editor);
        this.history = new TreatmentHistory(this, R.id.cow_treatment_history);

        this.database.onUpgrade(this.database.getWritableDatabase(), 0, 0);

        /* Default resources */
        DiagnosisTemplate dermatitis_digitalis_ii = new DiagnosisTemplate(this.database, 0, "Dermatitis digitalis II.st.", "Rozléčený dermatitis digitalis", "Dermatitis digitalis - doléčení", "DD", DiagnosisType.HOOF); dermatitis_digitalis_ii.insert();
        DiagnosisTemplate dermatitits_interdigitalis_ii = new DiagnosisTemplate(this.database, 0, "Dermatitis interdigitalis II.st.", "Rozléčený dermatitis interdigitalis", "Dermatitis interdigitalis - doléčení", "DID", DiagnosisType.HOOF); dermatitits_interdigitalis_ii.insert();
        DiagnosisTemplate nekrobacilosa_ii = new DiagnosisTemplate(this.database, 0, "Nekrobacilosa II.st.", "Rozléčená nekrobacilosa - opak. ošetření", "Vyhojená nekrobacilosa - doléčení", "NB2", DiagnosisType.HOOF); nekrobacilosa_ii.insert();
        DiagnosisTemplate nekrobacilosa_iii = new DiagnosisTemplate(this.database, 0, "Nekrobacilosa III.st.", "Rozléčená nekrobacilosa - opak. ošetření", "Vyhojená nekrobacilosa - doléčení", "NB3", DiagnosisType.HOOF); nekrobacilosa_iii.insert();
        DiagnosisTemplate rusterholzuv_vred_ii = new DiagnosisTemplate(this.database, 0, "Rusterholzův vřed II.st.", "Rozléčený R.V.- opakované ošetření", "Vyhojený R.V.- doléčení", "RV2", DiagnosisType.FINGER); rusterholzuv_vred_ii.insert();
        DiagnosisTemplate rusterholzuv_vred_iii = new DiagnosisTemplate(this.database, 0, "Rusterholzův vřed III.st.", "Rozléčený R.V.- opakované ošetření", "Vyhojený R.V.- doléčení", "RV3", DiagnosisType.FINGER); rusterholzuv_vred_iii.insert();
        DiagnosisTemplate rusterholzuv_vred_v_patce = new DiagnosisTemplate(this.database, 0, "Rusterholzův vřed v patce", "Rozléčený R.V.- opakované ošetření", "Vyhojený R.V.- doléčení", "RVP", DiagnosisType.FINGER); rusterholzuv_vred_v_patce.insert();
        DiagnosisTemplate hnisavy_zanet_patky = new DiagnosisTemplate(this.database, 0, "Hnisavý zánět patky", "Rozléčená po H.z. patky", "Vyhojená po H.z.patky", "HZP", DiagnosisType.FINGER); hnisavy_zanet_patky.insert();
        DiagnosisTemplate nekroza_patky = new DiagnosisTemplate(this.database, 0, "Nekroza patky", "Rozléčená patka - opakované ošetření", "Vyhojená patka - doléčení", "NP", DiagnosisType.FINGER); nekroza_patky.insert();
        DiagnosisTemplate porucha_bile_cary_ii = new DiagnosisTemplate(this.database, 0, "Porucha bílé čáry II.st.", "Rozléčená porucha bílé čáry- opak. oš.", "Vyhojená porucha bílé čáry - doléčení", "PBČ", DiagnosisType.FINGER); porucha_bile_cary_ii.insert();
        DiagnosisTemplate absces_spicky = new DiagnosisTemplate(this.database, 0, "Absces špičky", "Rozléčená špička - opakované ošetření", "Vyhojená špička - doléčení", "AŠ", DiagnosisType.FINGER); absces_spicky.insert();
        DiagnosisTemplate nekroza_spicky = new DiagnosisTemplate(this.database, 0, "Nekroza špičky", "Rozléčená špička - opakované ošetření", "Vyhojená špička - doléčení", "NŠ", DiagnosisType.FINGER); nekroza_spicky.insert();
        DiagnosisTemplate vred_spicky = new DiagnosisTemplate(this.database, 0, "Vřed špičky", "Rozléčená špička - opakované ošetření", "Vyhojená špička - doléčení", "VŠ", DiagnosisType.FINGER); vred_spicky.insert();
        DiagnosisTemplate dvojita_chodidlova_stena = new DiagnosisTemplate(this.database, 0, "Dvojitá chodidlová stěna", "Rozléčená dvojitá chod. stěna", "Vyhojená dvojitá chod. stěna", "DCHS", DiagnosisType.FINGER); dvojita_chodidlova_stena.insert();
        DiagnosisTemplate rozstep_rohoveho_pouzdra = new DiagnosisTemplate(this.database, 0, "Rozštěp rohového pouzdra", "Rozléčený roštěp roh. pouzdra", "Vyhojený roštěp roh. pouzdra", "RRP", DiagnosisType.FINGER); rozstep_rohoveho_pouzdra.insert();
        DiagnosisTemplate hnisavy_zanet_chodidla = new DiagnosisTemplate(this.database, 0, "Hnisavý zánět chodidla", "Rozléčená po H.z.chodidla - opak.ošetření", "Vyhojená po H.z.chodidla - doléčení", "HZCH", DiagnosisType.FINGER); hnisavy_zanet_chodidla.insert();
        DiagnosisTemplate hnisave_volna_stena = new DiagnosisTemplate(this.database, 0, "Hnisavě volná stěna", "Rozléčená H.v.stěna - opak.ošetření", "Vyhojená H.v.stěna - doléčení", "HVS", DiagnosisType.FINGER); hnisave_volna_stena.insert();
        DiagnosisTemplate poraneni_pri_osetreni = new DiagnosisTemplate(this.database, 0, "Poranění při ošetření", "Poranění při ošetření", "Poranění při ošetření", "PO", DiagnosisType.FINGER); poraneni_pri_osetreni.insert();
        DiagnosisTemplate poranena_spicka_pri_osetreni = new DiagnosisTemplate(this.database, 0, "Poraněná špička při ošetření", "Poraněná špička při ošetření", "Poraněná špička při ošetření", "PŠO", DiagnosisType.FINGER); poranena_spicka_pri_osetreni.insert();
        DiagnosisTemplate kulha_z_jineho_duvodu = new DiagnosisTemplate(this.database, 0, "Kulhá z jiného důvodu", "Kulhá z jiného důvodu", "Kulhá z jiného důvodu", "KZJ", DiagnosisType.HOOF); kulha_z_jineho_duvodu.insert();

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

        Company company = new Company(this.database, 0, "AGRAS BOHDALOV", null); company.insert();
        Cow cow = new Cow(this.database, 582344, 86, company, null); cow.insert();

        /* Load requested cow */
        Intent intent = this.getIntent();

        int cowID = intent.getIntExtra(EXTRA_COW_ID, 582344 /* TODO */);
        String user = "Bohous";// intent.getStringExtra(EXTRA_USER_NAME);

        this.user = user;
        this.setCow(this.database.getCowTable().select(cowID));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        /* Allow EditText saving after clicking outside */
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            View view = this.getCurrentFocus();

            if (view instanceof EditText)
            {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);

                if (!rect.contains((int)event.getRawX(), (int)event.getRawY()))
                {
                    view.clearFocus();

                    /* Close the keyboard */
                    InputMethodManager manager = (InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    public void refreshFull()
    {
        /* Reload the same cow */
        this.setCow(this.cow);
    }

    public void refresh()
    {
        /* Reload the same cow with the same treatment */
        Treatment current = this.editor.getTreatment();
        this.setCow(this.cow, current);
    }

    public Cow getCow()
    {
        return this.cow;
    }

    public void setCow(Cow cow)
    {
        this.cow = cow;
        this.header.setCow(cow);

        if (cow != null)
        {
            List<Treatment> treatments = cow.getTreatments();
            this.history.setTreatments(treatments);

            if (treatments.size() > 0)
            {
                Treatment treatment = treatments.get(treatments.size() - 1);
                this.editor.setTreatment(treatment);
            }
            else
            {
                this.editor.setTreatment(null);
            }
        }
        else
        {
            this.history.setTreatments(null);
            this.editor.setTreatment(null);
        }
    }

    public void setCow(Cow cow, Treatment treatment)
    {
        this.cow = cow;
        this.header.setCow(cow);

        if (cow != null)
        {
            List<Treatment> treatments = cow.getTreatments();
            this.history.setTreatments(treatments);
            this.editor.setTreatment(treatment);
        }
        else
        {
            this.history.setTreatments(null);
            this.editor.setTreatment(null);
        }
    }

    public String getUser()
    {
        return this.user;
    }

    public CowHeader getHeader()
    {
        return this.header;
    }

    public TreatmentEditor getEditor()
    {
        return this.editor;
    }

    public TreatmentHistory getHistory()
    {
        return this.history;
    }
}
