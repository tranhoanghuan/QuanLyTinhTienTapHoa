package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ThongkeActivity extends AppCompatActivity {
    private String UID;

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
