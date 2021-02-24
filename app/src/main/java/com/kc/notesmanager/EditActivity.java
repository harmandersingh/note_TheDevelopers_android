package com.kc.notesmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    DatabaseHelper db;

    EditText titl1,subjet1,nots1,loation1;
    TextView dae1,tme1;
    Button save;
    int PICK_IMAGE_REQUEST=100;
    Uri imageFilePath;
    Bitmap imagetoStore;
    ImageView imageView;

    Calendar calendar=Calendar.getInstance();
    FusedLocationProviderClient fusedLocationProviderClient;

    int hour,minuteeee;



    SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        db=new DatabaseHelper(this);
        imageView=findViewById(R.id.disimage);
         titl1=findViewById(R.id.dtitle);
         subjet1=findViewById(R.id.dsubject);
         nots1=findViewById(R.id.dnotes);
         loation1=findViewById(R.id.dlocation);
       dae1=findViewById(R.id.ddate);
          tme1=findViewById(R.id.dtime);
         save = findViewById(R.id.dsave);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(EditActivity.this);

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());


        loation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check conditiok
                if (ActivityCompat.checkSelfPermission(EditActivity.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(EditActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when both permission are granted

                    getCurrentlocation();
                } else {
                    //when permission not grated
                    //requeest permission
                    ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

                }
            }
        });
        tme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog=new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour=hourOfDay;
                        minuteeee=minute;

                        Calendar calendarerer=Calendar.getInstance();

                        calendarerer.set(0,0,0,hour,minuteeee);
                        tme1.setText(DateFormat.format("hh:mm aa",calendarerer));
                    }
                },12,0,false);
                timePickerDialog.updateTime(hour,minuteeee);
                timePickerDialog.show();
            }
        });




        DatePickerDialog.OnDateSetListener datee=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                dae1.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };





        dae1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(EditActivity.this,datee,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        Intent intent=getIntent();
   /*     if(getIntent().hasExtra("byteArray"))
        {
            Bitmap bitmap= BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"),
                    0,getIntent().getByteArrayExtra("byteArray").length);
            imageView.setImageBitmap(bitmap);

        }
*/
        String title1=    intent.getStringExtra("title");
        String subject1=    intent.getStringExtra("subject");
        String location1=    intent.getStringExtra("loaction");
        String date1=    intent.getStringExtra("date");
        String time1=   intent.getStringExtra("time");
        String notes1=  intent.getStringExtra("notes");

        titl1.setText(title1);
        subjet1.setText(subject1);
        nots1.setText(notes1);
        loation1.setText(location1);
        dae1.setText(date1);
        tme1.setText(time1);




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent objectintent=new Intent();
                    objectintent.setType("Image/*");
                    objectintent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(objectintent,PICK_IMAGE_REQUEST);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inttt=getIntent();
                String ID=  inttt.getStringExtra("id");
                String title=titl1.getText().toString();
                String subject=subjet1.getText().toString();
                String location=loation1.getText().toString();
                String date=dae1.getText().toString();
                String time=tme1.getText().toString();
                String notes=nots1.getText().toString();
               Bitmap image=imagetoStore;




                if(image==null || title.equals("") || subject.equals("") || notes.equals("") || date.equals("")||time.equals("") ||location.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please fill the fields",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    boolean b = db.upadtedata(ID,image, title, subject, notes, location, date, time);
                    if (b == true) {
                        Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(), ShowActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Data not updated", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });
    }

    private void getCurrentlocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            //when location service is enabled
            //get last location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location locsation = task.getResult();
                    if (locsation != null) {
                        String slat= String.valueOf(locsation.getLatitude());
                        String slong=String.valueOf(locsation.getLongitude());
                        StringBuffer stringBuffer=new StringBuffer();
                        stringBuffer.append(slong+","+slat);
                        loation1.setText(stringBuffer);

                    } else {
                        //when location result is null
                        //initialize location resquest
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                String slat= String.valueOf(location1.getLatitude());
                                String slong=String.valueOf(location1.getLongitude());
                                StringBuffer stringBuffer=new StringBuffer();
                                stringBuffer.append(slong+","+slat);
                                loation1.setText(stringBuffer);


                            }
                        };
                        if (ActivityCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        }else {
            //when location is not enabled
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
            {
                imageFilePath=data.getData();
                imagetoStore= MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);

                imageView.setImageBitmap(imagetoStore);
            }



        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==100 && grantResults.length>0 &&(grantResults[0]+grantResults[1]==PackageManager.PERMISSION_GRANTED))
        {
            getCurrentlocation();
        }
         else {
             Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT).show();
        }
    }
}