package bizzmandi.com.bizzm.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import bizzmandi.com.bizzm.R;
import bizzmandi.com.bizzm.ui.fragments.BaseFragment;
import bizzmandi.com.bizzm.ui.fragments.ProductRegistrationFragment;

/**
 * Created by inhrawat on 12/15/2015.
 */
public class ImgViewPagerAdapter extends PagerAdapter {

    int layout;
    Context ctx;
    LayoutInflater inflater;
    BaseFragment pdf;

    public ImgViewPagerAdapter(Context ctx,int layout,BaseFragment pdf) {

        this.layout = layout;
        this.ctx = ctx;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pdf=pdf;
    }
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = inflater.inflate(layout, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.productImg);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            pdf.setImage(view);
            }
        });

        container.addView(itemView);

        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
