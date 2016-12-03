package id.ac.umn.mobile.menu_1;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    private NotesAdapter notesAdapter;
    private List<Notes> notesList2 = new ArrayList<>();
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notes);
        if (toolbar != null) {
            toolbar.setTitle("About");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        recyclerView=(RecyclerView)findViewById(R.id.rv_notes);

        notesAdapter = new NotesAdapter(notesList2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notesAdapter);


        prepareNotes();
    }

    private void prepareNotes() {
        Notes n=new Notes("1","my course","mynote","the date");
        notesList2.add(n);
         n=new Notes("2","my course2","mynote2","the date2");
        notesList2.add(n);
        n=new Notes("2","my course2","mynote2","the date2");
        notesList2.add(n);
        notesAdapter.notifyDataSetChanged();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetNotes extends AsyncTask<String,Void,JSONArray>{

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);

        }

        @Override
        protected JSONArray doInBackground(String... params) {
            SharedPreferences pref
                    = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
            WebService webService = new WebService("http://learnit-database.esy.es/get_notes.php?username="+pref.getString("USERNAME",""),"GET", "");
            String jsonString = webService.responseBody;
            Log.d("result", jsonString);
            try
            {
                JSONArray notes = new JSONArray(jsonString);
                return notes;
//                for (int i = 0; i < profileArray.length(); i++) {
//                    JSONObject obj = profileArray.getJSONObject(i);
//                    fullname = obj.getString("fullname");
//                    score = obj.getInt("score");
//                    image = obj.getString("image");
//                    time_spent = obj.getInt("time_spent");
//                    //image = image.replaceAll("\\\\", "");
//                    Log.e("ABDEL",image);
//                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
