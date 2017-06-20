package Model;

/**
 * Created by Adam on 8/22/2016.
 */
public class Time {
    private String nameHospital;
    private String mkm;
    private String mtime;

    public Time(String nameHospital, String mkm, String mtime) {
        this.nameHospital = nameHospital;
        this.mkm = mkm;
        this.mtime = mtime;
    }

    public String getNameHospital() {
        return nameHospital;
    }

    public void setNameHospital(String nameHospital) {
        this.nameHospital = nameHospital;
    }

    public String getMkm() {
        return mkm;
    }

    public void setMkm(String mkm) {
        this.mkm = mkm;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }
}
