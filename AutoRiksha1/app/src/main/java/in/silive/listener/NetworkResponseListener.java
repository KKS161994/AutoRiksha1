package in.silive.listener;

import org.json.JSONException;

/**
 * Created by kartikey on 4/5/15.
 */
public interface NetworkResponseListener {
public void onPreExecute();
public void onPostExecute(String data) throws JSONException;
}
