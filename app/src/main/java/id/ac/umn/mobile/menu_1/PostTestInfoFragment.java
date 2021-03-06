package id.ac.umn.mobile.menu_1;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostTestInfoFragment extends Fragment {

    private Bundle data;
    private int level,course,prev_level;
    private String title;

    public PostTestInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_post_test_info, container, false);
        data = getArguments();
        level = data.getInt("level");
        prev_level =data.getInt("lvl");
        course = data.getInt("course");
        title =  data.getString("title");
        /*Toast.makeText(getActivity(),Integer.toString(data.getInt("course")), Toast.LENGTH_LONG).show();*/
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Fragment self = this;

        Button start = (Button) getView().findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                SoalPostFragment fragment = new SoalPostFragment();
                fragment.setArguments(data);
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.commit();
            }
        });

        Button cancel = (Button) getView().findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent;
                intent = new Intent(view.getContext(), CourseActivity.class);
                intent.putExtra("TITLE", title);
                intent.putExtra("course", course);
//                intent.putExtra("level", prev_level);
                intent.putExtra("level", level);
                view.getContext().startActivity(intent);*/
                getActivity().finish();
            }
        });
    }
}
