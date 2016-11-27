package id.ac.umn.mobile.menu_1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class LevelTestActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_test);
        int level = getIntent().getIntExtra("level",1);
        toolbar = (Toolbar) findViewById(R.id.toolbar_beginner);
        if (toolbar != null) {
            toolbar.setTitle("Level "+level+" test");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LevelTestInfoFragment fragment = new LevelTestInfoFragment();
        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String username = pref.getString("USERNAME", "");
        Bundle data = new Bundle();
        data.putString("username",username);
        data.putInt("level", level);
        fragment.setArguments(data);
        fragmentTransaction.replace(android.R.id.content, fragment);

        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

    }
}
