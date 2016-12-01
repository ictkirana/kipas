package kiranamegatara.com.kipas.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;
import kiranamegatara.com.kipas.Fragment.HistoryFragment;
import kiranamegatara.com.kipas.Fragment.HomeFragment;
import kiranamegatara.com.kipas.Fragment.Outstanding;
import kiranamegatara.com.kipas.Model.LoginUser;
import kiranamegatara.com.kipas.Model.SrtJalan;
import kiranamegatara.com.kipas.Model.SuratJalan;
import kiranamegatara.com.kipas.Model.User;
import kiranamegatara.com.kipas.Other.CircleTransform;
import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.SessionManager;

public class Main2Activity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,HistoryFragment.OnFragmentInteractionListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private SessionManager session;
    private User user;
    final ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();

    private Timer timer;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "https://siva.jsstatic.com/id/20143/images/sol/20143_logo_0_11601.png";
    private static final String urlProfileImg = "https://yt3.ggpht.com/-E_pUlsSjalk/AAAAAAAAAAI/AAAAAAAAAAA/xuBNGaofFA0/s900-c-k-no-rj-c0xffffff/photo.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_OUTSTANDING = "outstanding";
    private static final String TAG_LOG_OUT = "logout";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    String id,full_name,pwd,mail,nik,company_code,plant,plant_code,
            plant_name,company_name,authorized_warehouse,is_active,is_kirana,
            is_reset,fail_counter,date_last_login,date_created,date_updated,
            is_deleted,dateCrt,dateUpd;
    Realm getRealm,realm,realmclear;
    AQuery aQuery,a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        session = new SessionManager(getApplicationContext());

        //final User user = new User();
        final Intent intent = getIntent();
        final String email = intent.getStringExtra("email");
        full_name = intent.getStringExtra("fullname");
        nik = intent.getStringExtra("nik");
        plant = intent.getStringExtra("plant");
        authorized_warehouse = intent.getStringExtra("gudang");
        //GetUser(mail,pass);

        //session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        aQuery = new AQuery(getApplicationContext());

        //mail = user.get(SessionManager.keyEmail);
        //plant = user.get(SessionManager.keyPlant);

        Log.d("mail",""+email);
        Log.d("plant",""+plant);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Main2Activity.this,ScanActivity.class);
                intent1.putExtra("email_user",email);
                intent1.putExtra("plant",plant);
                intent1.putExtra("fullname",full_name);
                intent1.putExtra("nik",nik);
                intent1.putExtra("gudang",authorized_warehouse);
                startActivity(intent1);
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        loadSuratJalan();


