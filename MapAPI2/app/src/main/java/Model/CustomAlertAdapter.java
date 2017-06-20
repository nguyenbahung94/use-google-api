package Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.adam.mapapi2.R;

import java.util.List;

/**
 * Created by Adam on 8/22/2016.
 */
public class CustomAlertAdapter extends ArrayAdapter<onePlace> {
    Context mcontext=null;
    List<onePlace> listarray=null;
    LayoutInflater mInflater=null;

    public CustomAlertAdapter(Context context,List<onePlace> objects) {
        super(context, R.layout.list_customdialog, objects);
        mInflater=LayoutInflater.from(context);
        this.mcontext=context;
        this.listarray=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.list_customdialog,null);

            viewHolder.name=(TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.address=(TextView)convertView.findViewById(R.id.tv_address);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        onePlace onePlacetam=listarray.get(position);
        viewHolder.name.setText(onePlacetam.getName());
        viewHolder.address.setText(onePlacetam.getAddress());


        return convertView;
    }
    private static  class ViewHolder{
    TextView name;
    TextView address;
    }
}
