package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText txt_input_email, txt_input_password, txt_input_reEnterPassword;
    private Button btn_signup;
    private TextView txt_link_login;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        addControls();
        addEvents();
    }

    @Override
    public void onBackPressed() {

    }

    private void addEvents() {
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkConnected()){
                    showDialog();
                }
                else {
                    String email = txt_input_email.getText().toString();
                    String password = txt_input_password.getText().toString();
                    String rePass = txt_input_reEnterPassword.getText().toString();
                    if(!password.equals(rePass)){
                        Toast.makeText(SignUpActivity.this, "Mật khẩu nhập lại sai. Vui lòng kiểm tra lại!", Toast.LENGTH_LONG).show();
                    }
                    else if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(rePass)){
                        Toast.makeText(SignUpActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        signUp(email, password);
                        txt_input_email.setText(null);
                        txt_input_password.setText(null);
                        txt_input_reEnterPassword.setText(null);
                    }
                }
            }
        });

        txt_link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signUp(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    String UID = user.getUid();
                    mDatabase.child(UID).child("User").setValue(email);
                    String result = "Đăng ký thành công!";
                    showDialogResult(result);
                }
                else {
                    String result = "Email đã tồn tại. Vui lòng nhập email khác!";
                    showDialogResult(result);
                }
            }
        });
    }

    private void showDialogResult(final String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả đăng ký");
        builder.setMessage(result);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(result.equals("Đăng ký thành công!")) {
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addControls() {
        txt_input_email = findViewById(R.id.txt_input_email);
        txt_input_password = findViewById(R.id.txt_input_password);
        txt_input_reEnterPassword = findViewById(R.id.txt_input_reEnterPassword);
        btn_signup = findViewById(R.id.btn_signup);
        txt_link_login = findViewById(R.id.txt_link_login);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
