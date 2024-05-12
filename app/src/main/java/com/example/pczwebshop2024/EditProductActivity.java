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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextInputLayout productNameLayout = findViewById(R.id.productNameLayout);
        EditText productName = productNameLayout.getEditText();

        TextInputLayout productPriceLayout = findViewById(R.id.productPriceLayout);
        EditText productPrice = productPriceLayout.getEditText();

        TextInputLayout productDescriptionLayout = findViewById(R.id.productDescriptionLayout);
        EditText productDescription = productDescriptionLayout.getEditText();

        MaterialButton save = findViewById(R.id.save);
        MaterialButton delete = findViewById(R.id.delete);

        productName.setText(App.product.getName());
        productPrice.setText(String.valueOf(App.product.getPrice()));
        productDescription.setText(App.product.getDescription());

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db.collection("products").document(App.product.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProductActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProductActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Map<String, Object> product = new HashMap<>();
                product.put("name", Objects.requireNonNull(productName.getText()).toString());
                product.put("description", Objects.requireNonNull(productDescription.getText()).toString());
                product.put("price", Objects.requireNonNull(productPrice.getText()).toString());

                db.collection("products").document(App.product.getId()).set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProductActivity.this, "Saved succesfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProductActivity.this, "Failed to save product", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}

