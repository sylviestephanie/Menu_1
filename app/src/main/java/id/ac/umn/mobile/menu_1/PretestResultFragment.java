package id.ac.umn.mobile.menu_1;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PretestResultFragment extends Fragment {

    private int score;
    public PretestResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pretest_result, container, false);

        score = getArguments().getInt("score");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView message = (TextView) getActivity().findViewById(R.id.message);
        message.setText(message.getText().toString() + score );
        Button start = (Button) getView().findViewById(R.id.view_course);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CourseActivity.class);
                intent.putExtra("TITLE", getArguments().getString("title"));
                view.getContext().startActivity(intent);
            }
        });
    }
}
