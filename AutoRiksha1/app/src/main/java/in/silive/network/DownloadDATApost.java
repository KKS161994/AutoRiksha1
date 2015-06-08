package in.silive.network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.silive.listener.NetworkResponseListener;

/**
 * Created by kartikey on 4/4/15.
 */
public class DownloadDATApost extends AsyncTask<String, String, String> {
    static URL url = null;
    private static URL httpurl;
    private static int result_code;
    StringBuilder sb = new StringBuilder();
static String jsonString=null;
    InputStream is;
    static JSONObject jSonObject;
    static NetworkResponseListener nrl;
    public static void setJSON(JSONObject json) {
        jSonObject = json;
    }

    public static void setNRL(NetworkResponseListener newnrl) {
        nrl = newnrl;
    }

    public static void setUrl(URL url) {
        httpurl = url;
    }

    @Override
    protected void onPreExecute() {
        nrl.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String line = "";
        try {
            HttpURLConnection httpURL = (HttpURLConnection) httpurl.openConnection();
            httpURL.setDoOutput(true);
            httpURL.setRequestMethod("POST");
            httpURL.setUseCaches(false);
            httpURL.setConnectTimeout(10000);
            httpURL.setReadTimeout(10000);
            httpURL.setRequestProperty("Content-Type", "application/json");
            httpURL.connect();
            DataOutputStream out = new DataOutputStream(httpURL.getOutputStream());
            out.writeBytes(jSonObject.toString());
            out.close();
            out.flush();
            result_code = httpURL.getResponseCode();
 Log.d("Status", ""+result_code);
            if (result_code == HttpURLConnection.HTTP_OK||result_code==302||result_code==200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURL.getInputStream()));
                while ((line = br.readLine()) != null)
                    sb.append(line);
                Log.d("Status String", ""+sb);

                return sb.toString();
}
            else if(result_code==404)
                return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            nrl.onPostExecute(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }                   //  super.onPostExecute(s);
    }
    public static int getResultCode(){
        return result_code;
    }
}