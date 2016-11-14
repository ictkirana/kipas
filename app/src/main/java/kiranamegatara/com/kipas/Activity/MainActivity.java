package kiranamegatara.com.kipas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kiranamegatara.com.kipas.Controller.ExpendableListAdapter;
import kiranamegatara.com.kipas.R;
import kiranamegatara.com.kipas.Controller.RealmHelper;
import kiranamegatara.com.kipas.Controller.SessionManager;
import kiranamegatara.com.kipas.Model.SuratJalanModel;

public class MainActivity extends AppCompatActivity {
    SessionManager session;
    AQuery aQuery;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ArrayList<SuratJalanModel> data;
    RealmHelper realmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        aQuery = new AQuery(MainActivity.this);
        realmHelper = new RealmHelper(MainActivity.this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpendableListAdapter(MainActivity.this,listDataHeader,listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this,ScanActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void prepareListData() {
/*
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        try {
            data = realmHelper.findAll();
        }catch (Exception e){
        }

        //adding header data
        for (int i = 0 ; i < data.size(); i++){
            /*
            listDataHeader.add(data.get(i).getNosurat());
            List<String> detail = new ArrayList<String>();
            detail.add(data.get(i).getPlant());
            detail.add(data.get(i).getVendor());
            listDataChild.put(listDataHeader.get(i),detail);

            Log.d("id", String.valueOf(data.get(i).getId()));
            Log.d("number",data.get(i).getNosurat());
            Log.d("pabrik",data.get(i).getPlant());
            Log.d("vendor",data.get(i).getVendor());

        }
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_password) {
            session.logout();;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
