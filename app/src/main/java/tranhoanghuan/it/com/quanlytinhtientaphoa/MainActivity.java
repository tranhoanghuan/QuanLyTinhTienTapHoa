package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CardView cvTinhtien, cvThongke, cvQuantri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        addEvents();

    }

    private void checkNetwork() {
        if(!isNetworkConnected()){
            Toast.makeText(this, "Không có kết nối mạng! Vui lòng kết nối Internet", Toast.LENGTH_LONG).show();
        }

    }


    private void addEvents() {

        cvTinhtien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, TinhtienActivity.class);
                startActivity(it);
            }
        });

        cvThongke.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, ThongkeActivity.class);
                startActivity(it);
            }
        });

        cvQuantri.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, QuantriActivity.class);
                startActivity(it);
            }
        });
    }

    private void addControl() {
        cvTinhtien = (CardView) findViewById(R.id.cvTinhtien);
        cvQuantri = (CardView) findViewById(R.id.cvQuantri);
        cvThongke = (CardView) findViewById(R.id.cvThongke);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
