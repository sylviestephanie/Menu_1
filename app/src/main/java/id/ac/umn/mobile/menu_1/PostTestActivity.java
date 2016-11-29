package id.ac.umn.mobile.menu_1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class PostTestActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_test);
        int level = getIntent().getIntExtra("level",1);
        Log.d("levelinposttest", Integer.toString(level));
        int course=getIntent().getIntExtra("course",1);
        Log.d("courseinpost", Integer.toString(course));
        /*if(level == 1)
            course = getIntent().getIntExtra("course",1);
        else if(level == 2)
        {
            int temp = getIntent().getIntExtra("course",1);
            if(temp == 1) course = 4;
            else if(temp == 2) course = 5;
            else if(temp == 3) course = 6;
        }
        else
        {
            int temp = getIntent().getIntExtra("course",1);
            if(temp == 1) course = 7;
            else if(temp == 2) course = 8;
            else if(temp == 3) course = 9;
        }*/
        String title = getIntent().getStringExtra("TITLE");
        toolbar = (Toolbar) findViewById(R.id.toolbar_beginner);
        if (toolbar != null) {
            toolbar.setTitle("Post test");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PostTestInfoFragment fragment = new PostTestInfoFragment();
        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String username = pref.getString("USERNAME", "");
        String clevel = pref.getString("CURRENT_LEVEL","");
        Bundle data = new Bundle();
        data.putInt("course",course);
        Log.d("post_data", Integer.toString(course));
        data.putString("username",username);
        data.putString("title", title);
        data.putInt("level", Integer.parseInt(clevel));
//        data.putInt("lvl", level);
        fragment.setArguments(data);
        fragmentTransaction.replace(android.R.id.content, fragment);

        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

    }
}
