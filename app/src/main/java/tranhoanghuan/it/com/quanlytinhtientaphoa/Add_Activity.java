package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static tranhoanghuan.it.com.quanlytinhtientaphoa.MainActivity.UID;
import static tranhoanghuan.it.com.quanlytinhtientaphoa.QuantriActivity.keyList;


public class Add_Activity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 0;
    private SurfaceView cameraView;
    private Button btnAdd;
    private TextView txtName, txtPrice, txtBarcode;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private String bar = null;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_);
        addControls();
        addEvents();

    }

    private void addControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        cameraView = findViewById(R.id.camera);
        btnAdd = findViewById(R.id.btnAdd);
        txtBarcode = findViewById(R.id.txtBarcode);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        bar = null;
        progressDialog = new ProgressDialog(this);

        if (ActivityCompat.checkSelfPermission(Add_Activity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Add_Activity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        barcodeDetector = new BarcodeDetector.Builder(Add_Activity.this).setBarcodeFormats(Barcode.EAN_13 | Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(Add_Activity.this, barcodeDetector).setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(35.0f)
                .setAutoFocusEnabled(true)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Add_Activity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
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
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneGen1.startTone(ToneGenerator.TONE_DTMF_S,10);
                    bar = barcodes.valueAt(0).displayValue.toString();
                    txtBarcode.setText(bar);
                }
            }
        });

    }

    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkConnected()){
                    showDialog();
                }
                else {
                    String textName = txtName.getText().toString();
                    String textPrice = txtPrice.getText().toString();
                    if(TextUtils.isEmpty(textName) || TextUtils.isEmpty(textPrice)){
                        Toast.makeText(Add_Activity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        progressDialog.setMessage("Đang thêm dữ liệu");
                        progressDialog.show();
                        addData();
                    }
                }
            }
        });
    }

    private void addData() {
        final HangHoa hh = new HangHoa();
        hh.setName(txtName.getText().toString());
        hh.setPrice(Long.parseLong(txtPrice.getText().toString()));
        Map<String, Object> childUpdates = new HashMap<>();
        // No barcode
        String key = txtName.getText().toString();
        // Barcode
        if(bar != null){
           key = bar;
        }
        // Check HangHoa exist in Firebase database
        if(keyList.contains(key)){
            Toast.makeText(Add_Activity.this, "Hàng hóa này đã tồn tại trong cơ sở dữ liệu!", Toast.LENGTH_LONG).show();
            progressDialog.hide();
            bar = null;
            txtBarcode.setText(null);
            txtName.setText(null);
            txtPrice.setText(null);
        }
        else {
            childUpdates.put(key, hh);
            mDatabase.child(UID).child("Hanghoa").updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.hide();
                    Toast.makeText(Add_Activity.this, "Thêm thành công", Toast.LENGTH_LONG).show();
                    bar = null;
                    txtBarcode.setText(null);
                    txtName.setText(null);
                    txtPrice.setText(null);
                }
            });
        }
    }

    private void showDialog() {
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

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                cameraSource.start(cameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
