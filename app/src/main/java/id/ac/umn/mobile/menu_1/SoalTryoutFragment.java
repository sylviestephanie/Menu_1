package id.ac.umn.mobile.menu_1;


import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
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
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class SoalTryoutFragment extends Fragment {

    int course = 0;
    private ArrayList<Question> arrQ;
    private int qid = 0, score = 0;
    CountDownTimer countDownTimer;
    boolean times_up = false, submitted = false;
    TextView soal, timer;
    RadioButton a,b,c;
    Button next;
    Bundle data;

    public SoalTryoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_soal_tryout, container, false);
        data = getArguments();
        Random r = new Random();
        course = r.nextInt(3 - 1) + 1;

        new GetQuestion().execute();
        return rootView;
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
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected ArrayList<Question> doInBackground(Void... strings) {

            WebService webService = new WebService("http://learnit-database.esy.es/all_question.php?course="+course+"&type=4","GET", "");

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
            progressDialog.dismiss();

            arrQ = questions;

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

                            if (qid < 10) {
                                if (qid == 9) next.setText("Submit");
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
                            }

                            submitted = true;
                            FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                            TryoutResultFragment resultFragment = new TryoutResultFragment();
                            data.putInt("score",score);
                            resultFragment.setArguments(data);
                            fragmentTransaction.replace(android.R.id.content, resultFragment);
                            fragmentTransaction.commit();
                        }
                    }

                }
            });

            countDownTimer = new CountDownTimer(100000, 1000) {

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

                                    dialog.dismiss();
                                    FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                                    TryoutResultFragment resultFragment = new TryoutResultFragment();
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
