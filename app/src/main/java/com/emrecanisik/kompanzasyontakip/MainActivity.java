package com.emrecanisik.kompanzasyontakip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    EditText txtIlkTop;
    EditText txtSonTop;
    EditText txtIlkEri;
    EditText txtSonEri;
    EditText txtIlkKrc;
    EditText txtSonKrc;
    Button button;
    EditText editTextDate2;
    TextView SonucEnd;
    TextView SonucKap;
    TextView CezaEnd;
    TextView CezaKap;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        txtIlkTop = findViewById(R.id.txtIlkTop);
        txtSonTop = findViewById(R.id.txtSonTop);
        txtIlkEri = findViewById(R.id.txtIlkEri);
        txtSonEri = findViewById(R.id.txtSonEri);
        txtIlkKrc = findViewById(R.id.txtIlkKrc);
        txtSonKrc = findViewById(R.id.txtSonKrc);
        editTextDate2 = findViewById(R.id.editTextDate2);
        SonucEnd = findViewById(R.id.lblSonucEnd);
        SonucKap = findViewById(R.id.lblSonucKap);
        CezaEnd = findViewById(R.id.lblCezaEnd);
        CezaKap = findViewById(R.id.lblCezaKap);
        database = this.openOrCreateDatabase("KompanzasyonGecmis", MODE_PRIVATE, null);

            Intent intent = getIntent();
            String info = intent.getStringExtra("info");


            if (info.matches("new")) {

                txtIlkTop.setText("");
                txtSonTop.setText("");
                txtIlkEri.setText("");
                txtSonEri.setText("");
                txtSonKrc.setText("");
                txtIlkKrc.setText("");
                editTextDate2.setText("");
            }
            else{



                int cezaid = intent.getIntExtra("cezaId", 1);

                try {
                    Cursor cursor = database.rawQuery("SELECT *FROM gecmis WHERE id = ?", new String[]{String.valueOf(cezaid)});

                    int IlkTopIx = cursor.getColumnIndex("ilktop");
                    int SonTopIx = cursor.getColumnIndex("sontop");
                    int IlkEriIx = cursor.getColumnIndex("ilkeri");
                    int SonEriIx = cursor.getColumnIndex("soneri");
                    int IlkKrcIx = cursor.getColumnIndex("ilkkcr");
                    int SonKrcIx = cursor.getColumnIndex("sonkcr");
                    int textDateIx = cursor.getColumnIndex("tarih");

                    while (cursor.moveToNext()) {
                        txtIlkTop.setText(cursor.getInt(IlkTopIx));
                        txtSonTop.setText((cursor.getInt(SonTopIx)));
                        txtIlkEri.setText((cursor.getInt(IlkEriIx)));
                        txtSonEri.setText(cursor.getInt(SonEriIx));
                        txtSonKrc.setText(cursor.getInt(SonKrcIx));
                        txtIlkKrc.setText(cursor.getInt(IlkKrcIx));
                        editTextDate2.setText(cursor.getString(textDateIx));


                    }
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


       }




    double IlkTop;
    double SonTop;
    double IlkKrc;
    double SonKrc;
    double SonEri;
    double IlkEri;
    String tarih;
    //********************************* Button Hesap*********************************************************************************

    public void btnhesap(View view) {



        if (txtIlkTop.getText().toString().matches("") || txtSonTop.getText().toString().matches("") || txtIlkKrc.getText().toString().matches("") || txtSonKrc.getText().toString().matches("")
                || txtSonEri.getText().toString().matches("") || txtIlkEri.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Lütfen bir değer giriniz", Toast.LENGTH_LONG).show(); //uyarı mesajı ekledik.
        } else {

            IlkTop = Double.parseDouble(txtIlkTop.getText().toString());
            SonTop = Double.parseDouble(txtSonTop.getText().toString());
            IlkKrc = Double.parseDouble(txtIlkKrc.getText().toString());
            SonKrc = Double.parseDouble(txtSonKrc.getText().toString());
            SonEri = Double.parseDouble(txtSonEri.getText().toString());
            IlkEri = Double.parseDouble(txtIlkEri.getText().toString());
            tarih=editTextDate2.getText().toString();
            double farkKrc = SonKrc - IlkKrc;
            double farkEri = SonEri - IlkEri;
            double farkTop = SonTop - IlkTop;



            double SonucKapasitif = (farkKrc / farkTop) * 100;
            double SonucEnduktif = farkEri / farkTop * 100;

            DecimalFormat formatter4 = new DecimalFormat("##.###");

            SonucEnd.setText("" + formatter4.format(SonucEnduktif));
            SonucKap.setText("" + formatter4.format(SonucKapasitif));

            if (SonucKapasitif > 15) {
                CezaKap.setText("Var");
            }
            if (SonucKapasitif <= 15) {
                CezaKap.setText("Yok");
            }
            if (SonucEnduktif > 20) {
                CezaEnd.setText("Var");
            }
            if (SonucEnduktif <= 20) {
                CezaEnd.setText("Yok");
            }


        }
    }


    public void btnkaydet(View view) {
        if (txtIlkTop.getText().toString().matches("") || txtSonTop.getText().toString().matches("") || txtIlkKrc.getText().toString().matches("") || txtSonKrc.getText().toString().matches("")
                || txtSonEri.getText().toString().matches("") || txtIlkEri.getText().toString().matches("") || editTextDate2.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Lütfen bir değer giriniz", Toast.LENGTH_LONG).show(); //uyarı mesajı ekledik.
        } else {

            try {
                SQLiteDatabase database = this.openOrCreateDatabase("KompanzasyonGecmis", MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS gecmis (id INTEGER PRIMARY KEY ,ilktop VARCHAR,sontop VARCHAR,ilkeri VARCHAR,soneri VARCHAR,ilkkcr VARCHAR,sonkcr VARCHAR,tarih VARCHAR)");

                String sqlstring = "INSERT INTO gecmis(ilktop,sontop,ilkeri,soneri,ilkkcr,sonkcr,tarih)VALUES(?,?,?,?,?,?,?)";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlstring);
                sqLiteStatement.bindDouble(1, IlkTop);
                sqLiteStatement.bindDouble(2, SonTop);
                sqLiteStatement.bindDouble(3, IlkEri);
                sqLiteStatement.bindDouble(4, SonEri);
                sqLiteStatement.bindDouble(5, IlkKrc);
                sqLiteStatement.bindDouble(6, SonKrc);
                sqLiteStatement.bindString(7,tarih);
                sqLiteStatement.execute();


            } catch (Exception e) {

              e.printStackTrace();
            }
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }



        }





}