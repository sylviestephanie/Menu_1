package id.ac.umn.mobile.menu_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import id.ac.umn.mobile.menu_1.R;
import id.ac.umn.mobile.menu_1.app.Config;
import id.ac.umn.mobile.menu_1.utils.NotificationUtils;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pAdapter;
    private Boolean exit = false;


    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_school_white_48dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_person_white_48dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_poll_white_48dp));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        pAdapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pAdapter);
        toolbar.setTitle("Same Page");
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        toolbar.setTitle("Tutorial");
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        toolbar.setTitle("Profile");
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        toolbar.setTitle("Rank");
                        break;
                    default:
                        viewPager.setCurrentItem(tab.getPosition());
                        toolbar.setTitle("Tutorial");
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //txtMessage = (TextView) findViewById(R.id.txt_push_message);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();
    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
        SharedPreferences pref2 = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String name = pref2.getString("USERNAME", "0");
        Log.e(TAG,name);
        new RegisterFireBase().execute(name,regId);
        //WebService webService = new WebService("http://learnit-database.esy.es/register_firebase.php?username="+name+"&firebase_id="+regId,"GET", "");
    }

    class RegisterFireBase extends AsyncTask<String,Void,Object>{


        @Override
        protected Object doInBackground(String[] params) {
            WebService webService = new WebService("http://learnit-database.esy.es/register_firebase.php?username="+params[0]+"&firebase_id="+params[1],"GET", "");
            String jsonString = webService.responseBody;
            return null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }
        SharedPreferences pref = getSharedPreferences("LOGIN_DATA",MODE_PRIVATE); //utk di activity ini sendiri
        int current_level = Integer.parseInt(pref.getString("level","0"));

        @Override
        public Fragment getItem(int position) {
            SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
            String name = pref.getString("USERNAME", "0");
            int curr = Integer.parseInt(pref.getString("CURRENT_LEVEL","0"));

            switch (position) {
                case 0:
                    OneFragment tab0 = new OneFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("current_level",curr);
                    tab0.setArguments(bundle);
                    return tab0;
                case 1:
                    TwoFragment tab1 = new TwoFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("username", name);
                    bundle1.putInt("current_level",curr);
                    tab1.setArguments(bundle1);
                    return tab1;
                case 2:
                    ThreeFragment tab2 = new ThreeFragment();
                    return tab2;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_side,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                SharedPreferences.Editor prefEdit
                        = MainActivity.this.getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE).edit();
                prefEdit.clear();
                prefEdit.commit();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.About:
                Intent intent=new Intent(this,AboutActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }
}
