package in.silive.Service;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import in.silive.Config;
import in.silive.listener.NetworkResponseListener;
import in.silive.network.DownloadDATApost;

/**
 * Created by Shobhit Agarwal on 30-05-2015.
 */
public class LocationSendingService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,NetworkResponseListener {
    final static String UPDATE_MAP = "UPDATE_MAP";
    final static String latitude = "LATITUDE";
    final static String longitude = "LONGITUDE";
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(3000)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private GoogleApiClient mGoogleApiClient;
    private Location previousLocation = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
       setUpGoogleApiClientIfNeeded();
        mGoogleApiClient.connect();
    }

    private void setUpGoogleApiClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        Toast.makeText(this,"Service Destroyed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
/*Code
        to
        be implemeented her*/
        try {
            DownloadDATApost.setUrl(new URL(Config.DriverLocationUpdateAPI));
            DownloadDATApost.setNRL(this);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Id", "2");
            jsonObject.put("CurrentLocationLatitude", location.getLatitude());
            jsonObject.put("CurrentLocationLongitude", location.getLongitude());
            DownloadDATApost.setJSON(jsonObject);
            new DownloadDATApost().execute();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Location getPreviousLocation() {
        return previousLocation;
    }

    private void setPreviousLocation(Location location) {
        previousLocation = location;
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                REQUEST,
                this);
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(String data) throws JSONException {

    }
}