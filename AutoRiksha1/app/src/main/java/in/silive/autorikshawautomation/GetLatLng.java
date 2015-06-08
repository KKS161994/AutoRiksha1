package in.silive.autorikshawautomation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by kartikey on 4/6/15.
 */
public class GetLatLng {
    Context context;
    String adress;

    public GetLatLng(String adress, Context context) {
        this.adress = adress;
        this.context = context;
    }

    public LatLng getLatLng() throws IOException {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        address = coder.getFromLocationName(adress, 5);
        if (address == null)
            return null;
        else {
            if(address.size()>0) {
                Address location = address.get(0);
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                return ll;
            }

        }
    return null;
    }
}
