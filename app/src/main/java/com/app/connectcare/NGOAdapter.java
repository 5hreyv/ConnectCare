package com.app.connectcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NGOAdapter extends RecyclerView.Adapter<NGOAdapter.NGOViewHolder> {

    private List<NGO> ngoList;

    public NGOAdapter(List<NGO> ngoList) {
        this.ngoList = ngoList;
    }

    @NonNull
    @Override
    public NGOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ngo, parent, false);
        return new NGOViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NGOViewHolder holder, int position) {
        NGO ngo = ngoList.get(position);
        holder.nameTextView.setText(ngo.getName());
        holder.addressTextView.setText(ngo.getAddress());
    }

    @Override
    public int getItemCount() {
        return ngoList.size();
    }

    public static class NGOViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressTextView;

        public NGOViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.ngoName);
            addressTextView = itemView.findViewById(R.id.ngoAddress);
        }
    }
}
