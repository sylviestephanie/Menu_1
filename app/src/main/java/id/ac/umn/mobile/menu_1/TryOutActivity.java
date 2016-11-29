package id.ac.umn.mobile.menu_1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class TryOutActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_out);

        toolbar = (Toolbar) findViewById(R.id.toolbar_beginner);
        if (toolbar != null) {
            toolbar.setTitle("Try Out");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String username = pref.getString("USERNAME", "");
        Bundle data = new Bundle();
        data.putString("username",username);

        TryoutInfoFragment fragment = new TryoutInfoFragment();
        fragment.setArguments(data);
        fragmentTransaction.replace(android.R.id.content, fragment);

        fragmentTransaction.commit();
    }
}
