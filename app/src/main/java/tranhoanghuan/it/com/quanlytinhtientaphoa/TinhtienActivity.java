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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter.Adapter_HH_TT;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IHanghoaTT;


public class TinhtienActivity extends AppCompatActivity implements IHanghoaTT {
    private static final int REQUEST_CAMERA = 0;
    private SurfaceView camera_tt;
    private Button btnThanhtoan;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private String barcodeTT = null;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewTT;
    private ArrayList<HanghoaTinhtien> listHanghoaTT;
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {

    }

    private void addControls() {

        recyclerViewTT = findViewById(R.id.recycleSpBan);
        listHanghoaTT = new ArrayList<>();
        HangHoa h = new HangHoa("Test", 7000, 10000, 2);
        HangHoa h1 = new HangHoa("Test", 7000, 10000, 2);
        HangHoa h2 = new HangHoa("Test", 7000, 10000, 2);
        HangHoa h3 = new HangHoa("Test", 7000, 10000, 2);
        HanghoaTinhtien tt = new HanghoaTinhtien(h, 5);
        HanghoaTinhtien tt1 = new HanghoaTinhtien(h1, 5);
        HanghoaTinhtien tt2 = new HanghoaTinhtien(h2, 5);
        HanghoaTinhtien tt3 = new HanghoaTinhtien(h2, 5);
        listHanghoaTT.add(tt3);
        listHanghoaTT.add(tt);
        listHanghoaTT.add(tt1);
        listHanghoaTT.add(tt2);
        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recyclerViewTT.setHasFixedSize(true);
        recyclerViewTT.setLayoutManager(gridLayoutManager);
        adapter_hh_tt = new Adapter_HH_TT(this, listHanghoaTT, typeface, this);
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
        mDatabase.child(UID).child("Hanghoa").child(barcodeTT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    HangHoa tinhtien = dataSnapshot.getValue(HangHoa.class);
                    try {
                        float soLuong = ChooseSouong(tinhtien.getTenHang());
                        HanghoaTinhtien tinhtien1 = new HanghoaTinhtien(tinhtien, soLuong);
                        listHanghoaTT.add(tinhtien1);
                        adapter_hh_tt.notifyDataSetChanged();
                        Toast.makeText(TinhtienActivity.this, "so luong: " + adapter_hh_tt.getItemCount(),Toast.LENGTH_LONG).show();
                    }
                    catch (NullPointerException e){
                        Toast.makeText(TinhtienActivity.this, "Hàng hóa không tồn tại trong sơ sở dữ liệu.",Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private float ChooseSouong(String tenHang) {
        final float[] soLuongHH = {1};
        final float[] soLuongBan = new float[1];
        // create Dialog
        final Dialog dialog = new Dialog(TinhtienActivity.this);
        dialog.setTitle("Chọn số lượng hàng hóa");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sl);
        TextView txtTensp_sl = dialog.findViewById(R.id.txtTensp_sl);
        final EditText txt_sl_sp_sl = dialog.findViewById(R.id.txt_sl_sp_sl);
        ImageView imgSub_sl = dialog.findViewById(R.id.imgSub_sl);
        ImageView imgPlus_sl = dialog.findViewById(R.id.imgPlus_sl);
        Button btnLuu_sl = dialog.findViewById(R.id.btnLuu_sl);

        txtTensp_sl.setText(tenHang);
        txt_sl_sp_sl.setText(Float.toString(soLuongHH[0]));
        dialog.show();

        imgSub_sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(soLuongHH[0] > 1) {
                    soLuongHH[0]--;
                    txt_sl_sp_sl.setText(Float.toString(soLuongHH[0]));
                }
            }
        });

        imgPlus_sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soLuongHH[0]++;
                txt_sl_sp_sl.setText(Float.toString(soLuongHH[0]));
            }
        });


        btnLuu_sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sl = txt_sl_sp_sl.getText().toString();
                if(TextUtils.isEmpty(sl)) {
                    Toast.makeText(TinhtienActivity.this, "Chưa nhập số lượng!", Toast.LENGTH_LONG).show();
                }
                else if(Float.parseFloat(sl) <= 0){
                    Toast.makeText(TinhtienActivity.this, "Số lượng phải lớn hơn không.",Toast.LENGTH_LONG).show();
                    txt_sl_sp_sl.setText(sl);
                }
                else {
                    soLuongBan[0] = Float.parseFloat(sl);
                    dialog.cancel();
                }
            }
        });
        return soLuongBan[0];
    }


    @Override
    public void delHanghoaTT(int pos) {

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
