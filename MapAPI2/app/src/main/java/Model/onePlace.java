package Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Adam on 6/12/2016.
 */
public class onePlace {
    private LatLng CurrentLocation;
    private String name;
    private String address;
    private String mplaceId;

    public onePlace() {
    }

    public onePlace(LatLng currentLocation, String name, String address, String mplaceId) {
        CurrentLocation = currentLocation;
        this.name = name;
        this.address = address;
        this.mplaceId = mplaceId;
    }

    public LatLng getCurrentLocation() {
        return CurrentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        CurrentLocation = currentLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMplaceId() {
        return mplaceId;
    }

    public void setMplaceId(String mplaceId) {
        this.mplaceId = mplaceId;
    }

    @Override
    public String toString() {
        return "onePlace{" +
                "CurrentLocation=" + CurrentLocation +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", mplaceId='" + mplaceId + '\'' +
                '}';
    }
}
