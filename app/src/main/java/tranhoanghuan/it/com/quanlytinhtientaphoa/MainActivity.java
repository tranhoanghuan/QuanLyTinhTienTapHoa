package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CardView cvTinhtien, cvThongke, cvQuantri;
    private String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        checkNetwork();
        addEvents();

    }

    @Override
    public void onBackPressed() {

    }

    private void checkNetwork() {
        if(!isNetworkConnected()){
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Lỗi mạng");
           builder.setMessage("Không có mạng. Vui lòng kiểm tra lại!");
           builder.setCancelable(false);
           builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.dismiss();
               }
           });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
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
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID");
        Toast.makeText(this, UID,Toast.LENGTH_LONG).show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
