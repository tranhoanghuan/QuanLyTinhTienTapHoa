package tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tranhoanghuan.it.com.quanlytinhtientaphoa.HangHoa;
import tranhoanghuan.it.com.quanlytinhtientaphoa.R;
import tranhoanghuan.it.com.quanlytinhtientaphoa.SuaHH;

/**
 * Created by tranh on 13/03/2018.
 */

public class Adapter_HH extends RecyclerView.Adapter<item_HH> {
    Context context;
    List<HangHoa> hangHoaList;
    private Typeface typeface;

    public Adapter_HH() {
    }

    public Adapter_HH(Context context, List<HangHoa> hangHoaList, Typeface typeface) {
        this.context = context;
        this.hangHoaList = hangHoaList;
        this.typeface = typeface;
    }

    @Override
    public item_HH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quantri, parent, false);
        return new item_HH(view);
    }

    @Override
    public void onBindViewHolder(item_HH holder, final int position) {
        holder.txtTensp.setText(hangHoaList.get(position).getName());
        holder.txtGiasp.setText(hangHoaList.get(position).getPrice() + " VND");
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SuaHH.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hangHoaList.size();
    }
}

class item_HH extends RecyclerView.ViewHolder {
    TextView txtTensp, txtGiasp;
    ImageView img_delete;

    public item_HH(View itemView) {
        super(itemView);
        txtTensp = itemView.findViewById(R.id.txtTensp);
        txtGiasp = itemView.findViewById(R.id.txtGiasp);
        img_delete = itemView.findViewById(R.id.img_delete);
    }
}
