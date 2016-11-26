package id.ac.umn.mobile.menu_1;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment {
    private String username, level, fullname, score, image;
    private int current_level;
    private TextView username_text, level_text, fullname_text, score_text;
    private ImageView mImage;
    private LinearLayout layout;

    public TwoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_two, container, false);

        Bundle data = getArguments();
        username = data.getString("username");
        current_level = data.getInt("current_level");

        layout  = (LinearLayout) rootView.findViewById(R.id.progressbar_view_profile);

        // create bitmap from resource
        mImage = (ImageView) rootView.findViewById(R.id.user_profile_photo);
        username_text = (TextView) rootView.findViewById(R.id.user_profile_name);
        level_text = (TextView) rootView.findViewById(R.id.user_profile_level);
        fullname_text = (TextView) rootView.findViewById(R.id.full_name);
        score_text = (TextView) rootView.findViewById(R.id.current_score);

        username_text.setText(username);

        if(current_level == 1) {
            level = "Beginner";
        }
        else if(current_level == 2) {
            level ="Intermediate";
        }
        else{
            level="Advanced";
        }
        level_text.setText(level);

        new GetProfileDetails().execute();

        return rootView;
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    class GetProfileDetails extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void a) {
            layout.setVisibility(View.GONE);
            //Set image
            int imageid = getResources().getIdentifier(image, "drawable", "id.ac.umn.mobile.menu_1");
            Bitmap bm = BitmapFactory.decodeResource(getResources(), imageid);
            mImage.setImageBitmap(getCircleBitmap(bm));
            //Set fullname & score
            fullname_text.setText(fullname);
            score_text.setText(score);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            WebService webService = new WebService("https://learnit-database.000webhostapp.com/get_user_details.php?username="+username,"GET", "");
            String jsonString = webService.responseBody;
            try
            {
                JSONArray profileArray = new JSONArray(jsonString);
                for (int i = 0; i < profileArray.length(); i++) {
                    JSONObject obj = profileArray.getJSONObject(i);
                    fullname = obj.getString("fullname");
                    score = obj.getString("score");
                    image = obj.getString("image");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

}
