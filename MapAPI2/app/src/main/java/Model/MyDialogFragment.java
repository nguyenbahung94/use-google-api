package Model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.adam.mapapi2.MapsActivity;
import com.example.adam.mapapi2.R;

import java.util.List;

/**
 * Created by Adam on 8/26/2016.
 */
public class MyDialogFragment extends android.support.v4.app.DialogFragment {
    List<onePlace> placeList;
    ListView listView;
    ReturnOneplace returnOneplace;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.showlist_dialog,container,false);
        MapsActivity activity=(MapsActivity)getActivity();
        placeList=activity.getMyList();
        listView=(ListView)rootview.findViewById(R.id.list_view);
        getDialog().setTitle("List Hospital");
        CustomAlertAdapter adapter=new CustomAlertAdapter(getActivity(),placeList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               returnOneplace.getOnePlace(placeList.get(position));
            }
        });
        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
returnOneplace =(ReturnOneplace)context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public interface ReturnOneplace{
         void getOnePlace(onePlace onePlace);
    }
}
