package Model;

/**
 * Created by Adam on 11/21/2016.
 */

public class BenhViens {
private  String name;
    private String mabv;
//    private String placeid;
//    private Double kinhdo;
//    private Double vido;


    public BenhViens() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMabv() {
        return mabv;
    }

    public void setMabv(String mabv) {
        this.mabv = mabv;
    }

//    public String getPlaceid() {
//        return placeid;
//    }
//
//    public void setPlaceid(String placeid) {
//        this.placeid = placeid;
//    }
//
//    public Double getKinhdo() {
//        return kinhdo;
//    }
//
//    public void setKinhdo(Double kinhdo) {
//        this.kinhdo = kinhdo;
//    }
//
//    public Double getVido() {
//        return vido;
//    }
//
//    public void setVido(Double vido) {
//        this.vido = vido;
//    }

    @Override
    public String toString() {
        return "BenhViens{" +
                "name='" + name + '\'' +
                ", mabv='" + mabv + '\'' +
//                ", placeid='" + placeid + '\'' +
//                ", kinhdo=" + kinhdo +
//                ", vido=" + vido +
                '}';
    }
}
