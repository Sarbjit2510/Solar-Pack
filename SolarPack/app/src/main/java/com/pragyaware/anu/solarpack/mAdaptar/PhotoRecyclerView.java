package com.pragyaware.anu.solarpack.mAdaptar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mActivity.PreviewActivity;
import com.pragyaware.anu.solarpack.mUtil.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sarbjit on 07/05/2017.
 */

public class PhotoRecyclerView extends RecyclerView.Adapter<PhotoRecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<String> strings;

    public PhotoRecyclerView(Context context, ArrayList<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_adaptar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imgUrl = Constants.IMG_URL + strings.get(position) + "|300|300|80";
        Picasso.with(context).load(imgUrl).into(holder.photoImgVw);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImgVw;

        ViewHolder(View itemView) {
            super(itemView);
            photoImgVw = (ImageView) itemView.findViewById(R.id.photoImgVw);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PreviewActivity.class).putExtra("data", strings.get(getAdapterPosition())));
                    ((Activity) context).overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                }
            });
        }
    }


}
