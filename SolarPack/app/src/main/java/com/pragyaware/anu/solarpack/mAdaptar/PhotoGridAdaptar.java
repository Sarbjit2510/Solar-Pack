package com.pragyaware.anu.solarpack.mAdaptar;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pragyaware.anu.solarpack.R;

import java.util.ArrayList;

/**
 * Created by sarbjit on 06/09/2017.
 */
public class PhotoGridAdaptar extends BaseAdapter {

    private Context context;
    private ArrayList<Bitmap> bitmaps;

    public PhotoGridAdaptar(Context context, ArrayList<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int i) {
        return bitmaps.get(i);
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

        viewHolder.photoImgVw.setImageBitmap(bitmaps.get(i));

        return view1;
    }

    public class ViewHolder {
        ImageView photoImgVw;

        ViewHolder(View view) {
            photoImgVw = (ImageView) view.findViewById(R.id.photoImgVw);
        }

    }

}
