package com.emrecanisik.kompanzasyontakip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    ListView listView;
    ArrayList<String> nameArray;
    ArrayList<Integer> idArray;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = findViewById(R.id.listView);
        nameArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameArray);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(MainActivity2.this,MainActivity.class);
                intent.putExtra("cezaId",idArray.get(i));
                intent.putExtra("info","old");









                startActivity(intent);


            }
        });

        getData();



    }





    public void getData() {

        try {
            SQLiteDatabase database = this.openOrCreateDatabase("KompanzasyonGecmis", MODE_PRIVATE, null);

            Cursor cursor = database.rawQuery("SELECT *FROM gecmis", null);
            int nameIx = cursor.getColumnIndex("tarih");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()) {

                nameArray.add(cursor.getString(nameIx));
                idArray.add((cursor.getInt(idIx)));
                arrayAdapter.notifyDataSetChanged();

            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

      if(item.getItemId()==R.id.add_art_item){
            Intent intent=new Intent(MainActivity2.this,MainActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);






      }
      return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater(); // menu kutucuğunu çağırtma
        menuInflater.inflate(R.menu.add_history, menu);

        return super.onCreateOptionsMenu(menu);
    }


    }


