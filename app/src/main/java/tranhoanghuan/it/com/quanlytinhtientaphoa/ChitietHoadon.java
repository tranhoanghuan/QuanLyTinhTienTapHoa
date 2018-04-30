package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter.AdapterHoadon;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaHoadon;

public class ChitietHoadon extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String UID;
    private String keyHD;
    private RecyclerView recyclerViewHD;
    private ArrayList<HanghoaHoadon> listHD;
    private AdapterHoadon adapterHoadon;
    private Typeface typeface;
    private TextView txtThoigianhd, txtTongHD;
    private SimpleDateFormat sdf;
    private DecimalFormat dec;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_hoadon);
        addControls();
    }

    private void addControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        UID = intent.getStringExtra("UID_TT");
        keyHD = intent.getStringExtra("keyKD");
        recyclerViewHD = findViewById(R.id.recycleHoadon);
        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        recyclerViewHD.setHasFixedSize(true);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recyclerViewHD.setLayoutManager(gridLayoutManager);
        listHD = new ArrayList<>();
        loadDataFromFB();
        adapterHoadon = new AdapterHoadon(this, listHD, typeface);
        recyclerViewHD.setAdapter(adapterHoadon);
        dec = new DecimalFormat("##,###,###,###");
        sdf = new SimpleDateFormat("dd/MM/YYYY - HH:mm:ss");
        Date d = new Date();
        txtThoigianhd = findViewById(R.id.txtThoigianhd);
        txtThoigianhd.setText(sdf.format(d));
        txtTongHD = findViewById(R.id.txtTongHD);
    }


    private void loadDataFromFB() {
        mDatabase.child(UID).child("Hoadon").child(keyHD).addChildEventListener(new ChildEventListener() {
            long sum = 0L;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HanghoaHoadon hoadon = dataSnapshot.getValue(HanghoaHoadon.class);
                float tong = hoadon.getDonGia()*hoadon.getSoLuongHanghoa();
                sum += (long)tong;
                txtTongHD.setText("Tổng tiền: " + dec.format(sum) + " VND");
                listHD.add(hoadon);
                adapterHoadon.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hoadon, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.mnuHoadon) {
            setResult(27,intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }
}
