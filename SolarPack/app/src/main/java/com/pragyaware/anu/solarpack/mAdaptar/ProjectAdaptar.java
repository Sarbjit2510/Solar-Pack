package com.pragyaware.anu.solarpack.mAdaptar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mActivity.ViewReportActivity;
import com.pragyaware.anu.solarpack.mModel.ProjectModel;

import java.util.ArrayList;

/**
 * Created by sarbjit on 06/15/2017.
 */
public class ProjectAdaptar extends RecyclerView.Adapter<ProjectAdaptar.ViewHolder> {

    private Context context;
    private ArrayList<ProjectModel> projectModels;

    public ProjectAdaptar(Context context, ArrayList<ProjectModel> projectModels) {
        this.context = context;
        this.projectModels = projectModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapatar_project, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.projectTitleTxtVw.setText(context.getString(R.string.vendor_name) + " " +projectModels.get(position).getVendorName());
        holder.descTxtVw.setText(context.getString(R.string.village) + " " +projectModels.get(position).getReportVillage());
        holder.date_time.setText(context.getString(R.string.report_stamp) + " " +projectModels.get(position).getReportStamp());
        holder.typeTxtVw.setText(context.getString(R.string.project_typr) + " " + projectModels.get(position).getProjectType());
        holder.status.setText(context.getString(R.string.project_status) + " " + projectModels.get(position).getReportStatus());
        holder.user_name.setText(context.getString(R.string.user_name) + " " + projectModels.get(position).getUsrName());
        holder.user_mobile_no.setText(context.getString(R.string.user_mobile) + " " + projectModels.get(position).getUsrMobile());

    }

    @Override
    public int getItemCount() {
        return projectModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView projectTitleTxtVw, descTxtVw, typeTxtVw,status,date_time,user_name,user_mobile_no;

        ViewHolder(View itemView) {
            super(itemView);

            projectTitleTxtVw = (TextView) itemView.findViewById(R.id.projectTitleTxtVw);
            descTxtVw = (TextView) itemView.findViewById(R.id.descTxtVw);
            typeTxtVw = (TextView) itemView.findViewById(R.id.typeTxtVw);
            status=(TextView) itemView.findViewById(R.id.status);
            date_time=(TextView)itemView.findViewById(R.id.date_time);
            user_name=(TextView) itemView.findViewById(R.id.user_name);
            user_mobile_no=(TextView)itemView.findViewById(R.id.user_mobile_no);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    context.startActivity(new Intent(context, ViewReportActivity.class).putExtra("data", projectModels.get(getAdapterPosition())));
                    ((Activity) context).overridePendingTransition(R.anim.bottom_in, R.anim.top_out);

                }
            });

        }
    }

}
