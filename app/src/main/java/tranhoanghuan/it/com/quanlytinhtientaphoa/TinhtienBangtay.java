package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter.Adapter_HH_TTBT;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IHanghoaTTBT;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HangHoa;

public class TinhtienBangtay extends AppCompatActivity implements IHanghoaTTBT {
    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewHH_TTBT;
    private ArrayList<HangHoa> ListHH_TTBT;
    private ArrayList<String> keyList_TTBT;
    private ArrayList<HangHoa> ListHH_TTBT_Chon;
    private ArrayList<String> keyList_TTBT_Chon;
    private Adapter_HH_TTBT adapter_TTBT;
    private Typeface typeface_TTBT;
    private String UID;
    private Button btnHoanthanh;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinhtien_bangtay);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnHoanthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putStringArrayListExtra("key", keyList_TTBT_Chon);
                intent.putExtra("listHH", ListHH_TTBT_Chon);
                setResult(77,intent);
                finish();
            }
        });

    }

    private void addControls() {
        intent = getIntent();
        UID = intent.getStringExtra("UID_TT");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ListHH_TTBT = new ArrayList<>();
        keyList_TTBT = new ArrayList<>();
        ListHH_TTBT_Chon = new ArrayList<>();
        keyList_TTBT_Chon = new ArrayList<>();
        typeface_TTBT = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        recyclerViewHH_TTBT = findViewById(R.id.list_HH_ttbt);
        btnHoanthanh = findViewById(R.id.btnHoanthanh);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recyclerViewHH_TTBT.setHasFixedSize(true);
        recyclerViewHH_TTBT.setLayoutManager(gridLayoutManager);
        loadDataFromFB();
        adapter_TTBT = new Adapter_HH_TTBT(this, ListHH_TTBT, keyList_TTBT, typeface_TTBT, this);
        recyclerViewHH_TTBT.setAdapter(adapter_TTBT);


    }

    private void loadDataFromFB() {
        mDatabase.child(UID).child("Hanghoa").orderByChild("tenHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(checkKey(dataSnapshot.getKey().toString())){
                    HangHoa item = dataSnapshot.getValue(HangHoa.class);
                    ListHH_TTBT.add(item);
                    keyList_TTBT.add(dataSnapshot.getKey().toString());
                    adapter_TTBT.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(checkKey(dataSnapshot.getKey().toString())){
                    HangHoa item = dataSnapshot.getValue(HangHoa.class);
                    String key = dataSnapshot.getKey();
                    int index = keyList_TTBT.indexOf(key);
                    if(index > -1){
                        ListHH_TTBT.set(index, item);
                        adapter_TTBT.notifyItemChanged(index);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(checkKey(dataSnapshot.getKey().toString())){
                    String key = dataSnapshot.getKey();
                    int index = keyList_TTBT.indexOf(key);
                    if(index > -1){
                        ListHH_TTBT.remove(index);
                        keyList_TTBT.remove(index);
                        adapter_TTBT.notifyItemRemoved(index);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean checkKey(String s) {
        return s.contains("-");
    }

    @Override
    public void chonHanghoa(int pos, boolean isChecked) {
        if(isChecked){
            HangHoa hangHoa = ListHH_TTBT.get(pos);
            String key = keyList_TTBT.get(pos);
            ListHH_TTBT_Chon.add(hangHoa);
            keyList_TTBT_Chon.add(key);
        }
        else {
            HangHoa hangHoa = ListHH_TTBT.get(pos);
            String key = keyList_TTBT.get(pos);
            keyList_TTBT_Chon.remove(key);
            ListHH_TTBT_Chon.remove(hangHoa);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
