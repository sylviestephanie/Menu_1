package id.ac.umn.mobile.menu_1;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LevelTestInfoFragment extends Fragment {
    private Bundle data;
    private int level;

    public LevelTestInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_level_test_info, container, false);
        data = getArguments();
        level = data.getInt("level");
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();

        Button start = (Button) getView().findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                SoalLevelFragment fragment = new SoalLevelFragment();
                fragment.setArguments(data);
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.commit();
            }
        });

        Button cancel = (Button) getView().findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(view.getContext(), BeginnerActivity.class);
                intent.putExtra("LVL",level );
                startActivity(intent);*/
                getActivity().finish();
            }
        });

    }
}
