package com.example.ksiazkatelefoniczna;
import static com.example.ksiazkatelefoniczna.Modify.id;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    protected static final String DB_NAME = "database_name";
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

        /*ContentValues values = new ContentValues();
        values.put("name", "Aflonoanosnda");
        values.put("last_name", "Spien");
        values.put("country", "Golkowice");
        values.put("phone", "123456798");
        mDb.insert(TABLE_NAME,null, values);*/

        // Wyświetlanie danych z bazy danych w tabeli
        showDataInTable();
    }

    // Metoda wyświetlająca dane z bazy danych w tabeli
    protected void showDataInTable() {
        TableLayout table = findViewById(R.id.table);
        table.removeAllViews();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDb.close();
    }
}