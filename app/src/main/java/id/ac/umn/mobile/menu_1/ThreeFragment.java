package id.ac.umn.mobile.menu_1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ThreeFragment extends Fragment {
    private List<Leaderboard> leaderboardList;
    private View rootView;

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_three, container, false);
        new GetAllScore().execute();


        return rootView;
    }

    private void initializeData()
    {

    }

    class GetAllScore extends AsyncTask<Void, Void, ArrayList<Leaderboard>>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Leaderboard> leaderboards) {

            super.onPostExecute(leaderboards);
            progressDialog.dismiss();
            leaderboardList = leaderboards;
            RecyclerView rv= (RecyclerView) rootView.findViewById(R.id.rv);
            rv.setHasFixedSize(true);

            initializeData();

            RVLeaderboardAdapter adapter = new RVLeaderboardAdapter(leaderboardList);
            rv.setAdapter(adapter);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);

        }

        @Override
        protected ArrayList<Leaderboard> doInBackground(Void... voids) {
            WebService webService = new WebService("http://learnit-database.000webhostapp.com/get_all_scores.php","GET", "");
            String jsonString = webService.responseBody;
            //Log.d("result", jsonString);
            ArrayList<Leaderboard> arr = new ArrayList<>();
            try
            {
                JSONArray jsonArray = new JSONArray(jsonString);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject questObj = jsonArray.getJSONObject(i);
                    Leaderboard q = new Leaderboard(questObj.getString("username"),
                            questObj.getString("score"),
                            questObj.getString("current_level"));

                    arr.add(q);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            Log.d("size","size " + arr.size());
            return arr;
        }
    }

}
