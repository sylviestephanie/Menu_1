package id.ac.umn.mobile.menu_1;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SoalFragment extends Fragment {

    private int qid = 0;
    private int score = 0;
    private int course;
    //private Question current_q;
    private Bundle data;
    /*private ArrayList<Question> arr;*/
    TextView soal, timer;
    RadioButton a;
    RadioButton b;
    RadioButton c;
    Button next;

    public SoalFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new GetQuestion().execute();
       /* Bundle data = this.getArguments();
        course = Integer.toString(data.getInt("course"));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_soal, container, false);
        data = getArguments();
        course = data.getInt("course");
        Toast.makeText(getActivity(),Integer.toString(data.getInt("course")), Toast.LENGTH_LONG).show();
        new GetQuestion().execute();
        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        new GetQuestion().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        //new GetQuestion().execute();


    }

    private void setQuestion(Question current_q)
    {
        soal.setText(current_q.getQUESTION());
        a.setText(current_q.getOPTA());
        b.setText(current_q.getOPTB());
        c.setText(current_q.getOPTC());
        qid++;
    }

    class GetQuestion extends AsyncTask<Void,Void,ArrayList<Question>> {
        ProgressDialog progressDialog;
        Question current_q;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected ArrayList<Question> doInBackground(Void... strings) {

            WebService webService = new WebService("http://learnit-database.000webhostapp.com/all_question.php?course="+course,"GET", "");
            String jsonString = webService.responseBody;
            Log.d("result", jsonString);
            ArrayList<Question> arr = new ArrayList<>();
            try
            {
                JSONArray jsonArray = new JSONArray(jsonString);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject questObj = jsonArray.getJSONObject(i);
                    Question q = new Question(questObj.getInt("id"),
                            questObj.getString("question"),
                            questObj.getString("opt_a"),
                            questObj.getString("opt_b"),
                            questObj.getString("opt_c"),
                            questObj.getString("answer"));

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

        @Override
        protected void onPostExecute(final ArrayList<Question> questions) {
            super.onPostExecute(questions);
            progressDialog.hide();
            Log.d("q_size","q " + questions.size());
            current_q = questions.get(qid);

            soal = (TextView) getActivity().findViewById(R.id.question);
            timer = (TextView) getActivity().findViewById(R.id.timer);
            a = (RadioButton) getActivity().findViewById(R.id.opt_a);
            b = (RadioButton) getActivity().findViewById(R.id.opt_b);
            c = (RadioButton) getActivity().findViewById(R.id.opt_c);
            final RadioGroup group = (RadioGroup) getActivity().findViewById(R.id.ans_group);
            next = (Button) getActivity().findViewById(R.id.next);

            setQuestion(current_q);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(group.getCheckedRadioButtonId() != -1) {
                        RadioButton answer = (RadioButton) getActivity().findViewById(group.getCheckedRadioButtonId());
                        if (current_q.getANSWER().equals(answer.getText().toString())) {
                            score++;
                            Log.d("score", "Your score" + score);
                            Toast.makeText(view.getContext(), "correct", Toast.LENGTH_LONG);
                        }

                        if (qid < 5) {
                            current_q = questions.get(qid);
                            setQuestion(current_q);
                        } else {
                            Toast.makeText(getActivity(), "aa", Toast.LENGTH_LONG);
                        }
                    }
                }
            });

            new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    timer.setText("done!");
                }
            }.start();
            //arr = questions;
        }
    }

}
