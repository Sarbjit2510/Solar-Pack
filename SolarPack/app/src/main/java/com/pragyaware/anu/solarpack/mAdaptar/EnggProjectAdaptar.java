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
import com.pragyaware.anu.solarpack.mActivity.SiteEnggActivity;
import com.pragyaware.anu.solarpack.mActivity.SiteEnggFullUpdate;
import com.pragyaware.anu.solarpack.mActivity.SiteEngineerUpdate;
import com.pragyaware.anu.solarpack.mModel.DivisionModel;
import com.pragyaware.anu.solarpack.mModel.EnggMilestoneModel;
import com.pragyaware.anu.solarpack.mModel.EnggViewModel;
import com.pragyaware.anu.solarpack.mModel.MediaModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by sarbjit on 07/05/2017.
 */

public class EnggProjectAdaptar extends RecyclerView.Adapter<EnggProjectAdaptar.ViewHolder> {

    private Context context;
    private ArrayList<EnggViewModel> projectModels;
    private Realm realm;

    public EnggProjectAdaptar(Context context, ArrayList<EnggViewModel> projectModels) {
        this.context = context;
        this.projectModels = projectModels;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adaptar_view_project, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.benefTxtVw.setText(projectModels.get(position).getVendorID());
        holder.nameTxtVw.setText(projectModels.get(position).getVendorName());
        holder.villageTxtVw.setText(projectModels.get(position).getProjectVillage());
        String block = realm.where(DivisionModel.class).equalTo("ID", projectModels.get(position).getProjectDivisionID()).findFirst().getDivisionName();
        holder.blockTxtVw.setText(block);
        if (projectModels.get(position).getModels().get(projectModels.get(position).getModels().size() - 1).getReportAStatus().equalsIgnoreCase("1"))
            holder.projectTxtVw.setText("Approved");
        else
            holder.projectTxtVw.setText("Disapproved");
    }

    @Override
    public int getItemCount() {
        return projectModels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView benefTxtVw, nameTxtVw, villageTxtVw, blockTxtVw, projectTxtVw;

        ViewHolder(View itemView) {
            super(itemView);
            benefTxtVw = (TextView) itemView.findViewById(R.id.benefTxtVw);
            nameTxtVw = (TextView) itemView.findViewById(R.id.nameTxtVw);
            villageTxtVw = (TextView) itemView.findViewById(R.id.villageTxtVw);
            blockTxtVw = (TextView) itemView.findViewById(R.id.blockTxtVw);
            projectTxtVw = (TextView) itemView.findViewById(R.id.projectTxtVw);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RealmList<EnggMilestoneModel> mileStoneModels = projectModels.get(getAdapterPosition()).getModels();
                    RealmList<MediaModel> mediaModels = projectModels.get(getAdapterPosition()).getMediaModels();
                    ArrayList<EnggMilestoneModel> models = new ArrayList<>();
                    ArrayList<MediaModel> arrayList = new ArrayList<>();
                    models.addAll(mileStoneModels);
                    arrayList.addAll(mediaModels);
                    if (projectModels.get(getAdapterPosition()).getModels().get(projectModels.get(getAdapterPosition()).getModels().size() - 1)
                            .getReportAStatus().equalsIgnoreCase("1")) {
                        context.startActivity(new Intent(context, SiteEnggActivity.class).putExtra("data", projectModels.get(getAdapterPosition()))
                                .putParcelableArrayListExtra("milestone", models).putParcelableArrayListExtra("media", arrayList));
                    } else {
                        if (mileStoneModels.size() == 1) {
                            context.startActivity(new Intent(context, SiteEnggFullUpdate.class).putExtra("data", projectModels.get(getAdapterPosition()))
                                    .putParcelableArrayListExtra("milestone", models).putParcelableArrayListExtra("media", arrayList));
                        } else {
                            context.startActivity(new Intent(context, SiteEngineerUpdate.class).putExtra("data", projectModels.get(getAdapterPosition()))
                                    .putParcelableArrayListExtra("milestone", models).putParcelableArrayListExtra("media", arrayList));
                        }
                    }
                    ((Activity) context).overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                }
            });

        }
    }

}
