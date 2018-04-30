package tranhoanghuan.it.com.quanlytinhtientaphoa.Model;

import java.io.Serializable;

public class HanghoaThongke implements Serializable{
    private String tenHanghoaTK;
    private float soLuongTK;
    private long TongHanghoaTK;

    public HanghoaThongke() {

    }

    public HanghoaThongke(String tenHanghoaTK, float soLuongTK, long tongHanghoaTK) {
        this.tenHanghoaTK = tenHanghoaTK;
        this.soLuongTK = soLuongTK;
        TongHanghoaTK = tongHanghoaTK;
    }

    public String getTenHanghoaTK() {
        return tenHanghoaTK;
    }

    public void setTenHanghoaTK(String tenHanghoaTK) {
        this.tenHanghoaTK = tenHanghoaTK;
    }

    public float getSoLuongTK() {
        return soLuongTK;
    }

    public void setSoLuongTK(float soLuongTK) {
        this.soLuongTK = soLuongTK;
    }

    public long getTongHanghoaTK() {
        return TongHanghoaTK;
    }

    public void setTongHanghoaTK(long tongHanghoaTK) {
        TongHanghoaTK = tongHanghoaTK;
    }
}
