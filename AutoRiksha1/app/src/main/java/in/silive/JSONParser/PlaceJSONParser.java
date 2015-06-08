package in.silive.JSONParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class PlaceJSONParser {
    Context mContext;
    public PlaceJSONParser(Context mContext){
        this.mContext=mContext;
    }
    public List<HashMap<String, String>> parse(JSONObject jObject) {
        JSONArray jPlaces = null;
        try {
            jPlaces = jObject.getJSONArray("predictions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jPlaces);
    }
    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
        int placesCount = jPlaces.length();
        ArrayList<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> place = null;

        for (int i = 0; i < placesCount; i++) {
            try {
                /** Call getPlace with place JSON object to parse the place */
                place = getPlace((JSONObject) jPlaces.get(i));
                placesList.add(place);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }
    /** Parsing the Place JSON object */
    private HashMap<String, String> getPlace(JSONObject jPlace) {
        HashMap<String, String> place = new HashMap<String, String>();
        String id = "";
        String reference = "";
        String description = "";
        int length;
        try{
            description = jPlace.getString("description");
            id =  jPlace.getString("id");
            reference = jPlace.getString("reference");
Log.d("Description",description);
             //description=description.substring(13);
            place.put("description", description);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}