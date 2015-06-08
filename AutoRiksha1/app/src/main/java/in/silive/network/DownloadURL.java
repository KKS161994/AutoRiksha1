package in.silive.network;
/**
 * Created by kartikey on 4/7/15.
 */
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
public class DownloadURL extends AsyncTask<String,String,String> {
    String url=KeyValues.apiurl;
    StringBuilder sb=new StringBuilder();
    InputStream is;
public static NetworkResponseListener nrl=null;
    public void setDownloadURL(String urldata){
        this.url=this.url+urldata;
    }
    public static void setNetworkResponseListener(NetworkResponseListener mListener){
        nrl=mListener;
    }
    @Override
    protected String doInBackground(String... params) {
        URL httpurl=null;
        String line="";
        try {
            httpurl=new URL(url);
            HttpURLConnection httpURL=(HttpURLConnection) httpurl.openConnection();
            is=httpURL.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            while((line=br.readLine())!=null)
                sb.append(line);
        }  catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            nrl.onPostExecute(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}