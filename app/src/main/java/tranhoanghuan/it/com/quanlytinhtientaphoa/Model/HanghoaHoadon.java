package tranhoanghuan.it.com.quanlytinhtientaphoa.Model;

import java.io.Serializable;

public class HanghoaHoadon implements Serializable {
    private String tenHanghoaTT;
    private float soLuongHanghoa;
    private long donGia;

    public HanghoaHoadon() {
    }

    public HanghoaHoadon(String tenHanghoaTT, float soLuongHanghoa, long donGia) {
        this.tenHanghoaTT = tenHanghoaTT;
        this.soLuongHanghoa = soLuongHanghoa;
        this.donGia = donGia;
    }

    public String getTenHanghoaTT() {
        return tenHanghoaTT;
    }

    public void setTenHanghoaTT(String tenHanghoaTT) {
        this.tenHanghoaTT = tenHanghoaTT;
    }

    public float getSoLuongHanghoa() {
        return soLuongHanghoa;
    }

    public void setSoLuongHanghoa(float soLuongHanghoa) {
        this.soLuongHanghoa = soLuongHanghoa;
    }

    public long getDonGia() {
        return donGia;
    }

    public void setDonGia(long donGia) {
        this.donGia = donGia;
    }
}
