package com.example.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class CreateTableActivity extends AppCompatActivity {

    private TextInputEditText nameEditText, quantityEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_table);

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Find views by ID
        nameEditText = findViewById(R.id.productNameInput);
        quantityEditText = findViewById(R.id.stockInput);
        saveButton = findViewById(R.id.saveButton);

        // Set up the save button's click listener
        saveButton.setOnClickListener(v -> {
            // Get values from the text input fields
            String name = nameEditText.getText().toString();
            int quantity;

            // Convert quantity to integer, handle potential format exception
            try {
                quantity = Integer.parseInt(quantityEditText.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert data into the database
            boolean isInserted = dbHelper.insertTransaction(name, quantity);

            if (isInserted) {
                // Show a success message
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                // Start ProductListActivity and finish this activity
                Intent intent = new Intent(CreateTableActivity.this, ProductListActivity.class);
                startActivity(intent);
                finish(); // Close CreateTableActivity
            } else {
                // Show an error message if data wasn't saved
                Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}