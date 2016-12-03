package id.ac.umn.mobile.menu_1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abdelhaq on 03-Dec-16.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private List<Notes> noteList;

    public NotesAdapter(List<Notes> noteList){
        this.noteList=noteList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notes notes=noteList.get(position);
        holder.title.setText(notes.getCourse());
        holder.note.setText(notes.getNote());
        holder.date.setText(notes.getDate());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,note,date;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.title);
            note=(TextView) itemView.findViewById(R.id.note);
            date=(TextView) itemView.findViewById(R.id.date);
        }
    }

}
