package com.example.streetfix.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streetfix.R;
import com.example.streetfix.data.model.MyReport;

import java.util.List;

public class MyReportsAdapter extends RecyclerView.Adapter<MyReportsAdapter.ReportViewHolder> {

    public interface OnReportClickListener {
        void onReportClick(MyReport report);
    }

    private List<MyReport> reportList;
    private final OnReportClickListener listener;

    public MyReportsAdapter(List<MyReport> reportList, OnReportClickListener listener) {
        this.reportList = reportList;
        this.listener = listener;
    }

    public void updateList(List<MyReport> newList) {
        this.reportList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        MyReport report = reportList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(report.getUserPhotoUrl())
                .placeholder(report.getImageResId()) // tvoj sample placeholder
                .error(report.getImageResId())
                .circleCrop()
                .into(holder.imageReport);
        holder.textCategory.setText(report.getCategory());
        holder.textTitle.setText(report.getTitle());
        holder.textLocation.setText(report.getLocation());
        holder.textTime.setText(report.getTime());
        holder.textSupports.setText(report.getSupports() + " supports");
        holder.textComments.setText(report.getComments() + " comments");

        String status = report.getStatus();
        holder.textStatus.setText(formatStatus(status));

        if ("new".equals(status)) {
            holder.textStatus.setBackgroundResource(R.drawable.bg_status_new);
            holder.textStatus.setTextColor(holder.itemView.getContext().getColor(R.color.status_new_text));
        } else if ("in-progress".equals(status)) {
            holder.textStatus.setBackgroundResource(R.drawable.bg_status_in_progress);
            holder.textStatus.setTextColor(holder.itemView.getContext().getColor(R.color.status_progress_text));
        } else {
            holder.textStatus.setBackgroundResource(R.drawable.bg_status_resolved);
            holder.textStatus.setTextColor(holder.itemView.getContext().getColor(R.color.status_resolved_text));
        }

        holder.itemView.setOnClickListener(v -> listener.onReportClick(report));
    }

    @Override
    public int getItemCount() {
        return reportList != null ? reportList.size() : 0;
    }

    private String formatStatus(String status) {
        switch (status) {
            case "new":
                return "New";
            case "in-progress":
                return "In Progress";
            case "resolved":
                return "Resolved";
            default:
                return status;
        }
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        ImageView imageReport;
        TextView textCategory, textStatus, textTitle, textLocation, textTime, textSupports, textComments;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            imageReport = itemView.findViewById(R.id.imageReport);
            textCategory = itemView.findViewById(R.id.textCategory);
            textStatus = itemView.findViewById(R.id.textStatus);
            textTitle = itemView.findViewById(R.id.textTitle);
            textLocation = itemView.findViewById(R.id.textLocation);
            textTime = itemView.findViewById(R.id.textTime);
            textSupports = itemView.findViewById(R.id.textSupports);
            textComments = itemView.findViewById(R.id.textComments);
        }
    }
}