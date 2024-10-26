package com.example.scanner;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TransactionDetailsActivity extends AppCompatActivity {

    TableLayout tableLayout;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details);

        tableLayout = findViewById(R.id.tableLayout); // Reference to the TableLayout in the layout file
        dbHelper = new DatabaseHelper(this);

        loadDataIntoTable(); // Load and populate the table with data

        // Find the button in the layout
        View goToProductListButton = findViewById(R.id.goToProductListButton);

        // Set an OnClickListener for the button
        goToProductListButton.setOnClickListener(v -> {
            // Create an Intent to navigate to ProductListActivity
            Intent intent = new Intent(TransactionDetailsActivity.this, ProductListActivity.class);
            startActivity(intent);
        });
    }

    private void loadDataIntoTable() {
        Cursor cursor = dbHelper.getAllTransactions(); // Fetch data from the database

        if (cursor.getCount() == 0) {
            return; // No data found, exit early
        }

        // Iterate through each record in the database
        while (cursor.moveToNext()) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Create TextViews for each column (ID, Name, Quantity)
            TextView idText = new TextView(this);
            idText.setText(String.valueOf(cursor.getInt(0))); // ID
            idText.setPadding(8, 5, 5, 5);
            idText.setGravity(Gravity.START); // Align to left
            tableRow.addView(idText);

            TextView nameText = new TextView(this);
            nameText.setText(cursor.getString(1)); // Name
            nameText.setPadding(5, 5, 5, 5);
            nameText.setGravity(Gravity.CENTER); // Center align
            tableRow.addView(nameText);

            TextView quantityText = new TextView(this);
            quantityText.setText(String.valueOf(cursor.getInt(2))); // Quantity
            quantityText.setPadding(5, 5, 8, 5);
            quantityText.setGravity(Gravity.END); // Align to right
            tableRow.addView(quantityText);

            // Trashcan Icon (Delete)
            // Uncomment if delete functionality is implemented in DatabaseHelper
            // ImageView deleteIcon = new ImageView(this);
            // deleteIcon.setImageResource(R.drawable.baseline_delete_24); // Ensure you have the delete icon
            // deleteIcon.setPadding(5, 5, 5, 5);
            // deleteIcon.setOnClickListener(v -> {
            //     int idToDelete = cursor.getInt(0); // Get ID for deletion
            //     dbHelper.deleteTransaction(idToDelete); // Implement this method in your DatabaseHelper
            //     tableLayout.removeView(tableRow); // Remove the row from the table
            // });
            // tableRow.addView(deleteIcon);

            // Add the TableRow to the TableLayout
            tableLayout.addView(tableRow);
        }

        cursor.close(); // Close the cursor to avoid memory leaks
    }
}