package com.deloladrin.cows.activities.company.dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deloladrin.cows.R;
import com.deloladrin.cows.activities.ChildDialog;
import com.deloladrin.cows.activities.company.CompanyActivity;
import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.database.DatabaseBitmap;
import com.deloladrin.cows.dialogs.YesNoDialog;

import static android.app.Activity.RESULT_OK;

public class CompanyEditDialog extends ChildDialog<CompanyActivity> implements View.OnClickListener
{
    private static final int COMPANY_ICON_WIDTH = 256;
    private static final int COMPANY_ICON_HEIGHT = 256;

    private static final int COMPANY_IMAGE_REQUEST = 0;

    private Company company;

    private EditText name;
    private EditText group;
    private ImageView image;

    private Button imageSelect;
    private ImageButton imageDelete;
    private boolean imageValid;

    private Button cancel;
    private Button submit;

    private OnSubmitListener onSubmitListener;

    public CompanyEditDialog(CompanyActivity parent, Company company)
    {
        super(parent);
        this.setContentView(R.layout.dialog_template_edit_company);

        this.company = company;

        /* Load all children */
        this.name = this.findViewById(R.id.dialog_name);
        this.group = this.findViewById(R.id.dialog_group);
        this.image = this.findViewById(R.id.dialog_image);

        this.imageSelect = this.findViewById(R.id.dialog_image_select);
        this.imageDelete = this.findViewById(R.id.dialog_image_delete);

        this.cancel = this.findViewById(R.id.dialog_cancel);
        this.submit = this.findViewById(R.id.dialog_submit);

        /* Add events */
        this.cancel.setOnClickListener(this);
        this.submit.setOnClickListener(this);

        this.imageSelect.setOnClickListener(this);
        this.imageDelete.setOnClickListener(this);

        /* Load default values */
        this.initialize();
    }

    private void initialize()
    {
        Context context = this.getContext();

        /* Load current company data */
        this.name.setText(this.company.getName());
        this.group.setText(this.company.getGroup());

        DatabaseBitmap image = this.company.getImage();

        if (image != null)
        {
            this.setImage(image.getBitmap());
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.imageSelect))
        {
            /* Request to change image */
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            this.activity.startActivityForResult(intent, COMPANY_IMAGE_REQUEST);

            return;
        }

        if (view.equals(this.imageDelete))
        {
            /* Request to remove */
            YesNoDialog dialog = new YesNoDialog(this.getContext());
            dialog.setText(R.string.dialog_image_delete);

            dialog.setOnYesListener((YesNoDialog d) ->
            {
                /* Remove image */
                this.setImage((Bitmap)null);
            });

            dialog.show();
            return;
        }

        if (view.equals(this.submit))
        {
            /* Update and submit */
            String name = this.name.getText().toString();
            this.company.setName(name);

            String group = this.group.getText().toString();

            if (!group.isEmpty())
            {
                this.company.setGroup(group);
            }
            else
            {
                this.company.setGroup(null);
            }

            if (this.imageValid)
            {
                DatabaseBitmap image = new DatabaseBitmap(this.getImage());
                this.company.setImage(image);
            }
            else
            {
                this.company.setImage((byte[])null);
            }

            this.onSubmitListener.onSubmit(this.company);
        }

        this.dismiss();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == COMPANY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null)
        {
            /* Change current image */
            this.setImage(data);
        }
    }

    public Company getCompany()
    {
        return this.company;
    }

    public Bitmap getImage()
    {
        if (this.imageValid)
        {
            Drawable fullDrawable = this.image.getDrawable();
            Bitmap fullBitmap = ((BitmapDrawable)fullDrawable).getBitmap();

            return Bitmap.createScaledBitmap(fullBitmap, COMPANY_ICON_WIDTH, COMPANY_ICON_HEIGHT, false);
        }

        return null;
    }

    public void setImage(Bitmap bitmap)
    {
        /* Update image */
        this.imageValid = bitmap != null;
        this.image.setImageBitmap(bitmap);
    }

    public void setImage(Intent data)
    {
        /* Update image */
        Uri imageUri = data.getData();

        this.imageValid = true;
        this.image.setImageURI(imageUri);
    }

    public OnSubmitListener getOnSubmitListener()
    {
        return this.onSubmitListener;
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener)
    {
        this.onSubmitListener = onSubmitListener;
    }

    public interface OnSubmitListener
    {
        void onSubmit(Company company);
    }
}
