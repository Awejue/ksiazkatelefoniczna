package com.example.ksiazkatelefoniczna;
import static com.example.ksiazkatelefoniczna.Modify.id;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    protected static final String DB_NAME = "baza_danych";
    protected static final String TABLE_NAME = "osoby";

    protected SQLiteDatabase mDb;
    protected String[] dane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Otwieranie bazy danych
        mDb = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        // Tworzenie tabeli, jeśli nie istnieje
        mDb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, last_name TEXT, country TEXT, phone TEXT, avatar BLOB)");

        TextView search = findViewById(R.id.search);
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> showDataInTable("%"+search.getText().toString()+"%"));


        Button AddRecord = findViewById(R.id.dodaj);
        AddRecord.setOnClickListener(view -> {
            Intent intentMain = new Intent(MainActivity.this,
                    Add.class);
            MainActivity.this.startActivity(intentMain);
        });

        // Wyświetlanie danych z bazy danych w tabeli
        showDataInTable();
    }

    // Metoda wyświetlająca dane z bazy danych w tabeli
    protected void showDataInTable() {
        TableLayout table = findViewById(R.id.table);
        table.removeAllViewsInLayout();

        // Pobieranie danych z bazy danych
        Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null, null);

        // Dodawanie nagłówków tabeli
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"ID", "Imie", "Nazwisko", "Miejscowosc", "Numer"};
        for (String s : headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(s);
            headerRow.addView(tv);
        }
        table.addView(headerRow);

        while (cursor.moveToNext()) {
            dane = new String[] {String.valueOf(cursor.getInt(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)};
            TableRow row= new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            for (String s : dane) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(18);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(s);
                tv.setId(Integer.parseInt(dane[0]));
                tv.setOnLongClickListener(view -> {
                    id = String.valueOf(view.getId());
                    Intent intentMain = new Intent(MainActivity.this,
                            Modify.class);
                    MainActivity.this.startActivity(intentMain);
                    return true;
                });
                row.addView(tv);
            }
            table.addView(row);
        }
        cursor.close();
    }

    protected void showDataInTable(String args) {
        TableLayout table = findViewById(R.id.table);
        table.removeAllViewsInLayout();

        // Pobieranie danych z bazy danych
        Cursor cursor = mDb.query(TABLE_NAME, null, "id == ? OR name LIKE ? OR last_name LIKE ? OR country LIKE ? OR phone LIKE ?", new String[]{args,args,args,args,args}, null, null, null);

        // Dodawanie nagłówków tabeli
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"ID", "Imie", "Nazwisko", "Miejscowosc", "Numer"};
        for (String s : headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(s);
            headerRow.addView(tv);
        }
        table.addView(headerRow);

        while (cursor.moveToNext()) {
            dane = new String[] {String.valueOf(cursor.getInt(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)};
            TableRow row= new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            for (String s : dane) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(18);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(s);
                tv.setId(Integer.parseInt(dane[0]));
                tv.setOnLongClickListener(view -> {
                    id = String.valueOf(view.getId());
                    Intent intentMain = new Intent(MainActivity.this,
                            Modify.class);
                    MainActivity.this.startActivity(intentMain);
                    return true;
                });
                row.addView(tv);
            }
            table.addView(row);
        }
        cursor.close();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) showDataInTable();
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDb.close();
    }

}