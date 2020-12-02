package com.deloladrin.cows.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class DatabaseBitmap
{
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    private ByteBuffer buffer;

    public DatabaseBitmap(byte[] buffer)
    {
        this.buffer = ByteBuffer.wrap(buffer);
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

    private ByteBuffer compress(Bitmap bitmap)
    {
        int size = bitmap.getRowBytes() * bitmap.getHeight();

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * 2 + size);
        buffer.putInt(bitmap.getWidth());
        buffer.putInt(bitmap.getHeight());

        bitmap.copyPixelsToBuffer(buffer);
        buffer.flip();

        return buffer;
    }

    public byte[] getBytes()
    {
        return this.buffer.array();
    }

    public String getHexBytes()
    {
        this.buffer.rewind();

        int count = this.buffer.capacity();
        char[] chars = new char[2 * count];

        for (int i = 0; i < count; i++)
        {
            int value = this.buffer.get(i) & 0xFF;

            chars[i * 2] = HEX_CHARS[value >> 4];
            chars[i * 2 + 1] = HEX_CHARS[value & 0x0F];
        }

        return new String(chars);
    }

    public Bitmap getBitmap()
    {
        this.buffer.rewind();

        int width = buffer.getInt();
        int height = buffer.getInt();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);

        return bitmap;
    }
}
