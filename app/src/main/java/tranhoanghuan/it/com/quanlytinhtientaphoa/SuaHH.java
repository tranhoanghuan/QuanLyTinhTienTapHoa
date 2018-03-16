package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import static tranhoanghuan.it.com.quanlytinhtientaphoa.QuantriActivity.ListHH;
import static tranhoanghuan.it.com.quanlytinhtientaphoa.QuantriActivity.keyList;

public class SuaHH extends AppCompatActivity {
    private Button btnSave;
    private EditText txtName, txtPrice;
    private DatabaseReference mDatabase;
    private int pos;
    private String key;
    private HangHoa hangHoa;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_hh);
        addControls();
        loadData();
        addEvents();
    }

    private void loadData() {
        txtName.setText(hangHoa.getName());
        txtPrice.setText(Long.toString(hangHoa.getPrice()));
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Đang cập nhật dữ liệu");
                progressDialog.show();
                editData();
                finish();
            }
        });
    }

    private void editData() {
        mDatabase.child("Hanghoa").child(key).removeValue();
        final HangHoa hh = new HangHoa();
        if(TextUtils.isEmpty(txtName.getText().toString())){
            txtName.setError("Chưa nhập tên");
            return;
        }
        hh.setName(txtName.getText().toString());
        if(TextUtils.isEmpty(txtPrice.getText().toString())){
            txtName.setError("Chưa nhập giá");
            return;
        }
        hh.setPrice(Long.parseLong(txtPrice.getText().toString()));
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, hh);
        mDatabase.child("Hanghoa").updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.hide();
                Toast.makeText(SuaHH.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addControls() {
        Intent intent = getIntent();
        pos = intent.getIntExtra("position", -1);
        hangHoa = new HangHoa();
        hangHoa = ListHH.get(pos);
        key = keyList.get(pos);
        progressDialog = new ProgressDialog(this);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        btnSave = findViewById(R.id.btnSave);
    }
}
