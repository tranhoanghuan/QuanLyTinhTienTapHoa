package tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HangHoa;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IHanghoaTTBT;
import tranhoanghuan.it.com.quanlytinhtientaphoa.R;

public class Adapter_HH_TTBT extends RecyclerView.Adapter<item_HH_TTBT> {
    private Context context_ttbt;
    private List<HangHoa> hangHoaList_ttbt;
    private List<String> keyList_ttbt;
    private Typeface typeface_ttbt;
    private IHanghoaTTBT iHanghoa_ttbt;

    public Adapter_HH_TTBT() {

    }

    public Adapter_HH_TTBT(Context context_ttbt, List<HangHoa> hangHoaList_ttbt, List<String> keyList_ttbt, Typeface typeface_ttbt, IHanghoaTTBT iHanghoa_ttbt) {
        this.context_ttbt = context_ttbt;
        this.hangHoaList_ttbt = hangHoaList_ttbt;
        this.keyList_ttbt = keyList_ttbt;
        this.typeface_ttbt = typeface_ttbt;
        this.iHanghoa_ttbt = iHanghoa_ttbt;
    }

    @Override
    public item_HH_TTBT onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context_ttbt).inflate(R.layout.item_tinhtienbangtay, parent, false);
        return new item_HH_TTBT(view);
    }

    @Override
    public void onBindViewHolder(item_HH_TTBT holder, final int position) {
        HangHoa h = hangHoaList_ttbt.get(position);
        DecimalFormat dec = new DecimalFormat("##,###,###,###");
        holder.txtTensp_ttbt.setText(hangHoaList_ttbt.get(position).getTenHang());
        holder.txtGiasi_ttbt.setText("Giá sỉ: " + dec.format(h.getGiaSi()) + " VND");
        holder.txtGiale_ttbt.setText("Giá lẻ: " + dec.format(h.getGiale()) + " VND");
        holder.txtslGiasi_ttbt.setText("Số lượng (giá sỉ): " + h.getSlGiasi());
        ArrayList<HangHoa> listHanghoachon = new ArrayList<>();
        ArrayList<String> listKeychon = new ArrayList<>();
        holder.chk_ttbt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        iHanghoa_ttbt.chonHanghoa(position, true);
                    }
                    else {
                        iHanghoa_ttbt.chonHanghoa(position, false);
                    }
               }
         });

    }

    @Override
    public int getItemCount() {
        return hangHoaList_ttbt.size();
    }
}

class item_HH_TTBT extends RecyclerView.ViewHolder {
    TextView txtTensp_ttbt, txtGiasi_ttbt, txtGiale_ttbt, txtslGiasi_ttbt;
    CheckBox chk_ttbt;

    public item_HH_TTBT(View itemView) {
        super(itemView);
        txtslGiasi_ttbt = itemView.findViewById(R.id.txtslGiasi_ttbt);
        txtTensp_ttbt = itemView.findViewById(R.id.txtTensp_ttbt);
        txtGiasi_ttbt = itemView.findViewById(R.id.txtGiasi_ttbt);
        txtGiale_ttbt = itemView.findViewById(R.id.txtGiale_ttbt);
        chk_ttbt = itemView.findViewById(R.id.chk_ttbt);
    }
}
