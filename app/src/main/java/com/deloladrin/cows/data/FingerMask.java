package com.deloladrin.cows.data;

import android.content.Context;

import com.deloladrin.cows.R;

public enum FingerMask implements TargetMask
{
    NONE(0b00000000, R.string.mask_none),
    LF_LF(0b10000000, R.string.mask_lf_lf),
    LF_RF(0b01000000, R.string.mask_lf_rf),
    RF_LF(0b00100000, R.string.mask_rf_lf),
    RF_RF(0b00010000, R.string.mask_rf_rf),
    LB_LF(0b00001000, R.string.mask_lb_lf),
    LB_RF(0b00000100, R.string.mask_lb_rf),
    RB_LF(0b00000010, R.string.mask_rb_lf),
    RB_RF(0b00000001, R.string.mask_rb_rf);

    private int mask;
    private int name;

    FingerMask(int mask, int name)
    {
        this.mask = mask;
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

    public boolean contains(TargetMask mask)
    {
        return this.contains(mask.getMask());
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
        for (HoofMask value : HoofMask.values())
        {
            if (value.contains(this.mask))
            {
                return value;
            }
        }

        return null;
    }

    public String getName(Context context)
    {
        return context.getString(this.name);
    }
}
