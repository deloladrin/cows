package com.deloladrin.cows.data;

import android.content.Context;

import com.deloladrin.cows.R;

public enum HoofMask implements TargetMask
{
    LF(FingerMask.LF_LF, FingerMask.LF_RF, R.string.mask_lf),
    RF(FingerMask.RF_LF, FingerMask.RF_RF, R.string.mask_rf),
    LB(FingerMask.LB_LF, FingerMask.LB_RF, R.string.mask_lb),
    RB(FingerMask.RB_LF, FingerMask.RB_RF, R.string.mask_rb);

    private FingerMask left;
    private FingerMask right;
    private int name;

    HoofMask(FingerMask left, FingerMask right, int name)
    {
        this.left = left;
        this.right = right;
        this.name = name;
    }

    public static HoofMask parse(int mask)
    {
        for (HoofMask value : values())
        {
            if (mask == value.getMask())
            {
                return value;
            }
        }

        return null;
    }

    public static TargetMask parseUnknown(int mask)
    {
        return FingerMask.parseUnknown(mask);
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
        return this.left.getMask() | this.right.getMask();
    }

    public FingerMask getLeftFinger()
    {
        return this.left;
    }

    public FingerMask getRightFinger()
    {
        return this.right;
    }

    public String getName(Context context)
    {
        return context.getString(this.name);
    }
}
