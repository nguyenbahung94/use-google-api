package Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Adam on 5/29/2016.
 */
public class Route {
    public Distance distance;
    public DurationJ duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;
    public List<LatLng> points;

    public Distance getDistance() {
        return distance;
    }
}
