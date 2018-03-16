package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter.Adapter_HH;

public class QuantriActivity extends AppCompatActivity {
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
        adapter = new Adapter_HH(this, ListHH, typeface);
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


}
