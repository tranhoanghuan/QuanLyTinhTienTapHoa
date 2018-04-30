package tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaHoadon;
import tranhoanghuan.it.com.quanlytinhtientaphoa.R;

public class AdapterHoadon extends RecyclerView.Adapter<item_HD> {
    private Context contextHD;
    private List<HanghoaHoadon> HoadonList;
    private Typeface typefaceHD;

    public AdapterHoadon() {

    }

    public AdapterHoadon(Context contextHD, List<HanghoaHoadon> HoadonList, Typeface typefaceHD) {
        this.contextHD = contextHD;
        this.HoadonList = HoadonList;
        this.typefaceHD = typefaceHD;
    }

    @Override
    public item_HD onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextHD).inflate(R.layout.item_hoadon, parent, false);
        return new item_HD(view);
    }

    @Override
    public void onBindViewHolder(item_HD holder, int position) {
        HanghoaHoadon hoadon = HoadonList.get(position);
        DecimalFormat dec = new DecimalFormat("##,###,###,###");
        holder.txtTenHanghd.setText(hoadon.getTenHanghoaTT());
        holder.txtDongiahd.setText(dec.format(hoadon.getDonGia()) + " VND");
        holder.txtSoluonghd.setText(Float.toString(hoadon.getSoLuongHanghoa()));
        long tongtien = (long) (hoadon.getDonGia()*hoadon.getSoLuongHanghoa());
        holder.txtTongHHhd.setText(dec.format(tongtien) + " VND");
    }

    @Override
    public int getItemCount() {
        return HoadonList.size();
    }
}

class item_HD extends RecyclerView.ViewHolder {
    TextView txtTenHanghd, txtDongiahd, txtSoluonghd, txtTongHHhd;

    public item_HD(View itemView) {
        super(itemView);
        txtTenHanghd = itemView.findViewById(R.id.txtTenHanghd);
        txtDongiahd = itemView.findViewById(R.id.txtDongiahd);
        txtSoluonghd = itemView.findViewById(R.id.txtSoluonghd);
        txtTongHHhd = itemView.findViewById(R.id.txtTongHHhd);
    }
}
