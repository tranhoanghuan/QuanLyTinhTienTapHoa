package tranhoanghuan.it.com.quanlytinhtientaphoa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import tranhoanghuan.it.com.quanlytinhtientaphoa.ChitietThongke;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Interface.IThongke;
import tranhoanghuan.it.com.quanlytinhtientaphoa.Model.HanghoaThongke;
import tranhoanghuan.it.com.quanlytinhtientaphoa.R;

public class Adapter_TK_TQ extends RecyclerView.Adapter<item_TK_TQ> {
    private Context contextTK;
    private List<String> listTK;
    private Typeface typefaceTK;
    private List<String> listTK_phu;
    private String loai;
    private IThongke iThongke;

    public Adapter_TK_TQ() {

    }

    public Adapter_TK_TQ(Context contextTK, List<String> listTK, Typeface typefaceTK, List<String> listTK_phu, String loai, IThongke iThongke) {
        this.contextTK = contextTK;
        this.listTK = listTK;
        this.typefaceTK = typefaceTK;
        this.listTK_phu = listTK_phu;
        this.loai = loai;
        this.iThongke = iThongke;
    }

    @Override
    public item_TK_TQ onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextTK).inflate(R.layout.item_thongke, parent, false);
        return new item_TK_TQ(view);
    }

    @Override
    public void onBindViewHolder(item_TK_TQ holder, final int position) {
        final String name = loai + " " + listTK.get(position) + listTK_phu.get(position);
        holder.txtThongke.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iThongke.thongkeChitiet(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTK.size();
    }
}

class item_TK_TQ extends RecyclerView.ViewHolder {
    TextView txtThongke;

    public item_TK_TQ(View itemView) {
        super(itemView);
        txtThongke = itemView.findViewById(R.id.txtThongke);
    }
}
