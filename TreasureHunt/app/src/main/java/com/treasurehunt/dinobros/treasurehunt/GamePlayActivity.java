package com.treasurehunt.dinobros.treasurehunt;

import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GamePlayActivity extends AppCompatActivity implements LocationListener {
    String l1 = "38.034132168967275, -78.50826490670443";
    String l2 = "37, -80";
    LatLng loc1 = new LatLng(Double.parseDouble(l1.split(",")[0]), Double.parseDouble(l1.split(",")[1]));
    LatLng loc2 = new LatLng(Double.parseDouble(l2.split(",")[0]), Double.parseDouble(l2.split(",")[1]));
    ArrayList<LatLng> locs = new ArrayList<LatLng>();
    ArrayList<String> clues = new ArrayList<String>();
    int clueNum = 0;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    LatLng curLoc = new LatLng(-1000,-1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locs.add(loc1); locs.add(loc2);
        clues.add("You are here.");
        clues.add("You are not here.");
        TextView clueLabel = (TextView)findViewById(R.id.clueText);
        clueLabel.setText(clues.get(clueNum));
    }

    public void checkLoc(View v){
        if (curLoc.latitude!=-1000 && curLoc.longitude!=-1000){
            float[]res = new float[3];
            Location.distanceBetween(curLoc.latitude,curLoc.longitude,locs.get(clueNum).latitude,locs.get(clueNum).longitude, res);
            if (res[0]<15){
                TextView wrongRight = (TextView)findViewById(R.id.correctText);
                wrongRight.setText("You're Right! - "+locs.get(clueNum)+","+curLoc+","+res[0]);
            }
            else{
                TextView wrongRight = (TextView)findViewById(R.id.correctText);
                wrongRight.setText("Wrong! "+locs.get(clueNum)+","+curLoc+","+res[0]);
            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        curLoc = new LatLng(location.getLatitude(),location.getLongitude());
        if (curLoc.latitude!=-1000 && curLoc.longitude!=-1000){
            float[]res = new float[3];
            Location.distanceBetween(curLoc.latitude,curLoc.longitude,locs.get(clueNum).latitude,locs.get(clueNum).longitude, res);
            if (res[0]<12){
                TextView wrongRight = (TextView)findViewById(R.id.correctText);
                wrongRight.setText("You're Right! - "+locs.get(clueNum)+","+curLoc+","+res[0]);
            }
            else{
                TextView wrongRight = (TextView)findViewById(R.id.correctText);
                wrongRight.setText("Wrong! "+locs.get(clueNum)+","+curLoc+","+res[0]);
            }


        }
    }


    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
