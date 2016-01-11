package bizzmandi.com.bizzm.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import bizzmandi.com.bizzm.R;
import bizzmandi.com.bizzm.adapters.ImgViewPagerAdapter;
import bizzmandi.com.bizzm.applevel.BizzMandiApplication;
import bizzmandi.com.bizzm.network.NetworkRequestor;
import bizzmandi.com.bizzm.utilities.Constants;

/**
 * Created by inhrawat on 1/7/2016.
 */
public class DealFragment extends BaseFragment implements View.OnClickListener,View.OnTouchListener {
    EditText phoneNumber,productType,modelName,companyName,
             totalPrice,discountPrice,discountPercent,netPrice,validDate,
             dealsSpecification,latitude,longitude;
    ViewPager proudctImageViewPager;
    ImgViewPagerAdapter adapter;
    TextView productCategory;
    CheckBox limitedStock;
    Button submitDeal;
    ImageView productImage;
    Map<String,String> data=new HashMap<>();
    ProgressDialog pd;
    int dateValue=-1;
    Calendar calendar;
    LocationManager locationManager;
    String encodedProductImage="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        calendar=Calendar.getInstance();
        View view=inflater.inflate(R.layout.fragment_deals,container,false);
        proudctImageViewPager=(ViewPager)view.findViewById(R.id.productViewPager);
        proudctImageViewPager.setOffscreenPageLimit(5);

        adapter=new ImgViewPagerAdapter(getActivity(),R.layout.imagelayout,this);
        proudctImageViewPager.setAdapter(adapter);
        phoneNumber=(EditText)view.findViewById(R.id.phoneNumber);
        modelName=(EditText)view.findViewById(R.id.productModel);
        companyName=(EditText)view.findViewById(R.id.companyName);
        productCategory=(TextView)view.findViewById(R.id.shopCategory);
        productCategory.setOnTouchListener(this);
        productType=(EditText)view.findViewById(R.id.productType);
        totalPrice=(EditText)view.findViewById(R.id.totalPrice);
        discountPrice=(EditText)view.findViewById(R.id.discountPrice);
        discountPercent=(EditText)view.findViewById(R.id.discountPercent);
        netPrice=(EditText)view.findViewById(R.id.netPrice);
        validDate=(EditText)view.findViewById(R.id.availableDate);
        validDate.setOnTouchListener(this);
        dealsSpecification=(EditText)view.findViewById(R.id.productSpecification);
        latitude=(EditText)view.findViewById(R.id.latitude);
        longitude=(EditText)view.findViewById(R.id.longitude);

        limitedStock=(CheckBox)view.findViewById(R.id.limitedStock);

        submitDeal=(Button)view.findViewById(R.id.submitDeal);
        submitDeal.setOnClickListener(this);



        pd=new ProgressDialog(getActivity());
        pd.setMessage(Constants.POST_DEAL);
        pd.setCanceledOnTouchOutside(false);
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("BizzMandi");  // GPS not found
            builder.setMessage("Enable GPS ?"); // Want to enable?
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton("No", null);
            builder.create().show();
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }



        return view;
    }

    @Override
    public void onClick(View view) {

        submitProduct();
    }

    @Override
    public void successResponse(JSONObject data) {
        pd.cancel();
        Log.d("bizz", "===" + data);
        Toast.makeText(getActivity(), Constants.PRODUCT_REGISTER_SUCCESS, Toast.LENGTH_LONG).show();
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
        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
    }
    public void submitProduct(){
        TelephonyManager tMgr = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        data.put("imei",tMgr.getDeviceId());
        data.put("imagedata",encodedProductImage);
        data.put("producttype", "Electronics");
        data.put("phonenumber", phoneNumber.getText().toString());
        data.put("productcategory","Electronics");
        data.put("productmodel",modelName.getText().toString());
        data.put("productcompany",companyName.getText().toString());
        data.put("totalprice","900");
        data.put("discountprice","900");
        data.put("discountprecent","900");
        data.put("netprice","900");
        data.put("dealdescription","900");
        data.put("valid","900");
        data.put("limited","900");
        data.put("location","900,100");
        data.put("imagedata",encodedProductImage);


        NetworkRequestor networkRequest=new NetworkRequestor(Constants.DEAL,this,(BizzMandiApplication)getActivity().getApplication(),Constants.REQUEST_POST,data);
        networkRequest.sendRequest();
        pd.show();


    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {

            switch (view.getId()){
                case R.id.shopCategory:
                    CategoryFragment categoryFragment=new CategoryFragment();
                    categoryFragment.show(getFragmentManager(),"oo");
                    break;
                case R.id. availableDate:
                    new DatePickerDialog(getActivity(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                    break;
            }


        }

        return false;
    }
    DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                    validDate.setText(date+"/"+month+"/"+year);

        }
    };

    int CAMERA_PIC_REQUEST = 100;

    @Override
    public void setImage(View iv) {

        productImage=(ImageView) iv;
        productImage.setAdjustViewBounds(true);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING",0);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            productImage.setImageBitmap(photo);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            encodedProductImage= Base64.encodeToString(byteArray, Base64.DEFAULT);
        }

    }

    //LOCATION CALLBACKS

    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude.setText(String.valueOf(location.getLatitude()));
            longitude.setText(String.valueOf(location.getLongitude()));
            data.put("location", latitude.getText() + "," + longitude.getText());
            locationManager.removeUpdates(locationListener);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

    };


    //LOCATION CALLBACKS
}
