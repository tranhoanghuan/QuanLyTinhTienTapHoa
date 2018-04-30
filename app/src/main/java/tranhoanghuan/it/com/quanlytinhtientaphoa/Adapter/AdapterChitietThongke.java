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

import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaThongke;
import tranhoanghuan.it.com.quanlytinhtientaphoa.R;

public class AdapterChitietThongke extends RecyclerView.Adapter<item_CTKT> {
    private Context contextCTTK;
    private List<HanghoaThongke> HoadonListCTTK;
    private Typeface typefaceCTTK;

    public AdapterChitietThongke(){

    }

    public AdapterChitietThongke(Context contextCTTK, List<HanghoaThongke> hoadonListCTTK, Typeface typefaceCTTK) {
        this.contextCTTK = contextCTTK;
        this.HoadonListCTTK = hoadonListCTTK;
        this.typefaceCTTK = typefaceCTTK;
    }

    @Override
    public item_CTKT onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextCTTK).inflate(R.layout.item_thongkechitiet, parent, false);
        return new item_CTKT(view);
    }

    @Override
    public void onBindViewHolder(item_CTKT holder, int position) {
        DecimalFormat dec = new DecimalFormat("##,###,###,###");
        HanghoaThongke thongke = HoadonListCTTK.get(position);
        holder.txtTenHangTK.setText(thongke.getTenHanghoaTK());
        holder.txtSoluongTK.setText(Float.toString(thongke.getSoLuongTK()));
        holder.txtTongTK.setText(dec.format(thongke.getTongHanghoaTK()));
    }

    @Override
    public int getItemCount() {
        return HoadonListCTTK.size();
    }
}

class item_CTKT extends RecyclerView.ViewHolder {
    TextView txtTenHangTK, txtSoluongTK, txtTongTK;

    public item_CTKT(View itemView) {
        super(itemView);
        txtTenHangTK = itemView.findViewById(R.id.txtTenHangTKCT);
        txtSoluongTK = itemView.findViewById(R.id.txtSoluongTKCT);
        txtTongTK = itemView.findViewById(R.id.txtTongTKCT);
    }
}
