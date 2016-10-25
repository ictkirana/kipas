package kiranamegatara.com.kipas.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kiranamegatara.com.kipas.Activity.LoginActivity;
import kiranamegatara.com.kipas.Activity.Main2Activity;
import kiranamegatara.com.kipas.Activity.MainActivity;
import kiranamegatara.com.kipas.R;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        },2000);
    }
}
