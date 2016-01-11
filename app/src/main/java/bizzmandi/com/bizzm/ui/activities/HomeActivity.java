package bizzmandi.com.bizzm.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import bizzmandi.com.bizzm.R;
import bizzmandi.com.bizzm.adapters.RootLeftDrawerListAdapter;
import bizzmandi.com.bizzm.ui.fragments.BuyFragment;
import bizzmandi.com.bizzm.ui.fragments.DealFragment;
import bizzmandi.com.bizzm.ui.fragments.ProductRegistrationFragment;
import bizzmandi.com.bizzm.ui.fragments.SellFragment;
import bizzmandi.com.bizzm.ui.fragments.UserProfileFragment;

public class HomeActivity extends AppCompatActivity {
    ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    String []data;
    public static final String  TAG="HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDrawerList=(ListView)findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        data=getResources().getStringArray(R.array.leftmenu);
        RootLeftDrawerListAdapter adapter=new RootLeftDrawerListAdapter(this, data);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        setTitle(data[position]);
        Log.d(TAG, data[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (position){
            case 0:
                fragment = new SellFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();

                break;
            case 1:
                fragment = new DealFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragment = new BuyFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                break;
            case 3:
                //TODO :SEARCH FRAGMENT
                break;
            case 4:
                fragment = new ProductRegistrationFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                break;
            case 5:
                fragment = new UserProfileFragment();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                break;
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_productExpiring:
                return true;
            case R.id.action_deals:
                return true;
            case R.id.action_leads:
                return true;
            case R.id.action_review:
                return true;
            case R.id.action_chat:
                return true;
        }
        return  true;
    }
}
