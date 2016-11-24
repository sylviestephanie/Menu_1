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
            levels.add(new TutorialLevel("Beginner", "easy", R.drawable.ic_person_white_48dp));
            levels.add(new TutorialLevel("Intermediate", "medium", R.drawable.ic_https_white_48dp));
            levels.add(new TutorialLevel("Advanced", "hard", R.drawable.ic_https_white_48dp));
        }
        else if(current_level == 2)
        {
            levels.add(new TutorialLevel("Beginner", "easy", R.drawable.ic_person_white_48dp));
            levels.add(new TutorialLevel("Intermediate", "medium", R.drawable.ic_person_white_48dp));
            levels.add(new TutorialLevel("Advanced", "hard", R.drawable.ic_https_white_48dp));
        }
        else
        {
            levels.add(new TutorialLevel("Beginner", "easy", R.drawable.ic_person_white_48dp));
            levels.add(new TutorialLevel("Intermediate", "medium", R.drawable.ic_person_white_48dp));
            levels.add(new TutorialLevel("Advanced", "hard", R.drawable.ic_person_white_48dp));
        }
    }
}
