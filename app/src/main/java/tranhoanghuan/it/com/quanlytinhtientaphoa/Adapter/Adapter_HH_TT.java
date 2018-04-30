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
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaTinhtien;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IHanghoaTT;
import tranhoanghuan.it.com.quanlytinhtientaphoa.R;

public class Adapter_HH_TT extends RecyclerView.Adapter<item_HH_TT> {
    private Context context;
    private List<HanghoaTinhtien> hanghoaTinhtienList;
    private List<String> keyList_TT;
    private Typeface typeface;
    private IHanghoaTT iHanghoaTT;
    private ArrayList<Integer> index = new ArrayList();

    public Adapter_HH_TT() {
    }

    public Adapter_HH_TT(Context context, List<HanghoaTinhtien> hanghoaTinhtienList, List<String> keyList_TT ,Typeface typeface, IHanghoaTT iHanghoaTT) {
        this.context = context;
        this.hanghoaTinhtienList = hanghoaTinhtienList;
        this.keyList_TT = keyList_TT;
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
        final DecimalFormat dec = new DecimalFormat("##,###,###,###");
        final HanghoaTinhtien tinhtien = hanghoaTinhtienList.get(position);
        holder.txtTenspTT.setText(tinhtien.getHangHoa().getTenHang());
        holder.txt_sl_sp.setText(Float.toString(tinhtien.getSoLuong()));
        if(tinhtien.getSoLuong() >= tinhtien.getHangHoa().getSlGiasi()){
            tinhtien.setDonGiaTT(tinhtien.getHangHoa().getGiaSi());
            holder.txtGiaTT.setText(dec.format(tinhtien.getDonGiaTT()) + " VND");
        }
        else {
            tinhtien.setDonGiaTT(tinhtien.getHangHoa().getGiale());
            holder.txtGiaTT.setText(dec.format(tinhtien.getDonGiaTT()) + " VND");
        }
        holder.imgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tinhtien.getSoLuong() > 1){
                    tinhtien.setSoLuong( tinhtien.getSoLuong()-1F);
                    holder.txt_sl_sp.setText(Float.toString(tinhtien.getSoLuong()));
                    if(tinhtien.getSoLuong() >= tinhtien.getHangHoa().getSlGiasi()){
                        tinhtien.setDonGiaTT(tinhtien.getHangHoa().getGiaSi());
                        holder.txtGiaTT.setText(dec.format(tinhtien.getDonGiaTT()) + " VND");
                    }
                    else {
                        tinhtien.setDonGiaTT(tinhtien.getHangHoa().getGiale());
                        holder.txtGiaTT.setText(dec.format(tinhtien.getDonGiaTT()) + " VND");
                    }
                }
            }
        });
        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tinhtien.setSoLuong(tinhtien.getSoLuong());
                holder.txt_sl_sp.setText(Float.toString(tinhtien.getSoLuong()));
                if(tinhtien.getSoLuong() >= tinhtien.getHangHoa().getSlGiasi()){
                    tinhtien.setDonGiaTT(tinhtien.getHangHoa().getGiaSi());
                    holder.txtGiaTT.setText(dec.format(tinhtien.getDonGiaTT()) + " VND");
                }
                else {
                    tinhtien.setDonGiaTT(tinhtien.getHangHoa().getGiale());
                    holder.txtGiaTT.setText(dec.format(tinhtien.getDonGiaTT()) + " VND");
                }
            }
        });
        holder.img_delete_tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHanghoaTT.delHanghoaTT(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float sl_1 = Float.parseFloat(holder.txt_sl_sp.getText().toString());
                if(sl_1 > 0) {
                    tinhtien.setSoLuong(sl_1);
                }
                else {
                    holder.txt_sl_sp.setText(Float.toString(tinhtien.getSoLuong()));
                }
                if(tinhtien.getSoLuong() >= tinhtien.getHangHoa().getSlGiasi()){
                    tinhtien.setDonGiaTT(tinhtien.getHangHoa().getGiaSi());
                    holder.txtGiaTT.setText(dec.format(tinhtien.getDonGiaTT()) + " VND");
                }
                else {
                    tinhtien.setDonGiaTT(tinhtien.getHangHoa().getGiale());
                    holder.txtGiaTT.setText(dec.format(tinhtien.getDonGiaTT()) + " VND");
                }
            }
        });
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
