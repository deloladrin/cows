package com.deloladrin.cows.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

public class ViewUtils
{
    public static ColorMatrixColorFilter FILTER_GRAYSCALE = ViewUtils.createGrayscaleFilter();

    private static ColorMatrixColorFilter createGrayscaleFilter()
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        return new ColorMatrixColorFilter(matrix);
    }
}
