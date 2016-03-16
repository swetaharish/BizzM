package bizzmandi.com.bizzm.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bizzmandi.com.bizzm.applevel.BizzMandiApplication;
import bizzmandi.com.bizzm.utilities.Constants;

/**
 * Created by vij on 12/6/2015.
 */
public class NetworkRequestor {

    public static final String TAG="NetworkRequestor";
    BizzMandiApplication application;
    String url;
    NetworkCallback callback;
    int requestType;
    Map<String,String> data;
    public NetworkRequestor(String url, NetworkCallback callback,BizzMandiApplication application,int requestType,Map<String,String> data){
        this.url=url;
        this.callback=callback;
        this.application=application;
        this.requestType=requestType;
        this.data=data;
    }

    public void sendRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = application.getRequestQueue();

        String url = this.url;
        StringRequest request=null;

        switch (this.requestType) {
            case Constants.REQUEST_POST :
                request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if(jsonResponse.get("error") == false) {
                                callback.successResponse(jsonResponse);
                            }else{
                                callback.successResponse(jsonResponse);
                            }
                        } catch (JSONException exception) {
                            Log.d(TAG, "exception---" + exception.toString());
                        }
                        Log.d(TAG, "Response---" + response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "VolleryError--->" + error.toString());
                        callback.errorResponse(error);

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return data;
                    }
                };


            break;
        }

        // Add the request to the RequestQueue.
        queue.add(request);
    }



}
