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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri imageFilePath;
    private Bitmap imagetoStore;
    ImageView imageee;
    DatabaseHelper databaseHelper;
    EditText titl, subjet, nots, loation;
    TextView dae, tme;

    Calendar calendar = Calendar.getInstance();

    int hour, minuteeee;


    FusedLocationProviderClient fusedLocationProviderClient;
    Button save;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        databaseHelper = new DatabaseHelper(this);
        imageee = findViewById(R.id.eimage);
        titl = findViewById(R.id.etitle);
        subjet = findViewById(R.id.esubject);
        nots = findViewById(R.id.enotes);
        loation = findViewById(R.id.location);
        dae = findViewById(R.id.edate);
        tme = findViewById(R.id.etime);
        save = findViewById(R.id.savedata);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AddActivity.this);

      if (ActivityCompat.checkSelfPermission(AddActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AddActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when both permission are granted

            getCurrentlocation();
        } else {
          //when permission not grated
          //requeest permission
          ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                  Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

      }


        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AddActivity.this);

        dae.setText(simpleDateFormat.format(calendar.getTime()));



                tme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour = hourOfDay;
                                minuteeee = minute;

                                Calendar calendarerer = Calendar.getInstance();

                                calendarerer.set(0, 0, 0, hour, minuteeee);
                                tme.setText(DateFormat.format("hh:mm aa", calendarerer));
                            }
                        }, 12, 0, false);
                        timePickerDialog.updateTime(hour, minuteeee);
                        timePickerDialog.show();
                    }
                });


                DatePickerDialog.OnDateSetListener datee = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dae.setText(simpleDateFormat.format(calendar.getTime()));

                    }
                };


                dae.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new DatePickerDialog(AddActivity.this, datee,
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();


                    }
                });


                imageee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Intent objectintent = new Intent();
                            objectintent.setAction(Intent.ACTION_PICK);

                            objectintent.setType("image/*");
                            objectintent.setAction(Intent.ACTION_GET_CONTENT);

                            startActivityForResult(objectintent, PICK_IMAGE_REQUEST);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        loation.setText(stringBuffer);

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

                                loation.setText(stringBuffer);


                            }
                        };
                        if (ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageFilePath = data.getData();
                imagetoStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);

                imageee.setImageBitmap(imagetoStore);

            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titl.getText().toString();
                String subject = subjet.getText().toString();
                String notes = nots.getText().toString();
                String location = loation.getText().toString();
                String date = dae.getText().toString();
                String time = tme.getText().toString();
                Bitmap image = imagetoStore;

                if (image == null || title.equals("") || subject.equals("") || notes.equals("") || date.equals("") || time.equals("") || location.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill the fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean b = databaseHelper.insertdata(image, title, subject, notes, location, date, time);

                    if (b == true) {

                        Toast.makeText(getApplicationContext(), "Data added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        titl.setText("");
                        subjet.setText("");
                        nots.setText("");
                        loation.setText("");
                        dae.setText("");
                        tme.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "Data not added", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

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




