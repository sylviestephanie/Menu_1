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
    View ContainerHeader;
    FloatingActionButton mFab;
    public static String[] course, desc;

    ObjectAnimator fade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner);
        new GetFlag().execute();
    }

    private void initializeData() {
        courses = new ArrayList<>();
        courses.add(new TutorialCourse("Course 1", "desc", R.drawable.blue_1,flag1));
        courses.add(new TutorialCourse("Course 2", "desc", R.drawable.blue_1,flag2));
        courses.add(new TutorialCourse("Course 3", "desc", R.drawable.blue_1,flag3));

        /*for(int i =0; i<course.length; i++){
            TutorialCourse item = new TutorialCourse();
            item.setTitle(course[i]);
            item.setDesc(desc[i]);
            courses.add(item);
        }*/
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
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BeginnerActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... voids) {
            SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
            username = pref.getString("USERNAME", "");
            Log.d("user", username);
            WebService webService = new WebService("http://learnit-database.000webhostapp.com/flag_test.php?username="+username+"&type=1&id=1","GET", "");
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

            return arr;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);
            progressDialog.dismiss();
            if(hashMaps.get(0).get("success").equals("1"))
            {
                flag1 = Integer.parseInt(hashMaps.get(0).get("flag1"));
                flag2 = Integer.parseInt(hashMaps.get(0).get("flag2"));
                flag3 = Integer.parseInt(hashMaps.get(0).get("flag3"));
            }
            mFab = (FloatingActionButton)findViewById(R.id.favorite);
            toolbar = (Toolbar) findViewById(R.id.toolbar_beginner);

            RecyclerView rv= (RecyclerView)findViewById(R.id.rv_beginner);
            rv.setHasFixedSize(true);

            initializeData();

            RVCourseAdapter adapter = new RVCourseAdapter(courses);
            rv.setAdapter(adapter);

            LinearLayoutManager llm = new LinearLayoutManager(BeginnerActivity.this);
            rv.setLayoutManager(llm);

            //new getCourse().execute(); still error

            if (toolbar != null) {
                toolbar.setTitle("Beginner");
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }
    }

    class getCourse extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BeginnerActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            WebService service = new WebService("http://learnit-database.000webhostapp.com/course.php?level=1", "GET", "");
            String jsonString = service.responseBody;
            try {
                JSONArray courseArray = new JSONArray(jsonString);
                for (int i = 0; i < courseArray.length(); i++) {
                    JSONObject courseObject = courseArray.getJSONObject(i);
                    course[i] = courseObject.getString("title");
                    desc[i] = courseObject.getString("description");
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            initializeData();
        }
    }
}
