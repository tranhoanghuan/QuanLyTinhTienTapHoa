package tranhoanghuan.it.com.quanlytinhtientaphoa;

import java.io.Serializable;

public class HanghoaTinhtien implements Serializable {
    private HangHoa hangHoa;
    private float soLuong;

    public HanghoaTinhtien() {

    }

    public HanghoaTinhtien(HangHoa hangHoa, float soLuong) {
        this.hangHoa = hangHoa;
        this.soLuong = soLuong;
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
}
