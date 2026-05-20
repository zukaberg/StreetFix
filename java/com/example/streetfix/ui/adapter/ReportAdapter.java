package com.example.streetfix.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streetfix.R;
import com.example.streetfix.data.model.ReportItem;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private final List<ReportItem> reportList;

    public ReportAdapter(List<ReportItem> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        holder.bind(reportList.get(position));
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        ImageView imageReport;
        TextView textTitle, textCategory, textStatus, textLocation, textSupports, textComments, textTime, textUsername;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            imageReport = itemView.findViewById(R.id.imageReport);
            textTitle = itemView.findViewById(R.id.textTitle);
            textCategory = itemView.findViewById(R.id.textCategory);
            textStatus = itemView.findViewById(R.id.textStatus);
            textLocation = itemView.findViewById(R.id.textLocation);
            textSupports = itemView.findViewById(R.id.textSupports);
            textComments = itemView.findViewById(R.id.textComments);
            textTime = itemView.findViewById(R.id.textTime);
            textUsername = itemView.findViewById(R.id.textUsername);
        }

        void bind(ReportItem item) {
            imageReport.setImageResource(item.getImageResId());
            textTitle.setText(item.getTitle());
            textCategory.setText(item.getCategory());
            textLocation.setText(item.getLocation());
            textSupports.setText(item.getSupports() + " supports");
            textComments.setText(item.getComments() + " comments");
            textTime.setText(item.getTime());
            textUsername.setText("By " + item.getUsername());

            String status = item.getStatus();
            textStatus.setText(status);

            if (status.equalsIgnoreCase("In Progress")) {
                textStatus.setBackgroundResource(R.drawable.bg_status_progress);
            } else if (status.equalsIgnoreCase("Verified")) {
                textStatus.setBackgroundResource(R.drawable.bg_status_verified);
            } else if (status.equalsIgnoreCase("Resolved")) {
                textStatus.setBackgroundResource(R.drawable.bg_status_resolved);
            } else {
                textStatus.setBackgroundResource(R.drawable.bg_status_new);
            }
        }
    }
}