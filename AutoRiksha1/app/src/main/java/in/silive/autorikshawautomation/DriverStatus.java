package in.silive.autorikshawautomation;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import in.silive.Config;
import in.silive.listener.NetworkResponseListener;
import in.silive.network.DownloadDATApost;
/**
 * Created by kartikey on 4/4/15.
 */
public class DriverStatus extends Activity implements NetworkResponseListener,View.OnClickListener{
    ProgressDialog pDialog;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Geocoder gcd;
    Button btn;
    List<Address> addressesBoarding,addressesDestination ;
    TextView customerName, customerContact, customerDestination, customerBoarding, customerEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverstatus);
        gcd = new Geocoder(this, Locale.getDefault());
        btn=(Button)findViewById(R.id.diverstatusbutton);
        initialise();
        btn.setOnClickListener(this);
        try {
         //   SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
          //String id=pref.getString("DRIVER_ID","2");
            String id="2";
            DownloadDATApost.setNRL(this);
            DownloadDATApost.setUrl(new URL(Config.DriverCheckStatusAPI));
            JSONObject jObject = new JSONObject();
            jObject.put("Id",id);
            DownloadDATApost.setJSON(jObject);
        new DownloadDATApost().execute();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initialise() {
        customerName = (TextView) findViewById(R.id.driverstatusName);
        customerBoarding = (TextView) findViewById(R.id.driverstatusUserBoarding);
        customerContact = (TextView) findViewById(R.id.driverstatusContact);
        customerDestination = (TextView) findViewById(R.id.driverstatusUserdestination);
        customerEmail = (TextView) findViewById(R.id.driverstatusEmail);
    }
    @Override
    public void onPreExecute() {
        pDialog = ProgressDialog.show(this, "Status", "Loading");
    }
    @Override
    public void onBackPressed() {
    Intent i =new Intent(this,Driver_Class.class);
    startActivity(i);
    }
    @Override
    public void onPostExecute(String data) throws JSONException {
        pDialog.dismiss();
        String checkMatch="\"[]\"";
     /*   Toast.makeText(DriverStatus.this,"data recevied is"+data,Toast.LENGTH_SHORT).show();
     */   if (data.equals(checkMatch))
            showBox();
        else
        {
            data = data.replaceAll("\\\\", "");
        data = data.substring(1, data.length() - 1);

        jsonArray = new JSONArray(data);
        jsonObject = jsonArray.getJSONObject(0);
        try {
            customerEmail.setText(jsonObject.getString("EmailId"));
            customerName.setText(jsonObject.getString("FirstName") + " " + jsonObject.getString("LastName"));
            customerContact.setText(jsonObject.getString("MobileNo"));
            /*
            addressesBoarding= gcd.getFromLocation(Double.parseDouble(jsonObject.getString("BoardingLatitude")),Double.parseDouble(jsonObject.getString("BoardingLongitude")), 1);
            addressesDestination= gcd.getFromLocation(Double.parseDouble(jsonObject.getString("DestinationLatitude")),Double.parseDouble(jsonObject.getString("DestinationLongitude")), 1);
            */
            addressesBoarding = gcd.getFromLocation(28.67411, 77.44334, 1);
            addressesDestination = gcd.getFromLocation(28.676676, 77.500820, 1);
            if (addressesBoarding.size() > 0 && addressesDestination.size() > 0) {
                String customerboardString = addressesBoarding.get(0).getAddressLine(0) + " " + addressesBoarding.get(0).getAddressLine(1) + " " + addressesBoarding.get(0).getAddressLine(2);
                customerboardString = customerboardString.replaceAll("null", " ");
                String customerdestinString = addressesDestination.get(0).getAddressLine(0) + " " + addressesDestination.get(0).getAddressLine(1) + " " + addressesDestination.get(0).getAddressLine(2);
                customerdestinString = customerdestinString.replace("null", " ");
                customerBoarding.setText(customerboardString);
                customerDestination.setText(customerdestinString);
            } else {
                customerDestination.setText("Wait");
                customerBoarding.setText("Wait");
            }
        } catch (Exception e) {
        e.printStackTrace();}
    }
    }
    @Override
    public void onClick(View view) {
 Intent i=new Intent(DriverStatus.this,DriverStatusmoreInfo.class);
        startActivity(i);
    }
    public void showBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Sorry,\n You have no booking till now.")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent i=new Intent(DriverStatus.this,Driver_Class.class);
                                startActivity(i);
                                dialog.cancel();
                            }
                        })
                .setTitle("Error");
        AlertDialog alert = builder.create();
        alert.show();
    }
}