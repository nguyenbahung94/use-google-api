package Model;

import java.util.List;

/**
 * Created by Adam on 5/29/2016.
 */
public interface DerectionListener {
    void onDirecterStart();
    void onDirecterSuccess(List<Route> route);
    void onSearchStart();
    void onSearchSuccess(List<onePlace> onePlaceList);
    void onStartTime();
    void onSuccessTime(List<Time> listTime);
    void onSuccessGetListKhoa(List<String> listkhoa);
    void onSuccessGetListBvKhoa(List<BenhViens> listBV);
}
