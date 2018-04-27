package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private CardView cvTinhtien, cvThongke, cvQuantri;
    private String UID;
    private String nameUID = "UID_Main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID");
        if (savedInstanceState != null && UID == null ) {
            // Restore UID from saved state
           UID = savedInstanceState.getString("UID_M");
        }
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
                it.putExtra(nameUID,UID);
                startActivity(it);
            }
        });

        cvThongke.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, ThongkeActivity.class);
                it.putExtra(nameUID,UID);
                startActivity(it);
            }
        });

        cvQuantri.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, QuantriActivity.class);
                it.putExtra(nameUID,UID);
                startActivity(it);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSignOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mnuClose:
               Intent intentClose = new Intent(MainActivity.this, SignInActivity.class);
               // clear all activities in the stack
               intentClose.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               intentClose.putExtra("isClose", true);
               startActivity(intentClose);
               finish();
        }
        return super.onOptionsItemSelected(item);
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

    // Save UID
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("UID_M", UID);
        super.onSaveInstanceState(savedInstanceState);
    }
}
