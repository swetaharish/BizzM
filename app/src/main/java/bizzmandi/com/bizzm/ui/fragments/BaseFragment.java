package bizzmandi.com.bizzm.ui.fragments;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import bizzmandi.com.bizzm.network.NetworkCallback;

/**
 * Created by inhrawat on 12/15/2015.
 */
public class BaseFragment extends Fragment implements NetworkCallback,ViewPagerClickCallback {

    ProgressDialog pd;

    public BaseFragment(){
//        pd=new ProgressDialog(getActivity());
    }


    @Override
    public void successResponse(JSONObject data) {

    }

    @Override
    public void requestInvalid(JSONObject data) {

    }

    @Override
    public void errorResponse(VolleyError error) {

    }

    @Override
    public void setImage(View iv) {

    }
}
