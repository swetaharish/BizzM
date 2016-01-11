package bizzmandi.com.bizzm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import bizzmandi.com.bizzm.R;
import bizzmandi.com.bizzm.models.CategoryModel;

/**
 * Created by vij on 12/10/2015.
 */
public class CategoryAdapter extends ArrayAdapter<CategoryModel> {

private final Context context;
ArrayList<CategoryModel> list;

public CategoryAdapter(Context context, ArrayList<CategoryModel> list) {
        super(context, -1, list);
        this.context = context;
        this.list=list;
        }

static class ViewHolder {
    public TextView text;
//    public CheckBox categoryCheck;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView=convertView;
        if(rowView==null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.category_cell, parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.option);
//            viewHolder.categoryCheck= (CheckBox)rowView.findViewById(R.id.categoryCheck);
//            viewHolder.categoryCheck.setVisibility(View.INVISIBLE);
            rowView.setTag(viewHolder);

        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
//        holder.categoryCheck.setVisibility(View.INVISIBLE);
        holder.text.setText(list.get(position).getCategoryName());


        return rowView;
    }

}

