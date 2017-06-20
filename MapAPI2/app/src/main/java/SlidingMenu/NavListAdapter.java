package SlidingMenu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adam.mapapi2.R;

import java.util.List;

/**
 * Created by Adam on 6/9/2016.
 */
public class NavListAdapter extends ArrayAdapter<NavItems> {
    Context context;
    int reslayout;
    List<NavItems> listNavItems;
    public NavListAdapter(Context context, int reslayout, List<NavItems> listNavItems){
        super(context,reslayout,listNavItems);
        this.context=context;
        this.reslayout=reslayout;
        this.listNavItems=listNavItems;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=View.inflate(context,reslayout,null);
        TextView tvtitle=(TextView)v.findViewById(R.id.title_nav);
        ImageView nav_image=(ImageView) v.findViewById(R.id.nav_icon);
        NavItems nav_item=listNavItems.get(position);
        tvtitle.setText(nav_item.getTitle());
        nav_image.setImageResource(nav_item.getResIcon());
        return v;
    }
}
