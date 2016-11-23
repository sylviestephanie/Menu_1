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

    public OneFragment() {
        // Required empty public constructor
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

        initializeData();

        RVAdapter adapter = new RVAdapter(levels);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    private void initializeData(){
        levels = new ArrayList<>();
        levels.add(new TutorialLevel("Beginner", "easy", R.drawable.ic_person_white_48dp));
        levels.add(new TutorialLevel("Intermediate", "medium", R.drawable.ic_person_white_48dp));
        levels.add(new TutorialLevel("Advanced", "hard", R.drawable.ic_person_white_48dp));
    }
}
