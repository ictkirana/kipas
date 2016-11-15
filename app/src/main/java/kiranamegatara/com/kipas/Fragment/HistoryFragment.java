package kiranamegatara.com.kipas.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.HashMap;
import java.util.List;

import kiranamegatara.com.kipas.Controller.ExpendableListAdapter;
import kiranamegatara.com.kipas.Model.LoginUser;
import kiranamegatara.com.kipas.Model.LoginUserModel;
import kiranamegatara.com.kipas.Model.SrtJalanModel;
import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.RealmHelper;
import kiranamegatara.com.kipas.Controller.SessionManager;
import kiranamegatara.com.kipas.Model.SuratJalanModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;

    SessionManager session;
    AQuery aQuery;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    RealmHelper realmHelper;
    private ArrayList<SrtJalanModel> data;
    private ArrayList<LoginUserModel> usermodel;
    String pabrik;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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

        aQuery = new AQuery(getContext());

        session = new SessionManager(getContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        pabrik = user.get(SessionManager.keyPlant);

        // get the listview
        //expListView = (ExpandableListView);

        // preparing list data
        //prepareListData();

        //listAdapter = new ExpendableListAdapter(getContext(),listDataHeader,listDataChild);

        // setting list adapter
        //expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_his_sj";
        String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/get_his_sj";
        realmHelper = new RealmHelper(getContext());

        session = new SessionManager(getContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        //String plant = user.get(SessionManager.keyPlant);
        String plant = "DWJ1";
/*
        usermodel = new ArrayList<>();
        try {
            usermodel = realmHelper.findAllUser();
            for (int i = 0;i < usermodel.size(); i++){
            }
        }catch (Exception e){

        }
        String pabrik = usermodel.get(0).getPlant();
        */
        Log.d("plant dari session",""+ pabrik);
       // Log.d("plant dari realm",""+ pabrik);

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("plant_code",plant);

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
                        Toast.makeText(getContext(),hasil,Toast.LENGTH_LONG).show();
                        Log.d("surat_jalan","hasil" + hasil);

                        if (hasil.equalsIgnoreCase("true")){
                            JSONArray jsonarray = jsonObject.getJSONArray("data");
                            int length = jsonarray.length();
                            Log.d("jumlah surat jalan", "" + length);
                            Log.d("pesan",pesan);
                            for (int i = 0; i < jsonarray.length(); i++){
                                JSONObject b = jsonarray.getJSONObject(i);
                                String nosurat =b.getString("srt_jln_no");
                                String plant = b.getString("plant_code");
                                String gudang = b.getString("warehouse_code");
                                String fullname = b.getString("user_full_name");
                                String is_scaned = "1";
                                String date_scaned = b.getString("date_scaned");
                                String date_received = b.getString("date_received");
                                //realmHelper.addBarcode(nosurat,plant,gudang,fullname,is_scaned,date_scaned,date_received);
                                realmHelper.addBarcode(nosurat,plant);
                                //listDataHeader.add(nosurat);
                                //List<String> detail = new ArrayList<String>();
                                //detail.add(plant);
                                //listDataChild.put(listDataHeader.get(i),detail);
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_history, container, false);
        expListView = (ExpandableListView)view.findViewById(R.id.lvExp);


        try {
            realmHelper.deleteRealm();
        }catch (Exception e){

        }


        // preparing list data
        prepareListData();

        data = new ArrayList<>();
        try {
            data = realmHelper.findAll();
        }catch (Exception e){

        }

        for (int i = 0;i < data.size(); i++){
           // if (data.get(i).getIs_scaned() == "1" ) {
                listDataHeader.add(data.get(i).getNosurat());
                List<String> detail = new ArrayList<String>();
            //    detail.add(data.get(i).getFullname());
                detail.add(data.get(i).getPlant());
            //    detail.add(data.get(i).getGudang());
            //    detail.add(data.get(i).getDate_scaned());
            //    detail.add(data.get(i).getDate_received());
                listDataChild.put(listDataHeader.get(i), detail);
           // }
            //detail.clear();
        }
        int jumlah = listDataHeader.size();
        int jumlah2 = listDataChild.size();
        int jumlah3 = data.size();
        Log.d("jumlah" , jumlah + " " + jumlah2 + " " + jumlah3);

        listAdapter = new ExpendableListAdapter(getContext(),listDataHeader,listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
/*
        try {
            realmHelper.deleteRealm();
        }catch (Exception e){

        }
*/
        return view;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
