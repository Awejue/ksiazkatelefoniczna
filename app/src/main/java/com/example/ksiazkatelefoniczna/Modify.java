package com.example.ksiazkatelefoniczna;

import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Modify extends MainActivity{

    public static String id;
    protected String[] dane;

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

        EditText eName = new EditText(this);
        eName.setInputType(InputType.TYPE_CLASS_TEXT);
        EditText eLast_name = new EditText(this);
        eLast_name.setInputType(InputType.TYPE_CLASS_TEXT);
        EditText eCity = new EditText(this);
        eCity.setInputType(InputType.TYPE_CLASS_TEXT);
        EditText eNumber = new EditText(this);
        eNumber.setInputType(InputType.TYPE_CLASS_TEXT);

        Button changeData = findViewById(R.id.change);
        Button changeImage = findViewById(R.id.changeImage);

        changeData.setOnClickListener(view -> {

        });

        cursor.close();
    }

}
