package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter.Adapter_HH;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IHanghoa;

public class QuantriActivity extends AppCompatActivity implements IHanghoa{
     private DatabaseReference mDatabase;
     private RecyclerView recyclerViewHH;
     public static ArrayList<HangHoa> ListHH;
     public static ArrayList<String> keyList;
     public static Adapter_HH adapter;
     private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantri);
        addControls();
        addEvents();

    }

    private void addEvents() {

    }

    private void addControls() {
        ListHH = new ArrayList<HangHoa>();
        keyList= new ArrayList<String>();
        typeface = Typeface.createFromAsset(getAssets(), "font/vnf-quicksand-bold.ttf");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerViewHH = findViewById(R.id.list_HH);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recyclerViewHH.setHasFixedSize(true);
        recyclerViewHH.setLayoutManager(gridLayoutManager);
        loadDataFromFB();
        adapter = new Adapter_HH(this, ListHH, typeface, this);
        recyclerViewHH.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void loadDataFromFB() {
        mDatabase.child("Hanghoa").orderByChild("name").addChildEventListener(new ChildEventListener() {
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
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(QuantriActivity.this, Add_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void delHanghoa(final int pos) {
        final Dialog dialog = new Dialog(QuantriActivity.this);
        dialog.setTitle("Xác nhận xóa");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_del);
        Button btnOK = dialog.findViewById(R.id.btnOK);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Hanghoa").child(keyList.get(pos)).removeValue(new DatabaseReference.CompletionListener() {
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
