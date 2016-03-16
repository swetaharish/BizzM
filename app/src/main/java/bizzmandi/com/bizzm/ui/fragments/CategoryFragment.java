package bizzmandi.com.bizzm.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import bizzmandi.com.bizzm.R;
import bizzmandi.com.bizzm.adapters.CategoryAdapter;
import bizzmandi.com.bizzm.applevel.BizzMandiApplication;
import bizzmandi.com.bizzm.models.CategoryModel;
import bizzmandi.com.bizzm.network.NetworkCallback;
import bizzmandi.com.bizzm.network.NetworkRequestor;
import bizzmandi.com.bizzm.utilities.Constants;

/**
 * Created by vij on 12/9/2015.
 */
public class CategoryFragment extends DialogFragment implements View.OnClickListener,NetworkCallback,AdapterView.OnItemClickListener {

    TextView categoryDone,categoryBack;
    ListView categoryList;
    ArrayList<CategoryModel> selectedCategoryList=new ArrayList<>();

    JSONObject testJSON;
    public static final String TAG=CategoryFragment.class.getSimpleName();
    Stack<ArrayList<CategoryModel>> categoryStack=new Stack<>();
    ArrayList<CategoryModel> list=new ArrayList<>();
    Map<String,String> data=new HashMap<String,String>();
    ArrayAdapter<CategoryModel> adapter;
    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view=inflater.inflate(R.layout.category_layout,container,false);

        categoryDone=(TextView)view.findViewById(R.id.category_done);
        categoryDone.setOnClickListener(this);
        categoryDone.setVisibility(View.INVISIBLE);

        pd=new ProgressDialog(getActivity());
        pd.setMessage(Constants.LOADING_CATEGORY);
        pd.setCanceledOnTouchOutside(false);


        categoryBack=(TextView)view.findViewById(R.id.category_back);
        categoryBack.setOnClickListener(this);
        categoryBack.setVisibility(View.INVISIBLE);

        categoryList=(ListView)view.findViewById(R.id.categoryList);
        categoryList.setOnItemClickListener(this);

        adapter=new CategoryAdapter(getActivity(),list);

        categoryList.setAdapter(adapter);

        getCategories("0","main");

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.category_done:
                getDialog().dismiss();
                break;
            case R.id.category_back:
                categoryStack.pop();
                list.clear();
                list.addAll(categoryStack.peek());
                adapter.notifyDataSetChanged();
                if(categoryStack.size()==1){
                    categoryBack.setVisibility(View.GONE);
                }


                break;
        }
    }

    public void getCategories(String id,String name){
        data.put("id", id);
        data.put("categoryname",name);
        pd.show();
        NetworkRequestor networkRequest=new NetworkRequestor(Constants.GET_CATEGORY,this,(BizzMandiApplication)getActivity().getApplication(),Constants.REQUEST_POST,data);
        networkRequest.sendRequest();
    }

    @Override
    public void successResponse(JSONObject data) {
        pd.cancel();
        list.clear();
        Log.d(TAG,"Category Response success==="+data);
        try{
            JSONArray categoryJSONArray=(JSONArray)data.get("categories");
            ArrayList<CategoryModel>tempList=new ArrayList<>();

            for(int i=0;i<categoryJSONArray.length();i++){

                JSONObject categoryModel=(JSONObject)categoryJSONArray.get(i);
                CategoryModel tempCM=new CategoryModel((int)categoryModel.get("ID"),(String)categoryModel.get("CategoryName"));
                Log.d(TAG, "Category Response success===" + tempCM.toString());
                list.add(tempCM);
                tempList.add(tempCM);
            }

            categoryStack.push(tempList);
            adapter.notifyDataSetChanged();
            if(categoryStack.size()>1){
                categoryBack.setVisibility(View.VISIBLE);
            }

        }catch (JSONException e){

        }

    }

    @Override
    public void requestInvalid(JSONObject data) {

        Log.d(TAG,"Category Response Invalid==="+data);
        pd.cancel();

    }

    @Override
    public void errorResponse(VolleyError error) {
        pd.cancel();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.d(TAG,"--position---"+position);
        ArrayList<CategoryModel>data=categoryStack.peek();
        Log.d(TAG,"--Selected ---"+data.get(position).toString());
        getCategories(String.valueOf(data.get(position).getCategoryId()), data.get(position).getCategoryName());

    }
}
