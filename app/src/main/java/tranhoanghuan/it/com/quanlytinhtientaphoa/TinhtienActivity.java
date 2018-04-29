package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter.Adapter_HH_TT;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IHanghoaTT;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HangHoa;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaHoadon;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaTinhtien;


public class TinhtienActivity extends AppCompatActivity implements IHanghoaTT {
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CODE = 7;
    private static final int REQUEST_CODE_1 = 17;
    private SurfaceView camera_tt;
    private Button btnThanhtoan;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private String barcodeTT = null;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewTT;
    private ArrayList<HanghoaTinhtien> listHanghoaTT;
    private ArrayList<String> listKey;
    private Adapter_HH_TT adapter_hh_tt;
    private Typeface typeface;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinhtien);
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID_Main");
        if (savedInstanceState != null) {
            // Restore UID from saved state
            UID = savedInstanceState.getString("UID");
        }
        addControls();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tinhtien, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.mnutinhtien){
            Intent intent = new Intent(TinhtienActivity.this, TinhtienBangtay.class);
            intent.putExtra("UID_TT", UID);
            startActivityForResult(intent, REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == 77){
            ArrayList<String> key = data.getStringArrayListExtra("key");
            ArrayList<HangHoa> hh = (ArrayList<HangHoa>) data.getSerializableExtra("listHH");
            if(key.size() > 0 && hh.size() > 0){
                 for (int i=0; i< hh.size(); i++){
                     HanghoaTinhtien tt = new HanghoaTinhtien(hh.get(i), 1, 1);
                     listHanghoaTT.add(0, tt);
                     listKey.add(0, key.get(i));
                     adapter_hh_tt.notifyItemInserted(0);
                     recyclerViewTT.smoothScrollToPosition(0);
                     adapter_hh_tt.notifyDataSetChanged();
                 }

            }
        }
        if(requestCode == REQUEST_CODE_1 && resultCode == 27){
            listKey.clear();
            listHanghoaTT.clear();
            adapter_hh_tt.notifyDataSetChanged();
        }
    }

    private void addEvents() {
        btnThanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listHanghoaTT.size() > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("ddwMMYYYYHHmmss");
                    Date d = new Date();
                    final String keyHD = sdf.format(d);
                    Map<String, Object> childUpdates = new HashMap<>();
                    for(int i=0; i< listHanghoaTT.size(); i++){
                        HanghoaTinhtien tinhtien = listHanghoaTT.get(i);
                        HanghoaHoadon hoadon = new HanghoaHoadon(tinhtien.getHangHoa().getTenHang(), tinhtien.getSoLuong(), tinhtien.getDonGiaTT() );
                        childUpdates.put(listKey.get(i), hoadon);
                    }

                    mDatabase.child(UID).child("Hoadon").child(keyHD).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(TinhtienActivity.this, ChitietHoadon.class);
                            intent.putExtra("keyKD", keyHD);
                            intent.putExtra("UID_TT", UID);
                            startActivityForResult(intent, REQUEST_CODE_1);
                        }
                    });
                }
                else {
                    Toast.makeText(TinhtienActivity.this, "Bạn chưa chọn hàng hóa để tính tiền.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void addControls() {

        recyclerViewTT = findViewById(R.id.recycleSpBan);
        listHanghoaTT = new ArrayList<>();
        listKey = new ArrayList<>();
        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recyclerViewTT.setLayoutManager(gridLayoutManager);
        adapter_hh_tt = new Adapter_HH_TT(this, listHanghoaTT, listKey, typeface, this);
        recyclerViewTT.setAdapter(adapter_hh_tt);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        camera_tt = findViewById(R.id.camera_tt);
        btnThanhtoan = findViewById(R.id.btnThanhtoan);
        if (ActivityCompat.checkSelfPermission(TinhtienActivity.this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TinhtienActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }
        barcodeDetector = new BarcodeDetector.Builder(TinhtienActivity.this).setBarcodeFormats(Barcode.EAN_13 | Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(TinhtienActivity.this, barcodeDetector).setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(35.0f)
                .setAutoFocusEnabled(true)
                .build();

        camera_tt.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(TinhtienActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    cameraSource.start(camera_tt.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes != null && barcodes.size() > 0) {
                    String barcode = barcodes.valueAt(0).displayValue.toString();
                    if(!barcode.equals(barcodeTT)){
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                        barcodeTT = barcode;
                        loadHanghoaFromFirebase();
                    }
                }
            }
        });

    }

    private void loadHanghoaFromFirebase() {
        mDatabase.child(UID).child("Hanghoa").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag = true;
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    if (barcodeTT.equals(data.getKey())) {
                        HangHoa tinhtien = data.getValue(HangHoa.class);
                        float soLuong = 1;
                        long dg = 1;
                        HanghoaTinhtien tinhtien1 = new HanghoaTinhtien(tinhtien, soLuong, dg);
                        listHanghoaTT.add(0, tinhtien1);
                        listKey.add(0, barcodeTT);
                        adapter_hh_tt.notifyItemInserted(0);
                        recyclerViewTT.smoothScrollToPosition(0);
                        adapter_hh_tt.notifyDataSetChanged();
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    Toast.makeText(TinhtienActivity.this, "Hàng hóa không tồn tại trong sơ sở dữ liệu.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }




    @Override
    public void delHanghoaTT(final int pos) {
        final Dialog dialog = new Dialog(TinhtienActivity.this);
        dialog.setTitle("Xác nhận xóa");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_del);
        Button btnOK = dialog.findViewById(R.id.btnOK);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listHanghoaTT.remove(pos);
                listKey.remove(pos);
                adapter_hh_tt.notifyItemRemoved(pos);
                dialog.cancel();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                cameraSource.start(camera_tt.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(TinhtienActivity.this, "Camera chưa được cấp quyền hoạt động!", Toast.LENGTH_LONG).show();
        }
    }

    // Save UID
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("UID", UID);
    }
}
