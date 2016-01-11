package bizzmandi.com.bizzm.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import bizzmandi.com.bizzm.R;
import bizzmandi.com.bizzm.network.NetworkCallback;
import bizzmandi.com.bizzm.network.NetworkRequestor;
import bizzmandi.com.bizzm.utilities.Constants;

public class SplashActivity extends Activity implements NetworkCallback {

    TimerTask task;

    String deviceID;
    public static final String TAG="SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        task=new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,DeviceRegisterActivity.class);
                startActivity(intent);
            }
        };

        Timer timer=new Timer();
        timer.schedule(task, Constants.SPLASH_TIME);

    }

    @Override
    public void successResponse(JSONObject response) {

    }

    @Override
    public void errorResponse(VolleyError error) {

    }

    @Override
    public void requestInvalid(JSONObject data){


    }

}
