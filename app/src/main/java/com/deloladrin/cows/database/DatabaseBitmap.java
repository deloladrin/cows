package com.deloladrin.cows.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class DatabaseBitmap
{
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    private byte[] buffer;

    public DatabaseBitmap(byte[] buffer)
    {
        this.buffer = buffer;
    }

    public DatabaseBitmap(Bitmap bitmap)
    {
        this.buffer = this.compress(bitmap);
    }

    public DatabaseBitmap(Context context, int drawableID)
    {
        Drawable drawable = context.getResources().getDrawable(drawableID, context.getTheme());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        this.buffer = this.compress(bitmap);
    }

    private byte[] compress(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

        return stream.toByteArray();
    }

    public byte[] getBytes()
    {
        return this.buffer;
    }

    public String getHexBytes()
    {
        int count = this.buffer.length;
        char[] chars = new char[2 * count];

        for (int i = 0; i < count; i++)
        {
            int value = this.buffer[i] & 0xFF;

            chars[i * 2] = HEX_CHARS[value >> 4];
            chars[i * 2 + 1] = HEX_CHARS[value & 0x0F];
        }

        return new String(chars);
    }

    public Bitmap getBitmap()
    {
        return BitmapFactory.decodeByteArray(this.buffer, 0, this.buffer.length);
    }
}
