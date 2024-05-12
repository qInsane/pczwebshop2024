package com.example.pczwebshop2024;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextInputLayout productNameLayout = findViewById(R.id.productNameLayout);
        EditText productName = productNameLayout.getEditText();

        TextInputLayout productPriceLayout = findViewById(R.id.productPriceLayout);
        EditText productPrice = productPriceLayout.getEditText();

        TextInputLayout productDescriptionLayout = findViewById(R.id.productDescriptionLayout);
        EditText productDescription = productDescriptionLayout.getEditText();

        MaterialButton addProductButton = findViewById(R.id.addProductButton);
        MaterialButton cancelButton = findViewById(R.id.cancelButton); // Initialize cancel button

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> product = new HashMap<>();
                product.put("name", Objects.requireNonNull(productName.getText()).toString());
                product.put("price", Objects.requireNonNull(productPrice.getText()).toString());
                product.put("description", Objects.requireNonNull(productDescription.getText()).toString());

                db.collection("products").add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddProductActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity
            }
        });
    }
}

