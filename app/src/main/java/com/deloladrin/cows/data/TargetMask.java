package com.deloladrin.cows.data;

import android.content.Context;

public interface TargetMask
{
    int getMask();

    String getName(Context context);
    boolean contains(TargetMask mask);

    static TargetMask parse(int mask)
    {
        /* Is it a hoof mask? */
        HoofMask hoofMask = HoofMask.parse(mask);

        if (hoofMask == null)
        {
            /* It's probably finger mask */
            return FingerMask.parse(mask);
        }

        return hoofMask;
    }
}
