package com.example.ksiazkatelefoniczna;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Add extends MainActivity{

    protected ContentValues values = new ContentValues();
    private ImageView imageView;
    private ByteArrayOutputStream bos;

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap b = Bitmap.createScaledBitmap(selectedImageBitmap, 140,140,false);
                        imageView.setImageBitmap(b);

                        bos = new ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.PNG, 100, bos);

                    }
                }
            });

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {}

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add);

        EditText aName = findViewById(R.id.aName);
        EditText aLast_name = findViewById(R.id.aLastName);
        EditText aCity = findViewById(R.id.aCity);
        EditText aNumber = findViewById(R.id.aNumber);

        imageView = findViewById(R.id.avatar);

        Button addUser = findViewById(R.id.change);
        Button addImage = findViewById(R.id.addImage);
        ImageButton back = findViewById(R.id.back);

        addImage.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

            launchSomeActivity.launch(i);
        });

        addUser.setOnClickListener(view -> {
            if (!String.valueOf(aName.getText()).equals(""))
            {
                values.put("name", String.valueOf(aName.getText()));
            }
            if (!String.valueOf(aLast_name.getText()).equals(""))
            {
                values.put("last_name", String.valueOf(aLast_name.getText()));
            }
            if (!String.valueOf(aCity.getText()).equals(""))
            {
                values.put("country", String.valueOf(aCity.getText()));
            }
            if (!String.valueOf(aNumber.getText()).equals(""))
            {
                values.put("phone", String.valueOf(aNumber.getText()));
            }
            if (bos.size()!=0)
            {
                values.put("avatar", bos.toByteArray());
            }
            if (!values.isEmpty())
            {
                mDb.insert(TABLE_NAME,null, values);
                this.finish();
            }
        });

        back.setOnClickListener(view -> {
            this.finish();
        });
    }
}
