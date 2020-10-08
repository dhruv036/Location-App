package com.example.hikersapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView logi , lati,addresss,alti,acc;
    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void setlocation(Location location)
    {   addresss = (TextView) findViewById(R.id.textView15);
        logi = (TextView) findViewById(R.id.textView11);
        lati =(TextView) findViewById(R.id.textView13);
        alti = (TextView) findViewById(R.id.textView14);
        acc = (TextView) findViewById(R.id.textView12);
        alti.setText(""+location.getAltitude());
        acc.setText(""+location.getAccuracy());
        logi.setText(""+location.getLongitude());
        lati.setText(""+location.getLatitude());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (list != null && list.size() > 0)
            {     String add = "";
              if(list.get(0).getThoroughfare() != null)
              {
                  add += list.get(0).getThoroughfare() + " ";
              }
              if(list.get(0).getLocality() != null)
                {
                    add += list.get(0).getLocality() + "";
                }
                if(list.get(0).getPostalCode() != null)
                {
                    add += list.get(0).getPostalCode() + "";
                }
                if(list.get(0).getAdminArea() != null)
                {
                    add += list.get(0).getAdminArea();
                }
                addresss.setText(add);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener =  new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                setlocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,0,locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation != null)
            {
                updateLocationinfo(lastLocation);
            }
        }
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void updateLocationinfo(Location location)
    {
        setlocation(location);
    }
}
