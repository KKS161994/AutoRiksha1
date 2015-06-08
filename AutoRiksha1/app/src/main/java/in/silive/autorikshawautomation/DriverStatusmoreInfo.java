package in.silive.autorikshawautomation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import in.silive.Config;
import in.silive.CustomClasses.CustomDialogUserDetails;
import in.silive.listener.NetworkResponseListener;
import in.silive.model.GoogleMapV2Direction;
import in.silive.network.DownloadDATApost;
public class DriverStatusmoreInfo extends FragmentActivity {
    private LatLng DriverLocation = new LatLng(28.67656, 77.50024);
    private GoogleMap map;
    private LatLng loc;
    ProgressDialog progressDialog;
    double lat, lng;
    View rootView;
    private GoogleMapV2Direction md;
    private Document doc;
    Button btn;
    android.app.Fragment fragment;
    android.support.v4.app.FragmentTransaction fragTransaction;
    /*
     * method to draw the map
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
setContentView(R.layout.driverstatusmoreinfo);
        initializeMap();
        super.onCreate(savedInstanceState);
    }
    private void initializeMap() {
        if (map == null)
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                    R.id.driverstatusmaps)).getMap();
        loc = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(DriverLocation).title("Driver's present Location"));
        map.addMarker(new MarkerOptions().position(loc).title("Starting Point"));
        // Move the camera instantly to akgec with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(DriverLocation, 15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        map.setMyLocationEnabled(true);
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    public void drawRoute(LatLng llb, LatLng lld) {
        new DrawPath().execute();
    }
    private class DrawPath extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(DriverStatusmoreInfo.this, "Welcome", "Generating route");
        }
        @Override
        protected String doInBackground(String... params) {
            md = new GoogleMapV2Direction();
            doc = md.getDocument(null, null, GoogleMapV2Direction.MODE_DRIVING);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            ArrayList<LatLng> points = md.getDirection(doc);
            PolylineOptions pLine = new PolylineOptions().width(3).color(Color.BLUE);
            for (int i = 0; i < points.size(); i++) {
                pLine.add(points.get(i));
            }
            //   Toast.makeText(getActivity(), "Executed", Toast.LENGTH_SHORT).show();
            map.addPolyline(pLine);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }
}