package com.deloladrin.cows.data;

import android.content.Context;

import com.deloladrin.cows.R;

public enum FingerMask
{
    LF_LF(0b10000000, HoofMask.LF, R.string.mask_lf_lf),
    LF_RF(0b01000000, HoofMask.LF, R.string.mask_lf_rf),
    RF_LF(0b00100000, HoofMask.RF, R.string.mask_rf_lf),
    RF_RF(0b00010000, HoofMask.RF, R.string.mask_rf_rf),
    LB_LF(0b00001000, HoofMask.LB, R.string.mask_lb_lf),
    LB_RF(0b00000100, HoofMask.LB, R.string.mask_lb_rf),
    RB_LF(0b00000010, HoofMask.RB, R.string.mask_rb_lf),
    RB_RF(0b00000001, HoofMask.RB, R.string.mask_rb_rf);

    private int mask;
    private HoofMask hoof;
    private int name;

    FingerMask(int mask, HoofMask hoof, int name)
    {
        this.mask = mask;
        this.hoof = hoof;
        this.name = name;
    }

    public static FingerMask parse(int mask)
    {
        for (FingerMask value : values())
        {
            if (mask == value.getMask())
            {
                return value;
            }
        }

        return null;
    }


    public boolean contains(int value)
    {
        if ((this.getMask() & value) != 0)
        {
            return true;
        }

        return false;
    }

    public int getMask()
    {
        return this.mask;
    }

    public HoofMask getHoof()
    {
        return this.hoof;
    }

    public String getName(Context context)
    {
        return context.getString(this.name);
    }
}
