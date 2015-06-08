package in.silive.Service;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import in.silive.Config;
import in.silive.listener.NetworkResponseListener;
import in.silive.network.DownloadDATApost;
public class LocationUpdateService extends Service implements LocationListener, NetworkResponseListener {
    public static final String TAG = "UpdateLocationService";
    LocationManager locationManager;
    Location location;
    Address address;
    Geocoder geocoder;
    Double latitude, longitude;
    boolean isGPSEnabled = false, isNetworkEnabled = false, canGetLocation = false;
    long minTime = 5 * 1000;
    double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
    long minDistance = 10;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "OnCreate", Toast.LENGTH_SHORT).show();
        try {

            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            currentLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            currentLocation();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//                Toast.makeText(this, "false:" + "\n" + Math.abs(x2 - x1) + "\n" + Math.abs(y2 - y1), Toast.LENGTH_SHORT).show();
        //            currentLocation();
    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this,
                "Provider disabled: " + provider, Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this,
                "Provider enabled: " + provider, Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "OnDestroy", Toast.LENGTH_SHORT).show();
        if (locationManager != null)
            locationManager.removeUpdates(LocationUpdateService.this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "OnStart", Toast.LENGTH_SHORT).show();
        try {
            currentLocation();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public void currentLocation() throws IOException, JSONException {
        Location loc;
        String currentLoc = "";
        if (isNetworkEnabled && isGPSEnabled) {
            loc = getLocations();
            if (loc != null) {
                if (x1 == 0) {
                    x1 = loc.getLatitude();
                    y1 = loc.getLongitude();

                    DownloadDATApost.setNRL(this);
                    DownloadDATApost.setUrl(new URL(Config.DriverLocationUpdateAPI));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Id", "2");
                    jsonObject.put("CurrentLocationLatitude", loc.getLatitude());
                    jsonObject.put("CurrentLocationLongitude", loc.getLongitude());
                    DownloadDATApost.setJSON(jsonObject);
                    new DownloadDATApost().execute();

                } else {
                    if (x2 == 0) {
                        x2 = loc.getLatitude();
                        y2 = loc.getLongitude();

                        DownloadDATApost.setNRL(this);
                        DownloadDATApost.setUrl(new URL(Config.DriverLocationUpdateAPI));
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Id", "2");
                        jsonObject.put("CurrentLocationLatitude", loc.getLatitude());
                        jsonObject.put("CurrentLocationLongitude", loc.getLongitude());
                        DownloadDATApost.setJSON(jsonObject);
                        new DownloadDATApost().execute();

                    } else {
                        if (Math.abs(x2 - x1) > 0.0001 || Math.abs(y2 - y1) > 0.0001) {
                            Toast.makeText(this, "true:" + "\n" + Math.abs(x2 - x1) + "\n" + Math.abs(y2 - y1), Toast.LENGTH_LONG).show();
                            x1 = x2;
                            y1 = y2;
                            x2 = loc.getLatitude();
                            y2 = loc.getLongitude();
                            DownloadDATApost.setNRL(this);
                            DownloadDATApost.setUrl(new URL(Config.DriverLocationUpdateAPI));
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("Id", "2");
                            jsonObject.put("CurrentLocationLatitude", loc.getLatitude());
                            jsonObject.put("CurrentLocationLongitude", loc.getLongitude());
                            DownloadDATApost.setJSON(jsonObject);
                            new DownloadDATApost().execute();

                        }
                    }
                }
            }
        }
    }
    public Location getLocations(){
        try {
            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("Service status", "Not connected");
                Toast.makeText(this, "Not connected to network", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, minTime,
                        minDistance, this);
                Log.d("Network", "network");
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    Toast.makeText(this, "location manager == null", Toast.LENGTH_SHORT).show();
                    Log.d("location", "null");
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                minTime,
                                minDistance, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    return location;
                } else
                    Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("Error", "Exception getLocation" + e);
        }
        return null;
    }
    @Override
    public void onPreExecute() {
        Toast.makeText(this,"preExecute reached",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPostExecute(String data) throws JSONException {
        Toast.makeText(this,"Latitude and Longitude sent",Toast.LENGTH_SHORT).show();
    }
}