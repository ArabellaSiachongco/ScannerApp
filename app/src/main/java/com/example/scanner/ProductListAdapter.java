package com.example.scanner;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    // Constructor accepting a context and a cursor
    public ProductListAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout for each row in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false); // Use product_item.xml
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Move the cursor to the right position
        if (!cursor.moveToPosition(position)) {
            return; // Exit if the cursor cannot move to the specified position
        }

        // Retrieve data from the cursor for the current row
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));  // assuming column name is "id"
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));  // assuming column name is "name"
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));  // assuming column name is "quantity"

        // Bind data to the views in the ViewHolder
        holder.productIdTextView.setText(String.valueOf(id));
        holder.productNameTextView.setText(name);
        holder.productQuantityTextView.setText(String.valueOf(quantity));
    }

    @Override
    public int getItemCount() {
        // Return the total number of rows in the cursor
        return cursor != null ? cursor.getCount() : 0;
    }

    // ViewHolder class to hold references to each item’s views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productIdTextView;
        public TextView productNameTextView;
        public TextView productQuantityTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            productIdTextView = itemView.findViewById(R.id.productIdTextView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productQuantityTextView = itemView.findViewById(R.id.productQuantityTextView);
        }
    }

    // Method to swap a new cursor in case data is updated
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
