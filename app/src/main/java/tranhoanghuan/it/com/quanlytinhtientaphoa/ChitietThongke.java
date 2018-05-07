package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.ArrayList;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter.AdapterChitietThongke;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaHoadon;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaThongke;

public class ChitietThongke extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String UID;
    private Intent intent;
    private TextView txtLoaiTK, txtTongTienTK;
    private RecyclerView recycleTKCT;
    private ArrayList<HanghoaThongke> listHanghoaTKCT;
    private AdapterChitietThongke adapterCTTK;
    private Typeface typefaceCTTK;
    private DecimalFormat dec;
    private ArrayList<String> keyCTTK;
    private ArrayList<String> keyHHTK;
    private Long tongTienHH = 0L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_thongke);
        addControls();
    }

    private void addControls() {
        dec = new DecimalFormat("##,###,###,###");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        UID = intent.getStringExtra("UID_TKTQ");
        String name = intent.getStringExtra("name");
        keyCTTK = intent.getStringArrayListExtra("listKey");
        listHanghoaTKCT = new ArrayList<>();
        keyHHTK = new ArrayList<>();
        txtTongTienTK = findViewById(R.id.txtTongTienTK);
        txtLoaiTK = findViewById(R.id.txtLoaiTK);
        recycleTKCT = findViewById(R.id.recycleViewTKCT);
        typefaceCTTK = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recycleTKCT.setHasFixedSize(true);
        recycleTKCT.setLayoutManager(gridLayoutManager);
        loadDataFromFB();
        adapterCTTK = new AdapterChitietThongke(this, listHanghoaTKCT, typefaceCTTK);
        recycleTKCT.setAdapter(adapterCTTK);
        txtLoaiTK.setText(name);

    }

    private void loadDataFromFB() {
        for(String key : keyCTTK){
            mDatabase.child(UID).child("Hoadon").child(key).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String key_1 = dataSnapshot.getKey();
                    if(keyHHTK.contains(key_1)) {
                        int index = keyHHTK.indexOf(dataSnapshot.getKey());
                        HanghoaHoadon hoadon = dataSnapshot.getValue(HanghoaHoadon.class);
                        HanghoaThongke thongke = listHanghoaTKCT.get(index);
                        float sl = thongke.getSoLuongTK() + hoadon.getSoLuongHanghoa();
                        float tong = hoadon.getDonGia() * hoadon.getSoLuongHanghoa();
                        long tongHH = (long)tong + thongke.getTongHanghoaTK();
                        tongTienHH += (long)tong;
                        txtTongTienTK.setText("Tổng tiền: " + dec.format(tongTienHH) + " VND");
                        thongke.setSoLuongTK(sl);
                        thongke.setTongHanghoaTK(tongHH);
                        thongke.setTenHanghoaTK(hoadon.getTenHanghoaTT());
                        adapterCTTK.notifyItemChanged(index);
                    }

                    else {
                        HanghoaHoadon hoadon = dataSnapshot.getValue(HanghoaHoadon.class);
                        HanghoaThongke thongke = new HanghoaThongke();
                        thongke.setTenHanghoaTK(hoadon.getTenHanghoaTT());
                        thongke.setSoLuongTK(hoadon.getSoLuongHanghoa());
                        float tong = (float)hoadon.getDonGia() * hoadon.getSoLuongHanghoa();
                        tongTienHH += (long)tong;
                        txtTongTienTK.setText("Tổng tiền: " + dec.format(tongTienHH) + " VND");
                        thongke.setTongHanghoaTK((long)tong);
                        listHanghoaTKCT.add(thongke);
                        keyHHTK.add(dataSnapshot.getKey());
                        adapterCTTK.notifyDataSetChanged();
                    }
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


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
