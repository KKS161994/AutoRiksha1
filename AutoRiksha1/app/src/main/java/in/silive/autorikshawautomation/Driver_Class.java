package in.silive.autorikshawautomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import in.silive.Config;
import in.silive.Service.LocationSendingService;
import in.silive.listener.NetworkResponseListener;
import in.silive.network.DownloadDATApost;
/**
 * Created by kartikey on 4/3/15.
 */
public class Driver_Class extends Activity implements NetworkResponseListener,View.OnClickListener {
    Button available, notavailable, booked, checkstatus;
    String text;
    Intent intent;
    SharedPreferences pref;
    String id;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.driver);
         pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
         id=pref.getString("DRIVER_ID","2");

        //Initialise Buttons variable
        intent=new Intent(new Intent(getBaseContext(),LocationSendingService.class));
        available = (Button) findViewById(R.id.driverAvailable);
        notavailable = (Button) findViewById(R.id.driverNotAvailable);
        booked = (Button) findViewById(R.id.driverBooked);
        DownloadDATApost.setNRL(this);
        available.setOnClickListener(this);
        notavailable.setOnClickListener(this);
        booked.setOnClickListener(this);
        checkstatus = (Button) findViewById(R.id.driverCheckStatus);
        checkstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        stopService(intent);
                        Intent i = new Intent(Driver_Class.this,
                                DriverStatus.class);
                        startActivity(i);
                        finish();
            }
        });
       //intent = new Intent(Driver_Class.this,LocationUpdateService.class);
        //startActivity(intent);
  //startService(new Intent(Driver_Class.this,LocationUpdateService.class));
        Toast.makeText(this,"Service started",Toast.LENGTH_SHORT);
   //To be started
      //startService(new Intent(this,LocationUpdateService.class));
        super.onCreate(savedInstanceState);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
*/
    @Override
    public void onBackPressed() {
    showLogoutBox();
    }
    public void showLogoutBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Are You sure You Want to logout")
                .setCancelable(false)
                .setPositiveButton("Logout",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                ProgressDialog pDialog = ProgressDialog.show(Driver_Class.this, "LogOut", "Logging Out");

                                stopService(intent);
                                Intent i = new Intent(Driver_Class.this, SignupAndLogin.class);
                                startActivity(i);

                                pDialog.dismiss();
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setTitle("Error");
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        JSONObject jsonObject;
        //Setting text variable to a keyword so that it can be appended with url to change driver status on api
        switch (v.getId()) {
            case R.id.driverNotAvailable:
                text = "notavailable";
                Toast.makeText(this,"Service stopping",Toast.LENGTH_SHORT);

                try {
                    DownloadDATApost.setUrl(new URL(Config.driverchangeavailabilityAPI));
                    jsonObject=new JSONObject();
                    jsonObject.put("Status","false");
                    jsonObject.put("Id",id);
                DownloadDATApost.setJSON(jsonObject);
                    new DownloadDATApost().execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
            case R.id.driverAvailable:
                text = "available";
                try {
                    DownloadDATApost.setUrl(new URL(Config.driverchangeavailabilityAPI));
                    jsonObject=new JSONObject();
                    jsonObject.put("Status","true");
                    jsonObject.put("Id",id);
                    DownloadDATApost.setJSON(jsonObject);
                    new DownloadDATApost().execute();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.driverBooked:
                text = "notbooked";
                try {
                    DownloadDATApost.setUrl(new URL(Config.driverchangebookstatusAPI));
                    jsonObject=new JSONObject();
                    jsonObject.put("Status","false");
                    jsonObject.put("Id",id);
                    DownloadDATApost.setJSON(jsonObject);
                    new DownloadDATApost().execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onPreExecute() {
progressDialog=ProgressDialog.show(this,"Sending","Loading");
    }
    @Override
    public void onPostExecute(String data) throws JSONException {
        progressDialog.dismiss();
        if(DownloadDATApost.getResultCode()==404||DownloadDATApost.getResultCode()==200){
    Toast.makeText(this,"Sent your "+text+"status",Toast.LENGTH_SHORT).show();

    if(text.equals("available"))
    {
        startService(intent);
        Toast.makeText(this,"Service Started",Toast.LENGTH_SHORT).show();
    }
    else if(text.equals("notavailable"))
    {
        stopService(intent);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT);
    }
    else
        startService(intent);
}
}
}