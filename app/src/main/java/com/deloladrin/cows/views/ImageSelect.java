package com.deloladrin.cows.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deloladrin.cows.R;
import com.deloladrin.cows.database.DatabaseActivity;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.dialogs.YesNoDialog;

public class ImageSelect implements View.OnClickListener, DatabaseActivity.OnActivityResultListener
{
    private static final int COMPANY_IMAGE_REQUEST = 0;

    private DatabaseActivity activity;
    private LinearLayout layout;

    private int width;
    private int height;

    private ImageView view;
    private Button select;
    private ImageButton delete;

    private boolean valid;

    public ImageSelect(DatabaseActivity activity, LinearLayout layout, int width, int height)
    {
        this.activity = activity;
        this.layout = layout;

        this.width = width;
        this.height = height;

        /* Load all children */
        this.view = layout.findViewById(R.id.image_view);
        this.select = layout.findViewById(R.id.image_select);
        this.delete = layout.findViewById(R.id.image_delete);

        /* Add events */
        this.select.setOnClickListener(this);
        this.delete.setOnClickListener(this);

        this.activity.addOnActivityResultListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.select))
        {
            /* Request to change image */
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            this.activity.startActivityForResult(intent, COMPANY_IMAGE_REQUEST);

            return;
        }

        if (view.equals(this.delete))
        {
            /* Request to remove */
            YesNoDialog dialog = new YesNoDialog(this.activity);
            dialog.setText(R.string.dialog_image_delete);

            dialog.setOnYesListener((YesNoDialog d) ->
            {
                /* Remove image */
                this.setBitmap(null);
            });

            dialog.show();
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == COMPANY_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null)
        {
            /* Update image */
            Uri uri = data.getData();

            this.valid = true;
            this.view.setImageURI(uri);
        }
    }

    public Bitmap getBitmap()
    {
        if (this.valid)
        {
            Drawable fullDrawable = this.view.getDrawable();
            Bitmap fullBitmap = ((BitmapDrawable)fullDrawable).getBitmap();

            return Bitmap.createScaledBitmap(fullBitmap, this.width, this.height, false);
        }

        return null;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.view.setImageBitmap(bitmap);
        this.valid = bitmap != null;
    }

    public DatabaseBitmap getDatabaseBitmap()
    {
        Bitmap bitmap = this.getBitmap();

        if (bitmap != null)
        {
            return new DatabaseBitmap(bitmap);
        }

        return null;
    }

    public void setDatabaseBitmap(DatabaseBitmap bitmap)
    {
        Bitmap raw = null;

        if (bitmap != null)
        {
            raw = bitmap.getBitmap();
        }

        this.setBitmap(raw);
    }
}
