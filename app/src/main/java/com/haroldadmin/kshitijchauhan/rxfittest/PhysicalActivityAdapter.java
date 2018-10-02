package com.haroldadmin.kshitijchauhan.rxfittest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PhysicalActivityAdapter extends RecyclerView.Adapter<PhysicalActivityAdapter.ViewHolder> {

    private List<PhysicalActivity> listOfActivities;
    private Context context;

    public PhysicalActivityAdapter(List<PhysicalActivity> list, Context context) {
        this.listOfActivities = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.physical_activity_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindValues(listOfActivities.get(position));
    }

    @Override
    public int getItemCount() {
        return listOfActivities.size();
    }

    public void updateList(List<PhysicalActivity> list) {
        DiffUtilCallback diffUtilCallback = new DiffUtilCallback(this.listOfActivities, list);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        diffResult.dispatchUpdatesTo(this);

        this.listOfActivities.clear();
        this.listOfActivities.addAll(list);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView activityImageView;
        TextView activityTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            activityImageView = itemView.findViewById(R.id.activity_image_view);
            activityTextView = itemView.findViewById(R.id.activity_name_textview);
            startTimeTextView = itemView.findViewById(R.id.start_time_value);
            endTimeTextView = itemView.findViewById(R.id.end_time_value);
        }

        void bindValues(PhysicalActivity activity) {

            String timeFormat = "HH:mm a";
            SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);

            Date startDate = new Date(activity.getStartTime());
            Date endDate = new Date(activity.getEndTime());
            String startTime = formatter.format(startDate);
            String endTime = formatter.format(endDate);

            this.activityTextView.setText(activity.getName());
            this.startTimeTextView.setText(startTime);
            this.endTimeTextView.setText(endTime);

            switch (activity.getName().toLowerCase()) {
                case "running":
                    Glide.with(context).load(R.drawable.ic_baseline_directions_run_24px).into(activityImageView);
                    break;
                case "walking":
                    Glide.with(context).load(R.drawable.ic_baseline_directions_walk_24px).into(activityImageView);
                    break;
                case "in_vehicle":
                    Glide.with(context).load(R.drawable.ic_baseline_directions_car_24px).into(activityImageView);
                    break;
                default:
                    Glide.with(context).load(R.drawable.ic_baseline_fitness_center_24px).into(activityImageView);
            }
        }
    }

    class DiffUtilCallback extends DiffUtil.Callback {

        List<PhysicalActivity> oldList;
        List<PhysicalActivity> newList;

        public DiffUtilCallback(List<PhysicalActivity> oldList, List<PhysicalActivity> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return false;
        }
    }

}
