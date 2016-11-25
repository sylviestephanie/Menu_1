package id.ac.umn.mobile.menu_1;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BeginnerActivity extends AppCompatActivity implements AbsListView.OnScrollListener{

    private List<TutorialCourse> courses;

    final static String[] DUMMY_DATA = {
            "France",
            "Sweden",
            "Germany",
            "USA",
            "Portugal",
            "The Netherlands",
            "Belgium",
            "Spain",
            "United Kingdom",
            "Mexico",
            "Finland",
            "Norway",
            "Italy",
            "Ireland",
            "Brazil",
            "Japan"
    };
    private String username="";
    private int level;
    int flag1,flag2,flag3;
    Toolbar toolbar;
    View ContainerHeader;
    FloatingActionButton mFab;

    ObjectAnimator fade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginner);
        new GetFlag().execute();

        mFab = (FloatingActionButton)findViewById(R.id.favorite);
        toolbar = (Toolbar) findViewById(R.id.toolbar_beginner);
        //ListView listView = (ListView)findViewById(R.id.listview);

        RecyclerView rv= (RecyclerView)findViewById(R.id.rv_beginner);
        rv.setHasFixedSize(true);

        initializeData();

        RVCourseAdapter adapter = new RVCourseAdapter(courses);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        if (toolbar != null) {
            toolbar.setTitle("Beginner");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Inflate the header view and attach it to the ListView
        View headerView = LayoutInflater.from(this)
                .inflate(R.layout.beginner_details, rv, false);
        ContainerHeader = headerView.findViewById(R.id.container);
        //listView.addHeaderView(headerView);

        // prepare the fade in/out animator
        fade =  ObjectAnimator.ofFloat(ContainerHeader, "alpha", 0f, 1f);
        fade.setInterpolator(new DecelerateInterpolator());
        fade.setDuration(400);
        Toast.makeText(BeginnerActivity.this, username, Toast.LENGTH_LONG);
        /*listView.setOnScrollListener(this);
        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                DUMMY_DATA));*/

    }

    private void initializeData() {
        courses = new ArrayList<>();
        courses.add(new TutorialCourse("Course 1", "desc", R.drawable.blue_1,flag1));
        courses.add(new TutorialCourse("Course 2", "desc", R.drawable.blue_1,flag2));
        courses.add(new TutorialCourse("Course 3", "desc", R.drawable.blue_1,flag3));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    /**
     * Listen to the scroll events of the listView
     * @param view the listView
     * @param firstVisibleItem the first visible item
     * @param visibleItemCount the number of visible items
     * @param totalItemCount the amount of items
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onScroll(AbsListView view,
                         int firstVisibleItem,
                         int visibleItemCount,
                         int totalItemCount) {
        // we make sure the list is not null and empty, and the header is visible
        if (view != null && view.getChildCount() > 0 && firstVisibleItem == 0) {

            // we calculate the FAB's Y position
            int translation = view.getChildAt(0).getHeight() + view.getChildAt(0).getTop();
            mFab.setTranslationY(translation>0  ? translation : 0);

            // if we scrolled more than 16dps, we hide the content and display the title
            if (view.getChildAt(0).getTop() < -dpToPx(16)) {
                toggleHeader(false, false);
            } else {
                toggleHeader(true, true);
            }
        } else {
            toggleHeader(false, false);
        }

        // if the device uses Lollipop or above, we update the ToolBar's elevation
        // according to the scroll position.
        if (isLollipop()) {
            if (firstVisibleItem == 0) {
                toolbar.setElevation(0);
            } else {
                toolbar.setElevation(dpToPx(4));
            }
        }
    }

    /**
     * Start the animation to fade in or out the header's content
     * @param visible true if the header's content should appear
     * @param force true if we don't wait for the animation to be completed
     *              but force the change.
     */
    private void toggleHeader(boolean visible, boolean force) {
        if ((force && visible) || (visible && ContainerHeader.getAlpha() == 0f)) {
            fade.setFloatValues(ContainerHeader.getAlpha(), 1f);
            fade.start();
        } else if (force || (!visible && ContainerHeader.getAlpha() == 1f)){
            fade.setFloatValues(ContainerHeader.getAlpha(), 0f);
            fade.start();
        }
        // Toggle the visibility of the title.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(!visible);
        }
    }


    /**
     * Convert Dps into Pxs
     * @param dp a number of dp to convert
     * @return the value in pixels
     */
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int)(dp * (displayMetrics.densityDpi / 160f));
    }

    /**
     * Check if the device rocks, and runs Lollipop
     * @return true if Lollipop or above
     */
    public static boolean isLollipop() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
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
        }
    }
}
