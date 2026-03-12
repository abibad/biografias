package com.example.myapplication;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BiographyAdapter extends RecyclerView.Adapter<BiographyAdapter.ViewHolder> {

    private List<Biography> biographyList;
    private OnBiographyClickListener listener;

    public interface OnBiographyClickListener {
        void onEdit(Biography biography);
        void onDelete(Biography biography);
    }

    public BiographyAdapter(List<Biography> biographyList, OnBiographyClickListener listener) {
        this.biographyList = biographyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_biography, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Biography bio = biographyList.get(position);
        holder.tvName.setText(bio.getName());
        holder.tvHistoricalEvent.setText(bio.getHistoricalEvent());
        holder.tvBirthPlace.setText("📍 " + bio.getBirthPlace());
        holder.tvLifeData.setText(bio.getLifeData());
        
        if (bio.getImageUri() != null) {
            holder.ivPhoto.setImageURI(Uri.parse(bio.getImageUri()));
        } else {
            holder.ivPhoto.setImageResource(bio.getImageResId());
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(bio));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(bio));
    }

    @Override
    public int getItemCount() {
        return biographyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvName, tvBirthPlace, tvHistoricalEvent, tvLifeData;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            tvName = itemView.findViewById(R.id.tvName);
            tvBirthPlace = itemView.findViewById(R.id.tvBirthPlace);
            tvHistoricalEvent = itemView.findViewById(R.id.tvHistoricalEvent);
            tvLifeData = itemView.findViewById(R.id.tvLifeData);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
