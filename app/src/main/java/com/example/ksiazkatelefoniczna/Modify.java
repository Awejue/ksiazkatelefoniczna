package com.example.ksiazkatelefoniczna;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Modify extends MainActivity{

    public static String id;
    protected String[] dane;
    protected ContentValues values = new ContentValues();
    protected ContentValues valuesImage = new ContentValues();
    private ImageView imageView;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {}

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

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        valuesImage.put("avatar", bos.toByteArray());
                        mDb.update(TABLE_NAME, valuesImage, "id=?", new String[]{id});
                    }
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.modify);

        Cursor cursor = mDb.query(TABLE_NAME, new String[] {"name", "last_name", "country", "phone", "avatar"}, "id == ?", new String[] {id}, null, null, null);

        cursor.moveToNext();

        TextView name = findViewById(R.id.name);
        TextView last_name = findViewById(R.id.last_name);
        TextView city = findViewById(R.id.city);
        TextView number = findViewById(R.id.number);

        dane = new String[] {cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)};
        byte[] image = cursor.getBlob(4);

        name.setText(cursor.getString(0));
        last_name.setText(cursor.getString(1));
        city.setText(cursor.getString(2));
        number.setText(cursor.getString(3));

        EditText eName = findViewById(R.id.eName);
        eName.setHint(name.getText());
        EditText eLast_name = findViewById(R.id.eLastName);
        eLast_name.setHint(last_name.getText());
        EditText eCity = findViewById(R.id.eCity);
        eCity.setHint(city.getText());
        EditText eNumber = findViewById(R.id.eNumber);
        eNumber.setHint(number.getText());

        imageView = findViewById(R.id.avatar);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

        Button changeData = findViewById(R.id.change);
        Button changeImage = findViewById(R.id.changeImage);
        Button delete = findViewById(R.id.delete);
        ImageButton back = findViewById(R.id.back);

        changeImage.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);

            launchSomeActivity.launch(i);
        });

        changeData.setOnClickListener(view -> {
            if (name.getVisibility() == View.VISIBLE)
            {
                name.setVisibility(View.GONE);
                last_name.setVisibility(View.GONE);
                city.setVisibility(View.GONE);
                number.setVisibility(View.GONE);

                eName.setVisibility(View.VISIBLE);
                eLast_name.setVisibility(View.VISIBLE);
                eCity.setVisibility(View.VISIBLE);
                eNumber.setVisibility(View.VISIBLE);
            }
            else
            {
                eNumber.setVisibility(View.GONE);
                eName.setVisibility(View.GONE);
                eLast_name.setVisibility(View.GONE);
                eCity.setVisibility(View.GONE);

                name.setVisibility(View.VISIBLE);
                last_name.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);
                number.setVisibility(View.VISIBLE);
                if (!String.valueOf(eName.getText()).equals(""))
                {
                    values.put("name", String.valueOf(eName.getText()));
                    dane[0] = String.valueOf(eName.getText());
                    eName.setText("");
                    eName.setHint(dane[0]);
                }
                if (!String.valueOf(eLast_name.getText()).equals(""))
                {
                    values.put("last_name", String.valueOf(eLast_name.getText()));
                    dane[1] = String.valueOf(eLast_name.getText());
                    eLast_name.setText("");
                    eLast_name.setHint(dane[1]);
                }
                if (!String.valueOf(eCity.getText()).equals(""))
                {
                    values.put("country", String.valueOf(eCity.getText()));
                    dane[2] = String.valueOf(eCity.getText());
                    eCity.setText("");
                    eCity.setHint(dane[2]);
                }
                if (!String.valueOf(eNumber.getText()).equals(""))
                {
                    values.put("phone", String.valueOf(eNumber.getText()));
                    dane[3] = String.valueOf(eNumber.getText());
                    eNumber.setText("");
                    eNumber.setHint(dane[3]);
                }
                if (!values.isEmpty())
                {
                    mDb.update(TABLE_NAME, values, "id == ?", new String[] {id});
                    name.setText(dane[0]);
                    last_name.setText(dane[1]);
                    city.setText(dane[2]);
                    number.setText(dane[3]);
                }
            }
        });

        delete.setOnClickListener(view -> {
            mDb.delete(TABLE_NAME, "id == ?", new String[]{id});
            this.finish();
        });

        back.setOnClickListener(view -> {
            this.finish();
        });

        cursor.close();
    }

}
