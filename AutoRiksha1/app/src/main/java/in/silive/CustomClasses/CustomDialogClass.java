package in.silive.CustomClasses;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.silive.Config;
import in.silive.autorikshawautomation.R;
import in.silive.listener.NetworkResponseListener;
import in.silive.network.DownloadDATApost;
public class CustomDialogClass extends Dialog implements View.OnClickListener,NetworkResponseListener{
    /**
     * Created by Kartikay on 10-Apr-15.
     **/
    SharedPreferences prefs;
    Context mContext;
    EditText board, dest;
    String boarding, destination;
    Button book;
    ProgressDialog progressDialog;
    JSONObject jsonObject = new JSONObject();
    LatLng boardLatLng, destLatLng;
    EditText editBoard, editDest,noOfVehicle;
    private ProgressDialog pDialog;
    Context mainContext;
    SharedPreferences pref;
    String id;
    public CustomDialogClass(Context ctxt, String board, String dest, LatLng bBoard, LatLng dDestin) {
        super(ctxt);
        this.mainContext = ctxt;
        this.boarding = board;
        this.destination = dest;
        this.mContext = ctxt;
        this.boardLatLng = bBoard;
        this.destLatLng = dDestin;
//        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogxml);
        book = (Button) findViewById(R.id.CustomDialogBookVehicle);
        board = (EditText) findViewById(R.id.boarding);
        dest = (EditText) findViewById(R.id.destination);
        noOfVehicle=(EditText)findViewById(R.id.no_of_taxis);
        book.setOnClickListener(this);
        board.setText(boarding);
        dest.setText(destination);
        DownloadDATApost.setNRL(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CustomDialogBookVehicle:
                try {
                    pref = mContext.getSharedPreferences("MyPrefUSER",Context.MODE_PRIVATE);
                    id = pref.getString("USER_ID", "40");
                    jsonObject.put("CustomerId",id );
                    jsonObject.put("NoOfVehicles",1 );
                    jsonObject.put("BoardingLat", boardLatLng.latitude);
                    jsonObject.put("BoardingLong", boardLatLng.longitude);
                    jsonObject.put("DestinationLat", destLatLng.latitude);
                    jsonObject.put("DestinationLong", destLatLng.longitude);
                    DownloadDATApost.setJSON(jsonObject);
                    DownloadDATApost.setUrl(new URL(Config.BookVEHICLEAPI));
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
        progressDialog = ProgressDialog.show(mainContext, "Loading", "Registration");
    }
    @Override
    public void onPostExecute(String data) throws JSONException {
progressDialog.dismiss();
if(DownloadDATApost.getResultCode()==HttpURLConnection.HTTP_OK)
        createDialog("Booked Successfuly");
            //dismiss();
    }
public void createDialog(String message){
    new AlertDialog.Builder(mainContext)
            .setTitle("Booked")
            .setMessage(message)
            .setPositiveButton(android.R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                dismiss();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
}
}