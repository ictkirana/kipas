package kiranamegatara.com.kipas.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import kiranamegatara.com.kipas.Fragment.HistoryFragment;
import kiranamegatara.com.kipas.Fragment.HomeFragment;
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

    AQuery a;
    final ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "https://siva.jsstatic.com/id/20143/images/sol/20143_logo_0_11601.png";
    private static final String urlProfileImg = "https://yt3.ggpht.com/-E_pUlsSjalk/AAAAAAAAAAI/AAAAAAAAAAA/xuBNGaofFA0/s900-c-k-no-rj-c0xffffff/photo.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_LOG_OUT = "logout";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


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

        session = new SessionManager(Main2Activity.this);

        User user = new User();
        Intent intent = getIntent();
        String mail = intent.getStringExtra("email");
        String pass = intent.getStringExtra("password");
        GetUser(user,mail,pass);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Main2Activity.this,ScanActivity.class);
                startActivity(intent1);
            }
        });

        // load nav menu header data
        loadNavHeader(user);

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


//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

    }

    private User GetUser(User user, String email, String password){
        final String[] full_name = new String[1];
        final String[] pwd = new String[1];
        final String[] mail = new String[1];
        final String[] nik = new String[1];
        final String[] company_id = new String[1];
        final String[] company_name = new String[1];
        final String[] is_active = new String[1];
        final String[] is_reset = new String[1];
        final String[] fail_counter = new String[1];
        final String[] date_last_login = new String[1];
        final String[] date_created = new String[1];
        final String[] date_updated = new String[1];

        a = new AQuery(Main2Activity.this);
        String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/get_login";

        HashMap<String,String> params = new HashMap<String, String>();
        params.put("email",email);
        params.put("password",password);

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

                        if (hasil.equalsIgnoreCase("true")){
                            Log.d("pesan",pesan);
                            JSONArray jsonarray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonarray.length(); i++){
                                if (i == 0) {
                                    JSONObject b = jsonarray.getJSONObject(i);
                                    full_name[0] = b.getString("full_name");
                                    pwd[0] = b.getString("password");
                                    mail[0] = b.getString("email");
                                    nik[0] = b.getString("nik");
                                    company_id[0] = b.getString("company_id");
                                    company_name[0] = b.getString("company_name");
                                    is_active[0] = b.getString("is_active");
                                    is_reset[0] = b.getString("is_active");
                                    fail_counter[0] = b.getString("fail_counter");
                                    date_created[0] = b.getString("date_created");

                                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dateCrt = new Date();
                                    try {
                                        dateCrt = curFormater.parse(date_created[0]);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    } catch (java.text.ParseException e) {
                                        e.printStackTrace();
                                    }

                                    date_updated[0] = b.getString("date_updated");
                                    Date dateUpd = new Date();
                                    try {
                                        dateUpd = curFormater.parse(date_updated[0]);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    } catch (java.text.ParseException e) {
                                        e.printStackTrace();
                                    }

                                    Calendar c = Calendar.getInstance();
                                    date_last_login[0] = String.valueOf(c.getTime());
                                }
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        user.setFull_name(full_name[0]);
        user.setPassword(pwd[0]);
        user.setEmail(mail[0]);
        user.setNik(nik[0]);
        user.setCompany_id(company_id[0]);
        user.setCompany_name(company_name[0]);
        user.setIs_active(is_active[0]);
        user.setIs_reset(is_reset[0]);
        user.setFail_counter(fail_counter[0]);
        user.setDate_created(date_created[0]);
        user.setDate_updated(date_updated[0]);
        return user;
    };

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
                    case R.id.nav_history:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_HISTORY;
                        break;
                    case R.id.nav_logout:
                        navItemIndex = 2;
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
                // history
                HistoryFragment historyFragment = new HistoryFragment();
                return historyFragment;
            case 2:
                //logout
                Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
                session.logout();
                finish();
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

    private void loadNavHeader(User user) {
        // name, website
        txtName.setText(user.getFull_name());
        txtWebsite.setText(user.getEmail());

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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            finish();
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                finish();
            }
        }

        //super.onBackPressed();
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
            oldpwd.setText(pass);
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
        Toast.makeText(getApplicationContext(),pwd1,Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),pwd2,Toast.LENGTH_LONG).show();
        if (pwd1 == pwd2){
            a = new AQuery(Main2Activity.this);
            String url = "http://10.0.0.105/dev/fop/ws_sir/index.php/cls_ws_sir/change_password";

            HashMap<String,String> params = new HashMap<String, String>();
            params.put("email",mail);
            params.put("password",pwd1);

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
        }else {
            newpwd2.setError("password baru tidak sama!");
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {

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

}
