package com.example.scanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListActivity extends AppCompatActivity {

    private Button productButton;
    private RecyclerView productRecyclerView;
    private ProductListAdapter adapter;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        dbHelper = new DatabaseHelper(this);

        productButton= findViewById(R.id.createNewButton);
        productRecyclerView = findViewById(R.id.productRecyclerView);

        // Set layout manager
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
            adapter = new ProductListAdapter(this, cursor);
            productRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataIntoRecyclerView(); // Reload data when returning to the activity
    }
}