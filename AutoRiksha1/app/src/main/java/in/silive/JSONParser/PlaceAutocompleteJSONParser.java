package in.silive.JSONParser;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Kartikay on 01-May-15.
 */
public class PlaceAutocompleteJSONParser {
    String status;
    List<HashMap<String,String>> placesList;
    List<HashMap<String,String>> previousplacesList;
    String json;
    JSONObject jObject;
    private Context mContext;
    public PlaceAutocompleteJSONParser(Context context){
        mContext=context;
    }
    public void setJSON(String json){
    this.json=json;
}
    public void setPreviousplacesList(){placesList=null;}
    public List<HashMap<String,String>> getNewList() throws JSONException {
       placesList=new ArrayList<HashMap<String, String>>();
        JSONObject jsonObject=new JSONObject(json);
        status=jsonObject.getString("status");
        if(status.equals("OK"))
            parse(jsonObject);
        else return null;
return placesList;
    }
public List<HashMap<String,String>> parse(JSONObject jsonObject) throws JSONException {
JSONArray jsonArray=jsonObject.getJSONArray("predictions");
 int length=jsonArray.length();
        String description="";
        int i=0;
        for(i=0;i<length;i++){
            HashMap<String,String> places=new HashMap<String, String>();
            jObject=jsonArray.getJSONObject(i);
            description=jObject.getString("description");
            places.put("description",description);
            placesList.add(places);
        }
    return placesList;
    }
    public List<HashMap<String,String>> getListFromSavedList(String s,JSONObject jObject) throws JSONException {
        if (placesList == null) {
            placesList = parse(jObject);
        } else
        {       int length = placesList.size();
        int stringLength = s.length();
        int i = 0;
        HashMap<String, String> places = new HashMap<String, String>();

        for (i = 0; i < length; i++) {
            places = placesList.get(i);
            if (s.equals((places.get("description")).substring(0, stringLength)))
                previousplacesList.add(places);
            else
                continue;
        }
        placesList.clear();
        placesList = previousplacesList;
        previousplacesList.clear();
    }
        return placesList;
    }
   }