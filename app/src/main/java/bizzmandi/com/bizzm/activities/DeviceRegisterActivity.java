package bizzmandi.com.bizzm.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bizzmandi.com.bizzm.R;
import bizzmandi.com.bizzm.applevel.BizzMandiApplication;
import bizzmandi.com.bizzm.network.NetworkCallback;
import bizzmandi.com.bizzm.network.NetworkRequestor;
import bizzmandi.com.bizzm.utilities.Constants;

public class DeviceRegisterActivity extends Activity implements NetworkCallback {

    public static final String TAG="DeviceRegisterActivity";
    String IMEI,phoneNumber;
    Map<String,String> data=new HashMap<>();
    EditText phoneTxt;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_device);
        phoneTxt=(EditText)findViewById(R.id.phoneNumber);

        pd=new ProgressDialog(this);
        pd.setMessage(Constants.REGISTERING_DEVICE);
        pd.setCanceledOnTouchOutside(false);

        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI=tMgr.getDeviceId();
        Log.d(TAG,"--IMEI--" + IMEI);


    }

    public void onClick(View view){

        phoneNumber=phoneTxt.getText().toString();

        if(IMEI ==null || phoneNumber ==null){
            Toast.makeText(this,"Please enter the phone number ",Toast.LENGTH_LONG).show();

        }else{

            data.put("username",phoneNumber);
            data.put("imei",IMEI);
            NetworkRequestor networkRequest=new NetworkRequestor(Constants.REGISTER_DEVICE,this,(BizzMandiApplication)getApplication(),Constants.REQUEST_POST,data);
            networkRequest.sendRequest();
            pd.show();
        }

    }
    @Override
    public void successResponse(JSONObject response) {
        pd.cancel();
        phoneTxt.setText("");
        Toast.makeText(this,Constants.REGISTER_SUCCESS,Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void errorResponse(VolleyError error) {
        pd.cancel();
        Log.d(TAG, TAG + "===CALLBACK ERROR===");
    }

    @Override
    public void requestInvalid(JSONObject data){
        pd.cancel();
        try{
            Toast.makeText(this,(String)data.get("error_msg"),Toast.LENGTH_LONG).show();
        }
        catch (JSONException e){
            Toast.makeText(this,"Invalid request ",Toast.LENGTH_LONG).show();
        }

    }



}
