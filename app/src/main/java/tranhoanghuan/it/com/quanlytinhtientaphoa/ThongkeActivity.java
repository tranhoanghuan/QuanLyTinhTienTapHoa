package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;

public class ThongkeActivity extends AppCompatActivity {
    private String UID;
    CardView cvTuan_TK, cvThang_TK, cvNam_TK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID_Main");
        if (savedInstanceState != null) {
            // Restore UID from saved state
            UID = savedInstanceState.getString("UID");
        }
        addControls();
        addEvents();
    }

    private void addEvents() {
        cvTuan_TK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThongkeActivity.this, Thongke_tongquat.class);
                intent.putExtra("UID_TK", UID);
                intent.putExtra("Loai", "tuan");
                startActivity(intent);
            }
        });

        cvThang_TK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThongkeActivity.this, Thongke_tongquat.class);
                intent.putExtra("UID_TK", UID);
                intent.putExtra("Loai", "thang");
                startActivity(intent);
            }
        });

        cvNam_TK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThongkeActivity.this, Thongke_tongquat.class);
                intent.putExtra("UID_TK", UID);
                intent.putExtra("Loai", "nam");
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        cvTuan_TK = findViewById(R.id.cvTuan_TK);
        cvThang_TK = findViewById(R.id.cvThang_TK);
        cvNam_TK = findViewById(R.id.cvNam_TK);
    }


    // Save UID
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("UID", UID);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
