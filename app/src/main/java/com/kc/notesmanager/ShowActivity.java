package com.kc.notesmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    Adapterdata adapterdata;
    ArrayList<Modeldata> modeldata;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        modeldata=new ArrayList<>();
        manager=new LinearLayoutManager(this);
        databaseHelper=new DatabaseHelper(this);
        recyclerView.setLayoutManager(manager);


        Cursor cursor=databaseHelper.readdta();
        if(cursor.getCount()==0)
        {
            Toast.makeText(getApplicationContext(),"No data",Toast.LENGTH_SHORT).show();

        }

        while (cursor.moveToNext())
        {
            byte[] imagebytes=cursor.getBlob(1);
            Bitmap image= BitmapFactory.decodeByteArray(imagebytes,0,imagebytes.length);



            Modeldata data=new Modeldata(cursor.getString(0),
                    image,
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7));
                     modeldata.add(data);
        }

        adapterdata=new Adapterdata(this,modeldata);
        recyclerView.setAdapter(adapterdata);

    }
}