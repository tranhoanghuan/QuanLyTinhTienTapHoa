package tranhoanghuan.it.com.quanlytinhtientaphoa.Model;

import java.io.Serializable;

public class HanghoaTinhtien implements Serializable {
    private HangHoa hangHoa;
    private float soLuong;
    private long donGiaTT;

    public HanghoaTinhtien() {

    }

    public HanghoaTinhtien(HangHoa hangHoa, float soLuong, long donGiaTT) {
        this.hangHoa = hangHoa;
        this.soLuong = soLuong;
        this.donGiaTT = donGiaTT;
    }

    public HangHoa getHangHoa() {
        return hangHoa;
    }

    public void setHangHoa(HangHoa hangHoa) {
        this.hangHoa = hangHoa;
    }

    public float getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(float soLuong) {
        this.soLuong = soLuong;
    }

    public long getDonGiaTT() {
        return donGiaTT;
    }

    public void setDonGiaTT(long donGiaTT) {
        this.donGiaTT = donGiaTT;
    }
}
