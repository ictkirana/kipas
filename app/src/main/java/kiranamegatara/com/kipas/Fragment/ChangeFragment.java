package kiranamegatara.com.kipas.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import kiranamegatara.com.kipas.Controller.SessionManager;
import kiranamegatara.com.kipas.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeFragment extends Fragment {
    SessionManager session;
    TextView txtemail;
    public ChangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change,null);
        txtemail= (TextView) view.findViewById(R.id.txtEmailUser);
        session = new SessionManager(getContext().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManager.keyEmail);
        txtemail.setText(email);
        return view;
    }

}
