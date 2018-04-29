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

import java.text.DecimalFormat;
import java.util.List;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HangHoa;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IHanghoa;
import tranhoanghuan.it.com.quanlytinhtientaphoa.R;
import tranhoanghuan.it.com.quanlytinhtientaphoa.SuaHH;

/**
 * Created by tranh on 13/03/2018.
 */

public class Adapter_HH extends RecyclerView.Adapter<item_HH> {
    private Context context;
    private List<HangHoa> hangHoaList;
    private List<String> keyList;
    private Typeface typeface;
    private IHanghoa iHanghoa;
    private String UID;

    public Adapter_HH() {
    }

    public Adapter_HH(Context context, List<HangHoa> hangHoaList, List<String> keyList,Typeface typeface, IHanghoa iHanghoa, String UID) {
        this.context = context;
        this.hangHoaList = hangHoaList;
        this.keyList = keyList;
        this.typeface = typeface;
        this.iHanghoa = iHanghoa;
        this.UID = UID;
    }

    @Override
    public item_HH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quantri, parent, false);
        return new item_HH(view);
    }

    @Override
    public void onBindViewHolder(item_HH holder, final int position) {
        HangHoa h = hangHoaList.get(position);
        DecimalFormat dec = new DecimalFormat("##,###,###,###");
        holder.txtTensp.setText(hangHoaList.get(position).getTenHang());
        holder.txtGiasi.setText("Giá sỉ: " + dec.format(h.getGiaSi()) + " VND");
        holder.txtGiale.setText("Giá lẻ: " + dec.format(h.getGiale()) + " VND");
        holder.txtslGiasi.setText("Số lượng (giá sỉ): " + h.getSlGiasi());
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHanghoa.delHanghoa(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SuaHH.class);
                intent.putExtra("item", hangHoaList.get(position));
                intent.putExtra("UID_QT", UID);
                intent.putExtra("key", keyList.get(position));
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
    TextView txtTensp, txtGiasi, txtGiale, txtslGiasi;
    ImageView img_delete;

    public item_HH(View itemView) {
        super(itemView);
        txtslGiasi = itemView.findViewById(R.id.txtslGiasi);
        txtTensp = itemView.findViewById(R.id.txtTensp);
        txtGiasi = itemView.findViewById(R.id.txtGiasi);
        txtGiale = itemView.findViewById(R.id.txtGiale);
        img_delete = itemView.findViewById(R.id.img_delete);
    }
}
