package bizzmandi.com.bizzm.ui.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

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
public class SellFragment extends BaseFragment implements View.OnClickListener,ViewPagerClickCallback,View.OnTouchListener  {
    ViewPager proudctImageViewPager;
    ImgViewPagerAdapter adapter;
    Button submitProduct;
    EditText shopCategory,validDate,availableDate,companyName,laltitude,longitude,netPrice,unitPrice,productSpecification,
            modelNumber,productUnits;
    Spinner userType;
    int CAMERA_PIC_REQUEST = 100;
    ImageView productImage;
    private static final int DATE_DIALOG_ID = 3;
    Calendar calendar;
    int dateValue=-1;
    Map<String,String> data=new HashMap<>();

    String IMEI;
    LocationManager locationManager;
    SeekBar distance;
    RadioGroup group;

    String encodedProductImage="";
    ProgressDialog pd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sell,container,false);
        proudctImageViewPager=(ViewPager)view.findViewById(R.id.productViewPager);
        proudctImageViewPager.setOffscreenPageLimit(5);

        adapter=new ImgViewPagerAdapter(getActivity(),R.layout.imagelayout,this);
        proudctImageViewPager.setAdapter(adapter);

        shopCategory=(EditText)view.findViewById(R.id.shopCategory);
        availableDate=(EditText)view.findViewById(R.id.availableDate);
        companyName=(EditText)view.findViewById(R.id.companyName);
        netPrice=(EditText)view.findViewById(R.id.netPrice);
        unitPrice=(EditText)view.findViewById(R.id.unitPrice);
        productSpecification=(EditText)view.findViewById(R.id.productSpecification);

        laltitude=(EditText)view.findViewById(R.id.latitude);
        longitude=(EditText)view.findViewById(R.id.longitude);
        modelNumber=(EditText)view.findViewById(R.id.modelNumber);
        productUnits=(EditText)view.findViewById(R.id.productUnits);

        validDate=(EditText)view.findViewById(R.id.validDate);
        submitProduct=(Button)view.findViewById(R.id.submitProduct);
        group=(RadioGroup)view.findViewById(R.id.productCondition);

        distance=(SeekBar)view.findViewById(R.id.nearbydistance);


        availableDate.setOnTouchListener(this);
        validDate.setOnTouchListener(this);
        submitProduct.setOnClickListener(this);


        userType=(Spinner)view.findViewById(R.id.userType);

        calendar= Calendar.getInstance();
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
//            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        pd=new ProgressDialog(getActivity());
        pd.setMessage(Constants.ADD_PRODUCT);
        pd.setCanceledOnTouchOutside(false);



        return view;
    }

    @Override
    public void successResponse(JSONObject data) {
        pd.cancel();
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
        IMEI=tMgr.getDeviceId();
        data.put("imei",IMEI);
        data.put("producttype", "Electronics");
        data.put("productcategory","Electronics");
        data.put("productmodel",modelNumber.getText().toString());
        data.put("productcompany",companyName.getText().toString());


        data.put("distance",String.valueOf(distance.getProgress()));

        if(group.getCheckedRadioButtonId()!=-1){
            int id= group.getCheckedRadioButtonId();
            View radioButton = group.findViewById(id);
            int radioId = group.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) group.getChildAt(radioId);
            data.put("condition",btn.getText().toString());

        }

        data.put("buyorsell","0");



        data.put("description",productSpecification.getText().toString());
        data.put("imagedata",encodedProductImage);
        data.put("netprice",netPrice.getText().toString());
        data.put("availableby", availableDate.getText().toString());
        data.put("valid", validDate.getText().toString());
        data.put("city", "Electronics");
        data.put("unit",productUnits.getText().toString());

        data.put("usertype",userType.getSelectedItem().toString());
        data.put("price", "999");
        data.put("minunit","50");

        NetworkRequestor networkRequest=new NetworkRequestor(Constants.REGISTER_PRODUCT,this,(BizzMandiApplication)getActivity().getApplication(),Constants.REQUEST_POST,data);
        networkRequest.sendRequest();
        pd.show();


    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.submitProduct:
                submitProduct();

                break;

        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
                case R.id.availableDate:
                    dateValue=0;
                    new DatePickerDialog(getActivity(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                    break;
                case R.id.validDate:
                    dateValue=1;
                    new DatePickerDialog(getActivity(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                    break;
            }
        }

        return false;
    }

    DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int date) {

            switch (dateValue)
            {
                case 0:
                    availableDate.setText(date+"/"+month+"/"+year);
                    break;
                case 1:
                    validDate.setText(date+"/"+month+"/"+year);
                    break;
            }
            dateValue=-1;
        }
    };

    @Override
    public void setImage(View iv) {

    }
    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            laltitude.setText(String.valueOf(location.getLatitude()));
            longitude.setText(String.valueOf(location.getLongitude()));
            data.put("location", laltitude.getText() + "," + longitude.getText());
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
}
