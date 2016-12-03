package id.ac.umn.mobile.menu_1;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by abdelhaq on 03-Dec-16.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList listItemList = new ArrayList();
    private Context context = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {
//        for (int i = 0; i < 2; i++) {
//            Notes listItem = new Notes("a","a","a","a");
//            listItemList.add(listItem);
//        }
        //new GetNotes().execute();

    }
    private class GetNotes extends AsyncTask<Void,Void,JSONArray> {

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            Log.e("selesai load","yeay");
            try{
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String id = obj.getString("id");
                    String course = obj.getString("course");
                    String note = obj.getString("note");
                    String date = obj.getString("date");
                    Notes n=new Notes(id,course,note,date);
                    listItemList.add(n);
                    Log.e("the notes",course+note);
                }
            }catch (Exception e){e.printStackTrace();}
//            notesAdapter.notifyDataSetChanged();

        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            SharedPreferences pref
                    = context.getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
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
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        JSONArray notes=null;
        SharedPreferences pref
                = context.getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        WebService webService = new WebService("http://learnit-database.esy.es/get_notes.php?username="+pref.getString("USERNAME",""),"GET", "");
        String jsonString = webService.responseBody;
        Log.d("result", jsonString);
        try
        {
            notes = new JSONArray(jsonString);

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

        try{
            for (int i = 0; i < notes.length(); i++) {
                JSONObject obj = notes.getJSONObject(i);
                String id = obj.getString("id");
                String course = obj.getString("course");
                String note = obj.getString("note");
                String date = obj.getString("date");
                Notes n=new Notes(id,course,note,date);
                listItemList.add(n);
                Log.e("the notes",course+note);
            }
        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /*
    *Similar to getView of Adapter where instead of View
    *we return RemoteViews
    *
    */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.notes_row);
        Notes listItem = (Notes)listItemList.get(position);
        remoteView.setTextViewText(R.id.title, listItem.getCourse());
        remoteView.setTextViewText(R.id.note, listItem.getNote());
        remoteView.setTextViewText(R.id.date, listItem.getDate());
        remoteView.setTextViewText(R.id.delete,"");

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
