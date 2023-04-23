package com.example.ksiazkatelefoniczna;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class Modify extends MainActivity{

    public static String id;
    protected String[] dane;
    protected ContentValues values = new ContentValues();

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {}

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.modify);

        Cursor cursor = mDb.query(TABLE_NAME, new String[] {"name", "last_name", "country", "phone"}, "id == ?", new String[] {id}, null, null, null);

        cursor.moveToNext();

        TextView name = findViewById(R.id.name);
        TextView last_name = findViewById(R.id.last_name);
        TextView city = findViewById(R.id.city);
        TextView number = findViewById(R.id.number);

        dane = new String[] {cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)};

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

        Button changeData = findViewById(R.id.change);
        Button changeImage = findViewById(R.id.changeImage);

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
                    values.put("city", String.valueOf(eCity.getText()));
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

        cursor.close();
    }

}
