package in.silive.CustomClasses;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import in.silive.autorikshawautomation.R;
/**
 * Created by Kartikay on 24-Apr-15.
 */
public class CustomDialogUserDetails extends Dialog implements View.OnClickListener {
    JSONObject jsonObject;
    Context ctxt;
    Button btn;
    TextView name, contact, age;
    JSONArray jsonArray;

    public CustomDialogUserDetails(Context ctxt, JSONObject jO) {
        super(ctxt);
        this.ctxt = ctxt;
        jsonObject = jO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customdialogdriverdetails);
        btn = (Button) findViewById(R.id.customdialogButton);
        name = (TextView) findViewById(R.id.driverdetailsName);
        contact = (TextView) findViewById(R.id.driverdetailsContact);
        age = (TextView) findViewById(R.id.driverdetailsAge);
        try {
            String firstName=jsonObject.getString("FirstName");
            String lastName=jsonObject.getString("LastName");
            name.setText(jsonObject.getString("FirstName") + " " + jsonObject.getString("LastName"));
            contact.setText(jsonObject.getString("ContactNo"));
            age.setText(jsonObject.getString("Age"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}