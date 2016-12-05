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

import io.realm.Realm;
import io.realm.RealmResults;
import kiranamegatara.com.kipas.Controller.ExpendableListAdapter;
import kiranamegatara.com.kipas.Model.LoginUser;
import kiranamegatara.com.kipas.Model.SrtJalan;
import kiranamegatara.com.kipas.Model.SrtJalanModel;
import kiranamegatara.com.kipas.Model.SuratJalan;
import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.RealmHelper;
import kiranamegatara.com.kipas.Controller.SessionManager;

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
    String pabrik;
    Realm realm,getRealm;

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

        session = new SessionManager(getContext().getApplicationContext());

        //realm = Realm.getDefaultInstance();
        //realmHelper = new RealmHelper(getContext());
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
        //realmHelper = new RealmHelper(getContext());

        session = new SessionManager(getContext().getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String plant1 = user.get(SessionManager.keyPlant);
        //String plant = "DWJ1";

        Log.d("plant dari session",""+ pabrik);
       // Log.d("plant dari realm",""+ pabrik);

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("plant_code",plant1);

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
                                final String nosurat =b.getString("srt_jln_no");
                                final String plant = b.getString("plant_code");
                                final String gudang = b.getString("warehouse_code");
                                final String fullname = b.getString("user_full_name");
                                final String is_scaned = b.getString("is_scaned");
                                final String date_scaned = b.getString("date_scaned");
                                final String date_received = b.getString("date_received");
                                Log.d("scan",""+is_scaned);
                                Log.d("dari database",""+ b.getString("is_scaned"));
                                //realmHelper.addBarcode(nosurat,plant,gudang,fullname,is_scaned,date_scaned,date_received);

                                //realm.beginTransaction();
                                realm.executeTransaction(new Realm.Transaction(){
                                    @Override
                                    public void execute(Realm realm) {
                                        SrtJalan srtJalan = realm.createObject(SrtJalan.class);
                                        srtJalan.setNosurat(nosurat);
                                        srtJalan.setPlant(plant);
                                        srtJalan.setGudang(gudang);
                                        srtJalan.setFullname(fullname);
                                        srtJalan.setIs_scaned(is_scaned);
                                        srtJalan.setDate_scaned(date_scaned);
                                        srtJalan.setDate_received(date_received);
                                    }
                                });
                                //realm.commitTransaction();
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


        // preparing list data
        //prepareListData();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        //String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_his_sj";
        String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/get_his_sj";
        getRealm = Realm.getDefaultInstance();
/*
        session = new SessionManager(getContext().getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String pabriksesi = "";
        pabriksesi = user.get(SessionManager.keyPlant);

        RealmResults<LoginUser> users = realm.where(LoginUser.class).findAll();
        for (int i = 0; i < users.size();i++){
            pabriksesi = users.get(i).getPlant();
        }
        getRealm = Realm.getDefaultInstance();

        Log.d("plant dari sesi",""+ pabriksesi);

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("plant_code",pabriksesi);

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
                                Log.d("masuk","masuk loop");
                                JSONObject b = jsonarray.getJSONObject(i);
                                final String nosurat =b.getString("srt_jln_no");
                                final String plant = b.getString("plant_code");
                                final String gudang = b.getString("warehouse_code");
                                final String fullname = b.getString("user_full_name");
                                final String is_scaned = b.getString("is_scaned");
                                final String date_scaned = b.getString("date_scaned");
                                final String date_received = b.getString("date_received");
                                final String date_sent = b.getString("date_sent");
                                final String polisi_no = b.getString("polisi_no");
                                //realmHelper.addBarcode(nosurat,plant,gudang,fullname,is_scaned,date_scaned,date_received);
                                Log.d("is scaned history",i + ": "+ b.getString("is_scaned")+ nosurat);

                                getRealm.executeTransaction(new Realm.Transaction(){
                                    @Override
                                    public void execute(Realm realm) {
                                        SrtJalan srtJalan = getRealm.createObject(SrtJalan.class);
                                        srtJalan.setNosurat(nosurat);
                                        srtJalan.setPlant(plant);
                                        srtJalan.setGudang(gudang);
                                        srtJalan.setFullname(fullname);
                                        srtJalan.setIs_scaned(is_scaned);
                                        srtJalan.setDate_scaned(date_scaned);
                                        srtJalan.setDate_received(date_received);
                                        srtJalan.setDate_sent(date_sent);
                                        srtJalan.setPolisi_no(polisi_no);
                                    }
                                });

                                /*
                                listDataHeader.add(nosurat);
                                List<String> detail = new ArrayList<String>();
                                detail.add("Plant: "+plant);
                                detail.add("Gudang: "+gudang);
                                detail.add("Tanggal Kirim: "+date_sent);
                                detail.add("No Polisi: "+polisi_no);
                                detail.add("Tanggal Terima: "+ date_received);
                                detail.add("Tanggal Scan: "+ date_scaned);
                                Log.d("Plant: ",""+plant);
                                Log.d("Gudang: ",""+gudang);
                                Log.d("Tanggal Kirim: ",""+date_sent);
                                Log.d("No Polisi: ",""+polisi_no);
                                Log.d("Tanggal Terima: ",""+ date_received);
                                Log.d("Tanggal Scan: ",""+ date_scaned);
                                listDataChild.put(listDataHeader.get(i),detail);

                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        */
        RealmResults<SuratJalan> results = getRealm.where(SuratJalan.class)
                                        //.notEqualTo("date_scaned","0000-00-00 00:00:00")
                                        .findAll();
        Log.d("isi realm",""+ results.size());
        for (int i = 0; i < results.size(); i++){
            listDataHeader.add(results.get(i).getNosurat());
            Log.d("no surat",""+ results.get(i).getNosurat());
            List<String> detail = new ArrayList<String>();
            detail.add("Plant: "+results.get(i).getPlant());
            detail.add("Gudang: "+results.get(i).getGudang());
            detail.add("Tanggal Kirim: "+results.get(i).getDate_sent());
            detail.add("No Polisi: "+results.get(i).getPolisi_no());
            String tglTerima = results.get(i).getDate_received();
            detail.add("Tanggal Terima: "+ tglTerima.substring(0,10));
            String tglScan = results.get(i).getDate_scaned();
            detail.add("Tanggal Scan: "+ tglScan.substring(0,10));
            listDataChild.put(listDataHeader.get(i), detail);
        }

        Log.d("jumlah header",":"+listDataHeader.size());
        Log.d("jumlah detail",":"+listDataChild.size());
        listAdapter = new ExpendableListAdapter(getContext(),listDataHeader,listDataChild);


        // setting list adapter
        expListView.setAdapter(listAdapter);



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
