package id.ac.umn.mobile.menu_1;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by sylviestephanie on 11/23/16.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.LevelViewHolder>{
    private List<TutorialLevel> levels;

    RVAdapter(List<TutorialLevel> levels){
        this.levels = levels;
    }

    public static class LevelViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView levelName;
        TextView levelDesc;
        ImageView levelIcon;

        LevelViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            levelName = (TextView)itemView.findViewById(R.id.level_name);
            levelDesc = (TextView)itemView.findViewById(R.id.level_desc);
            levelIcon = (ImageView)itemView.findViewById(R.id.level_icon);
        }
    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        LevelViewHolder pvh = new LevelViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(LevelViewHolder personViewHolder, final int i) {
        personViewHolder.levelName.setText(levels.get(i).title);
        personViewHolder.levelDesc.setText(levels.get(i).info);
        personViewHolder.levelIcon.setImageResource(levels.get(i).icon);


        personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                Intent intent;
                if(i == 0)
                    intent = new Intent(v.getContext(), BeginnerActivity.class);
                else if(i == 1)
                    intent = new Intent(v.getContext(), BeginnerActivity.class);
                else
                    intent = new Intent(v.getContext(), BeginnerActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

}
