package com.example.scanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnScanBarcode, btnShowData, saveButton;
    TextView resultText;
    ArrayList<String> scannedBarcodes;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Make sure you're using the correct layout file

        // Initialize UI components
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        resultText = findViewById(R.id.result_text);
        btnShowData = findViewById(R.id.btnShowData);
        saveButton = findViewById(R.id.goToProductListButton); // The save button

        scannedBarcodes = new ArrayList<>();
        dbHelper = new DatabaseHelper(this); // Initialize your database helper

        // Set an OnClickListener on the scan barcode button
        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScannedBarcodeActivity.class);
                scanBarcodeLauncher.launch(intent);
            }
        });

        // Set an OnClickListener on the show data button
        btnShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAllData();
                Intent intent = new Intent(MainActivity.this, TransactionDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Set an OnClickListener on the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming you have logic to get the scanned product details
                String scannedProductName = "Sample Product";  // Replace with actual scanned product name
                int scannedQuantity = 1; // Replace with actual scanned quantity

                // Call method to update product quantity
                boolean isUpdated = dbHelper.insertOrUpdateProduct(scannedProductName, scannedQuantity);
                // Notify the user about the success or failure of the update
                if (isUpdated) {
                    Toast.makeText(MainActivity.this, "Product list updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Update failed. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Barcode scanner result handling
    private ActivityResultLauncher<Intent> scanBarcodeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("intentData")) {
                        String intentData = data.getStringExtra("intentData");

                        if (dbHelper.itemExists(intentData)) {
                            // Get the current quantity and increment it
                            int currentQuantity = dbHelper.getItemQuantity(intentData);
                            dbHelper.updateQuantity(intentData, currentQuantity + 1);
                        } else {
                            // Insert new item with quantity 1
                            dbHelper.insertTransaction(intentData, 1);
                        }


                        scannedBarcodes.add(intentData);
                        updateResultText();
                    }
                }
            }
    );

    // Update result text to show scanned barcodes
    private void updateResultText() {
        StringBuilder sb = new StringBuilder();
        for (String barcode : scannedBarcodes) {
            sb.append(barcode).append("\n");
        }
        resultText.setText(sb.toString());
    }

    // Display all data from the database
    private void displayAllData() {
        Cursor cursor = dbHelper.getAllTransactions();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        while (cursor.moveToNext()) {
            sb.append("ID: ").append(cursor.getInt(0)).append(", ");
            sb.append("Name: ").append(cursor.getString(1)).append(", ");
            sb.append("Quantity: ").append(cursor.getInt(2)).append("\n");
        }
        resultText.setText(sb.toString());
    }
}
