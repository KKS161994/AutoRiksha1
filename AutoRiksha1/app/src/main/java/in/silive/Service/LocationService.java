/*
package in.silive.bustrackerforconductor;

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

import listener.NetworkResponseListener;

public class UpdateLocationService extends Service implements LocationListener, NetworkResponseListener {

    public static final String TAG = "UpdateLocationService";
    LocationManager locationManager;
    Location location;
    Address address;
    Geocoder geocoder;
    Double latitude, longitude;
    SendPOSTRequest data;
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
            SendPOSTRequest.setNetworkResponseListener(this);
            data = new SendPOSTRequest();
            URL url = new URL("https://silive.in/bustracker/conductor");
//            SendPOSTRequest.setUrl(url,"1");
            data.execute();

            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            geocoder = new Geocoder(this, Locale.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (x1 == 0) {
            x1 = getLocation().getLatitude();
            y1 = getLocation().getLongitude();
            Toast.makeText(this, "true:" + "\n" + Math.abs(x2 - x1) + "\n" + Math.abs(y2 - y1), Toast.LENGTH_LONG).show();
        }
        x2 = getLocation().getLatitude();
        y2 = getLocation().getLongitude();
        if (Math.abs(x2 - x1) > 0.0001 || Math.abs(y2 - y1) > 0.0001) {
            Toast.makeText(this, "true:" + "\n" + Math.abs(x2 - x1) + "\n" + Math.abs(y2 - y1), Toast.LENGTH_LONG).show();
            x1 = x2;
            y1 = y2;
            data.execute();
//                currentLocation();
        } else {

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
            locationManager.removeUpdates(UpdateLocationService.this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "OnStart", Toast.LENGTH_SHORT).show();
        try {
            currentLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void currentLocation() throws IOException {

        Location loc;
        String currentLoc = "";
        if (isNetworkEnabled && isGPSEnabled) {
            loc = getLocation();
            if (loc != null) {
//                Toast.makeText(this, "Location : " + loc.getLatitude() + " , " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
                List<Address> list = null;
                list = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (list != null) {
                    address = list.get(0);
                    if (address != null) {
                        currentLoc = address.getAddressLine(0) + "\n" + address.getAdminArea() + "\n" + address.getCountryCode() + "\n" + address.getCountryName() + "\n" + address.getFeatureName() + "\n" + address.getLocality() + "\n" + address.getSubAdminArea() + "\n" + address.getSubLocality();
//                        Toast.makeText(this, currentLoc, Toast.LENGTH_SHORT).show();
                    } else
                        Log.d("error", "address is null");
                } else
                    Toast.makeText(this, "list == null", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "location == null 2", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public Location getLocation() {
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
    public void beforeRequest() {

    }

    @Override
    public void postRequest(String result) {
        Log.d("NFF", "response is " + result);
//        try {
//            JSONArray arr = new JSONArray(result);
//            for (int i = 0; i < arr.length(); i = i + 1) {
//                JSONObject obj = arr.getJSONObject(i);
//                NewsFeedItem item = new NewsFeedItem();
//                item.setTitle(obj.getString("Title"));
//                item.setFeed(obj.getString("Feed"));
//                item.setEventName(obj.getString("EventName"));
//                item.setTimestamp(obj.getString("TimeStamp"));
//                mNewsFeedItems.add(item);
//            }
//            mAdapter.notifyDataSetChanged();
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        } catch (NullPointerException ne) {
//            CustomDialog cusDialog = new CustomDialog();
//            ne.printStackTrace();
//        }
//
//        mProgressBar.setVisibility(View.GONE);
//        if (!result.equals("[]"))
//            dataLoading = false;
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

}*/
