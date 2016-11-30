package id.ac.umn.mobile.menu_1;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LevelTestResultFragment extends Fragment {

    private int score,level;
    private String username;
    private Bundle data;
    ImageView icon;
    TextView message,message1;
    Button btn,start;

    public LevelTestResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_level_test_result, container, false);
        data = getArguments();
        score = getArguments().getInt("score");
        username = data.getString("username");
        level = data.getInt("level");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        icon = (ImageView) getView().findViewById(R.id.icon);
        message1 = (TextView) getView().findViewById(R.id.message_1);
        btn = (Button) getView().findViewById(R.id.view_course);
        message = (TextView) getView().findViewById(R.id.message);
        start = (Button) getView().findViewById(R.id.view_course);

        message.setText(message.getText().toString() + score );
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(start.getText().equals("Learn Again"))
            {
                Intent intent = new Intent(view.getContext(), BeginnerActivity.class);
                intent.putExtra("LVL",level );
                startActivity(intent);
                getActivity().finish();
            }
            else
            {
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
            }
        });

        if(score>=60) {level++;new SaveScore().execute();}
        else
        {
            icon.setImageResource(R.drawable.cross_check);
            message1.setText("Press Button Below to Learn Again");
            btn.setText("Learn Again");
        }

    }

    class SaveScore extends AsyncTask<Void, Void, Void>
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
        protected Void doInBackground(Void... voids) {
            int prev_lvl = level-1;
            WebService webService = new WebService("http://learnit-database.esy.es/update_flag.php?username="+username+"&type=3&id="+prev_lvl+"&score="+score,"GET", "");
            String jsonString = webService.responseBody;
            Log.d("level",Integer.toString(level));
            Log.d("unane",username);
            WebService webService2 = new WebService("http://learnit-database.esy.es/update_current_level.php?username="+username+"&level="+level,"GET", "");
            jsonString = webService2.responseBody;
            Log.d("res",jsonString);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            SharedPreferences pref = getActivity().getSharedPreferences("LOGIN_PREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("CURRENT_LEVEL", Integer.toString(level));
            editor.commit();
        }
    }
}
