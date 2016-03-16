package bizzmandi.com.bizzm.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bizzmandi.com.bizzm.R;
import bizzmandi.com.bizzm.applevel.BizzMandiApplication;
import bizzmandi.com.bizzm.network.NetworkCallback;
import bizzmandi.com.bizzm.network.NetworkRequestor;
import bizzmandi.com.bizzm.utilities.Constants;

public class SplashActivity extends Activity implements NetworkCallback {

    public static final String TAG="SplashActivity";
    Map<String,String> data=new HashMap<String,String>();
    ProgressDialog pd;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pd=new ProgressDialog(this);
        pd.setMessage(Constants.IS_DEVICE_REGISTERED);
        pd.setCanceledOnTouchOutside(false);

        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        data.put("imei", tMgr.getDeviceId());

        NetworkRequestor networkRequest=new NetworkRequestor(Constants.REGISTER_DEVICE,this,(BizzMandiApplication)getApplication(),Constants.REQUEST_POST,data);
        networkRequest.sendRequest();
        pd.show();

    }

    @Override
    public void successResponse(JSONObject response) {
        intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
        pd.dismiss();
        finish();
    }

    @Override
    public void errorResponse(VolleyError error) {

    }

    @Override
    public void requestInvalid(JSONObject data){
        intent=new Intent(this,DeviceRegisterActivity.class);
        startActivity(intent);
        finish();
    }

}
