package in.silive.autorikshawautomation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import in.silive.CustomClasses.CustomDialogClass;
import in.silive.JSONParser.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import in.silive.listener.NetworkResponseListener;
import in.silive.model.GoogleMapV2Direction;
import in.silive.network.DownloadDATApost;
import in.silive.network.DownloadURL;
import in.silive.network.GPSTracker;
import in.silive.network.KeyValues;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
public class MapFragment extends Fragment implements OnClickListener {
    private GoogleMap map;
    private LatLng loc;
    private GPSTracker gps;
    double lat, lng;
    Button rideNow, rideLater, searchdestination, searchboarding;
    FetchJson fetch;
    private GoogleMapV2Direction md;
    private Document doc;
    String editedText = "";
    View rootView;
    AutoCompleteTextView boarding, destination;
    double x1 = 0, y1 = 0;
    LatLng llboard, lldestination;
    int check = 0;
    String urlforjson = "https://maps.googleapis.com/maps/api/place/search/json?location=-33.8670522,151.1957362&radius=500&types=food&sensor=true&key=AIzaSyBxe8bMBX93Rnwdu3ICB97yufeAVBmD1pQ", data = "";
    SupportMapFragment fragment;
    android.support.v4.app.FragmentTransaction fragTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Reached On CReate Map View", "Map Fragment");
        rootView = inflater.inflate(R.layout.activity_map, container, false);
        getActivity().setTitle("Home");
        boarding = (AutoCompleteTextView) rootView.findViewById(R.id.boardingedittextinmap);
        boarding.setThreshold(1);
        destination = (AutoCompleteTextView) rootView.findViewById(R.id.destinationedittextinmap);
        rideLater = (Button) rootView.findViewById(R.id.mapfragment_rideLater);
        rideLater.setOnClickListener(this);
        searchboarding = (Button) rootView.findViewById(R.id.searcbhoardinglocation);
        searchdestination = (Button) rootView.findViewById(R.id.searchdestinationlocation);
        try {
            searchboarding.setOnClickListener(this);
            searchdestination.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();

        }
        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((((destination.getText().toString()).length()) >= 2)) {
                    //  Toast.makeText(getActivity(), "Startes Searching", Toast.LENGTH_SHORT).show();
                    if (fetch != null) {
                        fetch.cancel(true);
                        fetch = null;
                    }//	new FetchJson().execute(tv.getText().toString());
                    editedText = destination.getText().toString();
                    fetch = new FetchJson();
                    fetch.execute(editedText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        boarding.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (((boarding.getText().toString()).length()) > 1) {

                    if (fetch != null)
                        fetch.cancel(true);
                    editedText = boarding.getText().toString();
                    new FetchJson().execute(editedText);
                    //   Toast.makeText(getActivity(), "Setting boarding", Toast.LENGTH_SHORT).show();
                    fetch = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return rootView;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        //caseR.id.mapfragment_rideLater):
        // LatLng llboard=null,lldestination=null;
        switch (v.getId()) {
            case R.id.mapfragment_rideLater:
                if (llboard == null || lldestination == null) {
                    if (llboard == null && lldestination == null) {
                        Toast.makeText(getActivity(), "Boarding and Destination Empty", Toast.LENGTH_SHORT).show();
                        boarding.setError("Boarding Location id empty");
                        destination.setError("Boarding Location Empty");
                    } else if (llboard == null) {
                        boarding.setError("Boarding Location id empty");
                        Toast.makeText(getActivity(), "Boarding Empty", Toast.LENGTH_SHORT).show();

                    }else {
                        destination.setError("Boarding Location Empty");
                        Toast.makeText(getActivity(), "Destination Empty", Toast.LENGTH_SHORT).show();
                    }
                }
else {
                    CustomDialogClass cdc = new CustomDialogClass(getActivity(), boarding.getText().toString(), destination.getText().toString(),llboard,lldestination);
                    //cdc.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    cdc.setTitle("Book Vehicle");
                    cdc.show();
                }           break;
            case R.id.searcbhoardinglocation:
                if (!((boarding.getText().toString()).equals(""))) {
                    try {
                        String boardingString = modifyString(boarding.getText().toString());
                        boarding.setText(boardingString);
                        llboard = new GetLatLng(boardingString, getActivity()).getLatLng();

                        if (llboard != null) {
                            map.addMarker(new MarkerOptions().position(llboard).title("Boarding Location"));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(llboard, 15));
                            if (llboard != null && lldestination != null)
                                drawRoute(llboard, lldestination);
                        } else
                            Toast.makeText(getActivity(), "Location Not Present\nPlease Recheck", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        //   Toast.makeText(getActivity(), "No results from geocoding ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    boarding.setError("Boarding Location Empty");
                }
                break;
            case R.id.searchdestinationlocation:
                if (!((destination.getText().toString()).equals(""))) {
                    try {
                        String destinationString = modifyString(destination.getText().toString());
                        destination.setText(destinationString);
                        lldestination = new GetLatLng(destinationString, getActivity()).getLatLng();
                        if (lldestination != null) {
                            map.addMarker(new MarkerOptions().position(lldestination).title("Destination"));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lldestination, 15));
                            if ((llboard != null) && (lldestination != null))
                                drawRoute(llboard, lldestination);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        //  Toast.makeText(getActivity(), "No results from geocoding ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    destination.setError("Boarding Location Empty");
                }
                break;
        }
    }

    public String modifyString(String mString) {
        // Toast.makeText(getActivity(),"Old String is"+mString.substring(13),Toast.LENGTH_SHORT).show();
        if (mString.charAt(0) == '{') {
            int len = mString.length();
            mString = mString.substring(13, len - 1);
            return mString;
        } else
            return mString;
    }

    public void drawRoute(LatLng llb, LatLng lld) {
        new DrawPath().execute();
    }

    private class DrawPath extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "", "Generating route");
        }

        @Override
        protected String doInBackground(String... params) {
            md = new GoogleMapV2Direction();
            doc = md.getDocument(llboard, lldestination, GoogleMapV2Direction.MODE_DRIVING);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        android.support.v4.app.FragmentManager fManager = getChildFragmentManager();
        fragment = (SupportMapFragment) fManager.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fManager.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }

    private void initializeMap() {
        if (map == null)
            map = ((SupportMapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
        gps = new GPSTracker(getActivity());
        lat = gps.getLatitude();
        lng = gps.getLongitude();
        loc = new LatLng(lat, lng);

        map.clear();
        map.addMarker(new MarkerOptions().position(loc).title("Present Location"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        map.setMyLocationEnabled(true);
        if (map!= null) {
            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location arg0) {
                    Geocoder gCoder = new Geocoder(getActivity());
                    List<android.location.Address> addresses;
                    try {
                        addresses = gCoder.getFromLocation(arg0.getLatitude(), arg0.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {
                            addresses.get(0).getAdminArea();
                            addresses.get(0).getLocality();
                            String CountryName = addresses.get(0).getCountryName();
                            addresses.get(0).getAdminArea();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);

            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        Log.d("Getting data", data);
        return data;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        if (map == null) {
            map = fragment.getMap();
            map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        }
        initializeMap();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    class FetchJson extends AsyncTask<String, String, String> {
        String finalUrl;

        @Override
        protected String doInBackground(String... place) {
            String data = "";
            String key = "AIzaSyCmttbYopFUOh-Y2FUtXCEmYGb3ifLu4yE";
            String input = "";
            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            String types = "types=geocode";
            String sensor = "sensor=false";
            String parameters = input + "&" /*+ types + "&"*/ + sensor + "&key="
                    + key;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
                    + output + "?" + parameters;
            try {
                url = url.replace(" ", "%20");
                data = downloadUrl(url);
                Log.d("URL finally is ", "UR: finally is " + url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //finalUrl=finalUrl.replace(" ", "%20");
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }

        private class ParserTask extends
                AsyncTask<String, Integer, List<HashMap<String, String>>> {
            JSONObject jObject;

            @Override
            protected List<HashMap<String, String>> doInBackground(
                    String... jsonData) {
                List<HashMap<String, String>> places = null;
                PlaceJSONParser placeJsonParser = new PlaceJSONParser(getActivity());
                try {
                    jObject = new JSONObject(jsonData[0]);
                    places = placeJsonParser.parse(jObject);
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }
                return places;
            }

            @Override
            protected void onPostExecute(List<HashMap<String, String>> result) {
                String[] from = new String[]{"description"};
                int[] to = new int[]{android.R.id.text1};
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), result,
                        android.R.layout.simple_list_item_1, from, to);
                if (editedText.equals(boarding.getText().toString())) {
                    boarding.setAdapter(adapter);
                } else {
                    destination.setAdapter(adapter);
                }
            }
        }
    }
}