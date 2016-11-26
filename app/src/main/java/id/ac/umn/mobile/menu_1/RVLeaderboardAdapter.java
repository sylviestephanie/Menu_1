package id.ac.umn.mobile.menu_1;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vannia Ferdina on 11/25/2016.
 */

public class RVLeaderboardAdapter extends RecyclerView.Adapter<RVLeaderboardAdapter.LeaderboardViewHolder>{

    private List<Leaderboard> leaderboards;
    RVLeaderboardAdapter(List<Leaderboard> leaderboards)
    {
        this.leaderboards = leaderboards;
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        CardView cvLeaderboard;
        TextView icon_entry;
        TextView name_entry;
        TextView level_entry;
        TextView score;

        LeaderboardViewHolder(View itemView) {
            super(itemView);
            cvLeaderboard = (CardView)itemView.findViewById(R.id.leaderboard_layout);

            icon_entry = (TextView)itemView.findViewById(R.id.icon_entry);
            name_entry = (TextView)itemView.findViewById(R.id.name_entry);
            level_entry = (TextView) itemView.findViewById(R.id.level_entry);
            score = (TextView) itemView.findViewById(R.id.score_txt);
        }
    }

    @Override
    public void onBindViewHolder(final LeaderboardViewHolder holder, final int i) {
        holder.icon_entry.setText(Integer.toString(i+1));
        holder.name_entry.setText(leaderboards.get(i).getUsername());
        holder.score.setText(leaderboards.get(i).getScore());
        if(leaderboards.get(i).getCurrent_level().equals("1")) { holder.level_entry.setText("Beginner"); }
        else if (leaderboards.get(i).getCurrent_level().equals("2")) holder.level_entry.setText("Intermediate");
        else holder.level_entry.setText("Advance");
    }

    @Override
    public LeaderboardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_leaderboard, viewGroup, false);
        LeaderboardViewHolder lvh = new LeaderboardViewHolder(v);
        return lvh;
    }

    @Override
    public int getItemCount() {
        return leaderboards.size();
    }


}
