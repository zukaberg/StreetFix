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
import com.example.streetfix.data.model.ReportItem;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    public interface OnReportClickListener {
        void onReportClick(ReportItem report);
    }

    private final List<ReportItem> reports;
    private final OnReportClickListener listener;

    public ReportAdapter(List<ReportItem> reports, OnReportClickListener listener) {
        this.reports = reports;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ReportItem report = reports.get(position);

        // SLIKA PRIJAVE (imageUrl + placeholder)
        if (report.hasImage() && report.getImageUrl() != null && !report.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(report.getImageUrl())
                    .placeholder(report.getImageResId())
                    .error(report.getImageResId())
                    .centerCrop()
                    .into(holder.imageReport);
        } else {
            holder.imageReport.setImageResource(report.getImageResId());
        }

        holder.textTitle.setText(report.getTitle());
        holder.textCategory.setText(getCategoryDisplayName(holder.itemView, report.getCategory()));
        holder.textLocation.setText(report.getLocation());
        holder.textStatus.setText(report.getStatus());
        holder.textSupports.setText(report.getSupports() + " supports");
        holder.textComments.setText(report.getComments() + " comments");
        holder.textTime.setText(report.getTime());
        holder.textUsername.setText(report.getUsername());

        holder.itemView.setOnClickListener(v -> listener.onReportClick(report));
    }

    private String getCategoryDisplayName(View view, String categoryKey) {
        if (categoryKey == null) return view.getContext().getString(R.string.other);

        switch (categoryKey) {
            case "potholes":
                return view.getContext().getString(R.string.potholes);
            case "street_lighting":
                return view.getContext().getString(R.string.street_lighting);
            case "trash":
                return view.getContext().getString(R.string.trash);
            case "sidewalks":
                return view.getContext().getString(R.string.sidewalks);
            case "traffic_signs":
                return view.getContext().getString(R.string.traffic_signs);
            case "graffiti":
                return view.getContext().getString(R.string.graffiti);
            default:
                return view.getContext().getString(R.string.other);
        }
    }

    @Override
    public int getItemCount() {
        return reports != null ? reports.size() : 0;
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {

        ImageView imageReport;
        TextView textTitle, textCategory, textLocation, textStatus, textSupports, textComments, textTime, textUsername;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);

            imageReport = itemView.findViewById(R.id.imageReport);
            textTitle = itemView.findViewById(R.id.textTitle);
            textCategory = itemView.findViewById(R.id.textCategory);
            textLocation = itemView.findViewById(R.id.textLocation);
            textStatus = itemView.findViewById(R.id.textStatus);
            textSupports = itemView.findViewById(R.id.textSupports);
            textComments = itemView.findViewById(R.id.textComments);
            textTime = itemView.findViewById(R.id.textTime);
            textUsername = itemView.findViewById(R.id.textUsername);
        }
    }
}