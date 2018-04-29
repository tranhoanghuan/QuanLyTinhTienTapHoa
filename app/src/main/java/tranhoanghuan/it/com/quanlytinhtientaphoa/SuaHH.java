package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HangHoa;

public class SuaHH extends AppCompatActivity {
    private Button btnSave;
    private EditText txtTenhang_edit, txtGiaLe_edit, txtGiaSi_edit, txtslGiasi_edit;
    private DatabaseReference mDatabase;
    private String key;
    private HangHoa hangHoa;
    private ProgressDialog progressDialog;
    private String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_hh);
        addControls();
        loadData();
        addEvents();
    }
    private void loadData() {
        txtTenhang_edit.setText(hangHoa.getTenHang());
        txtGiaLe_edit.setText(Long.toString(hangHoa.getGiale()));
        txtGiaSi_edit.setText(Long.toString(hangHoa.getGiaSi()));
        txtslGiasi_edit.setText(Float.toString(hangHoa.getSlGiasi()));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkConnected()){
                    showDialog();
                }
                else {
                    String tenHang = txtTenhang_edit.getText().toString();
                    String giaLe = txtGiaLe_edit.getText().toString();
                    String giaSi = txtGiaSi_edit.getText().toString();
                    String slGiasi = txtslGiasi_edit.getText().toString();
                    if(TextUtils.isEmpty(tenHang) || TextUtils.isEmpty(giaLe) || TextUtils.isEmpty(giaSi) || TextUtils.isEmpty(slGiasi)){
                        Toast.makeText(SuaHH.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_LONG).show();
                    }
                    else if(tenHang.equals(hangHoa.getTenHang()) && giaLe.equals(Long.toString(hangHoa.getGiale()))
                            && giaSi.equals(Long.toString(hangHoa.getGiaSi())) && slGiasi.equals(Float.toString(hangHoa.getSlGiasi()))){
                        finish();
                    }
                    else {
                        progressDialog.setMessage("Đang cập nhật dữ liệu");
                        progressDialog.show();
                        editData();
                        finish();
                    }
                }
            }
        });
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


    private void editData() {
        final HangHoa hh = new HangHoa();
        hh.setTenHang(txtTenhang_edit.getText().toString());
        hh.setGiale(Long.parseLong(txtGiaLe_edit.getText().toString()));
        hh.setGiaSi(Long.parseLong(txtGiaSi_edit.getText().toString()));
        hh.setSlGiasi(Float.parseFloat(txtslGiasi_edit.getText().toString()));
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, hh);
        mDatabase.child(UID).child("Hanghoa").updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.hide();
                Toast.makeText(SuaHH.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID_QT");
        hangHoa = (HangHoa) intent.getSerializableExtra("item");
        key = intent.getStringExtra("key");
        progressDialog = new ProgressDialog(this);
        txtslGiasi_edit = findViewById(R.id.txtslGiaSi_edit);
        txtTenhang_edit = findViewById(R.id.txtTenhang_edit);
        txtGiaLe_edit = findViewById(R.id.txtGiaLe_edit);
        txtGiaSi_edit = findViewById(R.id.txtGiaSi_edit);

        btnSave = findViewById(R.id.btnSave);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
