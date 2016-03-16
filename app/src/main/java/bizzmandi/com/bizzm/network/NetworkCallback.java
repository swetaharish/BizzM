package bizzmandi.com.bizzm.network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by vij on 12/6/2015.
 */
public interface NetworkCallback {

    public void successResponse(JSONObject data);
    public void requestInvalid(JSONObject data);

    public void errorResponse(VolleyError error);

}
