package bizzmandi.com.bizzm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import bizzmandi.com.bizzm.R;


/**
 * Created by inhrawat on 12/1/2015.
 */
public class RootLeftDrawerListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public RootLeftDrawerListAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    static class ViewHolder {
        public TextView text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView=convertView;
        if(rowView==null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.left_drawer_cell, parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.option);
            rowView.setTag(viewHolder);

        }
        ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.text.setText(values[position]);


        return rowView;
    }

}
