package com.pragyaware.anu.solarpack.mAdaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sarbjit on 06/16/2017.
 */
public class GridAdaptar extends BaseAdapter {

    private Context context;
    private ArrayList<String> strings;

    public GridAdaptar(Context context, ArrayList<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int i) {
        return strings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        ViewHolder viewHolder;
        if (view1 == null) {
            view1 = LayoutInflater.from(context).inflate(R.layout.adaptar_photo_grid, null);
            viewHolder = new ViewHolder(view1);
            view1.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view1.getTag();
        }

        String imgUrl = Constants.IMG_URL + strings.get(i) + "|100|100|40";
        Picasso.with(context).load(imgUrl).into(viewHolder.photoImgVw);

        return view1;
    }

    public class ViewHolder {
        ImageView photoImgVw;

        public ViewHolder(View view) {
            photoImgVw = (ImageView) view.findViewById(R.id.photoImgVw);
        }

    }

}
