package id.ac.umn.mobile.menu_1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment {
    private String username, level;
    private int current_level;

    public TwoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_two, container, false);

        Bundle data = getArguments();
        username = data.getString("username");
        current_level = data.getInt("current_level");

        TextView username_text = (TextView) rootView.findViewById(R.id.user_profile_name);
        username_text.setText(username);

        TextView level_text = (TextView) rootView.findViewById(R.id.user_profile_level);
        if(current_level == 1) {
            level = "Beginner";
        }
        else if(current_level == 2) {
            level ="Intermediate";
        }
        else{
            level="Advanced";
        }
        level_text.setText(level);

        return rootView;
    }

}
