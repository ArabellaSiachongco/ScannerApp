package com.example.scanner;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private OnProductActionListener actionListener;

    // Constructor with context, cursor, and action listener
    public ProductListAdapter(Context context, Cursor cursor, OnProductActionListener actionListener) {
        this.context = context;
        this.cursor = cursor;
        this.actionListener = actionListener;
    }

    // Define the interface for edit and delete actions
    public interface OnProductActionListener {
        void onEditClick(int position, int productId);
        void onDeleteClick(int position, int productId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        // Retrieve data from cursor
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

        // Bind data to the views in the ViewHolder
        holder.productIdTextView.setText(String.valueOf(id));
        holder.productNameTextView.setText(name);
        holder.productQuantityTextView.setText(String.valueOf(quantity));

        // Set listeners for edit and delete icons
        holder.editIcon.setOnClickListener(v -> actionListener.onEditClick(position, id));
        holder.deleteIcon.setOnClickListener(v -> actionListener.onDeleteClick(position, id));
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    // ViewHolder class to hold references to each item’s views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productIdTextView, productNameTextView, productQuantityTextView;
        ImageView editIcon, deleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productIdTextView = itemView.findViewById(R.id.productIdTextView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productQuantityTextView = itemView.findViewById(R.id.productQuantityTextView);
            editIcon = itemView.findViewById(R.id.editIcon);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }

    // Method to update the cursor when data changes
    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
