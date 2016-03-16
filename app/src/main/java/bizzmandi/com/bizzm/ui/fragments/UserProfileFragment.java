package bizzmandi.com.bizzm.ui.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bizzmandi.com.bizzm.R;
import bizzmandi.com.bizzm.applevel.BizzMandiApplication;
import bizzmandi.com.bizzm.network.NetworkCallback;
import bizzmandi.com.bizzm.network.NetworkRequestor;
import bizzmandi.com.bizzm.utilities.Constants;

public class UserProfileFragment extends Fragment implements NetworkCallback,View.OnClickListener {

    EditText username,password,confirmPassword,shopName,shopAddress,
             shopPhone,shopWorkingHours,shopAbout,shopKeywords,
             shopEmail,city,town,state,country;

    Map<String,String> data=new HashMap<String,String>();
    TextView category;
    CheckBox leadNotification;
    ProgressDialog pd;
    Button submit;
    ImageView shopImg;

    static final int REQUEST_IMAGE_CAPTURE = 1;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd=new ProgressDialog(getActivity());
        pd.setMessage(Constants.SAVING_PROFILE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_user_profile,container,false);

        shopImg=(ImageView)view.findViewById(R.id.shopImg);
        shopImg.setOnClickListener(this);
//        shopImg.setImageBitmap(Bitmap.createBitmap(R.drawable.photo_add));

        submit=(Button)view.findViewById(R.id.submitProfile);
        submit.setOnClickListener(this);
        category=(TextView)view.findViewById(R.id.shopCategory);
        username=(EditText)view.findViewById(R.id.username);
        password=(EditText)view.findViewById(R.id.password);
        confirmPassword=(EditText)view.findViewById(R.id.confirmPassword);
        shopName=(EditText)view.findViewById(R.id.shopName);
        shopAddress=(EditText)view.findViewById(R.id.shopAddress);
        shopPhone=(EditText)view.findViewById(R.id.shopPhone);
        shopWorkingHours=(EditText)view.findViewById(R.id.shopWorkingHours);
        shopAbout=(EditText)view.findViewById(R.id.shopAboutus);
        shopKeywords=(EditText)view.findViewById(R.id.shopKeywords);
        shopEmail=(EditText)view.findViewById(R.id.shopEmail);
        city=(EditText)view.findViewById(R.id.city);
        town=(EditText)view.findViewById(R.id.town);
        state=(EditText)view.findViewById(R.id.state);
        country=(EditText)view.findViewById(R.id.country);

        category.setOnClickListener(this);

        return view;
    }

    @Override
    public void successResponse(JSONObject data) {
        pd.cancel();
        Toast.makeText(getActivity(),Constants.PROFILE_SAVE_SUCCESS,Toast.LENGTH_LONG).show();
        password.setText("");
        confirmPassword.setText("");
        shopName.setText("");
        username.setText("");
        password.setText("");
        shopAddress.setText("");
        shopPhone.setText("");
        shopWorkingHours.setText("");
        shopAbout.setText("");
        city.setText("");
        town.setText("");
        state.setText("");
        country.setText("");
    }

    @Override
    public void requestInvalid(JSONObject data) {
        pd.cancel();
        try{
            Toast.makeText(getActivity(),(String)data.get("error_msg"),Toast.LENGTH_LONG).show();
        }
        catch (JSONException e){
            Toast.makeText(getActivity(),"Invalid request ",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void errorResponse(VolleyError error) {
        pd.cancel();

    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.submitProfile:

                if(password.getText().toString().equals(confirmPassword.getText().toString())) {

                    data.put("name", shopName.getText().toString());
                    data.put("username", username.getText().toString());
                    data.put("password", password.getText().toString());
                    data.put("address", shopAddress.getText().toString());
                    data.put("phone", shopPhone.getText().toString());
                    data.put("workin", shopWorkingHours.getText().toString());
                    data.put("description", shopAbout.getText().toString());
                    data.put("city", city.getText().toString());
                    data.put("town", town.getText().toString());
                    data.put("state", state.getText().toString());
                    data.put("country", country.getText().toString());

                    NetworkRequestor networkRequest=new NetworkRequestor(Constants.REGISTER_DEVICE,this,(BizzMandiApplication)getActivity().getApplication(),Constants.REQUEST_POST,data);
                    networkRequest.sendRequest();
                    pd.show();

                }else {
                    Toast.makeText(getActivity(), Constants.PASSWORD_MATCH_FAILED,Toast.LENGTH_LONG).show();
                    password.setText("");
                    confirmPassword.setText("");

                }


                break;
            case R.id.shopCategory:

                CategoryFragment categoryFragment=new CategoryFragment();
                categoryFragment.show(getFragmentManager(),"oo");

                break;


            case R.id.myDeals:
                break;
            case R.id.myProducts:
                break;

            case R.id.shopImg:
                if (isDeviceSupportCamera()) {
                    captureImage();
                }
                break;


        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            shopImg.setImageBitmap(imageBitmap);
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, Activity.REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
//                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}
