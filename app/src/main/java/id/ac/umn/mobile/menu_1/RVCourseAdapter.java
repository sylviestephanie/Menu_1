package id.ac.umn.mobile.menu_1;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        Button btnView;
        CourseViewHolder(View itemView) {
            super(itemView);
            cvBeginner = (CardView)itemView.findViewById(R.id.cv_beginner);

            cardTitle = (TextView)itemView.findViewById(R.id.card_title);
            cardDesc = (TextView)itemView.findViewById(R.id.card_desc);
            cardImage = (ImageView)itemView.findViewById(R.id.card_image);
            btnView=(Button)itemView.findViewById(R.id.action_button);
        }
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_beginner, viewGroup, false);
        CourseViewHolder cvh = new CourseViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final CourseViewHolder cardViewHolder, final int i) {
       cardViewHolder.cardTitle.setText(courses.get(i).title);
        cardViewHolder.cardDesc.setText(courses.get(i).desc);
        cardViewHolder.cardImage.setImageResource(courses.get(i).image);
        if(courses.get(i).flag == 1) cardViewHolder.btnView.setText("View");
        cardViewHolder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardViewHolder.btnView.getText().toString().equals("View")) {
                    cardViewHolder.cardDesc.setText("read");
                    Intent intent = new Intent(view.getContext(), CourseActivity.class);
                    intent.putExtra("TITLE", cardViewHolder.cardTitle.getText().toString());
                    int c = 0;
                    if(courses.get(i).getLevel() == 1)
                    {
                        c=i+1;
                    }
                    else if(courses.get(i).getLevel() == 2)
                    {
                        c = i+4;
                    }
                    else c=i+7;
                    intent.putExtra("course", c);
                    intent.putExtra("level", courses.get(i).getLevel());
                    view.getContext().startActivity(intent);
                }
                else
                {
                    Intent intent;
                    intent = new Intent(view.getContext(), PreTestActivity.class);
                    intent.putExtra("TITLE", cardViewHolder.cardTitle.getText().toString());
                    intent.putExtra("course", i+1);
                    intent.putExtra("level", courses.get(i).getLevel());
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }


}
