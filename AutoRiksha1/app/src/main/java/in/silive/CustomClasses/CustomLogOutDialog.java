package in.silive.CustomClasses;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.silive.autorikshawautomation.R;
import in.silive.autorikshawautomation.SignupAndLogin;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Kartikay on 10-Apr-15.
 */
public class CustomLogOutDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    Button logout,cancel;
    public CustomLogOutDialog(Context context) {
        super(context);
  this.mContext=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogxml);
    }
    @Override
    public void onClick(View v) {
switch (v.getId())
{
    case R.id.logoutdialog_Logout:
        ProgressDialog pDialog = ProgressDialog.show(mContext, "LogOut", "Logging Out");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     pDialog.dismiss();
        dismiss();
break;
    case R.id.logoutdialog_Cancel:
        dismiss();
        break;
}
    }


}
