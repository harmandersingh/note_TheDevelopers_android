package com.kc.notesmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity implements OnMapReadyCallback {
    ImageView objectimage;
   TextView titl,subjet,nots;
   MapView mapView;


    TextView dae,tme;
    DatabaseHelper db;
    ArrayList<Modeldata> list;

    String loc,lon,lot;
    int PICK_IMAGE_REQUEST=100;
    Uri imageFilePath;
    Bitmap imagetoStore;
    ByteArrayOutputStream bs;
    GoogleMap gmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        db=new DatabaseHelper(this);
        objectimage=findViewById(R.id.disimage);
        titl=findViewById(R.id.distitle);
        list=new ArrayList<>();
        subjet=findViewById(R.id.dissubject);
        nots=findViewById(R.id.disnotes);


        dae=findViewById(R.id.disdate);
        tme=findViewById(R.id.distime);

        mapView=findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



  Intent intent=getIntent();
      String ID=  intent.getStringExtra("id");
        String title= intent.getStringExtra("title");

        String subject= intent.getStringExtra("subject");
        String location= intent.getStringExtra("loaction");
        loc=intent.getStringExtra("loaction");
        String[] arr=loc.split(",");
         lon=arr[0];
         lot=arr[1];


        String date= intent.getStringExtra("date");
        String time= intent.getStringExtra("time");
        String notes= intent.getStringExtra("notes");
        if(getIntent().hasExtra("byteArray"))
        {
            Bitmap bitmap=BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"),
                    0,getIntent().getByteArrayExtra("byteArray").length);
            objectimage.setImageBitmap(bitmap);

        }


        titl.setText(title);
        subjet.setText(subject);
        nots.setText(notes);

        dae.setText(date);
        tme.setText(time);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:



                Intent intent=getIntent();
                String ID=  intent.getStringExtra("id");
                String title= intent.getStringExtra("title");

                String subject= intent.getStringExtra("subject");
                String location= intent.getStringExtra("loaction");
                String date= intent.getStringExtra("date");
                String time= intent.getStringExtra("time");
                String notes= intent.getStringExtra("notes");
                if(getIntent().hasExtra("byteArray"))
                {
                    Bitmap bitmap=BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"),
                            0,getIntent().getByteArrayExtra("byteArray").length);

                     bs=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,bs);

                }



                Intent in=new Intent(getApplicationContext(),EditActivity.class);
                in.putExtra("id",ID);
                in.putExtra("byteArray",bs.toByteArray());
                in.putExtra("title",title);
                in.putExtra("subject",subject);
                in.putExtra("loaction",location);
                in.putExtra("date",date);
                in.putExtra("time",time);
                in.putExtra("notes",notes);
                startActivity(in);

                return  true;




            case R.id.delete:

                Intent ii = getIntent();
                String id = ii.getStringExtra("id");


                long v = db.deletedata(id);
                if (v > 0) {
                    Toast.makeText(getApplicationContext(), "DATA DELETED", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), ShowActivity.class);
                    startActivity(intent1);

                } else {
                    Toast.makeText(getApplicationContext(), "DATA not DELETED", Toast.LENGTH_SHORT).show();
                }

                return true;


        }

        return super.onOptionsItemSelected(item);



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {


        super.onSaveInstanceState(outState, outPersistentState);


    /*    Bundle mapViewBUNDLE=outState.getBundle(MAP_VIEW_BUNDLE);

        if(mapViewBUNDLE==null)
        {
            mapViewBUNDLE=new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE,mapViewBUNDLE);
        }
 */   mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {

        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



       double d=Double.parseDouble(lon);
       double dd=Double.parseDouble(lot);

       gmap=googleMap;


        LatLng ny = new LatLng(dd,d);
        gmap.addMarker(new MarkerOptions().position(ny).title("PUNJAB"));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}

