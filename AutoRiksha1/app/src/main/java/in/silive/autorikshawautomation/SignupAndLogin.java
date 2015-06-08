package in.silive.autorikshawautomation;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fourmob.panningview.library.PanningView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import in.silive.Config;
import in.silive.listener.NetworkResponseListener;
import in.silive.network.DownloadDATA;
import in.silive.network.DownloadDATApost;
public class SignupAndLogin extends Activity implements NetworkResponseListener{
    Button login_btn, signup_btn;
    Intent i;
    String type;
    EditText email, password;
    PanningView panningView;
    ProgressDialog progressDialog;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginandsplash);
        DownloadDATApost.setNRL(this);

        panningView= (PanningView) findViewById(R.id.panningView);
        panningView.startPanning();
        email = (EditText) findViewById(R.id.login_name);
        password = (EditText) findViewById(R.id.loginpassword);
        login_btn = (Button) findViewById(R.id.login);
        signup_btn = (Button) findViewById(R.id.sign_up);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String emailRegexString = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

                if ((password.getText().toString()).equals("") || ((email.getText().toString()).equals("")) || (!Pattern.matches(emailRegexString, email.getText().toString()))) {
                    if ((password.getText().toString()).equals(""))
                        password.setError("Please Enter a suitable password");
                    if ((email.getText().toString()).equals(""))
                        email.setError("Please Enter a suitable email");
                    if (!Pattern.matches(emailRegexString, email.getText().toString()))
                        email.setError("Email Format Exception");
                } else
                {
                    RadioGroup usertypeRadioGroup = (RadioGroup) findViewById(R.id.typeOfUser);
                    Log.d("data","hello");
                    final int user_type = usertypeRadioGroup.getCheckedRadioButtonId();

                    if (user_type == R.id.loginuser) {
                        try {
                type="user";
                            DownloadDATApost.setUrl(new URL(Config.isUserValidAPI));
                            Log.d("datauser","hello"+Config.isUserValidAPI+email.getText().toString());
                            try {
                                jsonObject=new JSONObject();
                                jsonObject.put("UserName", (email.getText()).toString());
                                jsonObject.put("Password", (password.getText()).toString());
                                DownloadDATApost.setJSON(jsonObject);
                            }
                            catch (Exception e){
                                Log.d("exceptio",e.getMessage());
                            }
                            new DownloadDATApost().execute();
                        } catch (MalformedURLException e) {

                            e.printStackTrace();
                        }
                    } else {
                        try {
                            type="driver";
                           JSONObject jObject=new JSONObject();
                            jObject.put("UserName", (email.getText()).toString());
                            jObject.put("Password", (password.getText()).toString());

                            DownloadDATApost.setJSON(jObject);
                            DownloadDATApost.setUrl(new URL(Config.isDriverValidAPI));
                            Log.d("datadriver","hello"+Config.isDriverValidAPI);
                            new DownloadDATApost().execute();

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                RadioGroup usertypeRadioGroup = (RadioGroup) findViewById(R.id.typeOfUser);
                Log.d("data","hello");
                final int user_type = usertypeRadioGroup.getCheckedRadioButtonId();
if(user_type==R.id.loginuser) {
    new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            i = new Intent(SignupAndLogin.this,
                    SignUp.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            finish();
        }
    }, 100);
}
            else
            Toast.makeText(SignupAndLogin.this,"Driver Cant be registered",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPreExecute() {
progressDialog=ProgressDialog.show(this,"Signing","Loading");
        Log.d("data","hello");
    }

    @Override
    public void onPostExecute(String data) throws JSONException {
        progressDialog.dismiss();
        Intent intent;
        if(data==null)
   showBox("Fuck");
      //  ProgressDialog("Invalid Credentials");
//        Log.d("data",data);
else{
        JSONObject jsonObject=new JSONObject(data);
        String id=jsonObject.getString("id");
Log.d("type",type);
         Toast.makeText(this,"Logged in Succesfully",Toast.LENGTH_SHORT).show();
            if (type.equals("user")) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefUSER", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("USER_ID",id);

                intent = new Intent(this, MainActivity.class);
            } else {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                intent = new Intent(this, Driver_Class.class);
editor.putString("DRIVER_ID",id);
                editor.commit();
            }
            startActivity(intent);
        }
    }

    public void showBox(final String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Your EmailId Or Password seems incorrect")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        })
               .setTitle("Error");
        AlertDialog alert = builder.create();
        alert.show();
    }
}