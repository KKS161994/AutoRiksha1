package in.silive.autorikshawautomation;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import in.silive.Config;
import in.silive.CustomClasses.LoginAndRegister;
import in.silive.listener.NetworkResponseListener;
import in.silive.network.DownloadDATA;
import in.silive.network.DownloadDATApost;

public class SignUp extends ActionBarActivity implements NetworkResponseListener {
    Button onSignUpClick;
    ProgressDialog pDialog;
    LoginAndRegister cdc;
    JSONObject jsonObject = new JSONObject();
    EditText mobileNo, emailId, firstName, lastName, userName, password, emergencyNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getSupportActionBar();
        setContentView(R.layout.sign_up);
        DownloadDATApost.setNRL(this);
        onSignUpClick = (Button) findViewById(R.id.registersignup);
        onSignUpClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    initialise();
                    Log.d("MobileNo is ", "Mob no" + mobileNo.getText().toString());
                    jsonObject.put("MobileNo", (mobileNo.getText()).toString());
                    jsonObject.put("EmailId", (emailId.getText()).toString());
                    jsonObject.put("FirstName", (firstName.getText()).toString());
                    jsonObject.put("LastName", (lastName.getText()).toString());
                    jsonObject.put("UserName", (userName.getText()).toString());
                    jsonObject.put("Password", (password.getText()).toString());
                    jsonObject.put("EmergencyNo", (emergencyNo.getText()).toString());
                    DownloadDATApost.setJSON(jsonObject);
                    DownloadDATApost.setUrl(new URL(Config.SignUPAPI));
                    new DownloadDATApost().execute();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initialise() {
        mobileNo = (EditText) findViewById(R.id.mobno_signup);
        emailId = (EditText) findViewById(R.id.email_signup);
        firstName = (EditText) findViewById(R.id.firstname_signup);
        lastName = (EditText) findViewById(R.id.lastname_signup);
        password = (EditText) findViewById(R.id.setpassword_signup);
        userName = (EditText) findViewById(R.id.username_signup);
        emergencyNo = (EditText) findViewById(R.id.emerno_signup);
    }

    public void onBackPressed() {
        Intent i = new Intent(SignUp.this, SignupAndLogin.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPreExecute() {
        pDialog = ProgressDialog.show(SignUp.this, "Loading", "SRegistration");
    }

    @Override
    public void onPostExecute(String data) throws JSONException {
        pDialog.dismiss();
        if(data==null){
            createDialog("Error in Registration Try Again");
        }
        else{
            createDialog("Your Registration Id is"+data);
            SharedPreferences.Editor editor=getSharedPreferences("USERID",MODE_PRIVATE).edit();
            editor.putString("ID",data);
editor.commit();
        }
    }
    public void createDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Registration")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        startActivity(new Intent(SignUp.this, MainActivity.class));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}