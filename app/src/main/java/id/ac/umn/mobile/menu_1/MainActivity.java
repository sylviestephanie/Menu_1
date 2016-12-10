package id.ac.umn.mobile.menu_1;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.renderscript.RenderScript;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import id.ac.umn.mobile.menu_1.R;
import id.ac.umn.mobile.menu_1.app.Config;
import id.ac.umn.mobile.menu_1.utils.NotificationUtils;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pAdapter;



    private Boolean exit = false;
    private int RESULT_LOAD_IMAGE=1;

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtMessage;
    public static ContentResolver cr;
    public static Bitmap bitmapImg;

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.URL_SAFE);
    }
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.URL_SAFE);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            try {
                String temp=uri.toString();
                Log.e("tostring",uri.toString());
                Log.e("ABDEL2",Uri.parse(temp).toString());
                Uri newUri=Uri.parse(temp);

                UserPicture pict=new UserPicture(newUri,getContentResolver());

                Bitmap adjustedBitmap= pict.getBitmap();
                String xx=bitmapToBase64(adjustedBitmap);
                Log.println(Log.ERROR,"this is what we want",xx);
                Log.e("this is ano :","xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\naaaaaaaaaaaaaaaaaaaaaaaaaaaa\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                adjustedBitmap=base64ToBitmap(xx);

                adjustedBitmap=getCircleBitmap(adjustedBitmap);
                ImageView imageView = (ImageView) findViewById(R.id.user_profile_photo);
                imageView.setImageBitmap(adjustedBitmap);


                SharedPreferences.Editor prefEdit
                        = getSharedPreferences("USER_PREFERENCES", MODE_PRIVATE).edit();

//                prefEdit.putString("userPicture", encodeTobase64(adjustedBitmap));
//                prefEdit.commit();
//                Log.e("ABDEL",uri.toString());

                SharedPreferences pref
                        = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
                new UpdateUserPicture().execute(String.format(
                        "username=%s&image=%s",
                        pref.getString("USERNAME",""),
                        xx
                        //encodeTobase64(adjustedBitmap)
                        //uri.toString()

                ));
                //getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void change_profile(View v){
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
//        try{
//
//            getIntent().setAction(Intent.ACTION_OPEN_DOCUMENT);
//            Uri uri=Uri.parse("content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Ffile%2F9233/ORIGINAL/NONE/726927857");
//            String temp=uri.toString();
//            Log.e("tostring",uri.toString());
//            Log.e("ABDEL2",Uri.parse(temp).toString());
//            Uri newUri=Uri.parse(temp);
//            getContentResolver().takePersistableUriPermission(newUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            UserPicture pict=new UserPicture(newUri,getContentResolver());
//
//            Bitmap adjustedBitmap= pict.getBitmap();
//            adjustedBitmap=getCircleBitmap(adjustedBitmap);
//            ImageView imageView = (ImageView) findViewById(R.id.user_profile_photo);
//            imageView.setImageBitmap(adjustedBitmap);}catch (Exception e){
//            e.printStackTrace();
//        }


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

    }

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
                    //FirebaseMessaging.getInstance();

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
        new GetProfilePicture().execute();
        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES",MODE_PRIVATE);
        //alarmmanager only created once
        if(!pref.getBoolean("ALARMSET",false)) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar firingCal= Calendar.getInstance();

            firingCal.set(Calendar.HOUR_OF_DAY, 10); // At the hour you wanna fire
            firingCal.set(Calendar.MINUTE, 0); // Particular minute
            firingCal.set(Calendar.SECOND, 0); // particular second

            Long intendedTime = firingCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,intendedTime,1000 * 60 * 60,broadcast);

            SharedPreferences.Editor prefEdit= getSharedPreferences("LOGIN_PREFERENCES",MODE_PRIVATE).edit();
            prefEdit.putBoolean("ALARMSET", true);
            prefEdit.commit();
        }
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
        cr=getContentResolver();

        //WebService webService = new WebService("http://learnit-database.esy.es/register_firebase.php?username="+name+"&firebase_id="+regId,"GET", "");
    }
    class GetProfilePicture extends AsyncTask<Void,Void,String>{
        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            try {
//                Uri uri = Uri.parse(str);
//               getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                UserPicture pict = new UserPicture(uri, getContentResolver());


//                bitmapImg = pict.getBitmap();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
            String name = pref.getString("USERNAME", "0");
//            WebService webService = new WebService("http://learnit-database.esy.es/get_user_details.php?username="+name,"GET", "");
//            WebService webService = new WebService("https://10.0.2.2/android/get_user_details.php?username="+username,"GET", "");
//            String jsonString = webService.responseBody;
//            try
//            {
//
//                Log.d("result", jsonString);
//                JSONArray profileArray = new JSONArray(jsonString);
//                for (int i = 0; i < profileArray.length(); i++) {
//                    JSONObject obj = profileArray.getJSONObject(i);
//                    String image = obj.getString("image");
//                   // image = image.replaceAll("\\\\", "");
//                    return image;
//
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
            try {
//                URL url = new URL("http://learnit-database.esy.es/get_user_image.php?username="+name);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//
//                bitmapImg = BitmapFactory.decodeStream(input);
//baca udah valid
                WebService webService = new WebService("http://learnit-database.esy.es/get_user_image.php?username="+name,"GET", "");
                String jsonString = webService.responseBody;
//                Log.e("this is what we got",jsonString);

                bitmapImg = base64ToBitmap(jsonString);
            } catch (Exception e) {
                // Log exception
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }

    class RegisterFireBase extends AsyncTask<String,Void,Object>{


        @Override
        protected Object doInBackground(String[] params) {
            WebService webService = new WebService("http://learnit-database.esy.es/register_firebase.php?username="+params[0]+"&firebase_id="+params[1],"GET", "");
            String jsonString = webService.responseBody;
            return null;
        }
    }
    class UpdateUserPicture extends AsyncTask<String,Void,Object>{


        @Override
        protected Object doInBackground(String[] params) {
            WebService webService = new WebService("http://learnit-database.esy.es/update_user_picture.php","POST", params[0]);
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
            case R.id.Forum:
                Intent intent = new Intent(MainActivity.this, ForumActivity.class);
                startActivity(intent);
                break;
            case R.id.Notes:
                Intent intent2 = new Intent(MainActivity.this, NotesActivity.class);
                startActivity(intent2);
                break;
            case R.id.Logout:
                SharedPreferences.Editor prefEdit
                        = MainActivity.this.getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE).edit();
                prefEdit.clear();
                prefEdit.commit();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.About:
                intent=new Intent(this,AboutActivity.class);
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
