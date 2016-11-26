package id.ac.umn.mobile.menu_1;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PosttestResultFragment extends Fragment {

    private int score,course,level;
    private String username;
    private Bundle data;
    public PosttestResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_posttest_result, container, false);
        data = getArguments();
        score = getArguments().getInt("score");
        course = data.getInt("course");
        username = data.getString("username");
        level = data.getInt("level");
        new SaveScore().execute();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView message = (TextView) getActivity().findViewById(R.id.message);
        message.setText(message.getText().toString() + score );
        Button start = (Button) getView().findViewById(R.id.view_course);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), BeginnerActivity.class);
            intent.putExtra("LVL",level );
            startActivity(intent);
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
            /*WebService webService = new WebService("http://learnit-database.000webhostapp.com/update_score.php?username="+username+"&score="+score,"GET", "");
            String jsonString = webService.responseBody;*/
            WebService webService = new WebService("http://learnit-database.000webhostapp.com/update_flag.php?username="+username+"&type=2&id="+course+"&score="+score,"GET", "");
            String jsonString = webService.responseBody;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
