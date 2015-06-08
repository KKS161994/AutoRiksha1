package in.silive.autorikshawautomation;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.silive.listener.NetworkResponseListener;
import in.silive.network.DownloadDATA;
/**
 * Created by Kartikay on 16-Apr-15.
 */
public class LetsCheck extends Activity implements NetworkResponseListener, View.OnClickListener {
    ProgressDialog pDialog;
    Button btn;
    TextView tv;
    String url = "https://maps.googleapis.com/maps/api/place/search/json?location=-33.8670522,151.1957362&radius=500&types=food&sensor=true&key=AIzaSyCmttbYopFUOh-Y2FUtXCEmYGb3ifLu4yE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.letscheck);
        btn = (Button) findViewById(R.id.dd);
        tv = (TextView) findViewById(R.id.checkJSON);
        btn.setOnClickListener(this);
    }
    @Override
    public void onPreExecute() {
//        url = url.replace(" ", "%20");
        pDialog = ProgressDialog.show(LetsCheck.this, "Loading", "Loading Data");

    }
    @Override
    public void onPostExecute(String data) {
        try {
            pDialog.dismiss();
            tv.setText(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dd:
                DownloadDATA.setNRL(this);
                new DownloadDATA().execute(url);
                break;
        }
    }
}