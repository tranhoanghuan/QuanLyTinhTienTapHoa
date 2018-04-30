package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter.Adapter_TK_TQ;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IThongke;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaHoadon;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaThongke;

public class Thongke_tongquat extends AppCompatActivity implements IThongke{
    private DatabaseReference mDatabase;
    private String UID;
    private String loai;
    private RecyclerView recyclerViewTK;
    private ArrayList<String> listTK;
    private ArrayList<String> keyTK;
    private ArrayList<String> listTK_Phu;
    private Typeface typefaceTK;
    private ArrayList<Integer> flagKey;
    private Adapter_TK_TQ adapter_tk_tq;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke_tongquat);
        Intent intent = getIntent();
        loai = intent.getStringExtra("Loai");
        UID = intent.getStringExtra("UID_TK");
        if (savedInstanceState != null) {
            // Restore UID from saved state
            UID = savedInstanceState.getString("UID");
        }
        addControls();
    }

    private void addControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        recyclerViewTK = findViewById(R.id.recycleViewTK);
        typefaceTK = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recyclerViewTK.setHasFixedSize(true);
        recyclerViewTK.setLayoutManager(gridLayoutManager);
        listTK = new ArrayList<>();
        keyTK = new ArrayList<>();
        listTK_Phu = new ArrayList<>();
        flagKey = new ArrayList<>();
        switch (loai){
            case "tuan":
                loai = "Tuần";
                showProgress();
                loadDataFromFB_tuan();
                break;
            case "thang":
                loai = "Tháng";
                showProgress();
                loadDataFromFB_thang();
                break;
            case "nam":
                showProgress();
                loai = "Năm";
                loadDataFromFB_nam();
                break;
            default:
                break;
        }
        adapter_tk_tq = new Adapter_TK_TQ(this, listTK, typefaceTK, listTK_Phu, loai, this);
        recyclerViewTK.setAdapter(adapter_tk_tq);
    }

    private void showProgress() {
        progressDialog.setMessage("Đang phân tích dữ liệu");
        progressDialog.show();
    }

    private void loadDataFromFB_nam() {
        mDatabase.child(UID).child("Hoadon").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child(UID).child("Hoadon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                String strYear = key.substring(6, 10);
                if(key.length() == 15){
                    strYear = key.substring(5, 9);
                }
                if(!listTK.contains(strYear)){
                    flagKey.add(keyTK.size());
                    listTK.add(strYear);
                    String result = "";
                    listTK_Phu.add(result);
                    adapter_tk_tq.notifyDataSetChanged();
                }
                keyTK.add(key);

//                mDatabase.child(UID).child("Hoadon").child(key).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        if(keyTK.contains(dataSnapshot.getKey())) {
//                            int index = keyTK.indexOf(dataSnapshot.getKey());
//                            HanghoaHoadon hoadon = dataSnapshot.getValue(HanghoaHoadon.class);
//                            HanghoaThongke thongke = listHanghoaTK.get(index);
//                            float sl = thongke.getSoLuongTK() + hoadon.getSoLuongHanghoa();
//                            float tong = hoadon.getDonGia() *  hoadon.getSoLuongHanghoa();
//                            long tongHH = (long)tong + thongke.getTongHanghoaTK();
//                            thongke.setSoLuongTK(sl);
//                            thongke.setTongHanghoaTK(tongHH);
//                            adapter_tk_tq.notifyItemChanged(index);
//                        }
//
//                        else {
//                            HanghoaHoadon hoadon = dataSnapshot.getValue(HanghoaHoadon.class);
//                            HanghoaThongke thongke = new HanghoaThongke();
//                            thongke.setTenHanghoaTK(hoadon.getTenHanghoaTT());
//                            thongke.setSoLuongTK(hoadon.getSoLuongHanghoa());
//                            thongke.setTongHanghoaTK(hoadon.getDonGia()*(long)hoadon.getSoLuongHanghoa());
//                            listHanghoaTK.add(thongke);
//                            keyTK.add(dataSnapshot.getKey());
//                            adapter_tk_tq.notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
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

    private void loadDataFromFB_thang() {
        mDatabase.child(UID).child("Hoadon").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child(UID).child("Hoadon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                String strMonth = key.substring(4,6);
                String strYear = key.substring(6, 10);
                if(key.length() == 15){
                    strMonth= key.substring(3, 5);
                    strYear = key.substring(5, 9);
                }
                if(!listTK.contains(strMonth)){
                    flagKey.add(keyTK.size());
                    listTK.add(strMonth);
                    String result = " ( Năm " +strYear+  " )";
                    listTK_Phu.add(result);
                    adapter_tk_tq.notifyDataSetChanged();
                }
                keyTK.add(key);
//                mDatabase.child(UID).child("Hoadon").child(key).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        if(keyTK.contains(dataSnapshot.getKey())) {
//                            int index = keyTK.indexOf(dataSnapshot.getKey());
//                            HanghoaHoadon hoadon = dataSnapshot.getValue(HanghoaHoadon.class);
//                            HanghoaThongke thongke = listHanghoaTK.get(index);
//                            float sl = thongke.getSoLuongTK() + hoadon.getSoLuongHanghoa();
//                            float tong = hoadon.getDonGia() *  hoadon.getSoLuongHanghoa();
//                            long tongHH = (long)tong + thongke.getTongHanghoaTK();
//                            thongke.setSoLuongTK(sl);
//                            thongke.setTongHanghoaTK(tongHH);
//                            adapter_tk_tq.notifyItemChanged(index);
//                        }
//
//                        else {
//                            HanghoaHoadon hoadon = dataSnapshot.getValue(HanghoaHoadon.class);
//                            HanghoaThongke thongke = new HanghoaThongke();
//                            thongke.setTenHanghoaTK(hoadon.getTenHanghoaTT());
//                            thongke.setSoLuongTK(hoadon.getSoLuongHanghoa());
//                            thongke.setTongHanghoaTK(hoadon.getDonGia()*(long)hoadon.getSoLuongHanghoa());
//                            listHanghoaTK.add(thongke);
//                            keyTK.add(dataSnapshot.getKey());
//                            adapter_tk_tq.notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
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

    private void loadDataFromFB_tuan() {
        mDatabase.child(UID).child("Hoadon").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child(UID).child("Hoadon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                String week = key.substring(0,2);
                String strYear = key.substring(6, 10);
                if(key.length() == 15){
                    week = String.valueOf(key.charAt(0));
                    strYear = String.valueOf(key.substring(5, 9));
                }
                if(!listTK.contains(week)){
                    flagKey.add(keyTK.size());
                    listTK.add(week);
                    int tuan = Integer.parseInt(week);
                    int nam = Integer.parseInt(strYear);
                    Calendar c = new GregorianCalendar(Locale.getDefault());
                    c.set(Calendar.WEEK_OF_YEAR, tuan);
                    c.set(Calendar.YEAR, nam);
                    int firstDayOfWeek = c.getFirstDayOfWeek();
                    c.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
                    String firstDay = new SimpleDateFormat("dd/MM/YYYY").format(c.getTime());
                    c.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + 6);
                    String lastDay = new SimpleDateFormat("dd/MM/YYYY").format(c.getTime());
                    String result = " (" + firstDay + " - " + lastDay + ")";
                    listTK_Phu.add(result);
                    adapter_tk_tq.notifyDataSetChanged();
                }
                keyTK.add(key);

//                mDatabase.child(UID).child("Hoadon").child(key).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        if(keyTK.contains(dataSnapshot.getKey())) {
//                            int index = keyTK.indexOf(dataSnapshot.getKey());
//                            HanghoaHoadon hoadon = dataSnapshot.getValue(HanghoaHoadon.class);
//                            HanghoaThongke thongke = listHanghoaTK.get(index);
//                            float sl = thongke.getSoLuongTK() + hoadon.getSoLuongHanghoa();
//                            float tong = hoadon.getDonGia() *  hoadon.getSoLuongHanghoa();
//                            long tongHH = (long)tong + thongke.getTongHanghoaTK();
//                            thongke.setSoLuongTK(sl);
//                            thongke.setTongHanghoaTK(tongHH);
//                            adapter_tk_tq.notifyItemChanged(index);
//                        }
//
//                        else {
//                            HanghoaHoadon hoadon = dataSnapshot.getValue(HanghoaHoadon.class);
//                            HanghoaThongke thongke = new HanghoaThongke();
//                            thongke.setTenHanghoaTK(hoadon.getTenHanghoaTT());
//                            thongke.setSoLuongTK(hoadon.getSoLuongHanghoa());
//                            thongke.setTongHanghoaTK(hoadon.getDonGia()*(long)hoadon.getSoLuongHanghoa());
//                            listHanghoaTK.add(thongke);
//                            keyTK.add(dataSnapshot.getKey());
//                            adapter_tk_tq.notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

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

    @Override
    public void thongkeChitiet(int pos) {
        ArrayList<String> list = new ArrayList<String>();
        if(pos == (flagKey.size() - 1)){
            for(int i = flagKey.get(pos); i<keyTK.size(); i++){
                String key = keyTK.get(i);
                list.add(key);
            }
        }
        else {
            for(int i = flagKey.get(pos); i<flagKey.get(pos+1); i++){
                String key = keyTK.get(i);
                list.add(key);
            }
        }

        String name = loai + " " + listTK.get(pos);
        Intent intent = new Intent(Thongke_tongquat.this, ChitietThongke.class);
        intent.putStringArrayListExtra("listKey", list);
        intent.putExtra("name", name);
        intent.putExtra("UID_TKTQ", UID);
        startActivity(intent);
    }
}
