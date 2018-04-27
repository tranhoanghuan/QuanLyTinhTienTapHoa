package tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import tranhoanghuan.it.com.quanlytinhtientaphoa.HanghoaTinhtien;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IHanghoaTT;
import tranhoanghuan.it.com.quanlytinhtientaphoa.R;

public class Adapter_HH_TT extends RecyclerView.Adapter<item_HH_TT> {
    private Context context;
    private List<HanghoaTinhtien> hanghoaTinhtienList;
    private Typeface typeface;
    private IHanghoaTT iHanghoaTT;

    public Adapter_HH_TT() {
    }

    public Adapter_HH_TT(Context context, List<HanghoaTinhtien> hanghoaTinhtienList, Typeface typeface, IHanghoaTT iHanghoaTT) {
        this.context = context;
        this.hanghoaTinhtienList = hanghoaTinhtienList;
        this.typeface = typeface;
        this.iHanghoaTT = iHanghoaTT;
    }

    @Override
    public item_HH_TT onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tinhtien, parent, false);
        return new item_HH_TT(view);
    }

    @Override
    public void onBindViewHolder(final item_HH_TT holder, final int position) {
        final HanghoaTinhtien tinhtien = hanghoaTinhtienList.get(position);
        final DecimalFormat dec = new DecimalFormat("##,###,###,###");
        holder.txtTenspTT.setText(tinhtien.getHangHoa().getTenHang());
        final float[] sl = {tinhtien.getSoLuong()};
        holder.imgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sl[0] >1){
                    sl[0]--;
                    tinhtien.setSoLuong(sl[0]);
                    holder.txtGiaTT.setText(dec.format(tinhtien.getHangHoa().getGiaSi()) + " VND");
                }
            }
        });
        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sl[0]++;
                tinhtien.setSoLuong(sl[0]);
                holder.txtGiaTT.setText(dec.format(tinhtien.getHangHoa().getGiaSi()) + " VND");
            }
        });
        holder.img_delete_tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHanghoaTT.delHanghoaTT(position);
            }
        });
        holder.txt_sl_sp.setText(Float.toString(sl[0]));
        if(sl[0] >= tinhtien.getHangHoa().getSlGiasi()){
            holder.txtGiaTT.setText(dec.format(tinhtien.getHangHoa().getGiaSi()) + " VND");
        }
        else {
            holder.txtGiaTT.setText(dec.format(tinhtien.getHangHoa().getGiale()) + " VND");
        }
    }

    @Override
    public int getItemCount() {
        return hanghoaTinhtienList.size();
    }
}

class item_HH_TT extends RecyclerView.ViewHolder {
    TextView txtTenspTT, txtGiaTT;
    EditText txt_sl_sp;
    ImageView img_delete_tt, imgPlus, imgSub;
    public item_HH_TT(View itemView) {
        super(itemView);
        txtTenspTT = itemView.findViewById(R.id.txtTensp_tt);
        txtGiaTT = itemView.findViewById(R.id.txt_gia_tt);
        txt_sl_sp = itemView.findViewById(R.id.txt_sl_sp);
        img_delete_tt = itemView.findViewById(R.id.img_delete_tt);
        imgPlus = itemView.findViewById(R.id.imgPlus);
        imgSub = itemView.findViewById(R.id.imgSub);
    }
}
