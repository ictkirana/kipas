package kiranamegatara.com.kipas.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

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
import kiranamegatara.com.kipas.Controller.RealmHelper;
import kiranamegatara.com.kipas.Controller.SessionManager;
import kiranamegatara.com.kipas.Model.LoginUser;
import kiranamegatara.com.kipas.Model.SrtJalan;
import kiranamegatara.com.kipas.Model.SrtJalanModel;
import kiranamegatara.com.kipas.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Outstanding.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Outstanding#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Outstanding extends Fragment {
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
    Realm realm,getRealm;

    public Outstanding() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Outstanding.
     */
    // TODO: Rename and change types and number of parameters
    public static Outstanding newInstance(String param1, String param2) {
        Outstanding fragment = new Outstanding();
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
//        realmHelper = new RealmHelper(getContext());
        //get realm instance
        //this.realm = RealmController.with(this).getRealm();
        realm = Realm.getDefaultInstance();
        aQuery = new AQuery(getContext());
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_outs_sj";
        String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/get_outs_sj";


        session = new SessionManager(getContext().getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String plant = user.get(SessionManager.keyPlant);

        HashMap<String,String> params = new HashMap<String, String>();

        RealmResults<LoginUser> users = realm.where(LoginUser.class).findAll();
        String pabrik = "";
        for (int i = 0; i < users.size();i++){
            pabrik = users.get(i).getPlant();
        }
        getRealm = Realm.getDefaultInstance();

        params.put("plant_code",pabrik);
        Log.d("pabrik dari realm",pabrik);
        Log.d("plant dr session",""+plant);

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
                                //realmHelper.addBarcode(nosurat,plant);

                                Log.d("is_scaned",i + ": "+ b.getString("is_scaned"));
                                //realm.beginTransaction();
                                getRealm.executeTransaction(new Realm.Transaction(){
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
                                        srtJalan.setDate_sent(date_sent);
                                        srtJalan.setPolisi_no(polisi_no);
                                    }
                                });
                                //realm.commitTransaction();

                                listDataHeader.add(nosurat);
                                List<String> detail = new ArrayList<String>();
                                detail.add(plant);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_outstanding, container, false);
        expListView = (ExpandableListView)view.findViewById(R.id.lvExpOut);

        // preparing list data
        //prepareListData();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_outs_sj";
        String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/get_outs_sj";


        session = new SessionManager(getContext().getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String plant = user.get(SessionManager.keyPlant);

        HashMap<String,String> params = new HashMap<String, String>();

        RealmResults<LoginUser> users = realm.where(LoginUser.class).findAll();
        String pabrik = "";
        for (int i = 0; i < users.size();i++){
            pabrik = users.get(i).getPlant();
        }
        getRealm = Realm.getDefaultInstance();

        params.put("plant_code",pabrik);
        Log.d("pabrik dari realm",pabrik);
        Log.d("plant dr session",""+plant);

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
                                //realmHelper.addBarcode(nosurat,plant);

                                //realm.beginTransaction();
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
                                //realm.commitTransaction();

                                listDataHeader.add(nosurat);
                                List<String> detail = new ArrayList<String>();
                                detail.add("Plant: "+plant);
                                detail.add("Gudang: "+gudang);
                                detail.add("Tanggal Kirim: "+date_sent);
                                detail.add("No Polisi: "+polisi_no);
                                listDataChild.put(listDataHeader.get(i),detail);
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        RealmResults<SrtJalan> realmResults = getRealm.where(SrtJalan.class).findAll();
        Log.d("isi realm",""+realmResults.size());
        for (int i = 0; i < realmResults.size(); i++){
            listDataHeader.add(realmResults.get(i).getNosurat());
            List<String> detail = new ArrayList<String>();
            detail.add("Plant: "+realmResults.get(i).getPlant());
            detail.add("Gudang: "+realmResults.get(i).getGudang());
            detail.add("Tanggal Kirim: "+realmResults.get(i).getDate_sent());
            detail.add("No Polisi: "+realmResults.get(i).getPolisi_no());
            listDataChild.put(listDataHeader.get(i), detail);
        }
        /*
        try {
            data = realmHelper.findAll();
        }catch (Exception e){

        }
        for (int i = 0; i < data.size(); i++){
            String scan = data.get(i).getIs_scaned();
            if (scan == "1") {
                listDataHeader.add(data.get(i).getNosurat());
                List<String> detail = new ArrayList<String>();
                detail.add(data.get(i).getPlant());
                detail.add(data.get(i).getGudang());
                listDataChild.put(listDataHeader.get(i), detail);
            }
        }
        */

        realmResults = getRealm.where(SrtJalan.class).findAll();
        getRealm.beginTransaction();
        realmResults.clear();
        getRealm.commitTransaction();

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

    /*
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
*/
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
