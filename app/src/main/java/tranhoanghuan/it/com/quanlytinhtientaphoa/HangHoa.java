package tranhoanghuan.it.com.quanlytinhtientaphoa;

import java.io.Serializable;

/**
 * Created by tranh on 13/03/2018.
 */

public class HangHoa implements Serializable {
    private String name;
    private long price;

    public HangHoa() {
    }

    public HangHoa(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
