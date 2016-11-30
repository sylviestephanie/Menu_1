package id.ac.umn.mobile.menu_1;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private String user,pass;
    private Boolean remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = (EditText) findViewById(R.id.username_txt);
        final EditText password = (EditText) findViewById(R.id.password_txt);
        final CheckBox rememberMeCheck = (CheckBox) findViewById(R.id.remember_me);
        final ImageView title = (ImageView) findViewById(R.id.login_title);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        Button login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username.getText().toString().equals("") || password.getText().toString().equals(""))
                {
                    if(username.getText().toString().equals("")) username.setError("Required");
                    if(password.getText().toString().equals("")) password.setError("Required");
                }
                else
                {
                    user = username.getText().toString();
                    pass = password.getText().toString();
                    remember = rememberMeCheck.isChecked();

                    new Verification().execute(String.format(
                            "username=%s&password=%s",
                            user,
                            pass
                    ));

                    username.setText("");
                    password.setText("");
                }

                //Toast.makeText(LoginActivity.this,password.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences pref = getSharedPreferences("LOGIN_DATA",MODE_PRIVATE); //utk di activity ini sendiri
        username.setText(pref.getString("USERNAME",""));
        password.setText(pref.getString("PASSWORD",""));
        rememberMeCheck.setChecked(pref.getBoolean("REMEMBER_ME", false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        if(!pref.getString("USERNAME", "").equals("")) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }
    }

    class Verification extends AsyncTask<String, Void, ArrayList<HashMap<String,String>>>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Verifying Data");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected ArrayList<HashMap<String,String>> doInBackground(String... strings) {
            String formParam = strings[0];
            WebService webService = new WebService("http://learnit-database.esy.es/login.php","POST", formParam);
//            WebService webService = new WebService("http://10.0.2.2/android/login.php","POST", formParam);
            String jsonString = webService.responseBody;
            
            ArrayList<HashMap<String, String>> result = new ArrayList<>();
            try
            {
                JSONObject obj = new JSONObject(jsonString);
                String success = obj.getString("success");
                String message = null;

                if(success.equals("1"))
                {
                    message = obj.getString("level");
                }
                else message = obj.getString("message");

                HashMap<String, String> map = new HashMap<>();
                map.put("success", success);
                map.put("body", message);

                result.add(map);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);
            progressDialog.dismiss();
            //Toast.makeText(LoginActivity.this,hashMaps.get(0).get("body"), Toast.LENGTH_SHORT).show();
            if(hashMaps.get(0).get("success").equals("1"))
            {
                SharedPreferences.Editor prefEdit
                        = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE).edit();
                prefEdit.putString("USERNAME", user);
                prefEdit.putString("CURRENT_LEVEL", hashMaps.get(0).get("body").toString());
                prefEdit.commit();

                SharedPreferences.Editor prefData
                        = getSharedPreferences("LOGIN_DATA", MODE_PRIVATE).edit();

                if(remember)
                {
                    prefData.putString("USERNAME", user);
                    prefData.putString("PASSWORD", pass);
                }
                else
                {
                    prefData.clear();
                }
                prefData.putBoolean("REMEMBER_ME", remember);
                prefData.commit();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                finish();
            }
            else
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                builder1.setMessage("Username and Password Invalid");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        }
    }
}
