package id.ac.umn.mobile.menu_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

public class OneFragment extends Fragment {
    private List<TutorialLevel> levels;
    private int current_level;
    public OneFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        RecyclerView rv= (RecyclerView) rootView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        Bundle data = getArguments();

        current_level = data.getInt("current_level");

        initializeData();

        RVAdapter adapter = new RVAdapter(levels,current_level);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    private void initializeData(){
        levels = new ArrayList<>();
        if(current_level == 1)
        {
            levels.add(new TutorialLevel("Beginner", "Understand very basic concept of Computer Science.", R.drawable.level_icon));
            levels.add(new TutorialLevel("Intermediate", "Understand more features of Computer Science.", R.drawable.lock));
            levels.add(new TutorialLevel("Advanced", "Can utilize Computer Science into more advanced features.", R.drawable.lock));
            levels.add(new TutorialLevel("Try Out", "Test your skill.", R.drawable.lock));
        }
        else if(current_level == 2)
        {
            levels.add(new TutorialLevel("Beginner", "Understand very basic concept of Computer Science.", R.drawable.level_icon));
            levels.add(new TutorialLevel("Intermediate", "Understand more features of Computer Science.", R.drawable.level_icon));
            levels.add(new TutorialLevel("Advanced", "Can utilize Computer Science into more advanced features.", R.drawable.lock));
            levels.add(new TutorialLevel("Try Out", "Test your skill.", R.drawable.lock));
        }
        else if(current_level == 3)
        {
            levels.add(new TutorialLevel("Beginner", "Understand very basic concept of Computer Science.", R.drawable.level_icon));
            levels.add(new TutorialLevel("Intermediate", "Understand more features of Computer Science.", R.drawable.level_icon));
            levels.add(new TutorialLevel("Advanced", "Can utilize Computer Science into more advanced features.", R.drawable.level_icon));
            levels.add(new TutorialLevel("Try Out", "Test your skill.", R.drawable.lock));
        }
        else
        {
            levels.add(new TutorialLevel("Beginner", "Understand very basic concept of Computer Science.", R.drawable.level_icon));
            levels.add(new TutorialLevel("Intermediate", "Understand more features of Computer Science.", R.drawable.level_icon));
            levels.add(new TutorialLevel("Advanced", "Can utilize Computer Science into more advanced features.", R.drawable.level_icon));
            levels.add(new TutorialLevel("Try Out", "Test your skill.", R.drawable.level_icon));
        }
    }
}
