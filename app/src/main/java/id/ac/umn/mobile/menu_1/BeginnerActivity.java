package id.ac.umn.mobile.menu_1;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BeginnerActivity extends AppCompatActivity {

    private List<TutorialCourse> courses;

    private String username="";
    private int level;
    int flag1,flag2,flag3;
    Toolbar toolbar;
    FloatingActionButton mFab;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner);
        toolbar = (Toolbar) findViewById(R.id.toolbar_beginner);
        level = getIntent().getIntExtra("LVL", 0);
        if (toolbar != null) {
            if(level==1) toolbar.setTitle("Beginner");
            else if(level == 2) toolbar.setTitle("Intermediate");
            else toolbar.setTitle("Advanced");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        layout  = (LinearLayout) findViewById(R.id.progressbar_view);
        new GetFlag().execute();
    }

    private void initializeData() {
        courses.get(0).setFlag(flag1);
        courses.get(1).setFlag(flag2);
        courses.get(2).setFlag(flag3);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    class GetFlag extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layout.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... voids) {
            SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
            username = pref.getString("USERNAME", "");
            Log.d("user", username);
            WebService webService = new WebService("http://learnit-database.000webhostapp.com/flag_test.php?username="+username+"&type=1&id="+level,"GET", "");
            String jsonString = webService.responseBody;
            ArrayList<HashMap<String,String>> arr = new ArrayList<>();
            try{
                JSONObject obj =new JSONObject(jsonString);
                int code = obj.getInt("success");
                String flag1 = obj.getString("flag_1");
                String flag2 = obj.getString("flag_2");
                String flag3 = obj.getString("flag_3");

                HashMap<String, String> res = new HashMap<>();
                res.put("success", Integer.toString(code));
                res.put("flag1", flag1);
                res.put("flag2", flag2);
                res.put("flag3", flag3);
                arr.add(res);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            WebService service = new WebService("http://learnit-database.000webhostapp.com/course.php?level="+level, "GET", "");
            jsonString = service.responseBody;
            try {
                JSONArray courseArray = new JSONArray(jsonString);
                courses = new ArrayList<>();
                for (int i = 0; i < courseArray.length(); i++) {
                    JSONObject courseObject = courseArray.getJSONObject(i);
                    TutorialCourse c = new TutorialCourse();
                    c.setTitle(courseObject.getString("title"));
                    c.setDesc(courseObject.getString("description"));
                    int imageid = getResources().getIdentifier(courseObject.getString("image"), "drawable", "id.ac.umn.mobile.menu_1");
                    c.setImage(imageid);
                    c.setLevel(level);
                    courses.add(c);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            return arr;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);
            layout.setVisibility(View.GONE);
            if(hashMaps.get(0).get("success").equals("1"))
            {
                flag1 = Integer.parseInt(hashMaps.get(0).get("flag1"));
                flag2 = Integer.parseInt(hashMaps.get(0).get("flag2"));
                flag3 = Integer.parseInt(hashMaps.get(0).get("flag3"));
            }
            mFab = (FloatingActionButton)findViewById(R.id.favorite);

            RecyclerView rv= (RecyclerView)findViewById(R.id.rv_beginner);
            rv.setHasFixedSize(true);

            initializeData(); //initialize flag (hardcode)

            RVCourseAdapter adapter = new RVCourseAdapter(courses);
            rv.setAdapter(adapter);

            LinearLayoutManager llm = new LinearLayoutManager(BeginnerActivity.this);
            rv.setLayoutManager(llm);
        }
    }

}
