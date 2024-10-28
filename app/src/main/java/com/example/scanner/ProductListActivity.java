package com.example.scanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductListAdapter.OnProductActionListener {

    private Button productButton;
    private RecyclerView productRecyclerView;
    private ProductListAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        dbHelper = new DatabaseHelper(this);

        // Set up views
        productButton = findViewById(R.id.createNewButton);
        productRecyclerView = findViewById(R.id.productRecyclerView);

        // Set layout manager for RecyclerView
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load data into RecyclerView
        loadDataIntoRecyclerView();

        // Set up button to navigate to CreateTableActivity
        productButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, CreateTableActivity.class);
            startActivity(intent);
        });
    }

    private void loadDataIntoRecyclerView() {
        // Fetch data using the helper
        Cursor cursor = dbHelper.getAllTransactions();
        if (cursor != null) {
            // Pass `this` as the OnProductActionListener to handle edit/delete
            adapter = new ProductListAdapter(this, cursor, this);
            productRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataIntoRecyclerView(); // Reload data when returning to the activity
    }

    @Override
    public void onEditClick(int position, int productId) {
        // Handle edit action here, e.g., by opening an edit screen
        Toast.makeText(this, "Edit Product ID: " + productId, Toast.LENGTH_SHORT).show();
        // Start an edit activity if needed, passing the productId
    }

    @Override
    public void onDeleteClick(int position, int productId) {
        // Remove the product from the database and notify the adapter
        dbHelper.deleteTransaction(productId);
        loadDataIntoRecyclerView(); // Refresh list
        Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
    }
}
