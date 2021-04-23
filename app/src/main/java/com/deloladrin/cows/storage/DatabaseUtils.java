package com.deloladrin.cows.storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DatabaseUtils
{
    private static final int BITMAP_COMPRESS_QUALITY = 100;

    public static byte[] bitmapToBytes(Bitmap bitmap)
    {
        if (bitmap != null)
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, BITMAP_COMPRESS_QUALITY, stream);

            return stream.toByteArray();
        }

        return null;
    }

    public static Bitmap bytesToBitmap(byte[] bytes)
    {
        if (bytes != null)
        {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        return null;
    }
}
