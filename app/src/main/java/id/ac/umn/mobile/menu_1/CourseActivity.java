package id.ac.umn.mobile.menu_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    private String username="", info, summary, video;
    private TextView info_text, summary_text;
    private int level,course;
    int flag1,flag2,flag3;
    Toolbar toolbar;
    private LinearLayout layout;
    Button postTest;
    private static final int RECOVERY_REQUEST = 1;
    private GetVideo youtube_vid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        youtube_vid=new GetVideo();
        layout  = (LinearLayout) findViewById(R.id.progressbar_view);
        String title = getIntent().getStringExtra("TITLE");
        /*TextView tvTitle=(TextView)findViewById(R.id.title);*/
        info_text = (TextView) findViewById(R.id.info);
        summary_text = (TextView) findViewById(R.id.summary);
        /*tvTitle.setText(title);*/
        level = getIntent().getIntExtra("level",1);
        Log.d("lvlcourse", Integer.toString(course));
        course = getIntent().getIntExtra("course",1);
        Log.d("courseinactivity", Integer.toString(course));
        toolbar = (Toolbar) findViewById(R.id.toolbar_beginner);
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        new GetFlag().execute();
        postTest = (Button) findViewById(R.id.post_test);
        postTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(view.getContext(), PostTestActivity.class);
                intent.putExtra("TITLE", getIntent().getStringExtra("TITLE"));
                intent.putExtra("course", course);
                intent.putExtra("level", getIntent().getIntExtra("level",1));
                view.getContext().startActivity(intent);
                //finish();
            }
        });

        YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize("AIzaSyBBU7IRY2bCZot6cNO6xcEYYTy0hREDnqE", this);
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
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            youtube_vid.execute(player);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = "error";
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
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
            WebService webService = new WebService("http://learnit-database.esy.es/flag_test.php?username="+username+"&type=2&id="+level,"GET", "");
//            WebService webService = new WebService("http://10.0.2.2/android/flag_test.php?username="+username+"&type=2&id="+level,"GET", "");
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
            webService = new WebService("http://learnit-database.esy.es/get_course_details.php?course_id="+course,"GET", "");
            jsonString = webService.responseBody;
            try{
                JSONObject obj =new JSONObject(jsonString);
                info = obj.getString("info");
                summary = obj.getString("summary");
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
            layout.setVisibility(View.GONE);
            info_text.setText(info);
            summary_text.setText(summary);
            if(hashMaps.get(0).get("success").equals("1"))
            {
                flag1 = Integer.parseInt(hashMaps.get(0).get("flag1"));
                flag2 = Integer.parseInt(hashMaps.get(0).get("flag2"));
                flag3 = Integer.parseInt(hashMaps.get(0).get("flag3"));
            }
            if(level == 1) {
                if (course == 1 && flag1 != 1) postTest.setVisibility(View.VISIBLE);
                if (course == 2 && flag2 != 1) postTest.setVisibility(View.VISIBLE);
                if (course == 3 && flag3 != 1) postTest.setVisibility(View.VISIBLE);
            }
            else if(level == 2)
            {
                if (course == 4 && flag1 != 1) postTest.setVisibility(View.VISIBLE);
                if (course == 5 && flag2 != 1) postTest.setVisibility(View.VISIBLE);
                if (course == 6 && flag3 != 1) postTest.setVisibility(View.VISIBLE);
            }
            else{
                if (course == 7 && flag1 != 1) postTest.setVisibility(View.VISIBLE);
                if (course == 8 && flag2 != 1) postTest.setVisibility(View.VISIBLE);
                if (course == 9 && flag3 != 1) postTest.setVisibility(View.VISIBLE);
            }
        }
    }

    class GetVideo extends AsyncTask<YouTubePlayer,Void,Void>{
        @Override
        protected Void doInBackground(YouTubePlayer... params) {
            WebService webService = new WebService("http://learnit-database.esy.es/get_course_details.php?course_id="+course,"GET", "");
            String jsonString = webService.responseBody;
            YouTubePlayer player=params[0];
            try{
                JSONObject obj =new JSONObject(jsonString);
                video = obj.getString("video");
                player.cueVideo(video);
                player.play();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }


}
