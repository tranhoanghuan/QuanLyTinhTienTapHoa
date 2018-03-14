package tranhoanghuan.it.com.quanlytinhtientaphoa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class QuantriActivity extends AppCompatActivity {
   // public static DatabaseReference mDatabase;;

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
