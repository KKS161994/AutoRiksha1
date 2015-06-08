package in.silive.network;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
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
public class DownloadDATA extends AsyncTask<String, String, String> {
    static URL url = null;
    private static URL httpurl;
    StringBuilder sb = new StringBuilder();
    InputStream is;
    static NetworkResponseListener nrl;
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
            httpURL.connect();
            is = httpURL.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null)
                sb.append(line);
            return sb.toString();
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
}