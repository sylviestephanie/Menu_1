package id.ac.umn.mobile.menu_1;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sylviestephanie on 11/24/16.
 */
public class RVCourseAdapter extends RecyclerView.Adapter<RVCourseAdapter.CourseViewHolder>{
    private List<TutorialCourse> courses;

    RVCourseAdapter(List<TutorialCourse> courses){
        this.courses = courses;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        CardView cvBeginner;
        TextView cardTitle;
        TextView cardDesc;
        ImageView cardImage;

        CourseViewHolder(View itemView) {
            super(itemView);
            cvBeginner = (CardView)itemView.findViewById(R.id.cv_beginner);

            cardTitle = (TextView)itemView.findViewById(R.id.card_title);
            cardDesc = (TextView)itemView.findViewById(R.id.card_desc);
            cardImage = (ImageView)itemView.findViewById(R.id.card_image);
        }
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_beginner, viewGroup, false);
        CourseViewHolder cvh = new CourseViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CourseViewHolder cardViewHolder, final int i) {

        cardViewHolder.cardTitle.setText(courses.get(i).title);
        cardViewHolder.cardDesc.setText(courses.get(i).desc);
        cardViewHolder.cardImage.setImageResource(courses.get(i).image);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }


}
