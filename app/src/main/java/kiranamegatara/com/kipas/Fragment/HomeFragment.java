package kiranamegatara.com.kipas.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import kiranamegatara.com.kipas.Activity.LoginActivity;
import kiranamegatara.com.kipas.Activity.Main2Activity;
import kiranamegatara.com.kipas.Controller.BaseApp;
import kiranamegatara.com.kipas.Controller.ExpendableListAdapter;
import kiranamegatara.com.kipas.Controller.RealmHelper;
import kiranamegatara.com.kipas.Controller.SessionManager;
import kiranamegatara.com.kipas.Model.SrtJalanModel;
import kiranamegatara.com.kipas.Model.SuratJalanModel;
import kiranamegatara.com.kipas.R;

import static android.R.attr.data;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;
    AQuery aQuery;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private SessionManager session;
    RealmHelper realmHelper;
    private ArrayList<SrtJalanModel> data;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_history, container, false);
        final ExpandableListView expList = (ExpandableListView)view.findViewById(R.id.expSrtJalan);
        // preparing list data
        //prepareListData();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        realmHelper = new RealmHelper(getContext());

        aQuery = new AQuery(getContext());
        String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_outs_sj";

        session = new SessionManager(getContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String plant = user.get(SessionManager.keyPlant);

        HashMap<String,String> params = new HashMap<String, String>();
        final String pabrik = "DWJ1";
        params.put("plant_code",pabrik);
        Log.d("pabrik",pabrik);

        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("unduh data...");
        progress.setCancelable(false);
        progress.setIndeterminate(false);

        aQuery.progress(progress).ajax(url,params,String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null){
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        String hasil = jsonObject.getString("result");
                        String pesan = jsonObject.getString("msg");
                        //Toast.makeText(getContext(),hasil,Toast.LENGTH_LONG).show();
                        Log.d("surat_jalan","hasil " + hasil);

                        if (hasil.equalsIgnoreCase("true")){
                            JSONArray jsonarray = jsonObject.getJSONArray("data");
                            int length = jsonarray.length();
                            Log.d("jumlah surat jalan", "" + length);
                            Log.d("pesan",pesan);
                            for (int i = 0; i < jsonarray.length(); i++){
                                JSONObject b = jsonarray.getJSONObject(i);
                                String nosurat =b.getString("srt_jln_no");
                                String plant = b.getString("plant_code");
                                Log.d("plant_code",plant);
                                Log.d("srt_jnl_no",nosurat);
                                realmHelper.addBarcode(nosurat,plant);
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        data = new ArrayList<>();
        try {
            data = realmHelper.findAll();
        }catch (Exception e){

        }
        String plnt = "";
        List<String> detail = new ArrayList<String>();
        listDataHeader.add(pabrik);
        int jumlah = listDataHeader.size();
        int jumlah3 = data.size();
        Log.d("jumlah" , jumlah + " " + jumlah3);
        for (int i = 0;i < data.size(); i++){
            //if (data.get(i).getPlant() == plnt){
            detail.add(data.get(i).getNosurat());
            listDataChild.put(listDataHeader.get(i),detail);
            //}else{
            //    plnt = data.get(i).getPlant();
            //    listDataHeader.add(data.get(i).getPlant());
            //}
        }
        int jumlah2 = listDataChild.size();
        Log.d("jumlah" , jumlah + " " + jumlah2 + " " + jumlah3);
        listAdapter = new ExpendableListAdapter(getContext(),listDataHeader,listDataChild);

        // setting list adapter
        //expList.setAdapter(listAdapter);

        try {
            realmHelper.deleteRealm();
        }catch (Exception e){

        }
*/
        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        aQuery = new AQuery(getContext());
        String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_outs_sj";

        session = new SessionManager(getContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String plant = user.get(SessionManager.keyPlant);

        HashMap<String,String> params = new HashMap<String, String>();
        final String pabrik = "DWJ1";
        params.put("plant_code",pabrik);
        Log.d("pabrik",pabrik);

        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("unduh data...");
        progress.setCancelable(false);
        progress.setIndeterminate(false);

        aQuery.progress(progress).ajax(url,params,String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null){
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        String hasil = jsonObject.getString("result");
                        String pesan = jsonObject.getString("msg");
                        //Toast.makeText(getContext(),hasil,Toast.LENGTH_LONG).show();
                        Log.d("surat_jalan","hasil " + hasil);

                        if (hasil.equalsIgnoreCase("true")){
                            JSONArray jsonarray = jsonObject.getJSONArray("data");
                            int length = jsonarray.length();
                            Log.d("jumlah surat jalan", "" + length);
                            Log.d("pesan",pesan);
                            listDataHeader.add(pabrik);
                            for (int i = 0; i < jsonarray.length(); i++){
                                JSONObject b = jsonarray.getJSONObject(i);
                                List<String> detail = new ArrayList<String>();
                                String nosurat =b.getString("srt_jln_no");
                                Log.d("nosurat","" + nosurat);
                                detail.add(nosurat);
                                listDataChild.put(listDataHeader.get(i),detail);
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        @SuppressWarnings("StatementWithEmptyBody")
        boolean onNavigationItemSelected(MenuItem item);

        /*
                @SuppressWarnings("StatementWithEmptyBody")
                boolean onNavigationItemSelected(MenuItem item);
                */
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);


        public void onFragmentInteractionHome(Uri uri);
        public void openHome(View view);
    }
}
