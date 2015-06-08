package in.silive.autorikshawautomation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import in.silive.network.DownloadDATApost;
public class BookStatusFragment extends Fragment implements NetworkResponseListener,View.OnClickListener {
    private LatLng DriverLocation = new LatLng(28.67656, 77.50024);
    private GoogleMap map;
    private LatLng loc;
    private Document doc;
    ProgressDialog progressDialog;
    double lat, lng;
    JSONObject jsonObject;
    CustomDialogUserDetails cdud;
    TextView tv;
    View rootView;
    Button btn;
    SharedPreferences pref;
    String id;
    SupportMapFragment fragment;
    android.support.v4.app.FragmentTransaction fragTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.customerknowbookstatus, container, false);
        pref = getActivity().getSharedPreferences("MyPrefUSER",Context.MODE_PRIVATE);

        id = pref.getString("ID", "40");

        tv=(TextView)rootView.findViewById(R.id.customerbookcost);
        btn=(Button)rootView.findViewById(R.id.customerbookmoreDetails);
        btn.setOnClickListener(this);
        try {
            DownloadDATApost.setNRL(this);
            DownloadDATApost.setUrl(new URL(Config.CustomerKnowSTATUSAPI));
            SharedPreferences prefs = this.getActivity().getSharedPreferences("USERID", Context.MODE_PRIVATE);
            JSONObject jObject=new JSONObject();
 //           jObject.put("Id",prefs.getString("Id", null));
            jObject.put("Id",id);
            DownloadDATApost.setJSON(jObject);
            new DownloadDATApost().execute();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        android.support.v4.app.FragmentManager fManager = getChildFragmentManager();
        fragment = (SupportMapFragment) fManager.findFragmentById(R.id.maps);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fManager.beginTransaction().replace(R.id.maps, fragment).commit();
        }

    }

    /*
     * method to draw the map
     */
    @Override
    public void onClick(View view) {
        cdud.show();
    }
    private void initializeMap() {
        if (map == null)
            map = ((SupportMapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
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

    @Override
    public void onResume() {
        if (map == null) {
            map = fragment.getMap();
        }
       // initializeMap();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onPreExecute() {
        progressDialog = ProgressDialog.show(getActivity(), "Waiting", "Loating");
    }
    @Override
    public void onPostExecute(String data) throws JSONException {
        progressDialog.dismiss();
       // initializeMap();
        data=data.replaceAll("\\\\","");
        data=data.substring(1,data.length()-1);
        JSONArray jsonArray=new JSONArray(data);
            jsonObject=jsonArray.getJSONObject(0);
            String cost = jsonObject.getString("Cost");
        tv.setText(cost);
        cdud = new CustomDialogUserDetails(getActivity(),jsonObject);
        cdud.setTitle("Driver Details");
            double latitude = Double.parseDouble(jsonObject.getString("LocationLatitude"));
            loc = new LatLng(Double.parseDouble(jsonObject.getString("LocationLatitude")), Double.parseDouble(jsonObject.getString("LocationLongitude")));
        initializeMap();
    }
}