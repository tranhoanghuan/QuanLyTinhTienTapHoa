package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

public class SignInActivity extends AppCompatActivity {
    private Button btn_login;
    private TextView txtLink_signup;
    private EditText txt_input_email, txt_input_password;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        addControls();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void addEvents() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkConnected()){
                    showDialog();
                }
                else {
                    String email = txt_input_email.getText().toString();
                    String password = txt_input_password.getText().toString();
                    if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                        Toast.makeText(SignInActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        progressDialog.setMessage("Đang xác thực thông tin tài khoản");
                        progressDialog.show();
                        signIn(email, password);
                        txt_input_email.setText(null);
                        txt_input_password.setText(null);
                    }
                }
            }
        });

        txtLink_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, "Login is successful!", Toast.LENGTH_LONG).show();
                    progressDialog.hide();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String UID = user.getUid().toString();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.putExtra("UID",UID);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SignInActivity.this, "Login fail!", Toast.LENGTH_LONG).show();
                    progressDialog.hide();
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

    private void addControls() {
        btn_login = findViewById(R.id.btn_login);
        txtLink_signup = findViewById(R.id.link_signup);
        txt_input_email = findViewById(R.id.txt_input_email);
        txt_input_password = findViewById(R.id.txt_input_password);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
