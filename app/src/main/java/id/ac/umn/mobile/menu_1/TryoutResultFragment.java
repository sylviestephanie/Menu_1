package id.ac.umn.mobile.menu_1;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TryoutResultFragment extends Fragment {
    private int score,level;
    private String username;
    private Bundle data;
    ImageView icon;
    TextView message,message1;
    Button btn,start;


    public TryoutResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tryout_result, container, false);
        data = getArguments();
        score = getArguments().getInt("score");
        username = data.getString("username");
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

        if(score <= 50)
        {
            message1.setText("Oops! Try Again Later");
            icon.setImageResource(R.drawable.sad);
        }
        else
        {
            new SaveScore().execute();
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
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
            WebService webService = new WebService("http://learnit-database.esy.es/update_try_out_score.php?username="+username+"&score="+score,"GET", "");
            String jsonString = webService.responseBody;
            webService = new WebService("http://learnit-database.esy.es/update_time.php?username="+username+"&time=100","GET", "");
            jsonString = webService.responseBody;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
