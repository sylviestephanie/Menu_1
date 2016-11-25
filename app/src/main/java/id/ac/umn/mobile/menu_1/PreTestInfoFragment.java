package id.ac.umn.mobile.menu_1;


import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreTestInfoFragment extends Fragment {

    private Bundle data;

    public PreTestInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_pre_test_info, container, false);
        data = getArguments();
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
                SoalFragment fragment = new SoalFragment();
                fragment.setArguments(data);
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.commit();
            }
        });
    }
}
