package tranhoanghuan.it.com.quanlytinhtientaphoa;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter.Adapter_HH;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IHanghoa;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HangHoa;


public class QuantriActivity extends AppCompatActivity implements IHanghoa{
    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewHH;
    private ArrayList<HangHoa> ListHH;
    private ArrayList<String> keyList;
    private Adapter_HH adapter;
    private Typeface typeface;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantri);
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID_Main");
        if (savedInstanceState != null && UID == null) {
            // Restore UID from saved state
            UID = savedInstanceState.getString("UID");
        }
        addControls();
    }

    private void addControls() {
        ListHH = new ArrayList<>();
        keyList= new ArrayList<>();
        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerViewHH = findViewById(R.id.list_HH);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recyclerViewHH.setHasFixedSize(true);
        recyclerViewHH.setLayoutManager(gridLayoutManager);
        loadDataFromFB();
        adapter = new Adapter_HH(this, ListHH, keyList, typeface,this, UID);
        recyclerViewHH.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadDataFromFB() {
        mDatabase.child(UID).child("Hanghoa").orderByChild("tenHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HangHoa item = dataSnapshot.getValue(HangHoa.class);
                ListHH.add(item);
                keyList.add(dataSnapshot.getKey().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                HangHoa item = dataSnapshot.getValue(HangHoa.class);
                String key = dataSnapshot.getKey();
                int index = keyList.indexOf(key);
                if(index > -1){
                    ListHH.set(index, item);
                    adapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = keyList.indexOf(key);
                if(index > -1){
                    ListHH.remove(index);
                    keyList.remove(index);
                    adapter.notifyItemRemoved(index);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quantri, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(QuantriActivity.this, Add_Activity.class);
            intent.putExtra("UID_QT", UID);
            intent.putStringArrayListExtra("listKey", keyList);
            startActivity(intent);
        }
        else if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void delHanghoa(final int pos) {
        if(!isNetworkConnected()){
            showDialog();
        }
        else {
            final Dialog dialog = new Dialog(QuantriActivity.this);
            dialog.setTitle("Xác nhận xóa");
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_del);
            Button btnOK = dialog.findViewById(R.id.btnOK);
            Button btnHuy = dialog.findViewById(R.id.btnHuy);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.child(UID).child("Hanghoa").child(keyList.get(pos)).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Toast.makeText(QuantriActivity.this,"Xóa thành công!", Toast.LENGTH_LONG ).show();
                            adapter.notifyDataSetChanged();
                        }
                    });
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

    // Save UID
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("UID", UID);
    }
}
