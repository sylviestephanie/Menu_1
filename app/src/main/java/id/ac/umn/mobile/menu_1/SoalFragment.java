package id.ac.umn.mobile.menu_1;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
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
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SoalFragment extends Fragment {

    private int qid = 0;
    private int score = 0;
    private int course;
    //private Question current_q;
    private Bundle data;
    private ArrayList<Question> arrQ;
    boolean startTimer = true;
    TextView soal, timer;
    RadioButton a;
    RadioButton b;
    RadioButton c;
    Button next;
    CountDownTimer countDownTimer;
    boolean times_up = false;
    boolean submitted = false;
    final static long INTERVAL=1000;
    final static long TIMEOUT=10000;

    private String username="";

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Log.d("back","pressed");
                        return true;
                    }
                }
                return false;
            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_soal, container, false);
        data = getArguments();
        course = data.getInt("course");
        username = data.getString("username");
        Toast.makeText(getActivity(),Integer.toString(data.getInt("course")), Toast.LENGTH_LONG).show();
        new GetQuestion().execute();

        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        //new GetQuestion().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
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

            WebService webService = new WebService("http://learnit-database.esy.es/all_question.php?course="+course+"&type=1","GET", "");
//            WebService webService = new WebService("http://10.0.2.2/android/all_question.php?course="+course+"&type=1","GET", "");
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
            arrQ = questions;
            Log.d("qid","q " + qid);
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
                    if(next.getText().toString().equals("NEXT")) {
                        if (a.isChecked() || b.isChecked() || c.isChecked()) {
                            RadioButton answer = (RadioButton) getActivity().findViewById(group.getCheckedRadioButtonId());
                            if (current_q.getANSWER().equals(answer.getText().toString())) {
                                score+=10;
                                Log.d("score", "Your score" + score);
                                Toast.makeText(view.getContext(), "correct", Toast.LENGTH_LONG);
                            }

                            if (qid < 3) {
                                if (qid == 2) next.setText("Submit");
                                current_q = questions.get(qid);
                                setQuestion(current_q);
                            }
                            a.setChecked(false);
                            b.setChecked(false);
                            c.setChecked(false);
                        }
                    }
                    else
                    {
                        if (group.getCheckedRadioButtonId() != -1) {
                            RadioButton answer = (RadioButton) getActivity().findViewById(group.getCheckedRadioButtonId());
                            if (current_q.getANSWER().equals(answer.getText().toString())) {
                                score+=10;
                                Log.d("score", "Your score" + score);
                                Log.d("score", "submitted");
                            }

                            submitted = true;
                            FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                            PretestResultFragment resultFragment = new PretestResultFragment();
                            data.putInt("score",score);
                            resultFragment.setArguments(data);
                            fragmentTransaction.replace(android.R.id.content, resultFragment);
                            fragmentTransaction.commit();
                        }
                    }

                }
            });

            countDownTimer = new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    if(!submitted)
                    timer.setText(millisUntilFinished / 1000 + " s");
                    else cancel();
                }

                public void onFinish() {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setTitle("TIME'S UP");
                    builder1.setMessage("Your Score : " + score);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                    new SaveScore().execute();
                                    dialog.dismiss();
                                    FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                                    PretestResultFragment resultFragment = new PretestResultFragment();
                                    data.putInt("score",score);
                                    resultFragment.setArguments(data);
                                    fragmentTransaction.replace(android.R.id.content, resultFragment);
                                    fragmentTransaction.commit();
                                }
                            });

                    AlertDialog alert11 = builder1.create();

                    if(times_up == false) {alert11.show(); times_up = true;}
                }
            }.start();
        }


    }
}
