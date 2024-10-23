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
    Button btnScanBarcode, btnShowData;
    TextView resultText;
    ArrayList<String> scannedBarcodes;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        resultText = findViewById(R.id.result_text);
        btnShowData = findViewById(R.id.btnShowData);
        scannedBarcodes = new ArrayList<>();
        dbHelper = new DatabaseHelper(this);

        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScannedBarcodeActivity.class);
                scanBarcodeLauncher.launch(intent);
            }
        });

        btnShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAllData();
                Intent intent = new Intent(MainActivity.this, TransactionDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private ActivityResultLauncher<Intent> scanBarcodeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("intentData")) {
                        String intentData = data.getStringExtra("intentData");

                        // Check if the item already exists
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
                        Toast.makeText(MainActivity.this, intentData + " scanned", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    private void updateResultText() {
        StringBuilder sb = new StringBuilder();
        for (String barcode : scannedBarcodes) {
            sb.append(barcode).append("\n");
        }
        resultText.setText(sb.toString());
    }

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
