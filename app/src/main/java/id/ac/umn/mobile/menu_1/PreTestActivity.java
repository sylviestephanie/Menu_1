package id.ac.umn.mobile.menu_1;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class PreTestActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pre_test);
        int course = getIntent().getIntExtra("course",1);
        String title = getIntent().getStringExtra("TITLE");
        Log.d("course", "coure:"+course);
        //Toast.makeText(PreTestActivity.this, Integer.toString(course), Toast.LENGTH_LONG);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PreTestInfoFragment fragment = new PreTestInfoFragment();
        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String username = pref.getString("USERNAME", "");
        Bundle data = new Bundle();
        data.putInt("course",course);
        data.putString("username",username);
        data.putString("title", title);
        fragment.setArguments(data);
        fragmentTransaction.replace(android.R.id.content, fragment);

        fragmentTransaction.commit();
    }
}
