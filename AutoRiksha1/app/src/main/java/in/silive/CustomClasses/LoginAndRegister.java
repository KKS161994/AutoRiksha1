package in.silive.CustomClasses;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import in.silive.autorikshawautomation.MainActivity;
import in.silive.autorikshawautomation.R;
import in.silive.autorikshawautomation.SignUp;
/**
 * Created by Kartikay on 16-Apr-15.
 */
public class LoginAndRegister extends Dialog{
    String todo,message,fromActivity;
    Context mContext,towhere;
    TextView tv;
    Button btn;
    public LoginAndRegister(Context context,String toDo,String fromactivity,String msg) {
        super(context);
        todo=toDo;
        mContext=context;
        fromActivity=fromactivity;
        message=msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 setContentView(R.layout.loginandregistration);
    tv= (TextView) findViewById(R.id.loginandregistrationtext);
        btn= (Button) findViewById(R.id.loginandregistrationbutton);
    tv.setText(message);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(todo.equals("dismiss"))
                    dismiss();
                else if (todo.equals("intent")) {
                    {

                Intent i=new Intent(mContext, MainActivity.class);
                    mContext.startActivity(i);
                    }
                }
            }
        });
    }
}