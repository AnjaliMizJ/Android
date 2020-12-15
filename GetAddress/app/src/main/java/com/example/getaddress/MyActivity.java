package com.example.getaddress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MyActivity extends AppCompatActivity implements LocationListener {

    Button btnGPSShowLocation;
    Button btnShowAddress;
    TextView tvAddress;
    LocationManager locationManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adres);
        tvAddress = findViewById(R.id.tvAddress);


        grant_permission();
        btnShowAddress = findViewById(R.id.btnShowAddress);

        checkloactionisenablesornot();
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        btnShowAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                getlocation();

            }
        });
    }

    private void getlocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        }catch (Exception e)
        {e.printStackTrace();}
    }

    private void checkloactionisenablesornot() {
        LocationManager lm= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean getEnables= false;
        boolean networkEnabled = false;

        try {
            getEnables=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch (Exception e)
        {            e.printStackTrace();
        }
        try {
            networkEnabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception e)
        {            e.printStackTrace();
        }
        if (!getEnables && !networkEnabled)
        {
            new AlertDialog.Builder(MyActivity.this)
                    .setTitle("Enable GPS")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("cancel",null)
                    .show();
        }
    }

    private void grant_permission()
    {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
         ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},100);

        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            Log.e( "onLocationChanged: ", addresses.get(0).getCountryName());
            Log.e( "onLocationChanged: ", addresses.get(0).getPostalCode());
            Log.e( "onLocationChanged: ", addresses.get(0).getLocality());
           // Log.e( "onLocationChanged: ", addresses.get(0).getSubLocality());
            tvAddress.setText(addresses.get(0).getAddressLine(0)+"  "+addresses.get(0).getAddressLine(1)
                    );
            progressDialog.dismiss();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