//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

    }

    private void loadSuratJalan() {
        realmclear = Realm.getDefaultInstance();
        RealmResults<SrtJalan> resultSrtJalan;
        RealmResults<SuratJalan> history;
        try {
            resultSrtJalan = realmclear.where(SrtJalan.class).findAll();
            realmclear.beginTransaction();
            resultSrtJalan.clear();
            realmclear.commitTransaction();
            history = realmclear.where(SuratJalan.class).findAll();
            realmclear.beginTransaction();
            history.clear();
            realmclear.commitTransaction();
        }catch (Exception e){

        }
        loadHistory();
        loadOutstanding();
    }

    private void loadHistory() {
        String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_his_sj";
        //String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/get_his_sj";


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String plant = user.get(SessionManager.keyPlant);

        HashMap<String,String> params = new HashMap<String, String>();
        getRealm = Realm.getDefaultInstance();
        params.put("plant_code",plant);
        Log.d("plant dr session",""+plant);

        ProgressDialog progress = new ProgressDialog(getApplicationContext());
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
                                Log.d("surat jalan",nosurat);
                                /*
                                getRealm.executeTransaction(new Realm.Transaction(){
                                    @Override
                                    public void execute(Realm realm) {
                                        SuratJalan srtJalan = getRealm.createObject(SuratJalan.class);
                                        srtJalan.setNosurat(nosurat);
                                        srtJalan.setPlant(plant);
                                        srtJalan.setGudang(gudang);
                                        srtJalan.setFullname(fullname);
                                        srtJalan.setIs_scaned(is_scaned);
                                        srtJalan.setDate_scaned(date_scaned);
                                        srtJalan.setDate_received(date_received);
                                        srtJalan.setDate_sent(date_sent);
                                        srtJalan.setPolisi_no(polisi_no);
                                        Log.d("masuk execute","success");
                                    }
                                }, new Realm.Transaction.Callback(){
                                    @Override
                                    public void onSuccess() {
                                        Log.d("after save"," "+ getRealm.allObjects(SuratJalan.class).size());
                                        super.onSuccess();
                                    }
                                });
                                */
                                getRealm.beginTransaction();
                                SuratJalan srtJalan = getRealm.createObject(SuratJalan.class);
                                srtJalan.setNosurat(nosurat);
                                srtJalan.setPlant(plant);
                                srtJalan.setGudang(gudang);
                                srtJalan.setFullname(fullname);
                                srtJalan.setIs_scaned(is_scaned);
                                srtJalan.setDate_scaned(date_scaned);
                                srtJalan.setDate_received(date_received);
                                srtJalan.setDate_sent(date_sent);
                                srtJalan.setPolisi_no(polisi_no);
                                getRealm.copyFromRealm(srtJalan);
                                getRealm.commitTransaction();
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadOutstanding() {
        String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_outs_sj";
        //String url = "https://www.kmshipmentstatus.com/ws_sir/index.php/cls_ws_sir/get_outs_sj";

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String plant = user.get(SessionManager.keyPlant);

        HashMap<String,String> params = new HashMap<String, String>();
        realm = Realm.getDefaultInstance();
        params.put("plant_code",plant);

        ProgressDialog progress = new ProgressDialog(getApplicationContext());
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
                                        srtJalan.setDate_sent(date_sent);
                                        srtJalan.setPolisi_no(polisi_no);
                                    }
                                });
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_outstanding:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_OUTSTANDING;
                        break;
                    case R.id.nav_history:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_HISTORY;
                        break;
                    case R.id.nav_logout:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_LOG_OUT;
                        break;
                    /*
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(Main2Activity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    */
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // outstanding
                Outstanding outstanding = new Outstanding();
                return outstanding;
            case 2:
                // history
                HistoryFragment historyFragment = new HistoryFragment();
                return historyFragment;
            case 3:
                //logout
                //Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
                //session.logout();
                //finish();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Apakah anda yakin akan keluar?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                session.logout();
                                RealmResults<SrtJalan> srtJalen = realm.where(SrtJalan.class).findAll();
                                realm.beginTransaction();
                                srtJalen.clear();
                                realm.commitTransaction();
                                RealmResults<SuratJalan> history = getRealm.where(SuratJalan.class).findAll();
                                getRealm.beginTransaction();
                                history.clear();
                                getRealm.commitTransaction();
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                if (navItemIndex != 0) {
                                    navItemIndex = 0;
                                    CURRENT_TAG = TAG_HOME;
                                    loadHomeFragment();
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Logout!");
                alertDialog.show();

            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void loadNavHeader() {
        // name, website
        txtName.setText(full_name);
        txtWebsite.setText(mail);

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    @Override
    public void onBackPressed() {
        /*
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            finish();
        }
        */
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                //finish();
            }else if(navItemIndex == 0){
                //finish();
                System.exit(0);
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main2, menu);
        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main2, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent inte = getIntent();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_password) {

            final Dialog dialog = new Dialog(Main2Activity.this);
            dialog.setContentView(R.layout.change_pwd);
            final EditText newpwd1 = (EditText)dialog.findViewById(R.id.inp_newpwd1);
            final EditText newpwd2 = (EditText)dialog.findViewById(R.id.inp_newpwd2);
            final EditText oldpwd = (EditText)dialog.findViewById(R.id.inp_oldpwd);
            final TextView usermail = (TextView) dialog.findViewById(R.id.txtEmailUser);
            Button btn_ubah = (Button)dialog.findViewById(R.id.btn_Ubah);

            final String mail = inte.getStringExtra("email");
            final String pass = inte.getStringExtra("password");
            //oldpwd.setText(pass);
            usermail.setText(mail);
            btn_ubah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  Change_Password(mail,pass,newpwd1,newpwd2);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void Change_Password(String mail,String pass,EditText newpwd1, EditText newpwd2) {
        String pwd1 = newpwd1.getText().toString();
        String pwd2 = newpwd2.getText().toString();
        Log.d("pwd1",""+pwd1);
        Log.d("pwd2",""+pwd2);
        //if (pwd1 == pwd2){
            a = new AQuery(Main2Activity.this);
            String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/change_password";
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("email",mail);
            params.put("password_lama",pass);
            params.put("password_baru",pwd1);

            ProgressDialog progress = new ProgressDialog(Main2Activity.this);
            progress.setCancelable(false);
            progress.setIndeterminate(false);
            a.progress(progress).ajax(url,params,String.class, new AjaxCallback<String>(){
                @Override
                public void callback(String url, String object, AjaxStatus status) {
                    if (object != null){
                        try {
                            JSONObject jsonObject = new JSONObject(object);
                            String hasil = jsonObject.getString("result");
                            String pesan = jsonObject.getString("msg");
                            Toast.makeText(getApplicationContext(),hasil,Toast.LENGTH_LONG).show();
                            Log.d("pesan",pesan);
                            if (hasil.equalsIgnoreCase("true")){
                                Log.d("pesan",pesan);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        //}else {
        //    newpwd2.setError("password baru tidak sama!");
        //}
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_outstanding) {

        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_about) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteractionHome(Uri uri) {

    }

    @Override
    public void openHome(View view) {

    }

    @Override
    protected void onPause() {
        super.onPause();

        timer = new Timer();
        Log.i("Main", "Invoking logout timer");
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask();
        timer.schedule(logoutTimeTask, 900000); //auto logout in 15 minutes
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            Log.i("Main", "cancel timer");
            timer = null;
        }
    }

    private class LogOutTimerTask extends TimerTask{
        @Override
        public void run() {
            session.logout();
            finish();
        }
    }
}
