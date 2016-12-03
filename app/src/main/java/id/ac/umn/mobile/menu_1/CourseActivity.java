package id.ac.umn.mobile.menu_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CourseActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        Log.e("abdel","you call me?");
    }


    private String username="", info, summary, video;
    private TextView info_text, summary_text;
    private int level,course;
    int flag1,flag2,flag3, duration=0;
    Toolbar toolbar;
    private LinearLayout layout;
    Button postTest;
    private static final int RECOVERY_REQUEST = 1;
    private GetVideo youtube_vid;
    Intent intent;
    MenuItem saveToNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        youtube_vid=new GetVideo();
        layout  = (LinearLayout) findViewById(R.id.progressbar_view);
        final String title = getIntent().getStringExtra("TITLE");
        /*TextView tvTitle=(TextView)findViewById(R.id.title);*/
        info_text = (TextView) findViewById(R.id.info);
        summary_text = (TextView) findViewById(R.id.summary);
        /*tvTitle.setText(title);*/
        level = getIntent().getIntExtra("level",1);
        //Log.d("lvlcourse", Integer.toString(course));
        course = getIntent().getIntExtra("course",1);
        //Log.d("courseinactivity", Integer.toString(course));
        toolbar = (Toolbar) findViewById(R.id.toolbar_beginner);
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        intent = new Intent(CourseActivity.this, PostTestActivity.class);
        new GetFlag().execute();
        postTest = (Button) findViewById(R.id.post_test);
        postTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent;
                intent = new Intent(view.getContext(), PostTestActivity.class);*/
                intent.putExtra("TITLE", getIntent().getStringExtra("TITLE"));
                intent.putExtra("course", course);
                intent.putExtra("level", getIntent().getIntExtra("level",1));
                view.getContext().startActivity(intent);
                //finish();
            }
        });
        registerForContextMenu(summary_text);
        summary_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("hola","its a long click");
                return false;
            }
        });
        summary_text.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                saveToNotes= menu.add("Take notes");

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if(item.getItemId()==saveToNotes.getItemId()){
                    Log.e("YOU","wanna save something?");
                    Log.e("Here : ",getSelectedText(summary_text));

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String strDate = sdf.format(c.getTime());
                    Log.e("mydate",strDate);
                    SharedPreferences pref
                            = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
                    new SaveNotes().execute(
                            String.format(
                                    "username=%s&notes=%s&course=%s&date=%s",
                                    pref.getString("USERNAME",""),
                                    getSelectedText(summary_text),
                                    title,
                                    strDate

                                    )
                    );

                    Toast.makeText(CourseActivity.this, "Note taken : "+getSelectedText(summary_text), Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;


                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                Log.e("they call me","hiks");
            }
        });
        YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize("AIzaSyBBU7IRY2bCZot6cNO6xcEYYTy0hREDnqE", this);
    }
    private String getSelectedText(TextView tv) {
        String selectedText = "";
        int min = 0;
        int max = tv.getText().length();
        if (tv.isFocused()) {
            final int textStartIndex = tv.getSelectionStart();
            final int textEndIndex = tv.getSelectionEnd();

            min = Math.max(0, Math.min(textStartIndex, textEndIndex));
            max = Math.max(0, Math.max(textStartIndex, textEndIndex));
            selectedText = tv.getText().subSequence(min, max).toString().trim();
        }
        return selectedText;
        // Perform your
    }

    private class SaveNotes extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            WebService webService = new WebService("http://learnit-database.esy.es/new_notes.php","POST",params[0] );
//            WebService webService = new WebService("https://10.0.2.2/android/get_user_details.php?username="+username,"GET", "");
            String jsonString = webService.responseBody;
            Log.d("result", jsonString);
            return null;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, BeginnerActivity.class);
            intent.putExtra("LVL", level);
            NavUtils.navigateUpTo(this, intent); // reload course list activity to change button Take Pre-Test to View
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
                duration = player.getDurationMillis();
                intent.putExtra("DURATION", duration);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }


}
