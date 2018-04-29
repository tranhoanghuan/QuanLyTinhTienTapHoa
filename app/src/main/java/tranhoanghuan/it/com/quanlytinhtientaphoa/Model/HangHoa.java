package tranhoanghuan.it.com.quanlytinhtientaphoa.Model;

import java.io.Serializable;

/**
 * Created by tranh on 13/03/2018.
 */

public class HangHoa implements Serializable {
    private String tenHang;
    private long giaSi;
    private long Giale;
    private float slGiasi;

    public HangHoa() {
    }

    public HangHoa(String tenHang, long giaSi, long giale, float slGiasi) {
        this.tenHang = tenHang;
        this.giaSi = giaSi;
        Giale = giale;
        this.slGiasi = slGiasi;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public long getGiaSi() {
        return giaSi;
    }

    public void setGiaSi(long giaSi) {
        this.giaSi = giaSi;
    }

    public long getGiale() {
        return Giale;
    }

    public void setGiale(long giale) {
        Giale = giale;
    }

    public float getSlGiasi() {
        return slGiasi;
    }

    public void setSlGiasi(float slGiasi) {
        this.slGiasi = slGiasi;
    }
}
