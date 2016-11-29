package id.ac.umn.mobile.menu_1;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class TryoutInfoFragment extends Fragment {

    Bundle data;
    String username="";
    public TryoutInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tryout_info, container, false);
        data = getArguments();
        username = data.getString("username");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Button start = (Button) getView().findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                SoalTryoutFragment fragment = new SoalTryoutFragment();
                fragment.setArguments(data);
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.commit();
            }
        });

        Button cancel = (Button) getView().findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }
}
